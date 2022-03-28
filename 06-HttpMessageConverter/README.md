> 笔记来源：[【尚硅谷】SpringMVC教程丨一套快速上手spring mvc](https://www.bilibili.com/video/BV1Ry4y1574R)

[TOC]

# HttpMessageConverter

- `HttpMessageConverter`：报文信息转换器，将请求报文转换为 Java 对象，或将 Java 对象转换为响应报文
- `HttpMessageConverter`：提供了两个注解和两个类型：`@RequestBody`、`@ResponseBody`、`RequestEntity`、`ResponseEntity`

## 1、@RequestBody

`@RequestBody`可以获取请求体，需要在控制器方法设置一个形参，使用`@RequestBody`进行标识，当前请求的请求体就会为当前注解所标识的形参赋值

后台测试代码

```java
@Controller
@RequestMapping("/httpController")
public class HttpController {
    @PostMapping("/testRequestBody")
    public String testRequestBody(@RequestBody String requestBody) {
        System.out.println("requestBody: " + requestBody);
        return "success";
    }
}
```

前台测试代码

```html
<form th:action="@{httpController/testRequestBody}" method="post">
    用户名：<input type="text" name="username"><br/>
    密码：<input type="password" name="password"><br/>
    <input type="submit" value="测试@RequestBody">
</form>
```

测试结果

![动画  (1)](https://s2.loli.net/2022/03/28/sCMNOrQwX71qJBG.gif)

后台日志信息

```console
requestBody: username=admin&password=123456
```



## 2、RequestEntity

`RequestEntity`：封装请求报文的一种类型，需要在控制器方法的形参中设置该类型的形参，当前请求的请求报文就会赋值给该形参

可以通过`getHeaders()`获取请求头信息，通过`getBody()`获取请求体信息

后台测试代码

```java
@PostMapping("/testRequestEntity")
public String testRequestEntity(RequestEntity<String> requestEntity) {
    System.out.println("requestHeader: " + requestEntity.getHeaders());
    System.out.println("requestBody: " + requestEntity.getBody());
    return "success";
}
```

前台测试代码

```html
<form th:action="@{httpController/testRequestEntity}" method="post">
    用户名：<input type="text" name="username"><br/>
    密码：<input type="password" name="password"><br/>
    <input type="submit" value="测试RequestEntity">
</form>
```

测试结果

![动画  (2)](https://s2.loli.net/2022/03/28/lrgPL3TActadbQY.gif)

后台日志信息

```console
requestHeader: [host:"localhost:8080", connection:"keep-alive", content-length:"30", cache-control:"max-age=0", sec-ch-ua:"" Not A;Brand";v="99", "Chromium";v="99", "Google Chrome";v="99"", sec-ch-ua-mobile:"?0", sec-ch-ua-platform:""Windows"", upgrade-insecure-requests:"1", origin:"http://localhost:8080", user-agent:"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.51 Safari/537.36", accept:"text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9", sec-fetch-site:"same-origin", sec-fetch-mode:"navigate", sec-fetch-user:"?1", sec-fetch-dest:"document", referer:"http://localhost:8080/SpringMVC/", accept-encoding:"gzip, deflate, br", accept-language:"zh-CN,zh;q=0.9", Content-Type:"application/x-www-form-urlencoded;charset=UTF-8"]
requestBody: username=admin&password=123456
```



## 3、@ResponseBody

### 3.1、通过 HttpServletResponse 响应浏览器数据

后台测试代码

```java
@GetMapping("/testResponseByServletAPI")
public void testResponseByServletAPI(HttpServletResponse response) throws IOException {
    response.getWriter().print("hello, response");
}
```

前台测试代码

```html
<a th:href="@{/httpController/testResponseByServletAPI}">通过 HttpServletResponse 响应浏览器数据</a>
```

测试结果

![动画  (3)](https://s2.loli.net/2022/03/28/XeaB1EYMn7RKkZi.gif)

### 3.2、通过 @ResponseBody 响应浏览器数据

后台测试代码

```java
@GetMapping("/testResponseBody")
@ResponseBody
public String testResponseBody() throws IOException {
    return "success";
}
```

前台测试代码

```html
<a th:href="@{/httpController/testResponseBody}">通过 @ResponseBody 响应浏览器数据</a>
```

测试结果

![动画  (4)](https://s2.loli.net/2022/03/28/bJGORgZUBh2HYt9.gif)

### 3.3、通过 @ResponseBody 响应 User

后台测试代码

```java
@GetMapping("/testResponseUser")
@ResponseBody
public User testResponseUser() throws IOException {
    return new User("admin", "123456");
}
```

前台测试代码

```html
<a th:href="@{/httpController/testResponseUser}">通过 @ResponseBody 响应 User</a>
```

测试结果

![动画  (5)](https://s2.loli.net/2022/03/28/LoBuUjEzbFAqH8n.gif)

我这里报了`406 - 不可接收`的错误

> **存疑点**：课程中报的是`500 - No converter found for return value of type: class xxx`，不知道是什么原因，此处存疑

![image-20220328213941797](https://s2.loli.net/2022/03/28/Z2wPU1jkVCEgicK.png)

> **Q**：那么如何解决这个问题？
>
> **A**：不要响应 Java 对象，而是转换成 Json 对象

### 3.4、通过 @ResponseBody 响应 JSON

处理方式：引入`jackson`依赖

```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.13.2</version>
</dependency>
```

这里不修改任何代码，直接重新部署项目，再来测试下

![动画  (6)](https://s2.loli.net/2022/03/28/g6ZGYk5H42EUsCz.gif)

这里需要强调的是，并不是只引入`jackson`依赖就够了。实际上，`@ResponseBody`处理`json`的步骤如下

- <u>1）引入`jackson`依赖</u>
- <u>2）开启 MVC 注解驱动</u>
  - 此时在`HandlerAdaptor`中会自动装配一个消息转换器`MappingJackson2HttpMessageConverter`，可以将响应到浏览器的 Java 对象转换`json`格式的字符串
- <u>3）使用`@ResponseBody`注解标识控制器方法</u>
- <u>4）将 Java 对象作为控制器方法的返回值返回</u>
  - 这里会自动转换为`json`类型的==字符串==

上述步骤缺一不可，少一步都实现不了效果

> **回顾**：我们之前章节中就说过 MVC 注解驱动的功能很多，在 *04-SpringMVC 视图* 中处理了视图控制器问题，在 *05-RESTful 案例* 中处理静态资源问题，而在本章节中处理了 Java 对象转换为 JSON 对象的问题。这里再总结一下 MVC 注解驱动的作用：
>
> 1、解决视图控制器`view-controller`造成其他请求失效的问题
>
> 2、解决默认 Servlet 处理器`default-servlet-handler`造成`DispatcherServlet`失效的问题
>
> 3、解决 Java 对象转换为 Json 对象的问题
>
> **扩展**：Json 对象和 Json 数组
>
> 1、Json 对象用`{}`包裹，如`{"username":"admin","password":"123456"}`
>
> 2、Json 数组用`[]`包裹，如`["username":"admin","password":"123456"]`
>
> 3、Json 对象和 Json 数组可以相互嵌套，即 
>
> - Json 对象中可以包含 Json 对象和 Json 数组，如`["username":"admin","password":"123456", "ext" : {"age": 18, "gender": "1"}]`、`["username":"admin","password":"123456", "ext" : ["age": 18, "gender": "1"]]`
> - Json 数组中也可以包含 Json 对象和 Json 数组，如`{"username":"admin","password":"123456", "ext" : {"age": 18, "gender": "1"}}`、`{"username":"admin","password":"123456", "ext" : ["age": 18, "gender": "1"]}`
