package it.springbatch.springbatchlecture.batchconfig;// package it.springbatch.springbatchlecture;
//
// import java.util.Map;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.batch.core.Job;
// import org.springframework.batch.core.JobParameters;
// import org.springframework.batch.core.Step;
// import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
// import org.springframework.batch.core.job.builder.JobBuilder;
// import org.springframework.batch.core.repository.JobRepository;
// import org.springframework.batch.core.step.builder.StepBuilder;
// import org.springframework.batch.repeat.RepeatStatus;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.transaction.PlatformTransactionManager;
//
// @Configuration
// @RequiredArgsConstructor
// @Slf4j
// public class BatchConfiguration {
//
// 	private final JobRepository jobRepository;
// 	private final PlatformTransactionManager tx;
//
// 	@Bean
// 	public Job job() {
// 		return new JobBuilder("job", jobRepository)
// 				.start(step1())
// 				.next(step2())
// 				.build();
// 	}
//
// 	@Bean
// 	public Step step1() {
// 		log.info("Spring Batch helloStep1");
//
// 		// return new StepBuilder( "helloStep1", jobRepository)
// 		// 		.tasklet((contribution, chunkContext) -> {
// 		//
// 		// 			System.out.println("====================================");
// 		// 			System.out.println(" helloStep1 executed ");
// 		// 			System.out.println("====================================");
// 		//
// 		// 			JobParameters jobParameters = contribution.getStepExecution()
// 		// 					.getJobExecution()
// 		// 					.getJobParameters();
// 		//
// 		// 			jobParameters.getString("name");
// 		// 			jobParameters.getLong("seq");
// 		// 			jobParameters.getDate("date");
// 		// 			jobParameters.getDouble("age");
// 		//
// 		// 			Map<String, Object> jobParameters1 = chunkContext.getStepContext()
// 		// 					.getJobParameters();
// 		//
// 		// 			return RepeatStatus.FINISHED; //한번 실행하고 종료 된다.
// 		// 		}, tx)
// 		// 		.build();
//
// 		return new StepBuilder("step1", jobRepository)
// 				.tasklet(new CustomTasklet(),tx)
// 				.build();
// 	}
//
// 	@Bean
// 	public Step step2() {
// 		log.info("Spring Batch helloStep2");
//
// 		return new StepBuilder( "helloStep2", jobRepository)
// 				.tasklet((contribution, chunkContext) -> {
// 					System.out.println("====================================");
// 					System.out.println(" helloStep2 executed ");
// 					System.out.println("====================================");
// 					return RepeatStatus.FINISHED;
// 				}, tx)
// 				.build();
// 	}
// }
