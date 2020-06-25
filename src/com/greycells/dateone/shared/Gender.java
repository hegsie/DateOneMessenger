package com.greycells.dateone.shared;

public enum Gender {
	  Man(1),
	  Woman(2);

	private final int value;
	private Gender(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
	}
