package com.vectorx.springmvc.s00_helloworld.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// ControllerAdvice 标识当前类为异常处理的组件
@ControllerAdvice
public class ExceptionController {
    // ExceptionHandler 设置所标识方法处理的异常
    @ExceptionHandler(value = {ArithmeticException.class, NullPointerException.class})
    // ex 表示当前请求处理过程中出现的异常对象
    public String testException(Exception ex, Model model) {
        model.addAttribute("ex", ex);
        return "error";
    }
}
