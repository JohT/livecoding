package org.joht.livecoding.eventsourcing.infrastructure.axon.inject.cdi;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.function.Function;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.config.AggregateConfigurer;
import org.axonframework.config.Configuration;
import org.axonframework.config.Configurer;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.modelling.command.AggregateRoot;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.serialization.upcasting.event.EventUpcaster;

import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Typed;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.util.AnnotationLiteral;

/**
 * Discovers all components for axon using CDI's {@link BeanManager#getBeans(String)}.
 */
public class AxonComponentDiscovery {

	private final BeanManager beanManager;

	public static final AxonComponentDiscovery ofBeanManager(BeanManager beanManager) {
		return new AxonComponentDiscovery(beanManager);
	}
	
	public AxonComponentDiscovery(BeanManager beanManager) {
		this.beanManager = beanManager;
	}

	/**
	 * Attaches all discovered components to the given
	 * {@link AxonComponentDiscoveryContext#getConfigurer()}.
	 * 
	 * @param context {@link AxonComponentDiscoveryContext}
	 * @return {@link Configurer}
	 */
	public Configurer addDiscoveredComponentsTo(Configurer axonConfigurer) {
		RegisteredAnnotatedTypes beanTypes = getBeanTypes();
		registerAggregates(axonConfigurer, beanTypes);
		registerEventHandlers(axonConfigurer, beanTypes);
		registerEventUpcasters(axonConfigurer, beanTypes);
		registerCommandHandlers(axonConfigurer, beanTypes);
		registerQueryHandlers(axonConfigurer, beanTypes);
		registerSagas(axonConfigurer, beanTypes);
		registerResourceInjector(axonConfigurer);
		return axonConfigurer;
	}

	private void registerAggregates(Configurer axonConfigurer, RegisteredAnnotatedTypes beanTypes) {
		beanTypes.annotatedWith(AggregateRoot.class)
				.map(AggregateConfigurer::defaultConfiguration)
				.forEach(axonConfigurer::configureAggregate);
	}

	private void registerEventHandlers(Configurer axonConfigurer, RegisteredAnnotatedTypes beanTypes) {
		beanTypes.annotatedWithAnyOf(ProcessingGroup.class, EventHandler.class)
				.map(this::lookedUp)
				.forEach(axonConfigurer.eventProcessing()::registerEventHandler);
	}

	private void registerEventUpcasters(Configurer axonConfigurer, RegisteredAnnotatedTypes beanTypes) {
		beanTypes.subtypeOf(EventUpcaster.class)
				.map(this::lookedUpEventUpcaster)
				.forEach(axonConfigurer::registerEventUpcaster);
	}

	private void registerCommandHandlers(Configurer axonConfigurer, RegisteredAnnotatedTypes beanTypes) {
		beanTypes.annotatedWith(CommandHandler.class)
				.filter(beanTypes.without(AggregateRoot.class))
				.map(this::lookedUp)
				.forEach(axonConfigurer::registerCommandHandler);
	}

	private void registerQueryHandlers(Configurer axonConfigurer, RegisteredAnnotatedTypes beanTypes) {
		beanTypes.annotatedWith(QueryHandler.class)
				.map(this::lookedUp)
				.forEach(axonConfigurer::registerQueryHandler);
	}

	private void registerSagas(Configurer axonConfigurer, RegisteredAnnotatedTypes beanTypes) {
		beanTypes.annotatedWith(SagaEventHandler.class).forEach(axonConfigurer.eventProcessing()::registerSaga);
	}

	private void registerResourceInjector(Configurer axonConfigurer) {
		axonConfigurer.configureResourceInjector(CdiResourceInjector.useBeanManager(beanManager));
	}

	private Function<Configuration, Object> lookedUp(Class<?> typeToLookUp) {
		return config -> lookup(typeToLookUp);
	}

	private Function<Configuration, EventUpcaster> lookedUpEventUpcaster(Class<?> typeToLookUp) {
		return config -> (EventUpcaster) lookup(typeToLookUp);
	}

	private <U> Object lookup(Class<?> type, Annotation... qualifiers) {
		Bean<?> bean = beanManager.getBeans(type, qualifiers).iterator().next();
		CreationalContext<?> ctx = beanManager.createCreationalContext(bean);
		return beanManager.getReference(bean, type, ctx);
	}

	private RegisteredAnnotatedTypes getBeanTypes() {
		Set<Bean<?>> beans = beanManager.getBeans(Object.class, AnnotationLiteralAny.ANY);
		return RegisteredAnnotatedTypes
				.ofStream(beans.stream().filter(this::isBeanWithAtLeastOneType).map(Bean::getBeanClass));
	}

	private boolean isBeanWithAtLeastOneType(Bean<?> bean) {
		Typed annotation = bean.getBeanClass().getAnnotation(Typed.class);
		return (annotation != null) ? annotation.value().length > 0 : true;
	}

	private static class AnnotationLiteralAny extends AnnotationLiteral<Any> {
		private static final long serialVersionUID = 1L;
		public static final AnnotationLiteral<Any> ANY = new AnnotationLiteralAny();
	}
}