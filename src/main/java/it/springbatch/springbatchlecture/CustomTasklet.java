package it.springbatch.springbatchlecture;

import java.util.Map;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class CustomTasklet implements Tasklet {

	@Override
	public RepeatStatus execute(
			StepContribution contribution,
			ChunkContext chunkContext
	) throws Exception {
		System.out.println("====================================");
		System.out.println(" helloStep1 executed ");
		System.out.println("====================================");

		JobParameters jobParameters = contribution.getStepExecution()
				.getJobExecution()
				.getJobParameters();

		jobParameters.getString("name");
		jobParameters.getLong("seq");
		jobParameters.getDate("date");
		jobParameters.getDouble("age");

		Map<String, Object> jobParameters1 = chunkContext.getStepContext()
				.getJobParameters();

		return RepeatStatus.FINISHED;
	}
}
