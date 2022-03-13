> 笔记来源：[【尚硅谷】SpringMVC教程丨一套快速上手spring mvc](https://www.bilibili.com/video/BV1Ry4y1574R)

[TOC]

# SpringMVC 简介

## 1、课程介绍

![image-20220313095312928](https://s2.loli.net/2022/03/13/bGCqw6mIrO8sxTj.png)



## 2、什么是 MVC？

MVC 是一种软件架构思想，将软件分为<mark>模型、视图、控制器</mark>三部分

- M（Model，模型层）：处理数据的 JavaBean 类
  - 实体类 Bean：存储业务数据
  - 业务处理 Bean：Service 或 Dao，处理业务逻辑和数据访问
- V（View，视图层）：展示数据的 HTML 页面，与用户进行交互
- C（Controller，控制层）：接受请求和响应的 Servlet

**MVC 工作流程**

1. 用户通过视图层发送请求到服务器，在服务器中请求被Controller 接收
2. Controller 调用相应的 Model 层处理请求
3. Model 层处理完毕将结果返回到 Controller
4. Controller 再根据请求处理的结果找到相应的 View 视图
5. View 视图渲染数据后最终响应给浏览器



## 3、什么是 SpringMVC？

SpringMVC 是 Spring 的一个后续产品，是Spring的一个子项目

SpringMVC 是 Spring 为表述层开发提供的一整套完备的解决方案。在表述层框架历经 Struts、WebWork、Strust2 等诸多产品的历代更迭之后，目前业界普遍选择了 SpringMVC 作为 JavaEE 项目表述层开发的*首选方案*

> 注：三层架构分为<mark>表述层（或表示层）、业务逻辑层、数据访问层</mark>，表述层表示前台页面和后台 Servlet



## 4、SpringMVC 的特点

- <mark>Spring 家族原生产品</mark>：与 IOC 容器等基础设施无缝对接
- <mark>基于原生的 Servlet</mark>：通过了功能强大的<mark>前端控制器 DispatcherServlet</mark>，对请求和响应进行统一处理
- <mark>全面解决方案</mark>：表述层各细分领域需要解决的问题<mark>全方位覆盖</mark>
- <mark>代码清新简洁</mark>：大幅度提升开发效率
- <mark>即插即用</mark>：内部组件化程度高，组件可插拨，想要什么功能配置相应组件即可
- <mark>性能卓著</mark>：尤其适合现代大型、超大型互联网项目要求



## 5、HelloWorld

### 5.1、开发环境

- **IDE**：idea 2021.1
- **构建工具**：maven-3.8.3
- **服务器**：tomcat7
- **Spring版本**：5.3.16

### 5.2、创建 Maven 工程

1）新建工程，默认 NEXT

![image-20220313104139012](https://s2.loli.net/2022/03/13/oehVtcZrb6B3F9p.png)

2）填写工程名称、保存为止和 GAV 坐标，点击 FINISH

![image-20220313104207281](https://s2.loli.net/2022/03/13/tZPwh7a5kliSVjL.png)

3）`pom.xml`中添加并导入依赖

```xml
<dependencies>
    <!-- https://mvnrepository.com/artifact/org.springframework/spring-webmvc -->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-webmvc</artifactId>
        <version>5.3.16</version>
    </dependency>
    <!-- https://mvnrepository.com/artifact/ch.qos.logback/logback-classic -->
    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-classic</artifactId>
        <version>1.2.11</version>
        <scope>test</scope>
    </dependency>
    <!-- https://mvnrepository.com/artifact/javax.servlet/javax.servlet-api -->
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>javax.servlet-api</artifactId>
        <version>4.0.1</version>
        <scope>provided</scope>
    </dependency>
    <!-- https://mvnrepository.com/artifact/org.thymeleaf/thymeleaf-spring5 -->
    <dependency>
        <groupId>org.thymeleaf</groupId>
        <artifactId>thymeleaf-spring5</artifactId>
        <version>3.0.15.RELEASE</version>
    </dependency>
</dependencies>
```

根据依赖的传递性，相关的依赖也会被导入

![image-20220313115336752](https://s2.loli.net/2022/03/13/5SvltKnfRzopJZb.png)

4）项目工程结构中添加 web 模块，注意`web.xml`的路径要放在`src\main\webapp`下

![image-20220313110502676](https://s2.loli.net/2022/03/13/VGZl2ae5xorPYpy.png)

完成后的目录结构

![image-20220313105817035](https://s2.loli.net/2022/03/13/cdofMtVZmRAE1PX.png)

### 5.3、配置 web.xml

为什么要配置`web.xml`？注册 SpringMVC 的前端控制器 DispatcherServlet 

#### 1）默认配置方式

此配置作用下，SpringMVC 的配置文件默认位于 WEB-INF 下，默认名称为`<servlet-name>-servlet.xml`

例如，以下配置所对应 SpringMVC 的配置文件位于 WEB-INF 下，文件名为`springMVC-servlet.xml`

```xml
<!--配置 SpringMVC 的前端控制器，对浏览器发送的请求统一进行处理-->
<servlet>
    <servlet-name>springMVC</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>springMVC</servlet-name>
    <!--
      设置 SpringMVC 的核心控制器所能处理的请求的请求路径
      / 所匹配的请求可以是 /login 或 .html 或 .js 或 .css 方式的请求路径
      但是 / 不能匹配 .jsp 请求路径的请求
    -->
    <url-pattern>/</url-pattern>
</servlet-mapping>
```

默认配置方式对位置和名称都是默认的，这样并不好！Maven 工程配置文件应该统一放置在`resources`下，应该如何来实现呢？来看下面的“扩展配置方式”

#### 2）扩展配置方式

- 通过`init-param`标签设置 SpringMVC 配置文件的位置和名称
- 通过`load-on-startup`标签设置 SpringMVC 前端控制器 DispatcherServlet 的初始化时间

```xml
<!--配置 SpringMVC 的前端控制器，对浏览器发送的请求统一进行处理-->
<servlet>
    <servlet-name>springMVC</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <!--通过初始化参数指定SpringMVC配置文件的位置和名称-->
    <init-param>
        <!--contextConfigLocation 为固定值-->
        <param-name>contextConfigLocation</param-name>
        <!--使用 classpath: 表示从类路径查找配置文件，java 工程默认src下,maven 工程默认 src/main/resources 下-->
        <param-value>classpath:springMVC.xml</param-value>
    </init-param>
    <!--
        作为框架的核心组件，在启动过程中有大量的初始化操作要做
        而这些操作放在第一次请求时才执行会严重影响访问速度
        将前端控制器 DispatcherServlet 的初始化时间提前到服务启动时
    -->
    <load-on-startup>1</load-on-startup>
</servlet>
<servlet-mapping>
    <servlet-name>springMVC</servlet-name>
    <!--
        设置 SpringMVC 的核心控制器所能处理的请求的请求路径
        / 所匹配的请求可以是 /login 或 .html 或 .js 或 .css 方式的请求路径
        但是 / 不能匹配 .jsp 请求路径的请求
    -->
    <url-pattern>/</url-pattern>
</servlet-mapping>
```

> 注：`<url-pattern>`标签中使用`/`和`/*`的区别：
>
> `/`所匹配的请求可以是`/login`或`.html`或`.js`或`.css`方式的请求路径，但是`/`不能匹配`.jsp`请求路径的请求
>
> 因此就可以避免在访问`.jsp`页面时，该请求被 DispatcherServlet 处理，从而找不到相应的页面的情况
>
> `/*`则能够匹配所有请求，例如在使用过滤器时，若需要对所有请求进行过滤，就需要使用`/*`的写法

### 5.4、创建请求控制器

由于前端控制器对浏览器发送的请求进行了统一的处理，但是具体的请求有不同的处理过程，因此需要创建处理具体请求的类，即<mark>请求控制器</mark>

请求控制器中每一个处理请求的方法称为<mark>控制器方法</mark>

因为 SpringMVC 的控制器由一个 POJO（普通 Java 类）担任，因此需要通过`@Controller`注解将其标识为一个控制层组件，交给 Spring 的 IOC 容器管理，此时 SpringMVC 才能够识别控制器的存在

```java
@Controller
public class HelloController {
}
```

### 5.5、创建 SpringMVC 配置文件

```xml
<!--自动扫描包-->
<context:component-scan base-package="com.vectorx.springmvc"></context:component-scan>
<!--配置Thymeleaf视图解析器-->
<bean id="viewResolver" class="org.thymeleaf.spring5.view.ThymeleafViewResolver">
    <property name="order" value="1"/>
    <property name="characterEncoding" value="UTF-8"/>
    <property name="templateEngine">
        <bean class="org.thymeleaf.spring5.SpringTemplateEngine">
            <property name="templateResolver">
                <bean class="org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver">
                    <!--视图前缀-->
                    <property name="prefix" value="/WEB-INF/templates/"/>
                    <!--视图后缀-->
                    <property name="suffix" value=".html"/>
                    <property name="templateMode" value="HTML5"/>
                    <property name="characterEncoding" value="UTF-8"/>
                </bean>
            </property>
        </bean>
    </property>
</bean>
```

### 5.6、测试

#### 1）访问首页

创建首页`index.html`

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>首页</title>
</head>
<body>
<h1>首页</h1>
</body>
</html>
```

在请求控制器中创建处理请求的方法

```java
//@RequestMapping 注解：处理请求和控制器方法之间的映射关系
//@RequestMapping 注解的 value 属性可以通过请求地址匹配请求，/ 表示的当前工程的上下文路径
// localhost：8080/springMVC/
@RequestMapping("/")
public String index() {
    //返回视图名称
    return "index";
}
```

访问首页

![image-20220313131435926](https://s2.loli.net/2022/03/13/SIAZ2UP4KlEwc3u.png)

#### 2）访问指定页面

在主页`index.html`创建超链接

```html
<a href="/target">访问指定页面target.html</a>
```

但是这种写法是不行的，可以看到，当鼠标悬浮在超链接上时，左下角的跳转路径提示信息从 8080 下访问的

这是因为我们是以`/`开头的，它分为浏览器解析和服务器解析两种方式，而超链接中的绝对路径就是由浏览器解析的，而不是从上下文路径访问

![image-20220313133729906](https://s2.loli.net/2022/03/13/xjh9Xokm83rZzbJ.png)

虽然我们可以通过添加上下文的方式实现，因为上下文路径可以改，所以这种方式肯定是杜绝的

```html
<a href="/SpringMVC/target">访问指定页面target.html</a>
```

那应该如何处理呢？这里就可以使用`thymeleaf`来动态获取上下文路径

- 首先需要在`<html>`标签中引入`thymeleaf`的命名空间`xmlns:th="http://www.thymeleaf.org"`
- 然后使用`th:`前缀修饰标签属性，这里使用`th:href`来修饰`<a>`标签的`<href>`属性
- 最后`th:href`中的属性值中包裹一层`@{}`，这里值为`@{/target.html}`

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>首页</title>
</head>
<body>
<h1>首页</h1>
<a th:href="@{/target}">访问指定页面target.html</a>
</body>
</html>
```

同时，后台请求控制器也要加上对`target`请求进行处理的控制器方法

```java
@RequestMapping("/target")
public String toTarget() {
    return "target";
}
```

访问指定页面

![image-20220313133838685](https://s2.loli.net/2022/03/13/NlEHh2nZS1jRQtm.png)

跳转成功

![image-20220313133851794](https://s2.loli.net/2022/03/13/xelMJA5am6c3PIE.png)



## 6、SpringMVC 请求处理底层原理

- 浏览器发送请求，若请求地址符合前端控制器的`url-pattern`，该请求就会被前端控制器 DispatcherServlet 处理
- 前端控制器会读取 SpringMVC 的核心配置文件，通过扫描组件找到控制器，将请求地址和控制器中`@RequestMapping`注解的`value`属性值进行匹配。若匹配成功，该注解所标识的控制器方法就是处理请求的方法
- 处理请求的方法需要返回一个字符串类型的视图名称，该视图名称会被视图解析器解析，加上前缀和后缀组成视图的路径，通过 Thymeleaf 对视图进行渲染，最终*转发*到视图所对应页面



## 附录：SpringMVC 工程创建整体流程

**概览**

- 1）配置`pom.xml`、`web.xml`、`springMVC.xml`
- 2）创建前台页面和后台请求控制器

**详解**

1）添加`pom.xml`依赖

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.vectorx</groupId>
    <artifactId>springmvc-demo</artifactId>
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

2）创建`web.xml`配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">
    <servlet>
        <servlet-name>springMVC</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:springMVC.xml</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
        <servlet-name>springMVC</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
</web-app>
```

3）创建`springMVC.xml`配置文件

```xml
<context:component-scan base-package="com.vectorx.springmvc"></context:component-scan>
<bean id="viewResolver" class="org.thymeleaf.spring5.view.ThymeleafViewResolver">
    <property name="order" value="1"/>
    <property name="characterEncoding" value="UTF-8"/>
    <property name="templateEngine">
        <bean class="org.thymeleaf.spring5.SpringTemplateEngine">
            <property name="templateResolver">
                <bean class="org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver">
                    <property name="prefix" value="/WEB-INF/templates/"/>
                    <property name="suffix" value=".html"/>
                    <property name="templateMode" value="HTML5"/>
                    <property name="characterEncoding" value="UTF-8"/>
                </bean>
            </property>
        </bean>
    </property>
</bean>
```

4）创建前台页面

`index.html`

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>首页</title>
</head>
<body>
<h1>首页</h1>
<a th:href="@{/target}">访问指定页面target.html</a>
</body>
</html>
```

`target.html`

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>HelloWorld</title>
</head>
<body>
<h1>Hello World!</h1>
</body>
</html>
```

5）创建`Controller`请求控制器

```java
@Controller
public class HelloController {
    @RequestMapping("/")
    public String index() {
        //返回视图名称
        return "index";
    }
    @RequestMapping("/target")
    public String toTarget() {
        return "target";
    }
}
```



## 总结

最后奉上本节导图，内容仅供参考

![00-SpringMVC 简介](https://s2.loli.net/2022/03/13/5lmkNP4cw6JsuAe.png)