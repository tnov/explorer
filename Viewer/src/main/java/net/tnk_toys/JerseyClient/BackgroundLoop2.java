package net.tnk_toys.JerseyClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class BackgroundLoop2 {
	private static Logger logger = Logger.getLogger(BackgroundLoop2.class.getName());
	private ExecutorService service = null;
	private boolean isLoop = false;
	private int interval = 1;
	private TimeUnit timeUnit = TimeUnit.SECONDS;
	
	public static void main(String[] args) {
	}
	public void singleThread() {
		logger.info("execute peek");
		peek();
		logger.info("wait 10seconds");
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		shutdown();
	}
	
	private void peek() {
		service = Executors.newSingleThreadExecutor();
		service.execute(new Runnable() {
			
			@Override
			public void run() {
				logger.info("Start Thread");
				isLoop = true;
				while (isLoop) {
					logger.info("loop " + System.currentTimeMillis());
					try {
						Thread.sleep(interval);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				logger.info("End Thread");
			}
		});
	}
	
	private void shutdown() {
		logger.info("Start Shutdown");
		isLoop = false;
		if(!service.isShutdown()) {
			service.shutdown();
		}
		try {
			while (!service.awaitTermination(interval, timeUnit)) {
				logger.info("Waiting Shutdown");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.info("End Shutdown");
	}
}
