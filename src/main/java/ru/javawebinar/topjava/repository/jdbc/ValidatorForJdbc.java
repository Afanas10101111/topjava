package ru.javawebinar.topjava.repository.jdbc;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

public class ValidatorForJdbc{
    private final Validator validator;

    public ValidatorForJdbc(Validator validator) {
        this.validator = validator;
    }

    public <T> void validate(T entity) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(entity);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }
}
