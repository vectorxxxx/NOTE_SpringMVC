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

`@ResponseBody`用于标识一个控制器方法，可以将该方法的返回值直接作为响应报文的响应体响应到浏览器

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

### 3.4、SpringMVC 处理 JSON

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

### 3.5、SpringMVC 处理 AJAX

后台测试代码

```java
@PostMapping("/testAxios")
@ResponseBody
public String testAxios(User user) {
    return user.getUsername() + "," + user.getPassword();
}
```

前台测试代码

httpmessageconverter.html

```html
<div id="app">
    <a @click="testAxios" th:href="@{/httpController/testAxios}">SpringMVC 操作 AJAX</a>
</div>
<script type="text/javascript" th:src="@{/static/js/vue.js}"></script>
<script type="text/javascript" th:src="@{/static/js/axios.min.js}"></script>
<script type="text/javascript" th:src="@{/static/js/httpmessageconverter.js}"></script>
```

httpmessageconverter.js

```js
var vue = new Vue({
    el: "#app",
    methods: {
        testAxios: function (event) {
            testAxios(event.target.href);
        }
    }
});

function testAxios(url) {
    axios({
        method: "post",
        url: url,
        params: {
            username: "admin",
            password: "123456"
        }
    }).then(function (response) {
        alert(response.data);
    });
    event.preventDefault();
}
```

测试效果

