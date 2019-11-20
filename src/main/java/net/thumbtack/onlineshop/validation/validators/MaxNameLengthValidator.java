package net.thumbtack.onlineshop.validation.validators;

import net.thumbtack.onlineshop.config.ServiceConfig;
import net.thumbtack.onlineshop.config.ServerConfig;
import net.thumbtack.onlineshop.validation.MaxNameLength;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class MaxNameLengthValidator implements ConstraintValidator<MaxNameLength, String> {

    private ServerConfig serverConfig;

    @Override
    public void initialize(MaxNameLength maxNameLength) {
        serverConfig = ServiceConfig.getConfig();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return   s.length() < serverConfig.getMaxNameLength();
    }
}
