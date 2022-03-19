package com.vectorx.springmvc.s00_helloworld.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {

    //@RequestMapping 注解：处理请求和控制器方法之间的映射关系
    //@RequestMapping 注解的 value 属性可以通过请求地址匹配请求，/ 表示的当前工程的上下文路径
    // localhost：8080/springMVC/
    @RequestMapping("/")
    public String index() {
        //返回视图名称
        return "index";
    }

    @RequestMapping("/target")
    public String toTarget() {
        return "target";
    }

    @RequestMapping("/param")
    public String param() {
        return "param";
    }
}
