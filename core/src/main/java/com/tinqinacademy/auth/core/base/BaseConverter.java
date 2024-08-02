package com.tinqinacademy.auth.core.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;


public abstract class BaseConverter<S,T> implements Converter<S,T> {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public T convert(S input) {

        log.info("Start convert input: {}", input);
        T output = convertObject(input);
        log.info("End convert result: {}", output);

        return output;
    }

    protected abstract T convertObject(S source);
}
