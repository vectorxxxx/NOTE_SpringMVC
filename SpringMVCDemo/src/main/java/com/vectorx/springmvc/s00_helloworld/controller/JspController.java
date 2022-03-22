package com.vtestJspectorx.springmvc.s00_helloworld.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class JspController {
    @RequestMapping("/success")
    public String success() {
        return "success";
    }
}
