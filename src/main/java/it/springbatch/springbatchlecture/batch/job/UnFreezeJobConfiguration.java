package it.springbatch.springbatchlecture.batch.job;
import static it.springbatch.springbatchlecture.adapter.out.entity.QReservationJpaEntity.reservationJpaEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import it.springbatch.springbatchlecture.adapter.out.entity.QReservationJpaEntity;
import it.springbatch.springbatchlecture.adapter.out.entity.ReservationJpaEntity;
import it.springbatch.springbatchlecture.adapter.out.entity.ReservationJpaRepository;
import it.springbatch.springbatchlecture.batch.chunk.listner.SampleChunkListener;
import it.springbatch.springbatchlecture.batch.tasklet.UnFreezeEndTasklet;
import it.springbatch.springbatchlecture.batch.tasklet.UnFreezeStartTasklet;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import kr.freezz.application.model.ReservationStatus;
import kr.freezz.common.generator.IdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
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
	private final JPAQueryFactory jpaQueryFactory;
	private final EntityManagerFactory entityManagerFactory;
	private final ReservationJpaRepository reservationJpaRepository;
	private final SampleChunkListener sampleChunkListener;

	private int chunkSize = 10;

	@Bean
	public Job unfreezeJob() {
		return new JobBuilder("unfreezeJob", jobRepository)
				.start(this.unfreezeReservationStep())
				.build();
	}

	@Bean
	public Step unfreezeReservationStep() {
		log.info("Froze Reservation Step Processing..");

 		return new StepBuilder("unfreezeReservationStep", jobRepository)
				.listener(sampleChunkListener)
				// PlatformTransactionManager 명시 해줘야 함
				.<ReservationJpaEntity, ReservationJpaEntity> chunk(chunkSize,tx) //page size 와 chunk size 를 동일하게 설정
				.reader(itemReader())
				.writer(itemWriter())
				.build();
	}

	@Bean
	public ItemReader<? extends ReservationJpaEntity> itemReader() {
		HashMap<String, Object> parameters = new HashMap<>();
		// List<ReservationJpaEntity> fetch = jpaQueryFactory
		// 		.selectFrom(reservationJpaEntity)
		// 		.where(reservationJpaEntity.status.eq(ReservationStatus.FROZE))
		// 		.where(reservationJpaEntity.deleted.isFalse())
		// 		.where(reservationJpaEntity.arrivalTime.after(
		// 				Instant.now().plus(Duration.ofHours(9))))
		// 		.fetch();

		parameters.put("status", ReservationStatus.FROZE);
		parameters.put("deleted", false);

		JpaPagingItemReader<ReservationJpaEntity> reader = new JpaPagingItemReader<>();
		reader.setParameterValues(parameters);
		reader.setPageSize(chunkSize);
		reader.setQueryString(
				"SELECT r FROM ReservationJpaEntity r "
				+ "WHERE r.status = :status "
				+ "and deleted = :deleted"
		);
		reader.setEntityManagerFactory(entityManagerFactory);

		return reader;
	}


	@Bean
	public ItemWriter<? super ReservationJpaEntity> itemWriter() {
		return items -> {
			for (ReservationJpaEntity item : items) {
				log.info("예약 {} 상태 변경 작동", IdGenerator.convertStringId(item.getId()));
				item.setStatus(ReservationStatus.UNFROZE);
			}
			reservationJpaRepository.saveAll(items);
		};
	}

}
