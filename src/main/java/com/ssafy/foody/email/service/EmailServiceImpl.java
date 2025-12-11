package com.ssafy.foody.email.service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

	private final JavaMailSender javaMailSender;
	
	@Value("${spring.mail.username}")
    private String senderEmail;

    // Key: 이메일, Value: 인증정보(코드 + 만료시간)
	// 멀티 쓰레드 환경에서 동시성 문제를 해결하기 위해 ConcurrentHashMap 사용 (쓰기 메소드가 Lock Stripping(락 분할) 적용)
    private final Map<String, VerificationInfo> memoryStorage = new ConcurrentHashMap<>();

    // 인증 정보
    @Getter
    @AllArgsConstructor
    private static class VerificationInfo {
        private String code;
        private LocalDateTime expireTime;
    }

    // 인증 코드 발송
    @Override
    public void sendVerificationCode(String email) {
        // 코드 생성
        String code = createCode();
        
        // 유효기간 5분
        LocalDateTime expireTime = LocalDateTime.now().plusMinutes(5);

        // Map에 저장
        memoryStorage.put(email, new VerificationInfo(code, expireTime));

        // 이메일 전송
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
            
            helper.setTo(email);
            helper.setSubject("[Foody] 회원가입 인증 코드입니다.");
            helper.setText("인증 코드: <strong>" + code + "</strong><br>5분 안에 입력해주세요.", true);
            
            // 보내는 사람 설정 (이메일 주소, "표시될 이름")
            helper.setFrom(senderEmail, "Foody");
            
            javaMailSender.send(message);
            log.debug("인증 코드 발송 성공 (Memory): {}", email);
            
        } catch (MessagingException | UnsupportedEncodingException e) {
            memoryStorage.remove(email); // 실패 시 저장소에서도 삭제
            log.error("이메일 발송 실패", e);
            throw new RuntimeException("이메일 발송 중 오류가 발생했습니다.");
        }
    }

    // 인증 코드 검증
    @Override
    public boolean verifyCode(String email, String code) {
        // Map에서 꺼내오기
        VerificationInfo info = memoryStorage.get(email);

        // 기록 없음
        if (info == null) {
            return false;
        }

        // 코드 불일치
        if (!info.getCode().equals(code)) {
            return false;
        }

        // 시간 만료
        if (info.getExpireTime().isBefore(LocalDateTime.now())) {
            memoryStorage.remove(email);
            return false;
        }

        // 성공 -> 메모리에서 삭제 (재사용 방지)
        memoryStorage.remove(email);
        return true;
    }

    // 랜덤 코드 생성기
    private String createCode() {
        Random random = new Random();
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            key.append(random.nextInt(10));
        }
        return key.toString();
    }

}
