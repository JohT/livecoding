package org.joht.livecoding.changedatacapture.integrationtest.infrastructure;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.enterprise.concurrent.ManagedExecutorService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;

/**
 * The {@link ManagedExecutorService} can only be injected in a full Jakarta EE or MicroProfile environment.
 * This producer provides it for the "Weld" (= CDI only) test environment.
 */
@ApplicationScoped
public class ManagedExecutorServiceProducer {

	private static final Logger LOG = Logger.getLogger(ManagedExecutorServiceProducer.class.getName());

	@Produces
	@ApplicationScoped
	ManagedExecutorService produceManagedExecutorService() {
		LOG.info("Starting ManagedExecutorService");
		return ManagedExecutorServiceAdapter.ofExecutorService(Executors.newSingleThreadExecutor());
	}

	void shutdownExecutor(@Disposes ManagedExecutorService executorService) {
		LOG.info("Stopping executor service");
		try {
			List<Runnable> shutdownTasks = executorService.shutdownNow();
			for (Runnable shutdownTask : shutdownTasks) {
				if (shutdownTask instanceof Closeable) {
					try {
						((Closeable) shutdownTask).close();
					} catch (IOException e1) {
						LOG.info("Error while closing " + shutdownTask);
					}
				}
			}
			int shutdownTry = 0;
			while (!executorService.awaitTermination(2, TimeUnit.SECONDS) && shutdownTry <= 3) {
				LOG.info("Waiting another 2 seconds for the executor to shut down");
				shutdownTry++;
			}
		} catch (InterruptedException e) {
			LOG.log(Level.FINE, e, () -> "executor shutdown interrupted");
		}
	}

}