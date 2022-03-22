> 笔记来源：[【尚硅谷】SpringMVC教程丨一套快速上手spring mvc](https://www.bilibili.com/video/BV1Ry4y1574R)

[TOC]

# SpringMVC 视图

SpringMVC 中的视图是`View`接口，视图的作用渲染数据，将模型`Model`中的数据展示给用户 

SpringMVC 视图的种类很多，默认有**转发视图**`InternalResourceView`和**重定向视图**`RedirectView`

当工程引入`jstl`的依赖，转发视图会自动转换为`JstlView`（JSP 内容了解即可）

若使用的视图技术为`Thymeleaf`，在 SpringMVC 的配置文件中配置了`Thymeleaf`的视图解析器，由此视图解析器解析之后所得到的是`ThymeleafView`

> **注意**：只有在视图名称没有任何前缀时，视图被`Thymeleaf`视图解析器解析之后，创建的才是`ThymeleafView`。当视图名称包含前缀（如`forward:`或`redirect:`）时，分别对应的时`InternalResourceView`转发视图和`RedirectView`重定向视图

## 1、ThymeleafView

当控制器方法中所设置的视图名称没有任何前缀时，此时的视图名称会被 SpringMVC 配置文件中所配置的视图解析器解析，视图名称拼接视图前缀和视图后缀所得到的最终路径，会通过转发的方式实现跳转

后台测试代码

```java
@RequestMapping("/testThymeleaftView")
public String testThymeleaftView() {
    return "success";
}
```

前台测试代码

```html
<a th:href="@{/viewController/testThymeleaftView}">测试 ThymeleaftView</a><br/>
```

断点调试，查看创建的`View`视图对象为`ThymeleafView`对象

![image-20220322200839355](https://s2.loli.net/2022/03/22/D3KZ6tgpVO9Xozn.png)



## 2、转发视图

SpringMVC 中默认的转发视图是`InternalResourceView`

创建转发视图的情况：当控制器方法中所设置的视图名称以`forward:`为前缀时，创建`InternalResourceView`视图，此时的视图名称不会被 SpringMVC 配置文件中所配置的视图解析器解析，而是会将前缀`forward:`去掉，剩余部分作为最终路径通过转发的方式实现跳转

例如：`forward:/`、`forward:/viewController/testThymeleaftView`

后台测试代码

```java
@RequestMapping("/testForward")
public String testForward() {
    return "forward:/viewController/testThymeleaftView";
}
```

前台测试代码

```html
<a th:href="@{/viewController/testForward}">测试 InternalResourceView</a><br/>
```

断点调试，查看创建的`View`视图对象为`InternalResourceView`对象

![image-20220322201800705](https://s2.loli.net/2022/03/22/VCd79uTnoa3bBWw.png)

## 3、重定向视图

SpringMVC中默认的重定向视图是`RedirectView`

创建重定向视图的情况：当控制器方法中所设置的视图名称以`redirect:`为前缀时，创建`RedirectView`视图，此时的视图名称不会被 SpringMVC 配置文件中所配置的视图解析器解析，而是会将前缀`redirect:`去掉，剩余部分作为最终路径通过重定向的方式实现跳转

例如：`forward:/`、`forward:/viewController/testThymeleaftView`

后台测试代码

```java
@RequestMapping("/testRedirect")
public String testRedirect() {
    return "redirect:/viewController/testThymeleaftView";
}
```

前台测试代码

```html
<a th:href="@{/viewController/testRedirect}">测试 RedirectView</a><br/>
```

断点调试，查看创建的`View`视图对象为`RedirectView`对象

![image-20220322204348222](https://s2.loli.net/2022/03/22/IRZtcW1pUFA6kTx.png)



## 4、转发和重定向

转发和重定向的区别

- 1）转发对于浏览器来说只发送一次请求（另一次请求实际上在服务器内部发生，只是客户端中看不到而已）；重定向对于浏览器来说发送两次请求（不管是转发还是重定向，在服务器中其实都是两次请求）
- 2）转发在浏览器地址栏中呈现的是发送请求时的地址；重定向在浏览器地址栏中呈现的是重定向后的地址
- 3）转发时`request`域对象为同一个；重定向时`request`域对象不是同一个
- 4）转发可以访问`WEB-INF`下资源；重定向不能访问`WEB-INF`下资源（因为`WEB-INF`下资源具有安全性、隐藏性，只能通过服务器内部访问，不能通过服务器外部访问）
- 5）转发不可以跨域；重定向可以跨域（因为转发是在服务器内部发生的，所以只能访问服务器内部资源；而重定向是浏览器发送的两次请求，可以访问任意资源。如可以重定向到某度，但不能转发到某度）

以表格方式来对比*转发*和*重定向*

| 页面跳转方式                  | 转发     | 重定向     |
| :---------------------------- | :------- | :--------- |
| **请求次数（对浏览器而言）**  | 1️⃣        | 2️⃣          |
| **浏览器地址栏地址**          | 请求地址 | 重定向地址 |
| **request 域对象是否同一个**  | ✔️        | ❌          |
| **是否可访问 WEB-INF 下资源** | ✔️        | ❌          |
| **是否可跨域**                | ❌        | ✔️          |



## 5、视图控制器 view-controller

当前请求映射对应的控制器方法中，仅仅用来实现页面跳转，而没有其他请求过程的处理，即只需设置一个视图名称时，就可以将控制器方法使用`view-controller`标签进行表示

例如：我们在`HelloController`中配置的一个控制器方法，对应`view`请求，返回`view`视图

```java
@RequestMapping("/view")
public String view() {
    return "view";
}
```

此时通过在SpringmMVC 配置文件中添加`<mvc:view-controller>`标签，就可以代替上述控制器方法（将上述方法注释即可）

```xml
<mvc:view-controller path="/view" view-name="view"></mvc:view-controller>
```

其中

- `path`对应控制器方法上`@RequestMapping`中路径
- `view-name`对应控制器方法返回的视图名称

此时再来访问`/view`，同样会被`Thymeleaf`视图解析器解析，拼接上视图前缀和视图后缀后，找到对应路径下的`view.html`页面

> **注意**：在 SpringMVC 配置文件中配置了`view-controller`之后，控制器中所有的请求映射都会失效

测试结果

![动画  (1)](https://s2.loli.net/2022/03/22/u8g3hf2kEIynwYl.gif)

怎么解决这个问题呢？我们需要在 SpringMVC 配置文件中开启 MVC 的注解驱动

```xml
<!--
    当SpringMVC中设置任何一个view-controller时，其他控制器中的请求映射将全部失效，
    此时需要在SpringMVC的核心配置文件中设置开启mvc注解驱动的标签：
    -->
<!--开启 MVC 的注解驱动-->
<mvc:annotation-driven/>
```

 测试结果

![动画  (2)](https://s2.loli.net/2022/03/22/wfJnQeES5VbxX1K.gif)

> **额外的**：MVC 的注解驱动功能很多，例如
>
> 1、如果加上了默认的 Servlet 处理静态资源（如 JS、CSS 等），控制器请求映射会失效，这时需要配置 MVC 的注解驱动
>
> 2、JAVA 对象转换为 JSON 对象，同样需要配置 MVC 的注解驱动
>
> 因为使用场景很多，所以一般情况下 MVC 注解驱动默认是需要配置的。但是注意，需要了解在不同情况下 MVC 注解驱动的功能是什么



## 6、InternalResourceViewResolver

因为这里是使用`JSP`作为对`InternalResourceViewResolver`视图解析器的讲解，所以仅做了解即可

SpringMVC 配置文件：这里使用`InternalResourceViewResolver`代替`ThymeleafViewResolver`

```xml
<bean id="InternalResourceViewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver">
    <property name="prefix" value="/WEB-INF/templates/"/>
    <property name="suffix" value=".jsp"/>
</bean>
```

后台测试代码

```java
@Controller
public class JspController {
    @RequestMapping("/success")
    public String success() {
        return "success";
    }
}
```

前台测试代码

`index.jsp`

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Jsp</title>
    </head>
    <body>
        <a href="${pageContext.request.contextPath}/success">success.jsp</a>
    </body>
</html>
```

`success.jsp`

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Success</title>
    </head>
    <body>
        <h1>Success</h1>
    </body>
</html>
```

测试结果

![动画  (3)](https://s2.loli.net/2022/03/22/YOimhsXNCJEayBx.gif)



## 总结

本节内容较少，主要掌握

- SpringMVC 中默认的视图：`InternalResourceView`、`RedirectView`
  - 使用`forward:`前缀：`InternalResourceView`视图
  - 使用`redirect:`前缀：`RedirectView`视图
- `Thymeleaf`对应`ThymeleafView`视图（无任何前缀时），`jstl`对应`JstlView`
- 注意转发和重定向的区别：请求次数、浏览器地址栏地址、`request`域对象、访问`WEB-INF`下资源、跨域等方面
- `InternalResourceViewResolver`视图解析器的使用

附上导图，仅供参考

![04-SpringMVC 视图](https://s2.loli.net/2022/03/22/O1EviFRJZ9p6Dr5.png)