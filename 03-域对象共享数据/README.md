> 笔记来源：[【尚硅谷】SpringMVC教程丨一套快速上手spring mvc](https://www.bilibili.com/video/BV1Ry4y1574R)

[TOC]

# 域对象共享数据

## 1、三种域对象

- `Request`：一次请求
- `Session`：一次会话。从浏览器开启到浏览器关闭（只跟浏览器是否关闭有关，与服务器是否关闭无关）
  - **钝化**：浏览器未关闭而服务器关闭，`Session`数据序列化到磁盘上
  - **活化**：浏览器仍然关闭而服务器开启，将钝化内容读取到`Session`中
- `Application`/`Servlet Context`：上下文对象，整个应用范围。服务器开启时创建，服务器关闭时销毁，从头到尾只创建一次（只跟服务器是否关闭有关，与浏览器是否关闭无关）

选择域对象时，应该选择能实现功能、范围最小的域对象



## 2、向 request 域对象共享数据

### 2.1、通过 Servlet API

后台测试代码

```java
@RequestMapping("/testRequestByServletAPI")
public String testRequestByServletAPI(HttpServletRequest request) {
    request.setAttribute("testRequestScope", "hello, Servlet API!");
    return "successrequest";
}
```

前台测试代码

`index.html`

```html
<a th:href="@{/scopeController/testRequestByServletAPI}">通过Servlet API</a>
```

`successrequest.html`

```html
<p th:text="${testRequestScope}"></p>
```

测试结果

![动画  (4)](https://s2.loli.net/2022/03/21/yTbtYJzA5DiXmEG.gif)

可以发现，转发的页面中成功获取到了在后台通过`Request`对象向`request`域中设置的属性值并正确展示

### 2.2、通过 ModelAndView

> **食用方式**：在 SpringMVC 中，不管用的何种方式，本质上最后都会封装到`ModelAndView`。同时要注意使用`ModelAndView`向 request 域对象共享数据时，需要返回`ModelAndView`自身

后台测试代码

```java
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
```

前台测试代码

```html
<a th:href="@{/scopeController/testRequestByModelAndView}">通过 ModelAndView</a><br/>
```

测试结果

![动画  (5)](https://s2.loli.net/2022/03/21/dL7MXROFjkH5iNn.gif)

### 2.3、通过 Model

> **食用方式**：形式与`HttpServletRequest`类似

后台测试代码

```java
@RequestMapping("/testRequestByModel")
public String testRequestByModel(Model model) {
    //向请求域共享数据
    model.addAttribute("testRequestScope", "hello, ModelAndView!");
    return "successrequest";
}
```

前台测试代码

```html
<a th:href="@{/scopeController/testRequestByModel}">通过 Model</a><br/>
```

测试结果

![动画  (6)](https://s2.loli.net/2022/03/21/Z3Drj2poBkEwU4H.gif)

### 2.4、通过 Map

> **食用方式**：形式与`Model`方式类似

后台测试代码

```java
@RequestMapping("/testRequestByMap")
public String testRequestByMap(Map<String, Object> map) {
    //向请求域共享数据
    map.put("testRequestScope", "hello, Map!");
    return "successrequest";
}
```

前台测试代码

```html
<a th:href="@{/scopeController/testRequestByMap}">通过 Map</a><br/>
```

测试结果

![动画  (7)](https://s2.loli.net/2022/03/21/MawhQT9brj5S6u7.gif)

### 2.5、通过 ModelMap

> **食用方式**：形式与`Model`方式类似

后台测试代码

```java
@RequestMapping("/testRequestByModelMap")
public String testRequestByModelMap(ModelMap modelMap) {
    //向请求域共享数据
    modelMap.addAttribute("testRequestScope", "hello, ModelMap!");
    return "successrequest";
}
```

前台测试代码

```html
<a th:href="@{/scopeController/testRequestByModelMap}">通过 ModelMap</a><br/>
```

测试结果

![动画  (8)](https://s2.loli.net/2022/03/21/ZtIaicbxFRP8gzl.gif)

### 2.6、Model、ModelMap 和 Map

分别在上述对应的控制器方法中，添加打印 Model、ModelMap 和 Map 三个对象及其对应类名的逻辑

```java
System.out.println(model + "======" + model.getClass().getName());
System.out.println(map + "======" + map.getClass().getName());
System.out.println(modelMap + "======" + modelMap.getClass().getName());
```

通过分别点击前台超链接，并查看后台日志信息

```console
{testRequestScope=hello, Model!}======org.springframework.validation.support.BindingAwareModelMap
{testRequestScope=hello, Map!}======org.springframework.validation.support.BindingAwareModelMap
{testRequestScope=hello, ModelMap!}======org.springframework.validation.support.BindingAwareModelMap
```

可以发现

- Model、ModelMap 和 Map 三个对象输入格式是一致的，都为键值对形式
- 通过反射方法获取到的类都是同一个，即`BindingAwareModelMap`

查看`BindingAwareModelMap`的继承关系

![image-20220321211709324](https://s2.loli.net/2022/03/21/H8EP1QYU2daxoMe.png)

阅读源码，梳理出`Model`、`Map`、`ModelMap`三者的核心继承关系

```java
public class BindingAwareModelMap extends ExtendedModelMap {}
public class ExtendedModelMap extends ModelMap implements Model {}
public class ModelMap extends LinkedHashMap<String, Object> {}
public interface Model {}
```

可以发现

- `BindingAwareModelMap`继承`ModelMap`并实现`Model`接口
- `ModelMap`继承`LinkedHashMap`，而毫无疑问`LinkedHashMap`实现了`Map`接口

`Model`、`Map`和`ModelMap`三者的关系到此就一目了然了，其 UML 类图如下：

![ModelMap](https://s2.loli.net/2022/03/21/M7d1JNiq2Bg9Fl4.png)

> **结论**：`Model`、`Map`、`ModelMap`类型的形参本质上都是`BindingAwareModelMap`



## 3、向 session 域共享数据

> **食用方式**：形式与`HttpServletRequest`方式类似，形参为`HttpSession`。需要注意的是 SpringMVC 虽然提供了一个`@SessionAttribute`注解，但并不好用，因此反而建议直接使用原生 Servlet 中的`HttpSession`对象

后台测试代码

```java
@RequestMapping("/testSession")
public String testSession(HttpSession session) {
    //向session域共享数据
    session.setAttribute("testSessionScope", "hello, HttpSession!");
    return "successsession";
}
```

前台测试代码

`index.html`

```html
<a th:href="@{/scopeController/testSession}">通过 Servlet API 向 Session 域对象共享数据</a><br/>
```

`successsession.html`

```html
<p th:text="${session.testSessionScope}"></p>
```

测试结果

![动画  (9)](https://s2.loli.net/2022/03/21/s45XlRTYFqumGQy.gif)



## 4、向 application 域共享数据

> **食用方式**：形式与`HttpSession`方式类似，只不过需要先从`session`对象中获取`ServletContext`上下文对象，即`application`域对象，再做操作

后台测试代码

```java
@RequestMapping("/testApplication")
public String testApplication(HttpSession session) {
    ServletContext application = session.getServletContext();
    application.setAttribute("testApplicationScope", "hello, application!");
    return "successapplication";
}
```

前台测试代码

`index.html`

```html
<a th:href="@{/scopeController/testApplication}">通过 Servlet API 向 Application 域对象共享数据</a><br/>
```

`successapplication.html`

```html
<p th:text="${application.testApplicationScope}"></p>
```

测试结果

![动画  (10)](https://s2.loli.net/2022/03/21/b7nYHiERTPwAOSN.gif)



## 总结

域对象有三种：`request`（请求域）、`session`（会话域）和`application`（上下文）

向`request`域对象共享数据方式：本质都是`ModelAndView`

- `Servlet API`（不推荐）：`HttpServletRequest`
- `ModelAndView`：需要返回自身
- `Model`、`Map`、`ModelMap`：本质都是`BindingAwareModelMap`

向`session`域共享数据：`HttpSession`

向`application`域共享数据：`ServletContext`

附上导图，仅供参考

![03-域对象共享数据](https://s2.loli.net/2022/03/21/zY5s3IE9omMUZSN.png)