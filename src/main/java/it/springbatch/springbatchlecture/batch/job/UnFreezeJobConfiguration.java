package it.springbatch.springbatchlecture.batch.job;
import static it.springbatch.springbatchlecture.adapter.out.entity.QReservationJpaEntity.reservationJpaEntity;

import it.springbatch.springbatchlecture.adapter.out.entity.ReservationJpaEntity;
import it.springbatch.springbatchlecture.adapter.out.entity.ReservationJpaRepository;
import it.springbatch.springbatchlecture.batch.listner.CustomAnnotationJobExecutionListener;
import it.springbatch.springbatchlecture.batch.reader.QuerydslPagingItemReader;
import jakarta.persistence.EntityManagerFactory;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import kr.freezz.application.model.ReservationStatus;
import kr.freezz.common.generator.IdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class UnFreezeJobConfiguration {
	private final JobRepository jobRepository;
	private final PlatformTransactionManager tx;
	private final EntityManagerFactory entityManagerFactory;
	private final ReservationJpaRepository reservationJpaRepository;
	private int pageSize = 2;
	private int chunkSize = 2;

	@Bean
	public Job unfreezeJob() {
		return new JobBuilder("unFreezeJob", jobRepository)
				.start(unfreezeReservationStep())
				.listener(new CustomAnnotationJobExecutionListener())
				.build();
	}

	@Bean
	public Step unfreezeReservationStep() {
 		return new StepBuilder("unfreezeReservationStep", jobRepository)
				// .listener(sampleChunkListener)
				// PlatformTransactionManager 명시 해줘야 함
				.<ReservationJpaEntity, ReservationJpaEntity> chunk(chunkSize,tx) //page size 와 chunk size 를 동일하게 설정
				// .listener(new CustomStepExecutionListener())
				.reader(unfreezeReader())
				.processor(unfreezeProcessor())
				.writer(unfreezeWriter())
				.build();
	}

	@Bean
	public ItemReader<? extends ReservationJpaEntity> unfreezeReader() {


		QuerydslPagingItemReader<ReservationJpaEntity> reader = new QuerydslPagingItemReader<>(
				entityManagerFactory, pageSize,
				queryFactory -> queryFactory
						.selectFrom(reservationJpaEntity)
						.where(reservationJpaEntity.status.eq(ReservationStatus.FROZE))
						.where(reservationJpaEntity.deleted.isFalse())
						.where(reservationJpaEntity.arrivalTime.before(Instant.now().plus(Duration.ofHours(9))))
		);

		return reader;
	}

	@Bean
	public ItemProcessor<ReservationJpaEntity, ReservationJpaEntity> unfreezeProcessor(){
		return entity -> {
			log.info("예약 {} 의 상태를 UNFROZE" , IdGenerator.convertStringId(entity.getId()));
			entity.setStatus(ReservationStatus.UNFROZE);
			return entity;
		};
	}

	// JpaItemWriter
	@Bean
	public ItemWriter<? super ReservationJpaEntity> unfreezeWriter() {
		return items -> {
			for (ReservationJpaEntity item : items) {
				log.info("예약 {} 상태 변경 작동", IdGenerator.convertStringId(item.getId()));
			}
			reservationJpaRepository.saveAll(items);

		};
	}

}
