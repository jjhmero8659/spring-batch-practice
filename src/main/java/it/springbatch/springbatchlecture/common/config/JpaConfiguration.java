package it.springbatch.springbatchlecture.common.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories("it.springbatch.springbatchlecture.adapter.out")
@EntityScan("it.springbatch.springbatchlecture.adapter.out")
// @EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@EnableTransactionManagement
public class JpaConfiguration {

	// @Bean
	// public AuditorAware<String> auditorProvider(JwtDecoder jwtDecoder) {
	// 	// return new SpringSecurityAuditorAware(jwtDecoder);
	// 	return null;
	// }

}