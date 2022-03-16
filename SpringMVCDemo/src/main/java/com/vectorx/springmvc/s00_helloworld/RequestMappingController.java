package com.vectorx.springmvc.s00_helloworld;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/requestMappingController")
public class RequestMappingController {

    @RequestMapping(
            value = {"/testHeaders"},
            headers = {"Host=localhost:8080"}
    )
    public String testHeaders() {
        return "success";
    }

    @RequestMapping(
            value = {"/testParams"},
            params = {"username!=admin"}
    )
    public String testParams() {
        return "success";
    }

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
