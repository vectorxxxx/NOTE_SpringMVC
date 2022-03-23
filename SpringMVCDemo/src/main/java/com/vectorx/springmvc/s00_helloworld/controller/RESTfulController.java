package com.vectorx.springmvc.s00_helloworld.controller;

import com.vectorx.springmvc.s00_helloworld.bean.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("restfulcontroller")
public class RESTfulController {
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String getAllUser() {
        System.out.println("查询所有用户信息");
        return "success";
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public String getUserById(@PathVariable("id") String id) {
        System.out.println("根据用户ID查询用户信息：" + id);
        return "success";
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public String insertUser(User user) {
        System.out.println("添加用户信息：" + user);
        return "success";
    }

    @RequestMapping(value = "/user", method = RequestMethod.PUT)
    public String updateUser(User user) {
        System.out.println("修改用户信息：" + user);
        return "success";
    }

    @RequestMapping(value = "/user", method = RequestMethod.DELETE)
    public String deleteUser(User user) {
        System.out.println("删除用户信息：" + user);
        return "success";
    }
}
