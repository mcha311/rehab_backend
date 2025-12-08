package com.rehab.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * AI 모델 서버 통신 설정
 * 학과 서버에 배포된 허깅페이스 모델 엔드포인트와 통신
 */
@Configuration
public class AiModelConfig {

	@Value("${ai-model.base-url}")
	private String aiModelBaseUrl;

	@Value("${ai-model.connect-timeout:5000}")
	private int connectTimeout;

	@Value("${ai-model.read-timeout:30000}")
	private int readTimeout;

	@Bean
	public RestTemplate aiModelRestTemplate() {
		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		factory.setConnectTimeout(connectTimeout);  // 연결 타임아웃: 5초
		factory.setReadTimeout(readTimeout);        // 읽기 타임아웃: 30초
		return new RestTemplate(factory);
	}

	public String getAiModelBaseUrl() {
		return aiModelBaseUrl;
	}
}
