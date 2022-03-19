> 笔记来源：[【尚硅谷】SpringMVC教程丨一套快速上手spring mvc](https://www.bilibili.com/video/BV1Ry4y1574R)

[TOC]

# @RequestMapping 注解

## 1、功能

从注解名称上我们可以看到，`@RequestMapping`注解的作用就是<mark>将请求和处理请求的控制器方法关联起来，建立映射关系</mark>

SpringMVC 接收到指定的请求，就会来找到在映射关系中对应的控制器方法来处理这个请求

**控制器中有多个方法对应同一个请求的情况**

这是一种特殊情况，我们定义至少两个控制类，其中定义的控制器方法上`@ReqeustMapping`指定同一请求路径

```java
@Controller
public class HelloController {
    @RequestMapping("/")
    public String index() {
        return "index";
    }
}
@Controller
public class RequestMappingController {
    @RequestMapping("/")
    public String index() {
        return "target";
    }
}
```

如果存在两个或多个控制器，其控制器方法的`@RequestMapping`一致，即多个控制器方法试图都想要处理同一个请求时，这时启动 Tomcat 时会抛出`BeanCreationException`异常，并指明`There is already 'helloController' bean method`即 helloController 中存在处理同意请求的控制器方法

```java
org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping': Invocation of init method failed; nested exception is java.lang.IllegalStateException: Ambiguous mapping. Cannot map 'requestMappingController' method 
com.vectorx.springmvc.s00_helloworld.RequestMappingController#index()
to { [/]}: There is already 'helloController' bean method
com.vectorx.springmvc.s00_helloworld.HelloController#index() mapped.
```

但是这显然是不合理的，当一个系统方法很多，很难完全避免两个或多个同名方法的存在，那该怎么办呢？



## 2、位置

查看`@RequestMapping`注解的源码，可以清楚地看到`@Target`中有`TYPE`和`METHOD`两个属性值，表示其可以作用在类或方法上

![image-20220314205816839](https://s2.loli.net/2022/03/14/5EqxDJK8f3bFId4.png)

也就是说`@RequestMapping`不仅可以在控制器方法上进行使用，也可以在控制器类上进行使用。那两种方式有什么区别呢？

- `@RequestMapping`标识一个类：设置映射请求的请求路径的初始信息
- `@RequestMapping`标识一个方法：设置映射请求的请求路径的具体信息

```java
@Controller
@RequestMapping("/requestMappingController")
public class RequestMappingController {
    @RequestMapping("/success")
    public String success() {
        return "success";
    }
}
```

前台创建超链接，超链接跳转路径 = 控制器上`@RequestMapping`映射请求的初始路径 + 控制器方法上`@RequestMapping`映射请求的具体路径，即`/requestMappingController/success`，再将其使用`Tymeleaf`的`@{}`语法包裹起来，这样`Tymeleaf`会为我们自动加上上下文路径

```html
<a th:href="@{/requestMappingController/success}">访问指定页面success.html</a>
```

测试结果

![动画](https://s2.loli.net/2022/03/14/GVzji3KDpAR5F1C.gif)

这样就可以为不同的控制器方法，在设置映射请求的请求路径时，指定不同的初始信息，从而避免**控制器中有多个方法对应同一个请求的情况**，这也就解决了之前的问题



## 3、value 属性

`@RequestMapping`注解的`value`属性有哪些用途呢？

- **请求映射**：通过请求的请求地址匹配请求映射
- **匹配多个请求**：是一个字符串类型的数组，表示该请求映射能够匹配多个请求地址所对应的
- **必须设置**：至少通过一个请求地址匹配请求映射

### 匹配多个请求

查看`@RequestMapping`注解的源码，会发现其`value`属性返回值为 String 类型的数组，这也说明了之所以`@RequestMapping`注解的`value`属性可以匹配多个请求的原因。通过为`value`属性指定多个值的方式，就可以对个多个请求建立请求映射

![image-20220314211518142](https://s2.loli.net/2022/03/14/ThsQLp9l5cr4gJt.png)

在控制器方法上的`@RequestMapping`中新增一个`/test`请求映射

```java
@RequestMapping(value = {"/success", "/test"})
public String success() {
    return "success";
}
```

前台创建一个`/test`的请求路径的超链接，以便进行测试

```html
<a th:href="@{/requestMappingController/test}">>测试RequestMapping注解的value属性-->/test</a>
```

测试结果

![动画 (2)](https://s2.loli.net/2022/03/15/9aBXvKsqjFpLmrZ.gif)

这样，同一个控制器方法就可以实现对多个请求进行统一处理



## 4、method 属性

`@RequestMapping`注解的`method`属性有哪些用途呢？

- 通过请求的请求方式（`GET`或`POST`）匹配请求映射

> 1、常用的请求方式有 4 种：GET、POST、PUT、DELETE
>
> - GET 用于查询数据
> - POST 用于添加数据
> - PUT 用于更新数据
> - DELETE 用户删除数据
>
> 但是很多情况下，习惯上会让 POST 承担更多的职责，即通过 POST 进行增、删、改的操作，可以说是“一个人揽了三个人的活”
>
> 2、还有 4 种不常用的请求：HEAD、PATCH、OPTIONS、TRACE
>
> - HEAD 获取响应的报头，检查超链接的有效性、网页是否被修改，多用于自动搜索机器人获取网页的标志信息，获取 rss 种子信息，或者传递安全认证信息等
> - PATCH 用于更新数据
> - OPTIONS 用于测试服务器，解决同源策略和跨域请求问题
> - TRACE 用于测试或诊断
>
> 有的资料还会介绍 CONNECT 请求，好像用于 HTTP 代理的，很少人听过，当然用的更少（我也是刚知道，有懂的求科普）

- 是一个`RequestMethod`类型的数组，表示该请求映射能够匹配多种请求方式的请求

> 源码为证：`RequestMethod[] method() default {};`

同时注意到`method`属性默认值为空数组，是否说明控制器方法不添加`method`属性时，不同的请求方法都能够匹配呢？

### 1）无 method 时匹配哪些请求方式？

通过测试验证猜想

```html
<a th:href="@{/requestMappingController/test}">测试RequestMapping注解的method属性-->GET</a>
<br/>
<form th:action="@{/requestMappingController/test}" method="post">
    <input type="submit" value="测试RequestMapping注解的method属性-->POST">
</form>
```

测试结果：事实证明，控制器方法不添加`method`属性时，可以接收`GET`和`POST`的请求，那么应该是默认不对请求方式限制了

![动画 (3)](https://s2.loli.net/2022/03/15/UXLtYmFsDM2eA37.gif)

本着严谨的态度，再测试下是否在不添加`method`属性时，默认也支持`PUT`和`DELETE`请求

不过，`PUT`和`DELETE`请求比较特殊，需要使用到隐藏域，且`method`固定为`POST`

```html
<form th:action="@{/requestMappingController/test}" method="post">
    <input type="hidden" name="_method" value="put"/>
    <input type="submit" value="测试RequestMapping注解的method属性-->PUT">
</form>
<br/>
<form th:action="@{/requestMappingController/test}" method="post">
    <input type="hidden" name="_method" value="delete"/>
    <input type="submit" value="测试RequestMapping注解的method属性-->DELETE">
</form>
```

同时在`web.xml`中需要添加隐藏域请求方式的过滤器配置

```xml
<filter>
    <filter-name>HiddenHttpMethodFilter</filter-name>
    <filter-class>org.springframework.web.filter.HiddenHttpMethodFilter</filter-class>
</filter>
<filter-mapping>
    <filter-name>HiddenHttpMethodFilter</filter-name>
    <url-pattern>/</url-pattern>
</filter-mapping>
```

测试结果也是成功的

![动画 (4)](https://s2.loli.net/2022/03/15/PBKWU9yl2gTea6b.gif)

> 存疑点：本来也想测试下`HEAD`、`PTACH`、`OPTIONS`和`TRACE`这几种不常用的请求方式的，但是发现 form 表单好像不支持这些请求方式。即使使用隐藏域，也会变成`GET`的请求方式。很疑惑这些请求方式要怎么模拟，有懂的求科普。这里记录下，留个印象，以待后续考古o(╯□╰)o

### 2）不满足 method 会怎样？

**值得注意的是**

若当前请求的请求地址满足请求映射的`value`属性，但是请求方式不满足`method`属性，则浏览器报错

```js
HTTP Status 405 - Request method 'POST' not supported
```

![image-20220315233645093](https://s2.loli.net/2022/03/15/26cs9f3r5Wvx4wh.png)

代码验证

```java
@RequestMapping(value = {"/success", "/test"}, method = RequestMethod.GET)
public String success() {
    return "success";
}
```

验证结果：确实报了 405。同理可以将`method`属性值中`GET`改成`POST`、`PUT`和`DELETE`对应进行验证即可，这里就不一一验证了

![image-20220315233403374](https://s2.loli.net/2022/03/15/2WSBAXxHd1Eci4M.png)

> 虽然无关紧要，但就是好奇为嘛我的是中文提示(灬°ω°灬)
>
> 发现这个版本 tomcat 的 lib 包中有 i18n 相关 jar 包，原来是做了国际化，“不一定有用”的知识又增加了~
>
> ![image-20220315234352240](https://s2.loli.net/2022/03/15/Yf47JRrjptCSmAZ.png)

### 3）派生注解

对于处理指定请求方式的控制器方法，SpringMVC 中提供了`@RequestMapping`的派生注解

- 处理`GET`请求的映射->`@GetMapping`
- 处理`POST`请求的映射->`@PostMapping`
- 处理`PUT`请求的映射->`@PutMapping`
- 处理`DELETE`请求的映射->`@DeleteMapping`

这里对于派生注解很容易理解，用数学上的等价解释就是

$@GetMapping(value="...") <==> @RequestMapping(value="...", method=RequestMethod.GET)$

$@PostMapping(value="...") <==> @RequestMapping(value="...", method=RequestMethod.POST)$

$@PutMapping(value="...") <==> @RequestMapping(value="...", method=RequestMethod.PUT)$

$@DeleteMapping(value="...") <==> @RequestMapping(value="...", method=RequestMethod.DELETE)$

通过代码进行测试

后台代码

```java
@Controller
@RequestMapping("/requestMappingController")
public class RequestMappingController {
    @GetMapping("/success")
    public String successGet() {
        return "successget";
    }
    @PostMapping("/success")
    public String successPost() {
        return "successpost";
    }
    @PutMapping("/success")
    public String successPut() {
        return "successput";
    }
    @DeleteMapping("/success")
    public String successDelete() {
        return "successdelete";
    }
}
```

前台代码

```html
<a th:href="@{/requestMappingController/success}">测试GetMapping</a>
<br/><br/>
<form th:action="@{/requestMappingController/success}" method="post">
    <input type="submit" value="测试PostMapping">
</form>
<br/>
<form th:action="@{/requestMappingController/success}" method="post">
    <input type="hidden" name="_method" value="put"/>
    <input type="submit" value="测试PutMapping">
</form>
<br/>
<form th:action="@{/requestMappingController/success}" method="post">
    <input type="hidden" name="_method" value="delete"/>
    <input type="submit" value="测试DeleteMapping">
</form>
```

别忘了，新增四个测试页面`successget.html`、`successpost.html`、`successput.html`和`successdelete.html`，在这个四个不同页面中标注不同的内容以示区分

![image-20220316003448927](https://s2.loli.net/2022/03/16/Kmg6syE8Wq9UAX7.png)

验证结果：可以看到，`GET`/`POST`/`PUT`/`DELETE`等请求方式，均能够被正常接收和处理

![动画 (5)](https://s2.loli.net/2022/03/16/c2ZHN5GixC3bfIr.gif)

### 4）form 表单支持哪些请求方式？

- 常用的请求方式有`GET`，`POST`，`PUT`，`DELETE`，但是目前浏览器只支持`GET`和`POST`（OS：刚才还有点疑惑的，这里好像“水落石出了”）
- 若在 form 表单提交时，为`method`设置了其他请求方式的字符串（`PUT`或`DELETE`），则按照默认的`GET`请求方式处理
- 若要发送`PUT`和`DELETE`请求，则需要通过 Spring 提供的过滤器`HiddenHttpMethodFilter`，在 restful 部分会讲到（OS：我上面刚自己研究了下，没想到老师这里会讲`^_^`）

如果去除过滤器`HiddenHttpMethodFilter`的配置，同时注释掉隐藏域的代码，并将`method`为`post`值改成`put`和`delete`

```html
<form th:action="@{/requestMappingController/success}" method="put">
    <!--    <input type="hidden" name="_method" value="put"/>-->
    <input type="submit" value="测试PutMapping">
</form>
<br/>
<form th:action="@{/requestMappingController/success}" method="delete">
    <!--    <input type="hidden" name="_method" value="delete"/>-->
    <input type="submit" value="测试DeleteMapping">
</form>
```

按照上述的说法，会按照默认的`GET`请求方式处理，这里进行验证

![动画 (6)](https://s2.loli.net/2022/03/16/hnR8aJP7NDpAr4m.gif)

可以发现，本应走`PUT`和`DELETE`方式的请求，都被`GET`请求方式的控制器方法处理了。如果这里控制器中连相应`GET`请求方式都没有定义的话，肯定是报 405 了。这里注释掉`@GetMapping`的请求方法

```java
//@GetMapping("/success")
//public String successGet() {
//    return "successget";
//}
```

验证结果：显而易见

![动画 (7)](https://s2.loli.net/2022/03/16/1hxN7nOZQC3Azgc.gif)



## 5、params 属性

`@RequestMapping`注解的`params`属性通过请求的请求参数匹配请求映射

它是一个字符串类型的数组，可以通过四种表达式设置请求参数和请求映射的匹配关系

- `param`：要求请求映射所匹配的请求必须携带`param`请求参数
- `!param`：要求请求映射所匹配的请求必须不能携带`param`请求参数
- `param=value`：要求请求映射所匹配的请求必须携带`param`请求参数且`param=value`
- `param!=value`：要求请求映射所匹配的请求必须携带`param`请求参数但是`param!=value`

若当前请求满足`@RequestMapping`注解的`value`和`method`属性，但是不满足`params`属性，此时页面显示`400`错误，即请求不匹配

### 1）param

这里指定`params`的值指定为`username`，这就要求请求中必须携带`username`的请求参数

```java
@RequestMapping(
    value = {"/testParams"},
    params = {"username"}
)
public String testParams() {
    return "success";
}
```

前台测试代码：分别不加请求参数和加上请求参数，进行测试

```html
<a th:href="@{/requestMappingController/testParams}">测试RequestMapping注解的params属性==>testParams</a><br/>
<a th:href="@{/requestMappingController/testParams?username=admin}">测试RequestMapping注解的params属性==>testParams?username=admin</a>
```

测试结果

![动画 (8)](https://s2.loli.net/2022/03/17/2DLuKxzVQ5Wtsen.gif)

可以发现，当配置了`params`属性并指定相应的请求参数时，请求中必须要携带相应的请求参数信息，否则前台就会报抛出`400`的错误信息，符合预期

```js
HTTP Status 400：Parameter conditions "username" not met for actual request parameters
```

不过在`Tymeleaf`中使用问号的方式会有错误提示，虽然不影响功能，但不想要错误提示的话，最好通过`(...,...)`的方式进行包裹，多个参数间通过`,`隔开

```html
<a th:href="@{/requestMappingController/testParams(username='admin', password=123456)}">测试RequestMapping注解的params属性==>testParams(username='admin', password=123456)</a><br/>
```

测试验证

![动画 (9)](https://s2.loli.net/2022/03/17/A6C2bowu37HvtRV.gif)

可以发现，通过括号包裹的方式，`Tymeleaf`最终会帮我们将其解析成`?username=admin&password=123456`的格式

> 存疑点：实测发现，``testParams(username='admin', password=123456)`改成`testParams(username=admin, password=123456)`，即`admin`不加单引号也是可以的，这与课堂上所讲的并不一致，此点存疑

### 2）!param

这里将`params = {"username"}`中`username`前加上`!`即可，即`params = {"!username"}`，这就要求请求中的请求参数中不能携带`username`请求参数

```java
@RequestMapping(
    value = {"/testParams"},
    params = {"!username"}
)
public String testParams() {
    return "success";
}
```

测试结果

![动画 (10)](https://s2.loli.net/2022/03/17/RncNoefqA2d3pGi.gif)

可以发现，没有携带`username`请求参数的请求变得能够正常访问，而携带了`username`请求参数的请求反而出现了`400`的异常信息，符合预期

```js
HTTP Status 400：Parameter conditions "!username" not met for actual request parameters: username={admin}, password={123456}
```

### 3）param=value

这里`params`的值指定为`username=admin`的形式，即要求请求中不仅要携带`username`的请求参数，且值为`admin`

```java
@RequestMapping(
    value = {"/testParams"},
    params = {"username=admin"}
)
public String testParams() {
    return "success";
}
```

测试结果

![动画 (11)](https://s2.loli.net/2022/03/17/gbqTLYUxoBEZOA7.gif)

可以发现，不携带`username`请求参数的请求和携带`username`请求参数但不为`admin`的请求，均提示`400`的请求错误，符合预期

### 4）param!=value

这里将`params`的值指定为`username!=admin`，即要求请求中不仅要携带`username`的请求参数，且值不能为`admin`

```java
@RequestMapping(
    value = {"/testParams"},
    params = {"username!=admin"}
)
public String testParams() {
    return "success";
}
```

测试结果

![动画 (12)](https://s2.loli.net/2022/03/17/enDHI5Npc3WUTfm.gif)

实际测试结果发现：不携带`username`请求参数的请求和携带`username`请求参数但值不为`admin`的请求，可以正常访问；而携带`username`请求参数但值为`admin`的请求，不能正常访问，不完全符合预期

> 存疑点：不携带`username`请求参数的请求能够正常访问，这一点不符合课程中讲解的内容，此点存疑



## 6、headers 属性

`@RequestMapping`注解的`headers`属性通过请求的请求头信息匹配请求映射

它是一个字符串类型的数组，可以通过四种表达式设置请求头信息和请求映射的匹配关系

- `header`：要求请求映射所匹配的请求必须携带`header`请求头信息
- `header`：要求请求映射所匹配的请求必须不能携带`header`请求头信息
- `header=value`：要求请求映射所匹配的请求必须携带`header`请求头信息且`header=value`
- `header!=value`：要求请求映射所匹配的请求必须携带`header`请求头信息且`header!=value`

若当前请求满足`@RequestMapping`注解的`value`和`method`属性，但是不满足`headers`属性，此时页面显示`404`错误，即资源未找到

测试代码

```java
@RequestMapping(
    value = {"/testHeaders"},
    headers = {"Host=localhost:8081"}
)
public String testHeaders() {
    return "success";
}
```

测试结果

![动画 (13)](https://s2.loli.net/2022/03/17/4kLgyqEBFsDcAx1.gif)

因为我本地`tomcat`启动端口是`8080`，所以是匹配不成功的，此时显示`404`错误，符合预期

再将端口号修改为`8080`

```java
@RequestMapping(
    value = {"/testHeaders"},
    headers = {"Host=localhost:8080"}
)
public String testHeaders() {
    return "success";
}
```

测试结果

![动画 (14)](https://s2.loli.net/2022/03/17/1MRg6UKVGHhrzTl.gif)

这一次，因为端口号一致，所以成功跳转，符合预期



## 7、Ant 风格路径

- `?`：表示任意的单个字符
- `*`：表示任意的0个或多个字符
- `**`：表示任意的一层或多层目录。注意：在使用`**`时，只能使用`/**/xxx`的方式

> 探子来报：`**`经实测，0 层目录也可以，这里严谨来说，应该是“表示任意层目录”

### 1）?

后台测试代码

```java
//ant风格路径
@RequestMapping("/a?a/testAnt")
public String testAnt() {
    return "success";
}
```

前台测试代码

```html
Ant风格路径——?：<br/>
<a th:href="@{/requestMappingController/testAnt}">测试ant风格路径_/a?a/testAnt==>/testAnt</a><br/>
<a th:href="@{/requestMappingController/a1a/testAnt}">测试ant风格路径_/a?a/testAnt==>/a1a/testAnt</a><br/>
<a th:href="@{/requestMappingController/aaa/testAnt}">测试ant风格路径_/a?a/testAnt==>/aaa/testAnt</a><br/>
<a th:href="@{/requestMappingController/aaaa/testAnt}">测试ant风格路径_/a?a/testAnt==>/aaaa/testAnt</a><br/>
<a th:href="@{/requestMappingController/a/a/testAnt}">测试ant风格路径_/a?a/testAnt==>/a/a/testAnt</a><br/>
<a th:href="@{/requestMappingController/a?a/testAnt}">测试ant风格路径_/a?a/testAnt==>/a?a/testAnt</a><br/>
```

测试结果

![动画 (15)](https://s2.loli.net/2022/03/17/JzVZ8NOnREIGi1t.gif)

可以发现，`/a?a/testAnt`能够匹配的路径有

- `/a1a/testAnt`
- `/aaa/testAnt`

不能匹配的路径有

- `/testAnt`
- `/aaaa/testAnt`
- `/a/a/testAnt`
- `/a?a/testAnt`

即证明，`?`修饰的路径，有且必须有一个字符代替`?`的位置，即只能匹配单个字符，且不能为`/`和`?`这两种特殊字符（因为`/`和`?`在 url 路径中比较特殊，除此之外其他单个字符均可），符合预期

### 2）*

后台测试代码

```java
//ant风格路径
@RequestMapping("/a*a/testAnt")
public String testAnt() {
    return "success";
}
```

前台测试代码

```html
Ant风格路径——*：<br/>
<a th:href="@{/requestMappingController/aa/testAnt}">测试ant风格路径_/a*a/testAnt==>/aa/testAnt</a><br/>
<a th:href="@{/requestMappingController/a1a/testAnt}">测试ant风格路径_/a*a/testAnt==>/a1a/testAnt</a><br/>
<a th:href="@{/requestMappingController/aaaaa/testAnt}">测试ant风格路径_/a*a/testAnt==>/aaaaa/testAnt</a><br/>
```

测试结果

![动画 (16)](https://s2.loli.net/2022/03/17/I59C2dtfAmi8Pyx.gif)

可以发现，`/a*a/testAnt`能够匹配的路径有

- `/aa/testAnt`
- `/a1a/testAnt`
- `/aaaaa/testAnt`

即证明，`*`修饰的路径，允许 0 个或多个字符代替`*`的位置，符合预期

### 3）**

上面说到，在使用`**`时，只能使用`/**/xxx`的方式，这里对其进行验证

后台测试代码

```java
//ant风格路径
@RequestMapping("/a**a/testAnt")
public String testAnt() {
    return "success";
}
```

前台测试代码

```html
Ant风格路径——**：<br/>
<a th:href="@{/requestMappingController/aa/testAnt}">测试ant风格路径_/a**a/testAnt==>/aa/testAnt</a><br/>
<a th:href="@{/requestMappingController/a1a/testAnt}">测试ant风格路径_/a**a/testAnt==>/a1a/testAnt</a><br/>
<a th:href="@{/requestMappingController/a1a/testAnt}">测试ant风格路径_/a**a/testAnt==>/a11a/testAnt</a><br/>
<a th:href="@{/requestMappingController/a**a/testAnt}">测试ant风格路径_/a**a/testAnt==>/a**a/testAnt</a><br/>
<a th:href="@{/requestMappingController/aaaaa/testAnt}">测试ant风格路径_/a**a/testAnt==>/aaaaa/testAnt</a><br/>
<a th:href="@{/requestMappingController/a/a/testAnt}">测试ant风格路径_/a**a/testAnt==>/a/a/testAnt</a><br/>
<a th:href="@{/requestMappingController/a/d/e/a/testAnt}">测试ant风格路径_/a**a/testAnt==>/a/d/e/a/testAnt</a><br/>
```

测试结果

![动画 (17)](https://s2.loli.net/2022/03/17/YTpD34UtN9a8Euf.gif)

可以发现，`/a**a/testAnt`能够匹配的路径有

- `/aa/testAnt`
- `/a1a/testAnt`
- `/a11a/testAnt`
- `/a**a/testAnt`
- `/aaaaa/testAnt`

不能匹配的路径有

- `/a/a/testAnt`
- `/a/d/e/a/testAnt`

不符合预期

> 存疑点：这里`/a**a/`多层路径不能匹配，而 0 个或多个字符能够匹配，这与课程中的“两颗星真的就是两颗星”不符，其匹配规则与`/a*a/`一致，即`/a**a/ <==> /a*a/`，两颗星与一颗星作用相同，此点存疑

上述只是对`**`的错误用法时的匹配规则，下面才是真正对`**`的正确用法验证，请看

后台测试代码

```java
//ant风格路径
@RequestMapping("/**/testAnt")
public String testAnt() {
    return "success";
}
```

前台测试代码

```html
Ant风格路径——**：<br/>
<a th:href="@{/requestMappingController/testAnt}">测试ant风格路径_/a**a/testAnt==>/testAnt</a><br/>
<a th:href="@{/requestMappingController/a/testAnt}">测试ant风格路径_/a**a/testAnt==>/a/testAnt</a><br/>
<a th:href="@{/requestMappingController/a/a/a/a/testAnt}">测试ant风格路径_/a**a/testAnt==>/a/a/a/a/testAnt</a><br/>
```

测试结果

![动画 (18)](https://s2.loli.net/2022/03/17/nDTEVK84rHW9kuC.gif)

可以发现，不管中间添加多少层路径都是能够匹配成功的，符合预期



## 8、路径中的占位符

- 原始方式：`/deleteUser?id=1`
- rest 方式：`/deleteuser/11`

SpringMVC 路径中的占位符常用于 restful 风格中，当请求路径中将某些数据通过路径的方式传输到服务器中，就可以在相应的`@RequestMapping`注解的`value`属性中通过占位符`{xxx}`表示传输的数据，再通过`@PathVariable`注解，将占位符所表示的数据赋值给控制器方法的形参

### 无注解形参

- 测试条件：①只使用`{xxx}`占位符而不使用`@PathVariable`注解；②形参名称与请求中的占位符名称同名
- 测试目的：①请求能否匹配成功；②同名形参是否能够接收到请求路径中的占位符

后台测试代码

```java
@RequestMapping("/testRest/{id}/{username}")
public String testRest(String id, String username) {
    System.out.println("id=" + id + ", username=" + username);
    return "success";
}
```

前台测试代码

```html
路径中的占位符：<br/>
<a th:href="@{/requestMappingController/testRest/1/admin}">测试路径中的占位符==>/testRest/1/admin</a><br/>
```

测试结果

![动画 (19)](https://s2.loli.net/2022/03/17/t3CWIo91lONjUXE.gif)

后台日志

```java
id=null, username=null
```

可以发现，请求能够匹配成功，但是同名形参无法接收到占位符的值

### 带注解形参

查看`PathVariable`注解源码

![image-20220317223234047](https://s2.loli.net/2022/03/17/ybi6sNgSEBo1H5x.png)

可以看到，它只能作用在方法参数上，那么怎么用就一目了然了

后台测试代码

```java
@RequestMapping("/testRest/{id}/{username}")
public String testRest(@PathVariable("id") String id, @PathVariable("username") String username) {
    System.out.println("id=" + id + ", username=" + username);
    return "success";
}
```

测试结果

![动画 (19)](https://s2.loli.net/2022/03/17/AIjc5M14vqXWRVt.gif)

后台日志

```java
id=1, username=admin
```

可以发现，请求能够匹配成功，形参通过`@PathVariable`注解接收到了占位符的值

### 不设置占位符

```java
<a th:href="@{/requestMappingController/testRest}">测试路径中的占位符==>/testRest</a><br/>
```

测试结果

![动画 (20)](https://s2.loli.net/2022/03/17/8qwmGgoQjUsVLe5.gif)

可以看到，没有占位符时，直接显示了`404`错误，即表示路径中存在占位符的控制器方法不能匹配未设置占位符的请求

也就是说，路径中存在占位符的控制器方法，只能接收带了对应占位符的请求

### 占位符为空值或空格

```html
<a th:href="@{/requestMappingController/testRest///}">测试路径中的占位符_空值==>/testRest///</a><br/>
<a th:href="@{/requestMappingController/testRest/ / /}">测试路径中的占位符_空格==>/testRest/ / /</a><br/>
```

测试结果

![动画 (21)](https://s2.loli.net/2022/03/17/QypObUg467AJzEw.gif)

同时占位符为空格的情况是，后台打印了日志：`id= , username= `

可以看到，

- 空值匹配失败，报了`404`错误
- 空格匹配成功，路劲中对其解析成了对应的`URL`编码，即`%20`

### 小结

由以上情况测试结果可以得出

- SpringMVC 支持路径中含有占位符的形式
- 占位符只能通过`@PathVariable`注解获取（就目前所学知识而言）
- 占位符可以匹配特殊字符——空格，但不能匹配空字符



## 总结

`@RequestMapping`注解

- 功能：<mark>将请求和处理请求的控制器方法关联起来，建立映射关系</mark>
- 位置：作用在类上（请求路径的初始信息）；作用在方法上（请求路径的具体信息）
- `value`属性：可以匹配多个请求路径，匹配失败报`404`
- `method`属性：支持`GET`、`POST`、`PUT`、`DELETE`，默认不限制，匹配失败报`405`
- `params`属性：四种方式，`param`、`!param`、`param==value`、`param!=value`，匹配失败报`400`
- `headers`属性：四种方式，`header`、`!header`、`header==value`、`header!=value`，匹配失败报`400`
- 支持 Ant 风格路径：`?`（单个字符）、`*`（0 或多个字符）和`**`（0 或多层路径）
- 支持路径中的占位符：`{xxx}`占位符、`@PathVariable`赋值形参

以下导图仅供参考

![01-@RequestMapping](https://s2.loli.net/2022/03/17/gFfwxalQIjNbE3R.png)
