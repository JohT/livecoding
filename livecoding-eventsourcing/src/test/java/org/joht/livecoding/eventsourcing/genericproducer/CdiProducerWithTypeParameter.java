package org.joht.livecoding.eventsourcing.genericproducer;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;

/**
 * Shows how to write a CDI producer for a generic type with one type parameter 
 * and how to get and use the type parameter inside the producer.
 * <p>
 * Besides the possibility to write producer for on specific type parameter,
 * it is shown that it is also possible to do it for any type parameter in a single producer method.
 * It needs to be <code>@Dependent</code> scope (default for <code>@Producer</code>) for that.
 * <p>
 * Informations of the {@link InjectionPoint} lead to the specific type parameter that is used for  
 * <code>@Inject</code>.
 */
class CdiProducerWithTypeParameter {

	@Produces
	public <T> TypedInterface<T> produceTypedInterface(InjectionPoint injectionPoint) {
		Class<?> typeArgument = getFirstReturnTypeArgument(injectionPoint.getMember());
		return new TypedInterface<T>() {

			@Override
			public String getName() {
				return typeArgument.getSimpleName();
			}

			@Override
			@SuppressWarnings("unchecked")
			public T getType() {
				return (T) typeArgument;
			}
		};
	}

	private static Class<?> getFirstReturnTypeArgument(Member member) {
		Type genericType = null;
		if (member instanceof Field) {
			genericType = ((Field) member).getGenericType();
		}
		if (member instanceof Method) {
			genericType = ((Method) member).getGenericReturnType();
		}
		if (!(genericType instanceof ParameterizedType)) {
			return null;
		}
		Type[] typeArguments = ((ParameterizedType) genericType).getActualTypeArguments();
		if (typeArguments.length <= 0) {
			return null;
		}
		if (!(typeArguments[0] instanceof Class<?>)) {
			return null;
		}
		return (Class<?>)typeArguments[0];
	}

	public static interface TypedInterface<T> {
		String getName();

		T getType();
	}
}