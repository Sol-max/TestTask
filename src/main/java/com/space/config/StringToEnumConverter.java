package com.space.config;

import com.space.model.ShipType;
import org.springframework.core.convert.converter.Converter;

public class StringToEnumConverter implements Converter<String, ShipType> {
    @Override
    public ShipType convert(String source) {
        return ShipType.valueOf(source.toUpperCase());
    }
}
