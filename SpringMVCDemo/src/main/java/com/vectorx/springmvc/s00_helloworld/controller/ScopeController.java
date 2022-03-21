package com.vectorx.springmvc.s00_helloworld.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/scopeController")
public class ScopeController {

    @RequestMapping("/testRequestByServletAPI")
    public String testRequestByServletAPI(HttpServletRequest request) {
        request.setAttribute("testRequestScope", "hello, Servlet API!");
        return "successrequest";
    }

    @RequestMapping("/testRequestByModelAndView")
    public ModelAndView testRequestByModelAndView() {
        /**
         * ModelAndView有Model和View两个功能
         * Model用于向请求域共享数据
         * View用于设置视图，实现页面跳转
         */
        ModelAndView mv = new ModelAndView();
        //向请求域共享数据
        mv.addObject("testRequestScope", "hello, ModelAndView!");
        //设置视图，实现页面跳转
        mv.setViewName("successrequest");
        return mv;
    }

    @RequestMapping("/testRequestByModel")
    public String testRequestByModel(Model model) {
        //向请求域共享数据
        model.addAttribute("testRequestScope", "hello, Model!");
        System.out.println(model + "======" + model.getClass().getName());
        return "successrequest";
    }

    @RequestMapping("/testRequestByMap")
    public String testRequestByMap(Map<String, Object> map) {
        //向请求域共享数据
        map.put("testRequestScope", "hello, Map!");
        System.out.println(map + "======" + map.getClass().getName());
        return "successrequest";
    }

    @RequestMapping("/testRequestByModelMap")
    public String testRequestByModelMap(ModelMap modelMap) {
        //向请求域共享数据
        modelMap.addAttribute("testRequestScope", "hello, ModelMap!");
        System.out.println(modelMap + "======" + modelMap.getClass().getName());
        return "successrequest";
    }

    @RequestMapping("/testSession")
    public String testSession(HttpSession session) {
        session.setAttribute("testSessionScope", "hello, HttpSession!");
        return "successsession";
    }

    @RequestMapping("/testApplication")
    public String testApplication(HttpSession session) {
        ServletContext application = session.getServletContext();
        application.setAttribute("testApplicationScope", "hello, application!");
        return "successapplication";
    }
}
