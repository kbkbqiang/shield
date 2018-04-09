package com.niiwoo.shield.manage.web.util;

import org.apache.commons.lang3.text.StrBuilder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

/**
 * Created by luosiwen on 2017/8/16.
 */
public class ErrorsDisposal {


    /**
     *  获取错误信息
     * @param result
     * @return
     */
    public static String getErrorsMessage(BindingResult result) {
        List<FieldError> list = result.getFieldErrors();
        StrBuilder builderErrors = new StrBuilder();
        for (FieldError error : list) {
            builderErrors.append(error.getField()+error.getDefaultMessage()).append("、");
        }
        return builderErrors.length() > 0 ? builderErrors.substring(0,builderErrors.length()-1).toString() : "";
    }
}
