package de.wellnerbou.springsecuritydemo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@EnableWebSecurity
public class SecurityConfiguration {

	@Bean
	public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeHttpRequests((requests) -> requests
						.anyRequest().authenticated()
				)
				.formLogin(Customizer.withDefaults())
				.logout(LogoutConfigurer::permitAll)
				.httpBasic();
		return http.build();
	}

	@Bean
	public InMemoryUserDetailsManager userDetailsService(final PasswordEncoder passwordEncoder, final @Value("${users:user:password}") List<String> userStrings) {
		final List<UserDetails> users = userStrings.stream().map(s -> {
			final String[] split = s.split(":");
			return User.withUsername(split[0])
					.password(passwordEncoder.encode(split[1]))
					.roles("USER")
					.build();
		}).toList();

		return new InMemoryUserDetailsManager(users);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	@ConditionalOnMissingBean
	public WebSecurityCustomizer securityCustomizer() {
		return (web) -> web.ignoring().antMatchers("/static/**", "/favicon.ico");
	}

	@Bean
	@Profile(value = {"no-security"})
	public WebSecurityCustomizer noSecurityCustomizer() {
		return (web) -> web.ignoring().antMatchers("/**");
	}
}
