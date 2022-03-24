> 笔记来源：[【尚硅谷】SpringMVC教程丨一套快速上手spring mvc](https://www.bilibili.com/video/BV1Ry4y1574R)

[TOC]

# RESTful

## 1、RESTful 简介

`REST`：`Representational State Transfer`，表现层资源状态转移

- **表现层/表示层**：前端的视图页面到后端的控制层即为“表现层”
- **资源**：Web 工程部署到服务器上后，当前 Web 工程中的内容在服务器上都叫“资源”（万物皆资源）
- **状态**：资源的表现形式，例如，HTML/JSP 页面、CSS/JS 文件、图片/音频/视频等皆为资源的“状态”
- **转移**：浏览器发送请求到服务器，服务端就将请求的资源“转移”到客户端

`RESTful`：基于`REST`构建的 API 就是`RESTful`风格

### 1.1、资源

- 资源是一种看待服务器的方式，即将服务器看作是由很多离散的资源组成。每个资源是服务器上一个可命名的抽象概念
- 因为资源是一个抽象的概念，所以它不仅仅能代表服务器文件系统中的一个文件、数据库中的一张表等等具体的东西，也可以将资源设计的要多抽象有多抽象，只要想象力允许而且客户端应用开发者能够理解
- 与面向对象设计类似，资源是以*名词*为核心来组织的，首先关注的是“名词”。一个资源可以由一个或多个`URI`来标识。`URI`既是资源的名称，也是资源在 Web 上的地址。对某个资源感兴趣的客户端应用，可以通过资源的`URI`与其进行交互

> `URI`（`Uniform Resource Identifier`）：统一资源标志符
>
> `URL`（`Uniform Resource Locator`）：统一资源定位符
>
> `URI`是一个抽象的、高层次的概念，而`URL`是具体的方式。简单来说，`URL`是一种`URI`

### 1.2、资源的表述

- 资源的表述是一段对于资源在某个特定时刻的状态的描述。可以在客户端-服务器端之间转移（交换）
- 资源的表述可以有多种格式，例如`HTML/XML/JSON/纯文本/图片/视频/音频`等等
- 资源的表述格式可以通过协商机制来确定。请求-响应方向的表述通常使用不同的格式

### 1.3、状态转移

- 状态转移说的是：在客户端和服务器端之间转移（`transfer`）代表资源状态的表述
- 通过转移和操作资源的表述，来间接实现操作资源的目的



## 2、RESTful 实现

`RESTful`的实现，具体说就是：HTTP 协议里面，四个表示操作方式的动词`GET`、`POST`、`PUT`、`DELETE`

它们分别对应四种基本操作：

- `GET`用来获取资源
- `POST`用来新建资源
- `PUT`用来更新资源
- `DELETE`用来删除资源

`REST`风格`URL`地址不使用*问号键值对方式*携带请求参数，而是：

<mark>提倡使用统一的风格设计，从前到后各个单词使用斜杠分开，将要发送给服务器的数据作为`URL`地址的一部分，以保证整体风格的一致性</mark>

> 以往，我们访问资源的方式五花八问。例如，
>
> - 获取用户信息通过`getUserById`/`selectUserById`/`findUserById`/等
> - 删除用户信息通过`deleteUserById`/`removeUserById`等
> - 更新用户信息通过`updateUser`/`modifyUser`/`saveUser`等
> - 新增用户信息通过`addUser`/`createUser`/`insertUser`等
>
> 上述操作的资源都是用户信息。按照`RESTful`思想，既然操作的资源一样，那么请求路径就应该一样

用一张表格来对比传统方式和`REST`风格对资源操作的区别

| 操作 | 传统方式              | REST 风格               |
| :--- | :-------------------- | :---------------------- |
| 查询 | `getUserById?id=1`    | `user/1`-->`get`请求    |
| 保存 | `saveUser`            | `user`-->`post`请求     |
| 删除 | `deleteUserById?id=1` | `user/1`-->`delete`请求 |
| 更新 | `updateUser`          | `user`-->`put`请求      |



## 3、使用 RESTful 模拟操作用户资源

需求分析：使用 RESTful 模拟用户资源的增删改查，通过路径中的占位符传递请求参数，通过不同的请求方式对应资源的不同操作

| RESTful 路径 | 请求方式 | 操作                   |
| ------------ | -------- | ---------------------- |
| `/user`      | `GET`    | 查询所有用户信息       |
| `/user/1`    | `GET`    | 根据用户ID查询用户信息 |
| `/user`      | `POST`   | 添加用户信息           |
| `/user/1`    | `DELETE` | 根据用户ID删除用户信息 |
| `/user`      | `PUT`    | 修改用户信息           |

### 3.1、用户的查询、添加

**后台代码实现**

`RESTfulController.java`

```java
@Controller
@RequestMapping("restfulcontroller")
public class RESTfulController {
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String getAllUser() {
        System.out.println("查询所有用户信息");
        return "success";
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public String getUserById(@PathVariable("id") String id) {
        System.out.println("根据用户ID查询用户信息：" + id);
        return "success";
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public String insertUser(User user) { // User 沿用之前的对象
        System.out.println("添加用户信息：" + user);
        return "success";
    }
    
    /** 注：由于 PUT、DELETE 请求比较特殊，后面再做补充 */
}
```

**前台测试代码**

`restful.html`

```html

<a th:href="@{/restfulcontroller/user}">查询所有用户信息</a><br/>
<a th:href="@{/restfulcontroller/user/1}">根据用户ID查询用户信息</a><br/>
<form th:action="@{/restfulcontroller/user}" method="post">
    用户名：<input type="text" name="username"><br/>
    密码：<input type="password" name="password"><br/>
    性别：<input type="radio" name="gender" value="male">男<input type="radio" name="gender" value="female">女<br/>
    年龄：<input type="number" name="age"><br/>
    邮箱：<input type="text" name="email"><br/>
    <input type="submit" value="添加用户">
</form>
```

为了能够访问到`restful.html`前台页面资源，可以通过在控制器中定义一个控制器方法来返回其视图，也可以通过在*04-SpringMVC 视图*中提到的`view-controller`视图控制器代替。因为这里只是为了实现页面跳转，没有其他请求过程的处理，所以可以通过在 SpringMVC 配置文件中使用`<view-controller>`标签进行设置

```xml
<mvc:view-controller path="/restful" view-name="restful"></mvc:view-controller>
```

**测试结果**

![动画  (1)](https://s2.loli.net/2022/03/23/KPTOa3wUcHuCNzo.gif)

**后台日志信息**

```console
查询所有用户信息
根据用户ID查询用户信息：1
添加用户信息：User{username='admin', password='11111', gender='male', age='18', email='123@qq.com'}
```

### 3.2、用户的修改、删除

> 在*01-@RequestMapping 注解*中，我们提到过`form`表单默认只支持`GET`和`POST`请求，如果直接通过`method`属性指定为`PUT`和`DELETE`，会默认以`GET`请求方式处理
>
> 而想要实现`PUT`和`DELETE`请求，需要在`web.xml`中配置 SpringMVC 提供的`HiddenHttpMethodFilter`过滤器，并在前台页面使用隐藏域来设置`PUT`和`DELETE`类型的请求方式
>
> 当然，也可以使用`AJAX`发送`PUT`和`DELETE`请求，但是需要注意`PUT`和`DELETE`仅部分浏览器支持

为了更清楚地了解`HiddenHttpMethodFilter`过滤器到底干了什么，这里对其源码进行剖析

```java
public class HiddenHttpMethodFilter extends OncePerRequestFilter {
    private static final List<String> ALLOWED_METHODS = Collections.unmodifiableList(Arrays.asList(HttpMethod.PUT.name(),HttpMethod.DELETE.name(),HttpMethod.PATCH.name()));
    public static final String DEFAULT_METHOD_PARAM = "_method";
    private String methodParam = DEFAULT_METHOD_PARAM;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        HttpServletRequest requestToUse = request;
        // 要求原请求方式为 POST 请求
        if ("POST".equals(request.getMethod()) && request.getAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE) == null) {
            // methodParam值：_method
            // paramValue值：_method属性值
            String paramValue = request.getParameter(this.methodParam);
            if (StringUtils.hasLength(paramValue)) {
                // _method属性值转为全大写形式，put==>PUT，delete==>DELETE
                String method = paramValue.toUpperCase(Locale.ENGLISH);
                // 判断_method属性值是否为{"PUT","DELETE","PATCH"}中的一个
                if (ALLOWED_METHODS.contains(method)) {
                    // “偷梁换柱”，包装为一个新的请求对象
                    requestToUse = new HttpMethodRequestWrapper(request, method);
                }
            }
        }
        filterChain.doFilter(requestToUse, response);
    }
    
    private static class HttpMethodRequestWrapper extends HttpServletRequestWrapper {
        private final String method;
        public HttpMethodRequestWrapper(HttpServletRequest request, String method) {
            super(request);
            this.method = method;
        }
        @Override
        public String getMethod() {
            return this.method;
        }
    }
}
```

“源码在手，天下我有”。接下来，将理论付诸实践

**配置文件**

`web.xml`配置`HiddenHttpMethodFilter`过滤器

```xml
<filter>
    <filter-name>HiddenHttpMethodFilter</filter-name>
    <filter-class>org.springframework.web.filter.HiddenHttpMethodFilter</filter-class>
</filter>
<filter-mapping>
    <filter-name>HiddenHttpMethodFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

**后台代码实现**

```java
@RequestMapping(value = "/user", method = RequestMethod.PUT)
public String updateUser(User user) {
    System.out.println("修改用户信息：" + user);
    return "success";
}
// 这里的写法格式没有问题，但业务逻辑其实大有问题，下面再详细说
@RequestMapping(value = "/user", method = RequestMethod.DELETE)
public String deleteUser(User user) {
    System.out.println("删除用户信息：" + user);
    return "success";
}
```

**前台测试代码**

在添加用户的表单基础上，添加隐藏域`<input type="hidden" name="_method" value="put">`

```html
<form th:action="@{/restfulcontroller/user}" method="post">
    <input type="hidden" name="_method" value="put">
    用户名：<input type="text" name="username"><br/>
    密码：<input type="password" name="password"><br/>
    性别：<input type="radio" name="gender" value="male">男<input type="radio" name="gender" value="female">女<br/>
    年龄：<input type="number" name="age"><br/>
    邮箱：<input type="text" name="email"><br/>
    <input type="submit" value="修改用户">
</form>
<hr/>
<!-- 这里的写法格式没有问题，但业务逻辑其实大有问题，下面再详细说 -->
<form th:action="@{/restfulcontroller/user}" method="post">
    <input type="hidden" name="_method" value="delete">
    用户名：<input type="text" name="username"><br/>
    密码：<input type="password" name="password"><br/>
    性别：<input type="radio" name="gender" value="male">男<input type="radio" name="gender" value="female">女<br/>
    年龄：<input type="number" name="age"><br/>
    邮箱：<input type="text" name="email"><br/>
    <input type="submit" value="删除用户">
</form>
```

**测试结果**

修改用户

![动画  (2)](https://s2.loli.net/2022/03/23/41pTE83z5xO2eAH.gif)

删除用户

![动画  (3)](https://s2.loli.net/2022/03/23/MS4AatvrxFO2Zmb.gif)



**后台日志信息**

```console
修改用户信息：User{username='user', password='11111', gender='female', age='1', email='111@qq.com'}
删除用户信息：User{username='user', password='11111', gender='female', age='1', email='111@qq.com'}
```

到这里，应该需要指明的是我们在设计`RESTful`实现时，对删除用户信息的要求下面这样的

| RESTful 路径 | 请求方式 | 操作                   |
| ------------ | -------- | ---------------------- |
| `/user/1`    | `DELETE` | 根据用户ID删除用户信息 |

换句话说，这里应该通过超链接而非表单形式，即通过用户ID来对用户信息进行删除操作。而一般情况下，我们是通过行编辑删除某一行数据，或是通过选中表单的数据来进行批量删除，这些功能在详细案例时会详细介绍



## 4、CharacterEncodingFilter 和 HiddenHttpMethodFilter 的配置顺序

目前，我们在`web.xml`配置文件中对`CharacterEncodingFilter`和`HiddenHttpMethodFilter`的配置顺序如下

```xml
<!--处理编码-->
<filter>
    <filter-name>characterEncodingFilter</filter-name>
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
    <filter-name>characterEncodingFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
<!-- 略 -->
<!--配置 org.springframework.web.filter.HiddenHttpMethodFilter: 可以把 POST 请求转为 DELETE 或 POST 请求 -->
<filter>
    <filter-name>HiddenHttpMethodFilter</filter-name>
    <filter-class>org.springframework.web.filter.HiddenHttpMethodFilter</filter-class>
</filter>
<filter-mapping>
    <filter-name>HiddenHttpMethodFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

如果，将两者顺序颠倒互换，即

```xml
<!--配置 org.springframework.web.filter.HiddenHttpMethodFilter: 可以把 POST 请求转为 DELETE 或 POST 请求 -->
<filter>
    <filter-name>HiddenHttpMethodFilter</filter-name>
    <filter-class>org.springframework.web.filter.HiddenHttpMethodFilter</filter-class>
</filter>
<filter-mapping>
    <filter-name>HiddenHttpMethodFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
<!-- 略 -->
<!--处理编码-->
<filter>
    <filter-name>characterEncodingFilter</filter-name>
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
    <filter-name>characterEncodingFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

看看顺序改变之后，对请求的编码会有什么影响

![动画  (4)](https://s2.loli.net/2022/03/24/esOTvacg1VMWxA3.gif)

后台日志信息

```console
修改用户信息：User{username='å¼ ä¸', password='123456', gender='male', age='18', email='123@qq.com'}
```

可以发现，中文的“张三”乱码了，这是为什么呢？

> 在*02-SpringMVC 获取请求参数*一节中的*7、处理乱码问题*中，我们尝试在获取请求参数之前，通过`request.setCharacterEncoding("UTF-8");`来设置请求编码格式，但是没有生效。分析的原因是“请求参数获取在前，设置编码格式在后”导致的。
>
> 我们也提出了 2 种解决方案：
>
> - 1、获取请求参数之后，手动解码编码。但是这种显然不合理，所以直接 pass
> - 2、获取请求参数之前“做手脚”。这种方式就是 SpringMVC 中提供的`CharacterEncodingFilter`过滤器，来对请求编码做统一处理
>
> 现在的问题就是：在`CharacterEncodingFilter`之前配置了`HiddenHttpMethodFilter`导致了失效
>
> 所以我们需要搞清楚，为什么`CharacterEncodingFilter`的配置顺序会影响到编码的效果？或者说为什么`HiddenHttpMethodFilter`会使之失效？

通过上面对`HiddenHttpMethodFilter`源码的剖析，它会获得`_method`这个请求参数，这就导致执行到`CharacterEncodingFilter`过滤器时，已经是获取请求参数之后了，所以会导致上述中文乱码问题

因此，我们必须要将`CharacterEncodingFilter`过滤器尽量配置在其他过滤器之前。这样就能保证在任何过滤器获取请求之前，获得的失已经处理过编码格式的请求参数了

我们再将`CharacterEncodingFilter`和`HiddenHttpMethodFilter`的配置顺序还原至之前的状态，即`CharacterEncodingFilter`在前而`HiddenHttpMethodFilter`在后的情况，进行测试再查看后台日志信息

```console
修改用户信息：User{username='张三', password='', gender='male', age='18', email='123@qq.com'}
```

这时，当请求参数中包含中文时，就不会出现乱码的情况了



## 总结

本节重点掌握内容

- 明确`REST`和`RESTful`的关系，明确表现层、资源、状态、转移这几个概念的含义
  - `REST`，表现层资源状态转移
  - `RESTful`，基于`REST`构建的 API 就是`RESTful`风格
- 明确`RESTful`的实现，是通过不同的请求方式来对应资源的不同操作，通过路径中的占位符传递请求参数
- 熟练掌握如何通过`RESTful`进行资源的增删改查操作，以及如何处理`PUT`和`DELETE`这两种特殊的请求方式
- 明确`CharacterEncodingFilter`和`HiddenHttpMethodFilter`的配置顺序，明白两个过滤器的源码处理逻辑

附上导图，仅供参考

![05-RESTful](https://s2.loli.net/2022/03/24/CNhaekdvRVFZbTP.png)
