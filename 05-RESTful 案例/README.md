> 笔记来源：[【尚硅谷】SpringMVC教程丨一套快速上手spring mvc](https://www.bilibili.com/video/BV1Ry4y1574R)

[TOC]

# RESTful 案例

## 1、准备工作

### Step1、创建工程

`File`-`New`-`Project`，默认`Next`

![image-20220325203946095](https://s2.loli.net/2022/03/25/YV38g6bRBiNtqTC.png)

填写项目工程基本信息，点击`FINISH`

![image-20220325204142719](https://s2.loli.net/2022/03/25/Tx2hqYFzBKysZ9r.png)

### Step2、完善 POM

修改打包方式为`war`，并引入相关依赖

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.vectorx</groupId>
    <artifactId>SpringMVC_RESTful</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>war</packaging>

    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
            <version>5.3.16</version>
        </dependency>
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>1.2.11</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>4.0.1</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>org.thymeleaf</groupId>
            <artifactId>thymeleaf-spring5</artifactId>
            <version>3.0.15.RELEASE</version>
        </dependency>
    </dependencies>
</project>
```

### Step3、web.xml

`File`-`Project Structure`-`Modules`，在`Deployment Descriptors`中点击`+`号添加`Deployment Descriptor Location`，默认路径中不带`src\main\webapp\`，需要手动添加

![image-20220325205524524](https://s2.loli.net/2022/03/25/cBZ2JxXRdPmpS3F.png)

在`web.xml`中添加两个过滤器和一个前端控制器：

- 编码过滤器：`CharacterEncodingFilter`（注意顺序）
- 处理`PUT`和`DELETE`的请求过滤器：`HiddenHttpMethodFilter`
- 前端控制器：`DispatcherServlet`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">
    <!--配置编码过滤器-->
    <filter>
        <filter-name>CharacterEncodingFilter</filter-name>
        <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
        <init-param>
            <param-name>encoding</param-name>
            <param-value>UTF-8</param-value>
        </init-param>
        <init-param>
            <param-name>forceResponseEncoding</param-name>
            <param-value>true</param-value>
        </init-param>
    </filter>
    <filter-mapping>
        <filter-name>CharacterEncodingFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>

    <!--配置处理 PUT 和 DELETE 的请求过滤器-->
    <filter>
        <filter-name>HiddenHttpMethodFilter</filter-name>
        <filter-class>org.springframework.web.filter.HiddenHttpMethodFilter</filter-class>
    </filter>
    <filter-mapping>
        <filter-name>HiddenHttpMethodFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>

    <!--配置 SpringMVC 前端控制器-->
    <servlet>
        <servlet-name>DispatcherServlet</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:springMVC.xml</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
        <servlet-name>DispatcherServlet</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
</web-app>
```

### Step4、SpringMVC 配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd http://www.springframework.org/schema/mvc https://www.springframework.org/schema/mvc/spring-mvc.xsd">

    <!--自动扫描包-->
    <context:component-scan base-package="com.vectorx.restful"></context:component-scan>

    <!--配置Thymeleaf视图解析器-->
    <bean id="ThymeleafViewResolver" class="org.thymeleaf.spring5.view.ThymeleafViewResolver">
        <property name="order" value="1"></property>
        <property name="characterEncoding" value="UTF-8"></property>
        <property name="templateEngine">
            <bean class="org.thymeleaf.spring5.SpringTemplateEngine">
                <property name="templateResolver">
                    <bean class="org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver">
                        <property name="prefix" value="/WEB-INF/templates/"></property>
                        <property name="suffix" value=".html"></property>
                        <property name="templateMode" value="HTML5"></property>
                        <property name="characterEncoding" value="UTF-8"></property>
                    </bean>
                </property>
            </bean>
        </property>
    </bean>

    <!--配置视图控制器-->
    <mvc:view-controller path="/" view-name="index"></mvc:view-controller>

    <!--配置MVC注解驱动-->
    <mvc:annotation-driven/>
</beans>
```

### Step5、创建 Controller、Dao、Bean

EmployeeController

```java
@Controller
@RequestMapping("/employeeController")
public class EmployeeController {
    @Autowired
    private EmployeeDao employeeDao;
}
```

EmployeeDao

```java
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
```

EmployeeDaoImpl

```java
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
```

Employee

```java

public class Employee {
    private Integer id;
    private String lastName;
    private String email;
    private Integer gender;

    public Employee() {
    }

    public Employee(Integer id, String lastName, String email, Integer gender) {
        this.id = id;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
```



## 2、功能清单

| 功能               | URL路径       | 请求方式 |
| :----------------- | :------------ | :------- |
| 访问首页           | `/`           | `GET`    |
| 查询所有员工       | `/employee`   | `GET`    |
| 删除员工           | `/employee/1` | `DELETE` |
| 跳转到添加员工页面 | `/toAdd`      | `GET`    |
| 添加员工           | `/employee`   | `POST`   |
| 跳转到修改员工页面 | `/employee/2` | `GET`    |
| 修改员工           | `/employee`   | `PUT`    |



## 3、访问首页

index.html

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>首页</title>
</head>
<body>
<h1>首页</h1>
<a th:href="@{/employeeController/employee}">查看员工信息</a>
</body>
</html>
```

测试

![image-20220325222501012](https://s2.loli.net/2022/03/25/fK26yMwpDPVQmGS.png)





## 4、列表功能

EmployeeController.java

```java
@GetMapping("/employee")
public String getAllEmployee(Model model) {
    List<Employee> employeeList = employeeDao.getAll();
    model.addAttribute("employeeList", employeeList);
    return "employeelist";
}
```

employeelist.html

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
    <head>
        <meta charset="UTF-8">
        <title>员工信息</title>
        <style type="text/css">
            table {
                width: 50%;
                border: 1px black solid;
                border-collapse: collapse;
                vertical-align: middle;
                text-align: center;
            }
            tbody tr:nth-child(odd) {
                background-color: rgb(211, 216, 188);
            }
            th, td {
                border: 1px black solid;
            }
        </style>
    </head>
    <body>
        <table>
            <tr>
                <th colspan="5">员工信息</th>
            </tr>
            <tr>
                <th>ID</th>
                <th>姓名</th>
                <th>邮箱</th>
                <th>性别</th>
                <th>操作</th>
            </tr>
            <tr th:each="employee : ${employeeList}">
                <td th:text="${employee.id}"></td>
                <td th:text="${employee.lastName}"></td>
                <td th:text="${employee.email}"></td>
                <td th:text="${employee.gender == 1 ? '男' : '女'}"></td>
                <td>
                    <a href="">修改</a>
                    <a href="">删除</a>
                </td>
            </tr>
        </table>
    </body>
</html>
```

效果

![image-20220325231639027](https://s2.loli.net/2022/03/25/Y1vh6ASftp3FcED.png)



## 5、删除功能

在`webapp`下新建`static/css`和`static/js`，用来放置`css`文件和`js`文件

- 引入`static/js/vue.js`
- `static/css/employeelist.css`作为外部样式文件
- `static/js/employeelist.js`作为外部`js`文件

employeelist.html

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
    <head>
        <meta charset="UTF-8">
        <title>员工信息</title>
        <link th:href="@{/static/css/employeelist.css}" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <table id="employeeTable">
            <tr>
                <th colspan="5">员工信息</th>
            </tr>
            <tr>
                <th>ID</th>
                <th>姓名</th>
                <th>邮箱</th>
                <th>性别</th>
                <th>操作</th>
            </tr>
            <tr th:each="employee : ${employeeList}">
                <td th:text="${employee.id}"></td>
                <td th:text="${employee.lastName}"></td>
                <td th:text="${employee.email}"></td>
                <td th:text="${employee.gender == 1 ? '男' : '女'}"></td>
                <td>
                    <a href="">修改</a>
                    <!--<a th:href="@{/employeeController/employee/}+${employee.id}">删除</a>-->
                    <a @click="deleteEmployee" th:href="@{'/employeeController/employee/'+${employee.id}}">删除</a>
                </td>
            </tr>
        </table>
        <form method="post" id="deleteForm">
            <input type="hidden" name="_method" value="delete"/>
        </form>
        <script type="text/javascript" th:src="@{/static/js/vue.js}"></script>
        <script type="text/javascript" th:src="@{/static/js/employeelist.js}"></script>
    </body>
</html>
```

employeelist.css

```css
table {
    width: 50%;
    border: 1px black solid;
    border-collapse: collapse;
    /* 垂直水平居中 */
    vertical-align: middle;
    text-align: center;
}

/* 间隔变色 */
tbody tr:nth-child(odd) {
    background-color: rgb(211, 216, 188);
}

th, td {
    border: 1px black solid;
}
```

employeelist.js

```js
var vue = new Vue({
    el: "#employeeTable",
    methods: {
        deleteEmployee: function (event) {
            var deleteForm = document.getElementById("deleteForm");
            deleteForm.action = event.target.href;
            deleteForm.submit();
            event.preventDefault();
        }
    }
});
```

SpringMVC 配置文件

```xml
<!--开放静态资源访问-->
<mvc:default-servlet-handler/>
```

效果

![动画  (0)](https://s2.loli.net/2022/03/26/3Var97NEBhb16tA.gif)