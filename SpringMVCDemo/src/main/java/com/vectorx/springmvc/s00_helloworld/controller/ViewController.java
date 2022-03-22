package com.vectorx.springmvc.s00_helloworld.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/viewController")
public class ViewController {
    @RequestMapping("/testThymeleaftView")
    public String testThymeleaftView() {
        return "success";
    }

    @RequestMapping("/testForward")
    public String testForward() {
        return "forward:/viewController/testThymeleaftView";
    }

    @RequestMapping("/testRedirect")
    public String testRedirect() {
        return "redirect:/viewController/testThymeleaftView";
    }
}
