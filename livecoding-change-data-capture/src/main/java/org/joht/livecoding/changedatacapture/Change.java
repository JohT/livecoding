package org.joht.livecoding.changedatacapture;

import java.beans.ConstructorProperties;
import java.util.Objects;

public class Change<T> {

	private final T before;
	private final T after;
	private final Operation operation;
	
	public static final <T> Change<T> changed(T before, T after, Operation operation) {
		return new Change<T>(before, after, operation.getCode());
	}
	
	@ConstructorProperties({"before", "after", "op"})
	public Change(T before, T after, String operation) {
		this.before = before;
		this.after = after;
		this.operation = Operation.valueOfCode(operation);
	}

	public T getBefore() {
		return before;
	}

	public T getAfter() {
		return after;
	}

	public Operation getOperation() {
		return operation;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(after, before, operation);
	}

	@Override
	public boolean equals(Object obj) {
		if ((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}
		Change<?> other = (Change<?>) obj;
		return Objects.equals(after, other.after) //
				&& Objects.equals(before, other.before) //
				&& operation == other.operation;
	}

	@Override
	public String toString() {
		return "Change [before=" + before + ", after=" + after + ", operation=" + operation + "]";
	}
	
	public enum Operation {
		CREATE("c"),
		UPDATE("u"),
		DELETE("d"),
		READ_SNAPSHOT("r"),
		UNKNOWN("?"),
		;
		
		private final String code;

		private Operation(String code) {
			this.code = code;
		}
		
		public String getCode() {
			return code;
		}
		
		public boolean isUnknown() {
			return equals(UNKNOWN);
		}
		
		public static final Operation valueOfCode(String opCode) {
			for (Operation operation : values()) {
				if (operation.getCode().equals(opCode)) {
					return operation;
				}
			}
			return UNKNOWN;
		}
	}
}