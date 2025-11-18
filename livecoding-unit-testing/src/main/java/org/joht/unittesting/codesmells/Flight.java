package org.joht.unittesting.codesmells;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Flight {

	private static final Pattern FLIGHT_NUMBER_PATTERN = Pattern.compile("(\\w+)\\s+(\\d+)");

	private final String airlineCode;
	private final int number;
	private final int mileage;
	private final boolean cancelled;

	public static final Flight ofFlightNumber(String flightNumber) {
		Matcher flightNumberMatcher = FLIGHT_NUMBER_PATTERN.matcher(flightNumber);
		if (!flightNumberMatcher.find()) {
			throw new IllegalArgumentException(flightNumber + " is not a valid flightnumber");
		}
		return new Flight(flightNumberMatcher.group(1), Integer.valueOf(flightNumberMatcher.group(2)), 0, false);
	}

	public Flight(String airlineCode, int number, int mileage, boolean cancelled) {
		this.cancelled = cancelled;
		this.airlineCode = Objects.requireNonNull(airlineCode, () -> "airline code required");
		this.number = number;
		this.mileage = mileage;
	}

	public String getAirlineCode() {
		return airlineCode;
	}

	public int getNumber() {
		return number;
	}

	public String getFlightNumber() {
		return airlineCode + " " + number;
	}

	public String getAirline() {
		return Airline.ofCode(getAirlineCode()).map(Airline::getName).orElse(null);
	}

	public int getMileage() {
		return mileage;
	}

	public Flight withMileage(int value) {
		return new Flight(airlineCode, number, value, cancelled);
	}

	public int getMileageAsKm() {
		if (isCancelled()) {
			throw new IllegalArgumentException("Cannot get cancelled flight mileage");
		}
		return (int) Math.round(getMileage() * 1.60934);
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public Flight cancel() {
		return new Flight(airlineCode, number, mileage, true);
	}

	@Override
	public int hashCode() {
		return Objects.hash(airlineCode, number);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Flight other = (Flight) obj;
		return Objects.equals(airlineCode, other.airlineCode) && number == other.number;
	}

	@Override
	public String toString() {
		return "Flight [airlineCode=" + airlineCode + ", number=" + number + ", mileage=" + mileage + ", cancelled="
				+ cancelled + "]";
	}

	private enum Airline {
		AUSTRIAN("OS", "Austrian Airlines"), EASY_JET("U2", "easyJet"),;

		private final String code;
		private final String name;

		private Airline(String code, String name) {
			this.code = code;
			this.name = name;
		}

		public String getCode() {
			return code;
		}

		public String getName() {
			return name;
		}

		public static final Optional<Airline> ofCode(String code) {
			return EnumSet.allOf(Airline.class).stream()
					.filter(airline -> Objects.equals(airline.getCode(), code))
					.findFirst();
		}
	}
}