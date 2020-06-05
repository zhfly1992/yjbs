package com.fx.commons.utils.enums;

import java.util.EnumSet;
import java.util.Objects;

public interface CommonEnum {
	public int getValue();

	public String getKey();

	public static <E extends Enum<E> & CommonEnum> E getEnmu(Integer value, Class<E> clazz) {
		Objects.requireNonNull(value);
		EnumSet<E> all = EnumSet.allOf(clazz);
		return all.stream().filter(e -> e.getValue() == value).findFirst().orElse(null);
	}

	public static <E extends Enum<E> & CommonEnum> E getEnmu(String key, Class<E> clazz) {
		Objects.requireNonNull(key);
		EnumSet<E> all = EnumSet.allOf(clazz);
		return all.stream().filter(e -> e.getKey().equals(key)).findFirst().orElse(null);
	}
}
