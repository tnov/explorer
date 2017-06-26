package net.tnk_toys.JerseyClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Task {
	private static Logger logger = Logger.getLogger(Task.class.getName());
	private ExecutorService service = null;
	Future<?> feture = null;
	private long delay = 1L;
	private long period = 1L;
	private long wait = 1L;
	private TimeUnit timeUnit = TimeUnit.SECONDS;
	private TimeUnit waitTimeUnit = TimeUnit.SECONDS;
	private Runnable thread = null;

	public Task(Runnable thread,boolean isSingle) {
		this.thread = thread;
		if (isSingle) {
			this.service = Executors.newSingleThreadExecutor();
		} else {
			this.service = Executors.newSingleThreadScheduledExecutor();
		}
	}

	public Task setDelay(long delay) {
		this.delay = delay;
		return this;
	}

	public Task setPeriod(long period) {
		this.period = period;
		return this;
	}

	public Task setWait(long wait) {
		this.wait = wait;
		return this;
	}

	public Task setTimeUnit(TimeUnit unit) {
		this.timeUnit = unit;
		return this;
	}

	public Task setWaitTimeUnit(TimeUnit unit) {
		this.waitTimeUnit = unit;
		return this;
	}
	
	public static void main(String[] args) {
		Task task = new Task(new Runnable() {

			@Override
			public void run() {
				logger.info("Start Thread");
				logger.info("time = " + System.currentTimeMillis());
				logger.info("End Thread");
			}
		},false);
		task.run();
		logger.info("wait 5seconds");
		Task.wait(5, TimeUnit.SECONDS);
		task.cancel();
		logger.info("wait 5seconds");
		Task.wait(5, TimeUnit.SECONDS);
		task.run();
		logger.info("wait 5seconds");
		Task.wait(5, TimeUnit.SECONDS);
		task.shutdown();
	}

	public void run() {
		logger.info("start run");
		peek();
		logger.info("end run");
	}

	public void peek() {
		if (service instanceof ScheduledExecutorService) {
			feture = ((ScheduledExecutorService) service).scheduleAtFixedRate(thread, delay, period, timeUnit);
		} else {
			feture = service.submit(thread);
		}
	}

	public void cancel() {
		feture.cancel(false);
	}
	
	public void shutdown() {
		logger.info("Start Shutdown");
		if (!service.isShutdown()) {
			service.shutdown();
		}
		try {
			while (!service.awaitTermination(wait, waitTimeUnit)) {
				logger.info("Waiting Shutdown");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.info("End Shutdown");
	}
	
	public static void wait(long duration,TimeUnit timeUnit) {
		long wait = TimeUnit.MILLISECONDS.convert(duration, timeUnit);
		try {
			Thread.sleep(wait);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
