package com.shakov.validator;

public interface Validator<T> {

    ValidationResult validate(T object);
}
