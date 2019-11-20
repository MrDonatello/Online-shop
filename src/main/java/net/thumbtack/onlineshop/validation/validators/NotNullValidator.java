package net.thumbtack.onlineshop.validation.validators;

import net.thumbtack.onlineshop.validation.NotNullInterface;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotNullValidator implements ConstraintValidator<NotNullInterface, Object> {


    @Override
    public void initialize(NotNullInterface constraintAnnotation) {

    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        return o != null;
    }
}
