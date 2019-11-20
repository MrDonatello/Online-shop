package net.thumbtack.onlineshop.validation;

import net.thumbtack.onlineshop.exceptions.ErrorCode;
import net.thumbtack.onlineshop.validation.validators.MinPasswordLengthValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MinPasswordLengthValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MinPasswordLength {

    String message() default "Invalid min length";

    ErrorCode error() default ErrorCode.INVALID_MIN_LENGTH;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
