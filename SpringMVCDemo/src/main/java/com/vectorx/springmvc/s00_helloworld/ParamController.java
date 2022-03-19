package com.vectorx.springmvc.s00_helloworld;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Controller
@RequestMapping("/paramController")
public class ParamController {

    @RequestMapping("/testServletAPI")
    public String testServletAPI(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println("username=" + username + ",password=" + password);
        return "success";
    }

    @RequestMapping("/testServletAPI2")
    public String testServletAPI2(HttpServletRequest request) {
        String hobby = request.getParameter("hobby");
        String[] hobby2 = request.getParameterValues("hobby");
        System.out.println("hobby=" + hobby + ", hobby2=" + Arrays.toString(hobby2));
        return "success";
    }

    @RequestMapping("/testParam")
    public String testParam(String username, String password) {
        System.out.println("username=" + username + ",password=" + password);
        return "success";
    }

    @RequestMapping("/testParam2")
    public String testParam2(String username, String password, String hobby) {
        System.out.println("username=" + username + ", password=" + password + ", hobby=" + hobby);
        return "success";
    }

    @RequestMapping("/testParam3")
    public String testParam3(@RequestParam(value = "user_name", required = false) String username, String password, String[] hobby) {
        System.out.println("username=" + username + ", password=" + password + ", hobby=" + Arrays.toString(hobby));
        return "success";
    }
}
