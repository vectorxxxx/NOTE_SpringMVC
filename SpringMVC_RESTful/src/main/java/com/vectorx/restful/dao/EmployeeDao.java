package com.vectorx.restful.dao;

import com.vectorx.restful.com.vectorx.restful.bean.Employee;

import java.util.List;

public interface EmployeeDao {
    /**
     * 增改员工信息
     *
     * @param employee
     */
    void save(Employee employee);

    /**
     * 删除员工信息
     *
     * @param id
     */
    void deleteById(Integer id);

    /**
     * 获取所有员工信息
     *
     * @return
     */
    List<Employee> getAll();

    /**
     * 根据员工id获取员工信息
     *
     * @param id
     * @return
     */
    Employee getById(Integer id);
}
