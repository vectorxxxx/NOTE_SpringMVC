package com.vectorx.springmvc.s00_helloworld.controller;

import com.vectorx.springmvc.s00_helloworld.bean.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

@Controller
@RequestMapping("/paramController")
public class ParamController {

    @RequestMapping("/testServletAPI")
    public String testServletAPI(HttpServletRequest request) {
        HttpSession session = request.getSession();
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

    @RequestMapping("/testServletAPI3")
    public String testServletAPI3(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        String username = request.getParameter("username");
        System.out.println("username=" + username);
        //username = new String(username.getBytes("ISO-8859-1"), "UTF-8");
        //System.out.println("username=" + username);
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
    public String testParam3(
            @RequestParam(value = "user_name", required = true) String username,
            String password, String[] hobby) {
        System.out.println("username=" + username + ", password=" + password + ", hobby=" + Arrays.toString(hobby));
        return "success";
    }

    @RequestMapping("/testHeader")
    public String testHeader(
            @RequestHeader(value = "Host") String host,
            @RequestHeader(value = "Test", required = false, defaultValue = "RequestHeader") String test) {
        System.out.println("Host=" + host + ", test=" + test);
        return "success";
    }

    @RequestMapping("/testCookie")
    public String testCookie(
            @CookieValue(value = "JSESSIONID") String jSessionId,
            @CookieValue(value = "Test", required = false, defaultValue = "CookieValue") String test) {
        System.out.println("jSessionId=" + jSessionId + ", test=" + test);
        return "success";
    }

    @RequestMapping("/testBean")
    public String testBean(User user) {
        System.out.println(user);
        return "success";
    }
}
