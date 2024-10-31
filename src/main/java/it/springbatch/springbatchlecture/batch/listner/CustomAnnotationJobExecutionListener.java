package it.springbatch.springbatchlecture.batch.listner;

import java.time.Duration;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;
@Slf4j
public class CustomAnnotationJobExecutionListener {

	@BeforeJob
	public void beforeJob(JobExecution jobExecution) {
		log.info(
				"START================================차량 녹이기 Job================================START");

	}

	@AfterJob
	public void afterJob(JobExecution jobExecution) {
		// long seconds = Duration.between(jobExecution.getEndTime(), jobExecution.getStartTime())
		// 		.getSeconds();
		// System.out.println(jobExecution.getJobInstance().getJobName() + " 총 소요 seconds = " + seconds);

		log.info(
				"END==================================차량 녹이기 Job==================================END");

	}
}