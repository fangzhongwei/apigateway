package com.lawsofnature.apigateway.annotations;


import com.lawsofnature.common.exception.ErrorCode;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;

import static com.lawsofnature.common.exception.ErrorCode.EC_INVALID_REQUEST;

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