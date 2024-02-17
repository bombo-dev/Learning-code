package com.group.libraryapp.global.validator;


import javax.validation.*;
import java.util.Set;

public abstract class SelfValidator<T> {

    private Validator validator;

    public SelfValidator() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    protected void validateSelf() {
        Set<ConstraintViolation<T>> violations = validator.validate((T)this);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
