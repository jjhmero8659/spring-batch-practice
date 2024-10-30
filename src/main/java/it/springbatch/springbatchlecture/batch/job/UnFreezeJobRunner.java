package it.springbatch.springbatchlecture.batch.job;

import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UnFreezeJobRunner extends JobRunner{
	private final Scheduler scheduler;

	@Override
	protected void doRun(ApplicationArguments args) {
		JobDetail jobDetail = buildJobDetail(UnFreezeSchJob.class, "unFreezeJob", "batch", new HashMap());
		Trigger trigger = buildJobTrigger("0/30 * * * * ?");

		try {
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
}
