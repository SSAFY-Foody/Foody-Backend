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
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.foody.user.mapper.UserMapper;

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
    private final UserMapper userMapper;

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
            helper.setSubject("🍎 [Foody] 이메일 인증 코드입니다");

            String htmlContent = """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    </head>
                    <body style="margin: 0; padding: 0; font-family: 'Apple SD Gothic Neo', 'Noto Sans KR', sans-serif; background-color: #f5f5f5;">
                        <table width="100%%" cellpadding="0" cellspacing="0" style="background-color: #f5f5f5; padding: 40px 20px;">
                            <tr>
                                <td align="center">
                                    <table width="600" cellpadding="0" cellspacing="0" style="background-color: #ffffff; border-radius: 20px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); overflow: hidden;">
                                        <!-- Header -->
                                        <tr>
                                            <td style="background: linear-gradient(135deg, #10b981 0%%, #059669 100%%); padding: 40px 30px; text-align: center;">
                                                <h1 style="margin: 0; color: #ffffff; font-size: 28px; font-weight: 700;">Foody</h1>
                                                <p style="margin: 10px 0 0 0; color: #ffffff; font-size: 16px; opacity: 0.9;">건강한 식단 관리의 시작</p>
                                            </td>
                                        </tr>
                                        <!-- Content -->
                                        <tr>
                                            <td style="padding: 50px 40px;">
                                                <h2 style="margin: 0 0 20px 0; color: #1f2937; font-size: 24px; font-weight: 600;">이메일 인증 코드</h2>
                                                <p style="margin: 0 0 30px 0; color: #6b7280; font-size: 16px; line-height: 1.6;">
                                                    안녕하세요! Foody 회원가입을 위한 인증 코드를 보내드립니다.<br>
                                                    아래 6자리 코드를 입력해주세요.
                                                </p>

                                                <!-- Verification Code Box -->
                                                <table width="100%%" cellpadding="0" cellspacing="0" style="margin: 30px 0;">
                                                    <tr>
                                                        <td align="center" style="background: linear-gradient(135deg, #ecfdf5 0%%, #d1fae5 100%%); border: 2px solid #10b981; border-radius: 12px; padding: 30px;">
                                                            <div style="font-size: 36px; font-weight: 700; color: #059669; letter-spacing: 8px; font-family: 'Courier New', monospace;">
                                                                %s
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </table>

                                                <p style="margin: 30px 0 0 0; color: #ef4444; font-size: 14px; line-height: 1.6;">
                                                    ⏰ 이 인증 코드는 <strong>5분간</strong> 유효합니다.<br>
                                                    시간 내에 입력해주세요!
                                                </p>
                                            </td>
                                        </tr>
                                        <!-- Footer -->
                                        <tr>
                                            <td style="background-color: #f9fafb; padding: 30px 40px; border-top: 1px solid #e5e7eb;">
                                                <p style="margin: 0; color: #9ca3af; font-size: 13px; line-height: 1.6;">
                                                    본 메일은 Foody 회원가입 요청에 의해 자동 발송되었습니다.<br>
                                                    회원가입을 요청하지 않으셨다면 이 메일을 무시하셔도 됩니다.
                                                </p>
                                                <p style="margin: 15px 0 0 0; color: #9ca3af; font-size: 13px;">
                                                    © 2024 Foody. All rights reserved.
                                                </p>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </body>
                    </html>
                    """
                    .formatted(code);

            helper.setText(htmlContent, true);
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

    // 임시 비밀번호 발송
    @Override
    public void sendTemporaryPassword(String email, String tempPassword) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

            helper.setTo(email);
            helper.setFrom(senderEmail, "Foody");
            helper.setSubject("🔐 [Foody] 임시 비밀번호 안내");

            String htmlContent = """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    </head>
                    <body style="margin: 0; padding: 0; font-family: 'Apple SD Gothic Neo', 'Noto Sans KR', sans-serif; background-color: #f5f5f5;">
                        <table width="100%%" cellpadding="0" cellspacing="0" style="background-color: #f5f5f5; padding: 40px 20px;">
                            <tr>
                                <td align="center">
                                    <table width="600" cellpadding="0" cellspacing="0" style="background-color: #ffffff; border-radius: 20px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); overflow: hidden;">
                                        <!-- Header -->
                                        <tr>
                                            <td style="background: linear-gradient(135deg, #10b981 0%%, #059669 100%%); padding: 40px 30px; text-align: center;">
                                                <h1 style="margin: 0; color: #ffffff; font-size: 28px; font-weight: 700;">Foody</h1>
                                                <p style="margin: 10px 0 0 0; color: #ffffff; font-size: 16px; opacity: 0.9;">건강한 식단 관리의 시작</p>
                                            </td>
                                        </tr>
                                        <!-- Content -->
                                        <tr>
                                            <td style="padding: 50px 40px;">
                                                <h2 style="margin: 0 0 20px 0; color: #1f2937; font-size: 24px; font-weight: 600;">🔐 임시 비밀번호 안내</h2>
                                                <p style="margin: 0 0 30px 0; color: #6b7280; font-size: 16px; line-height: 1.6;">
                                                    안녕하세요!<br>
                                                    비밀번호 찾기 요청에 따라 임시 비밀번호를 발급해드렸습니다.
                                                </p>

                                                <!-- Temporary Password Box -->
                                                <table width="100%%" cellpadding="0" cellspacing="0" style="margin: 30px 0;">
                                                    <tr>
                                                        <td style="background-color: #fef3c7; border-left: 4px solid #f59e0b; border-radius: 8px; padding: 20px;">
                                                            <p style="margin: 0 0 10px 0; color: #92400e; font-size: 14px; font-weight: 600;">임시 비밀번호</p>
                                                            <div style="font-size: 24px; font-weight: 700; color: #b45309; font-family: 'Courier New', monospace; letter-spacing: 2px;">
                                                                %s
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </table>

                                                <div style="background-color: #fef2f2; border-left: 4px solid #ef4444; border-radius: 8px; padding: 20px; margin: 30px 0;">
                                                    <p style="margin: 0; color: #991b1b; font-size: 15px; font-weight: 600;">⚠️ 보안을 위한 안내</p>
                                                    <p style="margin: 10px 0 0 0; color: #991b1b; font-size: 14px; line-height: 1.6;">
                                                        • 로그인 후 <strong>반드시</strong> 비밀번호를 변경해주세요<br>
                                                        • 임시 비밀번호는 타인과 공유하지 마세요<br>
                                                        • 이 메일은 발송 후 삭제하시기 바랍니다
                                                    </p>
                                                </div>

                                                <table width="100%%" cellpadding="0" cellspacing="0" style="margin: 30px 0 0 0;">
                                                    <tr>
                                                        <td align="center">
                                                            <a href="#" style="display: inline-block; background: linear-gradient(135deg, #10b981 0%%, #059669 100%%); color: #ffffff; text-decoration: none; padding: 14px 40px; border-radius: 10px; font-weight: 600; font-size: 16px;">로그인하러 가기</a>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        <!-- Footer -->
                                        <tr>
                                            <td style="background-color: #f9fafb; padding: 30px 40px; border-top: 1px solid #e5e7eb;">
                                                <p style="margin: 0; color: #9ca3af; font-size: 13px; line-height: 1.6;">
                                                    본 메일은 Foody 비밀번호 찾기 요청에 의해 자동 발송되었습니다.<br>
                                                    비밀번호 찾기를 요청하지 않으셨다면 즉시 고객센터로 문의해주세요.
                                                </p>
                                                <p style="margin: 15px 0 0 0; color: #9ca3af; font-size: 13px;">
                                                    © 2024 Foody. All rights reserved.
                                                </p>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </body>
                    </html>
                    """
                    .formatted(tempPassword);

            helper.setText(htmlContent, true);

            javaMailSender.send(message);
            log.debug("임시 비밀번호 발송 성공: {}", email);

        } catch (Exception e) {
            log.error("임시 비밀번호 발송 실패", e);
            throw new RuntimeException("메일 발송 중 오류가 발생했습니다.");
        }
    }

    // 이메일 중복 체크
    @Override
    @Transactional(readOnly = true)
    public boolean isEmailDuplicate(String email) {
        // 아이디가 조회되면(null이 아니면) 이미 가입된 이메일임 -> true 반환
        String userId = userMapper.findIdByEmail(email);
        return userId != null;
    }

}
