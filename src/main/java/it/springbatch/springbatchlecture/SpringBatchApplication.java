package it.springbatch.springbatchlecture;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.JobRegistrySmartInitializingSingleton;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchApplication.class, args);
	}

	@Bean
	public static BeanDefinitionRegistryPostProcessor jobRegistryBeanPostProcessorRemover() {
		return registry -> registry.removeBeanDefinition("jobRegistryBeanPostProcessor");
	}

	@Bean
	public JobRegistrySmartInitializingSingleton jobRegistrySmartInitializingSingleton(
			JobRegistry jobRegistry) {
		return new JobRegistrySmartInitializingSingleton(jobRegistry);
	}
}
