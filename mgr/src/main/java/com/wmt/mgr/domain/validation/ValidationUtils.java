package com.wmt.mgr.domain.validation;

import org.hibernate.validator.HibernateValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-02-23 15:07
 * @version 1.0
 */
public class ValidationUtils {
    private static Logger logger = LoggerFactory.getLogger(ValidationUtils.class);

    /**
     * 使用hibernate的注解来进行验证
     *
     */
    private static Validator validator = Validation.byProvider(HibernateValidator.class).configure().failFast(true).buildValidatorFactory().getValidator();

    /**
     * 功能描述: <br>
     * 〈注解验证参数〉
     *
     * @param obj
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static <T> String validate(T obj) {
        try {
            Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);

            if (constraintViolations.size() > 0) {
                String message = constraintViolations.iterator().next().getMessage();
                return message;
            }
            return null;

        // 抛出检验异常
//        if (constraintViolations.size() > 0) {
//            throw new IllegalArgumentException(String.format("参数校验失败:%s", constraintViolations.iterator().next().getMessage()));
//        }
        } catch (Exception ex) {
            logger.error(ex.getMessage(),ex);
            return null;
        }
    }
}
