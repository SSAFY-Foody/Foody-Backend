package com.ssafy.foody.food.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.ssafy.foody.food.dto.AiFoodResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiFoodServiceImpl implements AiFoodService {
	
	private final WebClient webClient;
	
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
        
        String analyzeImageURL = String.format("%s:%s/api/vlm/food", aiServerBaseUrl, vlmServerPort);
        log.debug("AI 서버로 이미지 분석 요청 전송: {}", analyzeImageURL);

        try {
        	// Multipart Body 생성 (WebClient 전용 빌더 사용)
            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            
            // 파일 추가 (파일명 유지를 위해 ByteArrayResource 트릭 사용)
            builder.part("image", new ByteArrayResource(image.getBytes()) {
                @Override
                public String getFilename() {
                    return image.getOriginalFilename();
                }
            });

            // WebClient 요청 전송
            AiFoodResponse response = webClient.post()
                    .uri(analyzeImageURL)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(builder.build()))
                    .retrieve()
                    .bodyToMono(AiFoodResponse.class)
                    .block(); // 동기 처리

            log.debug("AI 분석 완료: {}", response);

            return response;

        } catch (IOException e) {
            log.error("이미지 파일 처리 중 오류 발생", e);
            throw new RuntimeException("이미지 처리에 실패했습니다.");
        } catch (Exception e) {
            log.error("AI 서버 통신 중 오류 발생", e);
            throw new RuntimeException("AI 분석 요청에 실패했습니다.");
        }
    }

}
