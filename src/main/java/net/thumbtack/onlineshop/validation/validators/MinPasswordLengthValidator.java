package net.thumbtack.onlineshop.validation.validators;

import net.thumbtack.onlineshop.config.ServerConfig;
import net.thumbtack.onlineshop.config.ServiceConfig;
import net.thumbtack.onlineshop.validation.MinPasswordLength;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MinPasswordLengthValidator implements ConstraintValidator<MinPasswordLength, String> {


   private ServerConfig serverConfig;

    @Override
    public void initialize(MinPasswordLength constraintAnnotation) {
        serverConfig = ServiceConfig.getConfig();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s.length() > serverConfig.getMinPasswordLength();
    }
}
