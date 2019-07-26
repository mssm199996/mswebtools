package com.security.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.security.Services.AuthenticationService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticationService authenticationService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.userDetailsService(this.authenticationService);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf()
				.disable()
				.exceptionHandling()

				// --------------------------------------------------------------

				.and()
				.authenticationProvider(authenticationProvider())
				.formLogin()
				.loginPage("/app/loginPage")
				.loginProcessingUrl("/app/loginService")
				.defaultSuccessUrl("/app/home", true)
				.failureUrl("/app/loginPage?error")
				
				// --------------------------------------------------------------

				.and()
				.logout()
				.logoutUrl("/logout")
				.logoutSuccessUrl("/app/loginPage")
				.invalidateHttpSession(true)

				// --------------------------------------------------------------

				.and()
				.authorizeRequests()
				.antMatchers(
						"/app/loginPage*", 
						"/app/loginService")
				.anonymous()
				
				// --------------------------------------------------------------

				.antMatchers("/app/home**").authenticated()

				// --------------------------------------------------------------

				.antMatchers(
						"/css/**", 
						"/font/**", 
						"/img/**", 
						"/js/**",
						"/assets/**", 
						"/Scripts/**", 
						"/Other/**",
						"/frontend/**", 
						"/VAADIN/**", 
						"/PUSH/**", 
						"/UIDL/**",
						"/error/**", 
						"/accessDenied/**", 
						"/vaadinServlet/**",
						"/HEARTBEAT/**", 
						"/resources/**",
						"/app/*",
						"/*",
						"/publicServices/**")
				.permitAll()
				
			.anyRequest().authenticated();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		AuthenticationProvider provider = new AuthenticationProvider();
		provider.setUserDetailsService(this.authenticationService);

		return provider;
	}
}
