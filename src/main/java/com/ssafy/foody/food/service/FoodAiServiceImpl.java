package com.ssafy.foody.food.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.foody.food.dto.AiFoodResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FoodAiServiceImpl implements FoodAiService {
	
	private final RestTemplate restTemplate;
	
	// AI 서버 BASE URL
	@Value("${ai.server.base.url}")
    private String aiServerBaseUrl;
	
	// VLM 서버 port
	@Value("${ai.vlm.server.port}")
    private String vlmServerPort;

	@Override
    public AiFoodResponse analyzeImage(MultipartFile image) {
        if (image.isEmpty()) {
            throw new IllegalArgumentException("이미지 파일이 비어있습니다.");
        }

        try {
        	// 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // 바디 설정 (파일 데이터 담기)
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            
            // MultipartFile을 ByteArrayResource로 변환하여 전송
            ByteArrayResource resource = new ByteArrayResource(image.getBytes()) {
                @Override
                public String getFilename() {
                    return image.getOriginalFilename();
                }
            };
            
            body.add("image", resource);

            // 요청 엔티티 생성
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // 이미지 분석 엔드포인트
            String analyzeImageURL = String.format("%s:%s/api/vlm/food", aiServerBaseUrl, vlmServerPort);
            
            // POST 요청 전송
            log.info("AI 서버로 이미지 분석 요청 전송: {}", analyzeImageURL);
            ResponseEntity<AiFoodResponse> response = restTemplate.postForEntity(
            		analyzeImageURL,
                    requestEntity,
                    AiFoodResponse.class
            );

            log.info("AI 분석 완료: {}", response.getBody());
            return response.getBody();

        } catch (IOException e) {
            log.error("이미지 파일 처리 중 오류 발생", e);
            throw new RuntimeException("이미지 처리에 실패했습니다.");
        } catch (Exception e) {
            log.error("AI 서버 통신 중 오류 발생", e);
            throw new RuntimeException("AI 분석 요청에 실패했습니다.");
        }
    }

}
