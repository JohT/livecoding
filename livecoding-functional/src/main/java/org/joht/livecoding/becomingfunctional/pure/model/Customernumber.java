package org.joht.livecoding.becomingfunctional.pure.model;

import java.util.Objects;

public class Customernumber implements Comparable<Customernumber> {

	private final long value;

	public static final Customernumber of(long value) {
		return new Customernumber(value);
	}

	public static final Customernumber ofNumber(Number number) {
		return new Customernumber(number.longValue());
	}

	protected Customernumber(long value) {
		this.value = value;
	}

	public long getValue() {
		return value;
	}

	public Long asLong() {
		return Long.valueOf(value);
	}

	@Override
	public int compareTo(Customernumber o) {
		return Long.compare(getValue(), o.getValue());
	}

	@Override
	public boolean equals(final Object other) {
		if ((other == null) || (!getClass().equals(other.getClass()))) {
			return false;
		}
		return Objects.equals(value, ((Customernumber) other).value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	@Override
	public String toString() {
		return "Customernumber [value=" + value + "]";
	}
}
