package com.ydx.datamigration.handler;


import com.google.common.base.Throwables;
import com.ydx.datamigration.enums.ConstantEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;


/**
 *  controller全局异常捕获
 */

@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger=LoggerFactory.getLogger("err");

    public static final String DEFAULT_ERROR_VIEW = ConstantEnum.DEFAULT_ERROR_VIEW_NAME.getValue();

    @ExceptionHandler(Exception.class)
    public ModelAndView defaultExceptionHandler(HttpServletRequest request, Exception e) {
        logger.error(Throwables.getStackTraceAsString(e));
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("url", request.getRequestURL());
        mav.setViewName(DEFAULT_ERROR_VIEW);
        return mav;

    }

}

