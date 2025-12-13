package com.rehab.oauth;

import com.rehab.domain.entity.User;
import com.rehab.domain.repository.user.UserRepository;
import com.rehab.security.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

	private final UserRepository userRepository;
	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
										Authentication authentication) throws IOException {

		CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

		String provider = "kakao";
		String providerId = oAuth2User.getProviderId();
		String email = oAuth2User.getEmail();
		String nickname = oAuth2User.getNickname();
		// 1) 기존 회원인지 조회
		User user = userRepository.findByProviderAndProviderId(provider, providerId)
			.orElseGet(() -> {
				// 신규 유저 생성
				User newUser = User.createKakaoUser(
					providerId,
					email,
					nickname
				);
				return userRepository.save(newUser);
			});

		// 2) JWT 생성
		String accessToken = jwtTokenProvider.createAccessToken(user.getUserId(), user.getRole().toString(),null);
		String refreshToken = jwtTokenProvider.createRefreshToken(user.getUserId(), user.getRole().toString());

		String frontBaseUrl = "https://rehab-web-fe.vercel.app";

		String redirectUrl = frontBaseUrl
			+ "/oauth/success"
			+ "?accessToken=" + URLEncoder.encode(accessToken, StandardCharsets.UTF_8)
			+ "&refreshToken=" + URLEncoder.encode(refreshToken, StandardCharsets.UTF_8);

		response.sendRedirect(redirectUrl);
	}
}
