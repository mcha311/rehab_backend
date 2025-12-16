package com.rehab.config;


import com.rehab.domain.repository.user.UserRepository;
import com.rehab.oauth.CustomOAuth2UserService;
import com.rehab.oauth.OAuth2LoginSuccessHandler;
import com.rehab.security.jwt.JwtAuthenticationFilter;
import com.rehab.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtTokenProvider jwtTokenProvider;
	private final UserRepository userRepository;
	private final CustomOAuth2UserService customOAuth2UserService;
	private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;


	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.cors(Customizer.withDefaults())
			.csrf(csrf -> csrf.disable())
			.formLogin(form -> form.disable())
			.httpBasic(basic -> basic.disable())
			.sessionManagement(session ->
				session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				.requestMatchers(
					"/",
					"/health",
					"/auth/**",
					"/temp/**",
					"/login/oauth2/**",
					"/oauth2/**",
					"/swagger-ui/**",
					"/swagger-ui.html",
					"/v3/api-docs/**",
					"/api/**",
					"/users/**"
				).permitAll()
				.anyRequest().authenticated()
			)
			.oauth2Login(oauth2 -> oauth2
				.loginPage("/oauth2/authorization/kakao")
				//.failureUrl("/auth/login-failure")
				.userInfoEndpoint(userInfo -> {
					userInfo.userService(customOAuth2UserService);
				})
				.successHandler(oAuth2LoginSuccessHandler)
			)
			// 인증 실패 시 401 JSON 응답 반환 (HTTP 리다이렉트 방지)
			.exceptionHandling(exception -> exception
				.authenticationEntryPoint((request, response, authException) -> {
					response.setStatus(401);
					response.setContentType("application/json;charset=UTF-8");
					response.getWriter().write(
						"{\"isSuccess\":false,\"code\":\"AUTH_001\"," +
							"\"message\":\"인증이 필요합니다.\",\"result\":null}"
					);
				})
			)
			.httpBasic(httpBasic -> httpBasic.disable())
			.formLogin(form -> form.disable())
			.logout(logout -> logout.disable());

		http.addFilterBefore(
			new JwtAuthenticationFilter(jwtTokenProvider, userRepository),
			UsernamePasswordAuthenticationFilter.class
		);

		return http.build();
	}


	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}

