package org.joht.livecoding.changedatacapture.integrationtest.infrastructure;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

import jakarta.enterprise.concurrent.ContextService;
import jakarta.enterprise.concurrent.ManagedExecutorService;

/**
 * Provides the given {@link ExecutorService} as {@link ManagedExecutorService}.
 * 
 * @see <a href=
 *      "https://github.com/eclipse-ee4j/concurrency-ri/blob/master/src/main/java/org/glassfish/enterprise/concurrent/ManagedExecutorServiceImpl.java">Glassfish
 *      ManagedExecutorServiceImpl.java</a>
 * @see <a href=
 *      "https://github.com/eclipse-ee4j/concurrency-ri/blob/master/src/main/java/org/glassfish/enterprise/concurrent/AbstractManagedExecutorService.java">Glassfish
 *      AbstractManagedExecutorService.java</a>
 * @see <a href=
 *      "https://github.com/eclipse-ee4j/concurrency-ri/blob/master/src/main/java/org/glassfish/enterprise/concurrent/internal/ManagedCompletableFuture.java">Glassfish
 *      ManagedCompletableFuture.java</a>
 */
class ManagedExecutorServiceAdapter implements ManagedExecutorService {

	private final ExecutorService executorService;

	public static final ManagedExecutorService ofExecutorService(ExecutorService executorService) {
		return new ManagedExecutorServiceAdapter(executorService);
	}

	ManagedExecutorServiceAdapter(ExecutorService executorService) {
		this.executorService = executorService;
	}

	@Override
	public void execute(Runnable command) {
		executorService.execute(command);
	}

	@Override
	public void shutdown() {
		executorService.shutdown();
	}

	@Override
	public List<Runnable> shutdownNow() {
		return executorService.shutdownNow();
	}

	@Override
	public boolean isShutdown() {
		return executorService.isShutdown();
	}

	@Override
	public boolean isTerminated() {
		return executorService.isTerminated();
	}

	@Override
	public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
		return executorService.awaitTermination(timeout, unit);
	}

	@Override
	public <T> Future<T> submit(Callable<T> task) {
		return executorService.submit(task);
	}

	@Override
	public <T> Future<T> submit(Runnable task, T result) {
		return executorService.submit(task, result);
	}

	@Override
	public Future<?> submit(Runnable task) {
		return executorService.submit(task);
	}

	@Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
		return executorService.invokeAll(tasks);
	}

	@Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
			throws InterruptedException {
		return executorService.invokeAll(tasks, timeout, unit);
	}

	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
		return executorService.invokeAny(tasks);
	}

	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
			throws InterruptedException, ExecutionException, TimeoutException {
		return executorService.invokeAny(tasks, timeout, unit);
	}

	@Override
	public String toString() {
		return "ManagedExecutorServiceAdapter [executorService=" + executorService + "]";
	}

	/**
	 * {@inheritDoc}
	 * @see <a href="https://github.com/eclipse-ee4j/concurrency-ri/blob/master/src/main/java/org/glassfish/enterprise/concurrent/internal/ManagedCompletableFuture.java">Glassfish ManagedCompletableFuture.java</a>
	 */
	@Override
	public <U> CompletableFuture<U> completedFuture(U value) {
		CompletableFuture<U> future = new CompletableFuture<>();
		future.complete(value);
		return future;
	}

	/**
	 * {@inheritDoc}
	 * @see <a href="https://github.com/eclipse-ee4j/concurrency-ri/blob/master/src/main/java/org/glassfish/enterprise/concurrent/internal/ManagedCompletableFuture.java">Glassfish ManagedCompletableFuture.java</a>
	 */
	@Override
	public <U> CompletionStage<U> completedStage(U value) {
		CompletableFuture<U> future = new CompletableFuture<>();
		future.complete(value);
		return future;
	}

	/**
	 * {@inheritDoc}
	 * @see <a href="https://github.com/eclipse-ee4j/concurrency-ri/blob/master/src/main/java/org/glassfish/enterprise/concurrent/internal/ManagedCompletableFuture.java">Glassfish ManagedCompletableFuture.java</a>
	 */
	@Override
	public <T> CompletableFuture<T> copy(CompletableFuture<T> future) {
		return copyInternal(future);
	}

	/**
	 * {@inheritDoc}
	 * @see <a href="https://github.com/eclipse-ee4j/concurrency-ri/blob/master/src/main/java/org/glassfish/enterprise/concurrent/internal/ManagedCompletableFuture.java">Glassfish ManagedCompletableFuture.java</a>
	 */
	@Override
	public <T> CompletionStage<T> copy(CompletionStage<T> stage) {
		return copyInternal(stage);
	}

	// Taken from https://github.com/eclipse-ee4j/concurrency-ri/blob/master/src/main/java/org/glassfish/enterprise/concurrent/internal/ManagedCompletableFuture.java
	private <T> CompletableFuture<T> copyInternal(CompletionStage<T> future) {
		final CompletableFuture<T> managedFuture = new CompletableFuture<>();
        future.whenComplete((result, exception) -> {
            if (exception == null) {
                managedFuture.complete(result);
            } else {
                managedFuture.completeExceptionally(exception);
            }
        });
        return managedFuture;
    }

	
	/**
	 * {@inheritDoc}
	 * @see <a href=https://stackoverflow.com/questions/49116855/completablefuture-already-completed-with-an-exception">StackTrace CompletableFuture already completed with an exception</a>
	 */
	@Override
	public <U> CompletableFuture<U> failedFuture(Throwable exception) {
		CompletableFuture<U> future = new CompletableFuture<>();
		future.completeExceptionally(exception);
		return future;
	}

	/**
	 * {@inheritDoc}
	 * @see <a href=https://stackoverflow.com/questions/49116855/completablefuture-already-completed-with-an-exception">StackTrace CompletableFuture already completed with an exception</a>
	 */
	@Override
	public <U> CompletionStage<U> failedStage(Throwable exception) {
		CompletableFuture<U> future = new CompletableFuture<>();
		future.completeExceptionally(exception);
		return future;
	}

	/**
	 * {@inheritDoc}
	 * @throws UnsupportedOperationException because its not not implemented
	 */
	@Override
	public ContextService getContextService() {
		throw new UnsupportedOperationException("Not implemented in " + getClass());
	}

	/**
	 * {@inheritDoc}
	 * @see <a href="https://github.com/eclipse-ee4j/concurrency-ri/blob/master/src/main/java/org/glassfish/enterprise/concurrent/internal/ManagedCompletableFuture.java">Glassfish ManagedCompletableFuture.java</a>
	 */
	@Override
	public <U> CompletableFuture<U> newIncompleteFuture() {
		return new CompletableFuture<>();
	}

	/**
	 * {@inheritDoc}
	 * @see <a href="https://github.com/eclipse-ee4j/concurrency-ri/blob/master/src/main/java/org/glassfish/enterprise/concurrent/internal/ManagedCompletableFuture.java">Glassfish ManagedCompletableFuture.java</a>
	 */
	@Override
	public CompletableFuture<Void> runAsync(Runnable runnable) {
		 return CompletableFuture.runAsync(runnable, executorService);
	}

	/**
	 * {@inheritDoc}
	 * @see <a href="https://github.com/eclipse-ee4j/concurrency-ri/blob/master/src/main/java/org/glassfish/enterprise/concurrent/internal/ManagedCompletableFuture.java">Glassfish ManagedCompletableFuture.java</a>
	 */
	@Override
	public <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier) {
		 return CompletableFuture.supplyAsync(supplier, executorService);
	}
}