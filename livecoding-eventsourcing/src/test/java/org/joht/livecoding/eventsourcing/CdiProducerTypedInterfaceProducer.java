package org.joht.livecoding.eventsourcing;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;

//TODO delete, if generic type producer for aggregate repositories works
public class CdiProducerTypedInterfaceProducer {

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