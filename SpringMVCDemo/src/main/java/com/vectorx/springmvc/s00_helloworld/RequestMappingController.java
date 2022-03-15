package com.vectorx.springmvc.s00_helloworld;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/requestMappingController")
public class RequestMappingController {
    @GetMapping("/success")
    public String successGet() {
       return "successget";
    }

    @PostMapping("/success")
    public String successPost() {
        return "successpost";
    }

    @PutMapping("/success")
    public String successPut() {
        return "successput";
    }

    @DeleteMapping("/success")
    public String successDelete() {
        return "successdelete";
    }

    //@RequestMapping(value = {"/success", "/test"}, method = RequestMethod.GET)
    //public String success() {
    //    return "success";
    //}

    //@RequestMapping(value = {"/success", "/test"})
    //public String success() {
    //    return "success";
    //}

    //@RequestMapping("/")
    //public String index() {
    //    return "target";
    //}
}
