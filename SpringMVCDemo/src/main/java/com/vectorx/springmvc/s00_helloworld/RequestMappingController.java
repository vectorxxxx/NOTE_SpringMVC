package com.vectorx.springmvc.s00_helloworld;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/requestMappingController")
public class RequestMappingController {
    
    @RequestMapping("/testRest/{id}/{username}")
    //public String testRest(String id, String username) {
    public String testRest(@PathVariable("id") String id, @PathVariable("username") String username) {
        System.out.println("id=" + id + ", username=" + username);
        return "success";
    }

    //ant风格路径
    //@RequestMapping("/a?a/testAnt")
    //@RequestMapping("/a*a/testAnt")
    //@RequestMapping("/a**a/testAnt")
    //@RequestMapping("/**/testAnt")
    //public String testAnt() {
    //    return "success";
    //}

    ////headers属性
    //@RequestMapping(
    //        value = {"/testHeaders"},
    //        headers = {"Host=localhost:8080"}
    //)
    //public String testHeaders() {
    //    return "success";
    //}

    ////params属性
    //@RequestMapping(
    //        value = {"/testParams"},
    //        params = {"username!=admin"}
    //)
    //public String testParams() {
    //    return "success";
    //}

    ////派生注解
    //@GetMapping("/success")
    //public String successGet() {
    //    return "successget";
    //}
    //
    //@PostMapping("/success")
    //public String successPost() {
    //    return "successpost";
    //}
    //
    //@PutMapping("/success")
    //public String successPut() {
    //    return "successput";
    //}
    //
    //@DeleteMapping("/success")
    //public String successDelete() {
    //    return "successdelete";
    //}

    ////method属性
    //@RequestMapping(value = {"/success", "/test"}, method = RequestMethod.GET)
    //public String success() {
    //    return "success";
    //}

    ////value属性
    //@RequestMapping(value = {"/success", "/test"})
    //public String success() {
    //    return "success";
    //}

    //@RequestMapping("/")
    //public String index() {
    //    return "target";
    //}
}