![动画  (7)](https://s2.loli.net/2022/03/29/2w6PCHSpkXMKFlJ.gif)

### 3.6、@RestController 注解

`@RestController`注解是 SpringMVC 提供的一个复合注解，标识在控制器的类上，就相当于为类添加了`@Controller`注解，并且为其中的每个方法添加了`@ResponseBody`注解

这里简单修改下后台代码，将`@Controller`注解替换为`@RestController`注解，并去除控制器方法上的`@ResponseBody`注解

```java
@RestController
@RequestMapping("/httpController")
public class HttpController {
    @PostMapping("/testAxios")
    public String testAxios(User user) {
        return user.getUsername() + "," + user.getPassword();
    }
}
```

测试效果

![动画  (8)](https://s2.loli.net/2022/03/29/B1owhmPvlGeyfra.gif)

可以发现，虽然控制器方法上没有加`@ResponseBody`注解，但是效果是一样的，依然可以将控制器方法的返回值作为响应报文的响应体返回给浏览器



## 4、ResponseEntity

`ResponseEntity`用于控制器方法的返回值类型，该控制器方法的返回值就是响应到浏览器的响应报文

### 4.1、文件下载

后台测试代码

```java
@Controller
@RequestMapping("/fileUploadDownloadController")
public class FileUploadDownloadController {
    @GetMapping("/testDownload")
    public ResponseEntity<byte[]> testDownload(HttpSession session) {
        ServletContext context = session.getServletContext();
        // 文件位置和名称
        final String path = "/static/img/";
        String fileName = "1.png";
        // 响应体
        String realPath = context.getRealPath(path + fileName);
        byte[] bytes = readFile(realPath);
        // 响应头
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.set("Content-Disposition", "attachment;filename=" + fileName);
        // 响应状态码
        HttpStatus status = HttpStatus.OK;
        // 响应实体
        return new ResponseEntity<>(bytes, headers, status);
    }

    /**
     * 读取文件流
     *
     * @param realPath
     * @return
     */
    private byte[] readFile(String realPath) {
        System.out.println(realPath);
        final int initSize = 0;
        byte[] bytes = new byte[initSize];
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(realPath));) {
            bytes = new byte[bis.available()];
            bis.read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }
}
```

前台测试代码

```html
<a th:href="@{/fileUploadDownloadController/testDownload}">测试下载文件</a>
```

测试效果

![动画  (8)](https://s2.loli.net/2022/03/30/N7wOZpi4IhgAt1l.gif)

> **记低级失误**：
>
> 1、将
>
> @Controller
> @RequestMapping("/fileUploadDownloadController")
> 
> 写成了
>
> @Controller("/fileUploadDownloadController")
>
> 导致了请求直接报`404`找不到对应资源，需要格外注意！！！
> 
> 2、读取文件代码缺少` bis.read(bytes);`导致字节数组只做了初始化而没有赋值，导致下载文件出现“损坏”的问题，需要格外注意！！！

### 4.2、文件上传

文件上传要求`form`表单的请求方式必须为`post`，并且添加属性`enctype="multipart/form-data"`

SpringMVC 将上传的文件封装到`MultipartFile`对象中，通过此对象可以获取文件相关信息

#### 1）添加依赖

`commons-fileupload`的 jar 包是上传功能必不可少的

```xml
<dependency>
    <groupId>commons-fileupload</groupId>
    <artifactId>commons-fileupload</artifactId>
    <version>1.4</version>
</dependency>
```

#### 2）配置文件上传解析器

SpringMVC 配置文件中添加`CommonsMultipartResolver`的依赖注入

```xml
<!--配置文件上传解析器，将上传文件自动封装为MutilpartFile对象-->
<bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver"></bean>
```

#### 3）后台代码

使用`MultipartFile`对象接收上传文件

```java
@PostMapping("/testUpload")
public String testUpload(MultipartFile photo, HttpSession session) throws IOException {
    // 目标目录
    String photoPath = session.getServletContext().getRealPath("photo");
    File file = new File(photoPath);
    if (!file.exists()) {
        file.mkdir();
    }
    // 目标文件名
    String fileName = photo.getOriginalFilename();
    // 上传文件到服务器
    photo.transferTo(new File(photoPath + File.separator + fileName));
    return "success";
}
```

#### 4）前台代码

要求请求方式必须为`post`，并且`enctype`属性值必须为`multipart/form-data`

> **Q**：enctype 是什么？
>
> **A**：enctype 即`encode type`，表示编码类型，它规定了在发送到服务器之前应该如何对表单数据进行编码
>
> 默认地，form 表单数据编码默认值为`application/x-www-form-urlencoded`。除此之外，`enctype`还可以设置为`text/plain`
>
> 这三种类型总结一下就是：
>
> - `application/x-www-form-urlencoded`：默认值，URL 编码
> - `multipart/form-data`：文件类型
> - `text/plain`：纯文本格式类型

```html
<form th:action="@{/fileUploadDownloadController/testUpload}" method="post" enctype="multipart/form-data">
    头像：<input type="file" name="photo"/><br/>
    <input type="submit" value="上传"/>
</form>
```

测试结果

![动画  (9)](https://s2.loli.net/2022/03/30/sUvlI9QcatMVdJf.gif)

查看文件是否上传成功

![image-20220330224959389](https://s2.loli.net/2022/03/30/s9y1CDr5V2WHiRd.png)

#### 5）处理同名问题

如果多次上传同名文件，会发现原文件会被同名新文件替换（覆盖）掉，如何解决这个问题呢？

其实，处理同名问题有多种方式，这里采用`UUID`生成随机序列来实现，只需要做简单的修改即可

```java
@PostMapping("/testUpload")
public String testUpload(MultipartFile photo, HttpSession session) throws IOException {
    // 目标目录
    String photoPath = session.getServletContext().getRealPath("photo");
    File file = new File(photoPath);
    if (!file.exists()) {
        file.mkdir();
    }
    // 目标文件名
    String srcName = photo.getOriginalFilename();
    String suffixName = srcName.substring(srcName.lastIndexOf("."));
    String prefixName = UUID.randomUUID().toString();
    String destName = prefixName + suffixName;
    // 上传文件到服务器
    photo.transferTo(new File(photoPath + File.separator + destName));
    return "success";
}
```

再次测试

![image-20220330230259507](https://s2.loli.net/2022/03/30/fGLcVTKmu6ZkPYn.png)

可以看到，最新上传的文件名为一串随机序列，这样就避免同名文件上传出现覆盖的问题了
