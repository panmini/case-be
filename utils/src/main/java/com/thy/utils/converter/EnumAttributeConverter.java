package com.thy.utils.converter;

import com.thy.enums.BaseEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Objects;
import java.util.stream.Stream;

@Converter
public abstract class EnumAttributeConverter<E extends Enum<E> & BaseEnum>
        implements AttributeConverter<E, Integer> {

    private final Class<E> enumClass;

    protected EnumAttributeConverter(Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public Integer convertToDatabaseColumn(E attribute) {
        return attribute != null ? attribute.getIntValue() : null;
    }

    @Override
    public E convertToEntityAttribute(Integer value) {
        if (value == null) return null;

        return Stream.of(enumClass.getEnumConstants())
                .filter(e -> Objects.equals(e.getIntValue(), value))
                .findFirst()
                .orElseThrow(
                        () ->
                                new IllegalArgumentException(
                                        "Unknown enum value " + value + " for " + enumClass.getSimpleName()));
    }
}
