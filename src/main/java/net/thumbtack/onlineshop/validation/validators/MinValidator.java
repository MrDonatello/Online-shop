package net.thumbtack.onlineshop.validation.validators;

import net.thumbtack.onlineshop.validation.MinInterface;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MinValidator implements ConstraintValidator<MinInterface, Integer> {


    @Override
    public void initialize(MinInterface constraintAnnotation) {

    }

    @Override
    public boolean isValid(Integer i, ConstraintValidatorContext constraintValidatorContext) {

        return i >= 1;
    }
}
