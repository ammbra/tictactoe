package com.example.tictactoe.config;


import com.example.tictactoe.player.PlayerDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig  {

	private final PlayerDetailsService playerDetailsService;

	@Autowired
	public SecurityConfig(PlayerDetailsService playerDetailsService) {
		this.playerDetailsService = playerDetailsService;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PlayerDetailsService.CRYPT_PASSWORD_ENCODER;
	}


	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.userDetailsService(playerDetailsService);
		return http
				.authorizeHttpRequests((authorize) -> authorize
						.requestMatchers("/register/**", "/css/*", "/js/*", "/actuator/*").permitAll()
						.anyRequest().authenticated())
				.formLogin(formLogin -> formLogin
						.loginPage("/login").permitAll())
				.logout(logout -> logout.
						logoutSuccessHandler((request, response, authentication) -> response.sendRedirect("/login"))
						.permitAll().deleteCookies("JSESSIONID"))
				.rememberMe(remember -> remember.key("RememberMe"))
				.build();
	}

}