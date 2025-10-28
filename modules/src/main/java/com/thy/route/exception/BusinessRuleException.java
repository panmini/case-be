package com.thy.route.exception;

import com.thy.exception.BaseException;

public class BusinessRuleException extends BaseException {
    public BusinessRuleException(String message) {
        super("BUSINESS_RULE_VIOLATION", message);
    }
}
