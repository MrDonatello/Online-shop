package net.thumbtack.onlineshop.validation;

import net.thumbtack.onlineshop.exceptions.ErrorCode;
import net.thumbtack.onlineshop.validation.validators.PatternValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PatternValidator.class)
public @interface PatternInterface {

    String message();

    String regexp();

    ErrorCode error();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
