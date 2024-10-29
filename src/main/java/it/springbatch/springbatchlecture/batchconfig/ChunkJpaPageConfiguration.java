package it.springbatch.springbatchlecture.batchconfig;

import com.querydsl.jpa.impl.JPAQuery;
import it.springbatch.springbatchlecture.adapter.out.entity.QReservationJpaEntity;
import it.springbatch.springbatchlecture.adapter.out.entity.ReservationJpaEntity;
import jakarta.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;
import kr.freezz.application.model.ReservationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ChunkJpaPageConfiguration {
	private final JobRepository jobRepository;
	private final PlatformTransactionManager tx;
	private final EntityManagerFactory entityManagerFactory;

	@Bean(name = "pageJob")
	public Job pageJob() {
		return new JobBuilder("jobPage", jobRepository)
				.start(this.step1())
				.build();
	}

	@Bean
	public Step step1() {
		log.info("Spring Chunk step1");

 		return new StepBuilder("step1", jobRepository)
				// PlatformTransactionManager 명시 해줘야 함
				.<ReservationJpaEntity, ReservationJpaEntity> chunk(2,tx) //page size 와 chunk size 를 동일하게 설정
				.reader(itemReader())
				.writer(itemWriter())
				.build();
	}


	@Bean
	public ItemReader<? extends ReservationJpaEntity> itemReader() {
		HashMap<String, Object> parameters = new HashMap<>();
		parameters.put("status", ReservationStatus.FROZE);

		JpaPagingItemReader<ReservationJpaEntity> reader = new JpaPagingItemReader<>();
		reader.setParameterValues(parameters);
		reader.setPageSize(2);
		reader.setQueryString("SELECT r FROM ReservationJpaEntity r WHERE r.status = :status");
		reader.setEntityManagerFactory(entityManagerFactory);

		return reader;
	}


	@Bean
	public ItemWriter<? super ReservationJpaEntity> itemWriter() {
		return items -> {
			for (ReservationJpaEntity item : items) {
				System.out.println(item.toString());
			}
		};
	}

}
