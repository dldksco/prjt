package com.side.global.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.side.global.filter.JwtFilter;
import com.side.global.jwt.JwtAccessDeniedHandler;
import com.side.global.jwt.JwtAuthenticationEntryPoint;
import com.side.global.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

	private final JwtTokenProvider jwtTokenProvider;
    @Bean
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}


//    @Bean
//    public UserDetailsService userDetailsService() {
//        return new ShopmeUserDetailsService();
//    }

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.httpBasic().disable() // rest api ????????? ???????????? ????????????. ??????????????? ???????????? ???????????? ???????????? ??????????????? ??????.
				.csrf().disable() // rest api????????? csrf ????????? ?????????????????? disable??????.
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt token?????? ??????????????? ?????????															// ?????????????????? ????????????.
				.and().authorizeRequests() // ?????? ??????????????? ?????? ???????????? ??????
				.antMatchers("/user/**", "/board/**").permitAll() // ?????? ??? ?????? ????????? ????????? ????????????
//				.antMatchers("/board","/user/**").permitAll() // ??????????????????????????????
				.anyRequest().hasRole("USER")
				.and().exceptionHandling().authenticationEntryPoint(new JwtAuthenticationEntryPoint())
				.and().addFilterBefore(
						new JwtFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);// jwt
																													// token
																													// ?????????
																													// id/password
																													// ??????
																													// ??????
																													// ??????
																													// ?????????
		return http.build();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {

		return (web) -> web.ignoring().antMatchers("/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html",
				"/webjars/**", "/swagger/**");
	}
}
