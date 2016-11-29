package com.lawsofnature.apigateway.annotations;

import com.lawsofnature.common.exception.ErrorCode;
import scala.Int;

import javax.inject.Qualifier;
import java.lang.annotation.*;

import static com.lawsofnature.common.exception.ErrorCode.EC_INVALID_REQUEST;

/**
 * Created by fangzhongwei on 2016/11/3.
 */
//@Target(ElementType.FIELD)
@Qualifier
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldValidate {
    String mask() default "";

    boolean required() default false;

    int minLength() default -1;

    int maxLength() default -1;

    int min() default 0x80000000;

    int max() default 0x7fffffff;

    ErrorCode error() default EC_INVALID_REQUEST;
}