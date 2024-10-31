package it.springbatch.springbatchlecture.batch.listner;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class CustomStepExecutionListener implements StepExecutionListener {

	@Override
	public void beforeStep(StepExecution stepExecution) {
		System.out.println(stepExecution.getStepName() + " Started");
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		ExitStatus exitStatus = stepExecution.getExitStatus();
		BatchStatus batchStatus = stepExecution.getStatus();

		System.out.println(stepExecution.getStepName() + " exitStatus = " + exitStatus);
		System.out.println(stepExecution.getStepName() + " batchStatus = " + batchStatus);

		return ExitStatus.COMPLETED;
	}
}