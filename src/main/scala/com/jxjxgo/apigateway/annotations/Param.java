package com.jxjxgo.apigateway.annotations;

import com.jxjxgo.apigateway.enumerate.ParamSource;
import com.jxjxgo.common.exception.ErrorCode;

import javax.validation.Constraint;
import java.lang.annotation.*;

import static com.jxjxgo.common.exception.ErrorCode.EC_INVALID_REQUEST;

/**
 * Created by fangzhongwei on 2016/11/3.
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = {}
)
public @interface Param {
    ParamSource source() default ParamSource.PARAM;

    String name() default "";

    boolean required() default false;

    String mask() default "";

    int minLength() default -1;

    int maxLength() default -1;

    int min() default 0x80000000;

    int max() default 0x7fffffff;

    ErrorCode error() default EC_INVALID_REQUEST;
}