package org.joht.livecoding.eventsourcing.infrastructure.axon.inject.cdi;

import java.util.function.Function;

import org.axonframework.modelling.saga.ResourceInjector;

import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.spi.AnnotatedType;
import jakarta.enterprise.inject.spi.BeanContainer;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.enterprise.inject.spi.InjectionTarget;
import jakarta.enterprise.inject.spi.InjectionTargetFactory;

class CdiResourceInjector implements ResourceInjector {

	public static final ResourceInjector standard() {
		return new CdiResourceInjector(CDI.current().getBeanManager());
	}

	public static final <P> Function<P, ResourceInjector> useBeanManager(BeanManager beanManager) {
		return p -> new CdiResourceInjector(beanManager);
	}

	/**
	 * Creates a new {@link ResourceInjector} for CDI injection if the given {@link BeanContainer} is a {@link BeanManager} (CDI full).
	 * Returns a {@link ResourceInjector} that fails with a {@link UnsupportedOperationException} when used, if only CDI lite is available.
	 * @param <P>
	 * @param beanContainer {@link BeanContainer}
	 * @return Function that creates the {@link ResourceInjector}.
	 */
	public static final <P> Function<P, ResourceInjector> useBeanContainerIfCapable(BeanContainer beanContainer) {
		if (beanContainer instanceof BeanManager) {
			return useBeanManager((BeanManager) beanContainer);
		}
		return p -> FailingResourceInjector.INSTANCE;
	}

	private final BeanManager beanManager;

	private CdiResourceInjector(BeanManager beanManager) {
		this.beanManager = beanManager;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void injectResources(Object resource) {
		if (resource != null) {
			injectResourcesUsingCdi(resource);
		}
	}

	private <T> void injectResourcesUsingCdi(T resource) {
		@SuppressWarnings("unchecked")
		Class<T> type = (Class<T>) resource.getClass();

		AnnotatedType<T> annotatedType = beanManager.createAnnotatedType(type);
		InjectionTargetFactory<T> targetFactory = beanManager.getInjectionTargetFactory(annotatedType);
		InjectionTarget<T> target = targetFactory.createInjectionTarget(null);
		CreationalContext<T> creationalContext = beanManager.createCreationalContext(null);
		target.inject(resource, creationalContext);
		target.postConstruct(resource);
	}

	@Override
	public String toString() {
		return "CdiResourceInjector [beanManager=" + beanManager + "]";
	}

	private enum FailingResourceInjector implements ResourceInjector {
		INSTANCE;

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void injectResources(Object saga) {
			String message = "CDI lite's BeanContainer doesn't support injection. Injection not possible for: " + saga;
			throw new UnsupportedOperationException(message);
		}
	}
}