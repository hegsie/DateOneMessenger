package com.greycells.dateone.shared;

public enum InterestedIn {
	Men(1),
	Women(2);

	private final int value;
	private InterestedIn(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
