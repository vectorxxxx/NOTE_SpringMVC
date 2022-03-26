package com.vectorx.restful.controller;

import com.vectorx.restful.com.vectorx.restful.bean.Employee;
import com.vectorx.restful.dao.EmployeeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/employeeController")
public class EmployeeController {
    @Autowired
    private EmployeeDao employeeDao;

    // ======列表功能======
    @GetMapping("/employee")
    public String getAllEmployee(Model model) {
        List<Employee> employeeList = employeeDao.getAll();
        model.addAttribute("employeeList", employeeList);
        return "employeelist";
    }

    // ======删除功能======
    @DeleteMapping("/employee/{id}")
    public String deleteEmployee(@PathVariable("id") Integer id) {
        employeeDao.deleteById(id);
        return "redirect:/employeeController/employee";
    }

    // ======添加功能======
    @GetMapping("/toAdd")
    public String toAdd() {
        return "employeeadd";
    }

    @PostMapping("/employee")
    public String addEmployee(Employee employee) {
        employeeDao.save(employee);
        return "redirect:/employeeController/employee";
    }

    // ======修改功能======
    @GetMapping("/employee/{id}")
    public String getEmployeeById(@PathVariable("id") Integer id, Model model) {
        Employee employee = employeeDao.getById(id);
        model.addAttribute("employee", employee);
        return "employeeedit";
    }

    @PutMapping("/employee")
    public String editEmployee(Employee employee) {
        employeeDao.save(employee);
        return "redirect:/employeeController/employee";
    }
}
