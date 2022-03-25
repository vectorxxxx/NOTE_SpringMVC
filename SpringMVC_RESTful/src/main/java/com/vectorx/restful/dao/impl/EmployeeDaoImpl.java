package com.vectorx.restful.dao.impl;

import com.vectorx.restful.com.vectorx.restful.bean.Employee;
import com.vectorx.restful.dao.EmployeeDao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class EmployeeDaoImpl implements EmployeeDao {
    private static Map<Integer, Employee> employeeMap;
    private static Integer initId = 1000;

    static {
        employeeMap = new HashMap<>();
        employeeMap.put(++initId, new Employee(initId, "张三", "zhangsan@qq.com", 1));
        employeeMap.put(++initId, new Employee(initId, "李四", "lisi@qq.com", 0));
        employeeMap.put(++initId, new Employee(initId, "王五", "wangwu@qq.com", 0));
        employeeMap.put(++initId, new Employee(initId, "赵六", "zhaoliu@qq.com", 1));
    }

    @Override
    public void save(Employee employee) {
        if (employee.getId() == null) {
            employee.setId(++initId);
        }
        employeeMap.put(employee.getId(), employee);
    }

    @Override
    public void deleteById(Integer id) {
        employeeMap.remove(id);
    }

    @Override
    public List<Employee> getAll() {
        return new ArrayList<>(employeeMap.values());
    }

    @Override
    public Employee getById(Integer id) {
        return employeeMap.get(id);
    }
}
