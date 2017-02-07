package com.jxjxgo.apigateway.annotations;


import com.jxjxgo.common.exception.ErrorCode;

import javax.validation.Constraint;
import java.lang.annotation.*;

import static com.jxjxgo.common.exception.ErrorCode.EC_INVALID_REQUEST;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = {}
)
public @interface FieldValidate {
    String mask() default "";

    boolean required() default false;

    int minLength() default -1;

    int maxLength() default -1;

    int min() default 0x80000000;

    int max() default 0x7fffffff;

    ErrorCode error() default EC_INVALID_REQUEST;
}