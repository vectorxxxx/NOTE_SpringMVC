> 笔记来源：[【尚硅谷】SpringMVC教程丨一套快速上手spring mvc](https://www.bilibili.com/video/BV1Ry4y1574R)

[TOC]

# SpringMVC 获取请求参数

## 1、通过 Servlet API 获取

将`HttpServletRequest`作为控制器方法的形参，此时`HttpServletRequest`类型的参数表示封装了当前请求的请求报文的对象

后台测试代码

```java
@RequestMapping("/testServletAPI")
public String testServletAPI(HttpServletRequest request) {
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    System.out.println("username=" + username + ",password=" + password);
    return "success";
}
```

前台测试代码

```html
<a th:href="@{/paramController/testServletAPI(username='admin',password='123456')}">通过 Servlet API 获取</a><br/>
```

测试结果

![动画 ](https://s2.loli.net/2022/03/18/QKqArlmZxPzCpoh.gif)

后台日志信息

```java
username=admin,password=123456
```

> **Q**：为何将`HttpServletRequest request`传入 testServletAPI() 方法中就可以使用？
>
> **A**：SpringMVC 的 IOC 容器帮我们注入了`HttpServletRequest `请求对象，同时`DispatherServlet`为我们调用 testServletAPI() 方法时自动给`request`参数赋了值，因此可以在方法形参位置传入请求对象`HttpServletRequest `就可以直接使用其`getParameter()`方法获取参数

尽管上述 Servlet API 原生方式可以获取请求参数，但是这样做就没有必要了。因为 SpringMVC 中帮我们封装好了更加便捷的方式获取请求参数



## 2、通过控制器方法形参获取

在控制器方法的形参位置，设置和请求参数同名的形参，当浏览器发送请求，匹配到请求映射时，在`DispatcherServlet`中就会将请求参数赋值给相应的形参

> **注意**：在`@RequestMapping`注解的“路径中的占位符”一节中，我们测试过了 restful 风格在不使用`@PathVariable`转而通过同名形参的方式，试图获取*占位符*的值，不过 SpringMVC 并没有很智能地给我们为同名参数赋值。但是这里 SpringMVC 允许我们使用同名形参为*请求参数*赋值。这是*占位符*和*请求参数*的一个区别，需要注意区分！！！

### 2.1、同名形参

后台测试代码

```java
@RequestMapping("/testParam")
public String testParam(String username, String password) {
    System.out.println("username=" + username + ",password=" + password);
    return "success";
}
```

前台测试代码

```html
<a th:href="@{/paramController/testParam(username='admin',password='123456')}">通过控制器方法形参获取</a><br/>
```

测试结果

![动画  (1)](https://s2.loli.net/2022/03/18/SfYarMA4hQJ3RTi.gif)

后台日志信息

```java
username=admin,password=123456
```

### 2.2、同名形参多值

若请求所传输的请求参数中有多值情况，此时可以在控制器方法的形参中设置*字符串数组*或者*字符串类型*的形参接收此请求参数

- 若使用字符串数组类型的形参，此参数的数组中包含了每一个数据
- 若使用字符串类型的形参，此参数的值为每个数据中间使用逗号拼接的结果

当某个请求参数有多个值时，比如前台含有复选框的情况，还能否使用这种方式呢？“实践出真知”，现在就进行测试

后台测试代码

```java
@RequestMapping("/testParam2")
public String testParam2(String username, String password, String hobby) {
    System.out.println("username=" + username + ", password=" + password + ", hobby=" + hobby);
    return "success";
}
```

前台测试代码

```html
<!--为了更直观地在地址栏中看到请求参数，这里使用get类型请求方式-->
<form th:action="@{paramController/testParam2}" method="get">
    用户名：<input type="text" name="username"><br/>
    密码：<input type="password" name="password"><br/>
    爱好：<input type="checkbox" name="hobby" value="Spring">Spring
    <input type="checkbox" name="hobby" value="SpringMVC">SpringMVC
    <input type="checkbox" name="hobby" value="SpringBoot">SpringBoot
    <br/><input type="submit" value="测试请求参数">
</form>
```

测试结果

![动画  (2)](https://s2.loli.net/2022/03/18/qLZdEQg1G9HuJ2i.gif)

前台请求路径中复选框的值为`hobby=Spring&hobby=SpringMVC&hobby=SpringBoot`，即出现了多个`hobby=value`的情况

后台日志信息

```java
username=hah, password=111111, hobby=Spring,SpringMVC,SpringBoot
```

可见 SpringMVC 的控制器方法，对多个`hobby`值使用了`,`进行拼接并赋值给同名形参

> **扩展**：如果这里使用 Servlet API 进行获取请求参数，就不能使用`getParameter()`方法获取 hobby 值了，而要使用`getParameterValues()`方法
>
> 后台代码测试
>
> ```java
> @RequestMapping("/testServletAPI2")
> public String testServletAPI2(HttpServletRequest request) {
>  String hobby = request.getParameter("hobby");
>  String[] hobby2 = request.getParameterValues("hobby");
>  System.out.println("hobby=" + hobby + ", hobby2=" + Arrays.toString(hobby2));
>  return "success";
> }
> ```
>
> 后台日志信息：通过`getParameter()`只能获取到 hobby 的第一个值，而`getParameterValues()`可以以数组的形式返回 hobby 的所有值
>
> ```java
> username=sdfg, password=sdfg, hobby=Spring, hobby2=[Spring, SpringMVC, SpringBoot]
> ```
>
> 当然还是那句话：不建议在 SpringMVC 中使用原生 Servlet API 方法！！！这里稍作回顾和了解即可

另外，控制器方法中使用`String`类型的数组接收 hobby 值也是可以的

```java
@RequestMapping("/testParam3")
public String testParam3(String username, String password, String[] hobby) {
    System.out.println("username=" + username + ", password=" + password + ", hobby=" + Arrays.toString(hobby));
    return "success";
}
```

后台日志信息

```java
username=aaaaaaaaa, password=aaaaaaaa, hobby=[Spring, SpringMVC, SpringBoot]
```



## 3、@RequestParam

`@RequestParam`是<mark>将请求参数和控制器方法的形参创建映射关系</mark>

一共有三个属性：

- `value`：指定为形参赋值的请求参数的参数名
- `required`：设置是否必须传输此请求参数，默认值为`true`
  - 若设置为`true`，则当前请求必须传输`value`所指定的请求参数，若没有传输该请求参数，且没有设置`defaultValue`属性，则页面报错`400：Required String parameter'xxx'is not present`；
  - 若设置为`false`，则当前请求不是必须传输`value`所指定的请求参数，若没有传输，则注解所标识的形参的值为`null` 
- `defaultValue`：不管`required`属性值为`true`或`false`，当`value`所指定的请求参数没有传输或传输的值为空值时，则使用默认值为形参赋值

实际开发中，请求参数与控制器方法形参未必一致，一旦出现这种情况，还能否接收到请求参数了呢？

这里简单地将前台`name="username"`改为`name="user_name"`进行测试，看下后台日志信息，果然没有接收到 user_name 这个请求参数

```java
username=null, password=aaaaaaaa, hobby=[Spring, SpringMVC, SpringBoot]
```

> **扩展思考**：这里也侧面证明一件事，SpringMVC 中对请求参数的赋值是根据是否同名来决定的，而不会根据参数在方法上的第几个位置决定，也就是说 SpringMVC 没有考虑将*请求参数个数、类型与顺序*与*控制器方法形参个数、类型与顺序*进行绑定。如果我们来设计 SpringMVC，应该考虑这种方案么？
>
> 个人觉得，这种方案虽然可以实现与 Java 重载方法的一一绑定关系，但实际操作起来有一定难度：
>
> - 比如数字类型可以当作 String 处理，也可以当作 Integer 处理，不好区分
> - 退一步来讲，如果考虑重载方法，SpringMVC 底层势必要对类中所有重载方法进行循环，判断是否满足个数、类型和顺序的要求，性能上一定有所影响
>
> 而限制请求路径和请求方式不能完全相同的话，就没有这种苦恼了。即使是重载方法，通过不同请求路径或请求方法来界定到底访问哪个方法就可以了
>
> SpringMVC 借助注解的方式，将请求参数与控制器方法形参关系绑定的决定权，交到开发者的手中。这种开发思维启发我们，如果有些功能不能很好地在底层进行实现，甚至可能会留下很多隐患时，还不如交给实际使用者，由他们去决定，否则很容易被使用者诟病（没有，我没有暗示某语言啊(●'◡'●)）

此时使用`@RequestParam`注解就可以实现请求参数与控制器方法形参的绑定

后台测试代码

```java
@RequestMapping("/testParam3")
public String testParam3(@RequestParam("user_name") String username, String password, String[] hobby) {
    System.out.println("username=" + username + ", password=" + password + ", hobby=" + Arrays.toString(hobby));
    return "success";
}
```

后台日志信息

```
username=ss, password=aaaaa, hobby=[Spring, SpringMVC, SpringBoot]
```

关于`@RequestParam`怎么使用，可以看下源码

```java
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {
    @AliasFor("name")
    String value() default "";
    
    @AliasFor("value")
    String name() default "";
    
    boolean required() default true;
    
    String defaultValue() default ValueConstants.DEFAULT_NONE;
}
```

- `name`和`value`：绑定的请求参数名，互为别名，用哪个都一样
- `required`属性：默认为`true`，表示必须要有此参数，不传则报错；不确定是否传参又不想报错，赋值为`false`即可
- `defaultValue`属性：不管`required`是`true`还是`false`，只要请求参数值为空（`""`或`null`），就为形参附上此值

### 3.1、required

`required`默认为`true`，即要求该请求参数不能为空。因为是默认值，所以添加`required="true"`与不写`required`属性是一样的

这里先测试下默认情况下不传对应请求参数时系统的反应如何，只需要将`user_name`一行注释即可，或直接在浏览器地址栏删除该请求参数也一样

测试结果

![image-20220318220615418](https://s2.loli.net/2022/03/18/a3sw5tfcev7lGkI.png)

报错信息：`400`错误的请求，必须的请求参数'user_name'...不存在

```html
HTTP Status 400 - Required request parameter 'user_name' for method parameter type String is not present
```

经测试，不论是为 username 传空值还是不传值，都是`400`错误

```
/testParam3?user_name=&password=11&hobby=Spring&hobby=SpringMVC
/testParam3?password=11&hobby=Spring&hobby=SpringMVC
```

如果将`required`设置为`false`，还会报错吗？

后台测试代码：只需要对`@RequestParam("user_name")`稍作改动，修改为`@RequestParam(value = "user_name", required = false)`即可

```java
@RequestMapping("/testParam3")
public String testParam3(@RequestParam(value = "user_name", required = false) String username, String password, String[] hobby) {
    System.out.println("username=" + username + ", password=" + password + ", hobby=" + Arrays.toString(hobby));
    return "success";
}
```

测试结果：可以发现，这次并没有报`400`错误

![image-20220318221131481](https://s2.loli.net/2022/03/18/3mWdpg4VTKhMzSR.png)

后台日志信息

```java
username=null, password=1111, hobby=[Spring, SpringMVC]
```

这是不传 user_name 的情况，如果是传空值呢？

测试结果：同样访问成功，没有报`400`错误

![image-20220318222554198](https://s2.loli.net/2022/03/18/rmZGLnNF5aAP2xj.png)

后台日志信息

```java
username=, password=111, hobby=[Spring, SpringMVC]
```

> **Q**：不是说默认是`true`吗？为什么在没有使用`@RequestParam`注解时，也能正常访问呢？
>
> **A**：这个默认值本身就是在使用`@RequestParam`注解时生效的，如果都没有使用到`@RequestParam`，就没有相应限制了

### 3.2、defaultValue

后台测试代码

```java
@RequestMapping("/testParam3")
public String testParam3(
    @RequestParam(value = "user_name", required = false, defaultValue = "heh") String username,
    String password, String[] hobby) {
    System.out.println("username=" + username + ", password=" + password + ", hobby=" + Arrays.toString(hobby));
    return "success";
}
```

请求路径：传空值和不传值两种情况

```url
/testParam3?user_name=&password=asdf&hobby=Spring&hobby=SpringMVC
/testParam3?password=asdf&hobby=Spring&hobby=SpringMVC
```

后台日志信息

```console
username=heh, password=asdf, hobby=[Spring, SpringMVC]
```

可以发现，不管是为 username 传空值还是不传值，最终都会被赋上默认值

这里将`required`修改为`true`，即默认值的情况，发现也是可以请求成功的

> **注意**：`required`一节测试中，在`required`的默认值情况下，没有为请求参数赋值传值或传空值，会产生`400`的错误。
>
> 而只要为请求参数设置默认值，即使用`@RequestParam`注解的`defaultValue`属性赋上值，就不会有`400`错误了。
>
> 换句话说，只要设置了`defaultValue`属性值，`required`属性就失效形同虚设了



## 4、@RequestHeader

`@RequestHeader`是<mark>将请求头信息和控制器方法的形参创建映射关系</mark>

一共有三个属性：`value`、`required`、`defaultValue`，用法同`@RequestParam`

因为`@RequestHeader`与`@RequestParam`别无二致，所以这里我们简单测试下效果

后台测试代码

```java
@RequestMapping("/testHeader")
public String testHeader(
    @RequestHeader(value = "Host") String host,
    @RequestHeader(value = "Test", required = false, defaultValue = "RequestHeader") String test) {
    System.out.println("Host=" + host + ", test=" + test);
    return "success";
}
```

请求路径

```url
http://localhost:8080/SpringMVC/paramController/testParam4
```

后台日志信息

```console
Host=localhost:8080, test=RequestHeader
```



## 5、@CookieValue

`@CookieValue`是<mark>将 Cookie 数据和控制器方法的形参创建映射关系</mark>

一共有三个属性：`value`、`required`、`defaultValue`，用法同`@RequestParam`

> **注意**：
>
> - 在`JSP`中，`Session`依赖于`Cookie`，`Session`是服务器端的会话技术，`Cookie`是客户端的会话技术。
> - 会话技术默认的生命周期是浏览器开启和浏览器关闭，只要浏览器不关闭，`Cookie`将一直存在。
> - 调用`getSession()`方法时，首先会检测请求报文中是否有携带`JSESSIONID`的`Cookie`。如果没有，说明当前会话是第一次创建`Session`对象，则
>   - 在服务端创建一个`Cookie`，以键值对形式存储。键是固定的`JSESSIONID`，值是一个 UUID 随机序列
>   - 在服务端创建一个`HttpSession`对象，并放在服务器所维护的 Map 集合中。Map 的键是`JSESSIONID`的值，值就是`HttpSession`对象
>   - 最后把`Cookie`相应给浏览器客户端，此时`JSESSIONID`的`Cookie`存在于响应报文中。每次浏览器向服务器发送请求都会携带`Cookie`，此后`JSESSIONID`的`Cookie`将存在于请求报文中

为了能获取到`Cookie`值，需要先调用下`getSession()`方法。我们直接在之前的 testServletAPI() 方法中稍作修改

```java
@RequestMapping("/testServletAPI")
public String testServletAPI(HttpServletRequest request) {
    HttpSession session = request.getSession();
    // ...
}
```

首次发送请求后，F12 查看前台该请求的*响应报文*信息

![image-20220319113953469](https://s2.loli.net/2022/03/19/UVnWNgq2FQiXmuj.png)

会发现在`Set-Cookie`属性中存在`JSESSIONID=xxx`的信息

```headers
Set-Cookie: JSESSIONID=C3DFF845C38BF655C02DDA0BD2DD5638; Path=/SpringMVC; HttpOnly
```

后面每次发送请求，`JSESSIONID`的`Cookie`将会放在*请求报文*信息

![image-20220319120344633](https://s2.loli.net/2022/03/19/OJ9tdEyHU72AmhL.png)

会发现在`Cookie`属性中存在`JSESSIONID=xxx`的信息

```headers
Cookie: JSESSIONID=C3DFF845C38BF655C02DDA0BD2DD5638
```

经过上面的折腾，我们产生了`Cookie`数据，现在我们就可以使用`@CookieValue`注解进行操作了。正片开始~

后台测试代码

```java
@RequestMapping("/testCookie")
public String testCookie(
    @CookieValue(value = "JSESSIONID") String jSessionId,
    @CookieValue(value = "Test", required = false, defaultValue = "CookieValue") String test) {
    System.out.println("jSessionId=" + jSessionId + ", test=" + test);
    return "success";
}
```

前台请求报文信息

![image-20220319121031037](https://s2.loli.net/2022/03/19/QLDiYPmeBuUal3t.png)

后台日志信息

```console
jSessionId=C3DFF845C38BF655C02DDA0BD2DD5638, test=CookieValue
```



## 6、通过实体类获取

可以在控制器方法的形参位置设置一个实体类类型的形参，此时浏览器传输的请求参数的参数名和实体类中的属性名一致，那么请求参数就会为此属性赋值

前台测试代码

```html
<form th:action="@{/paramController/testBean}" method="post">
    用户名：<input type="text" name="username"><br/>
    密码：<input type="password" name="password"><br/>
    性别：<input type="radio" name="gender" value="男人">男
    <input type="radio" name="gender" value="女人">女<br/>
    年龄：<input type="text" name="age"><br/>
    邮箱：<input type="text" name="email"><br/>
    <input type="submit" value="测试请求参数">
</form>
```

后台测试代码

```java
@RequestMapping("/testBean")
public String testBean(User user) {
    System.out.println(user);
    return "success";
}
```

User 类：要求属性名与请求参数名一致

```java
public class User {
    private String username;
    private String password;
    private String gender;
    private String age;
    private String email;
    // Setter、Getter方法略

    @Override
    public String toString() {
        return "User{" +"username='" + username + '\'' + ", password='" + password + '\'' + ", gender='" + gender + '\'' +", age='" + age + '\'' + ", email='" + email + '\'' +'}';
    }
}
```

测试结果

![动画  (3)](https://s2.loli.net/2022/03/19/DN3rKT7219pkeJG.gif)

后台日志信息

```console
User{username='aa', password='11', gender='å¥³äºº', age='12', email='123@qq.com'}
User{username='aa', password='11', gender='ç·äºº', age='12', email='123@qq.com'}
```

貌似基本成功了，但却出现了乱码的情况，什么原因呢？



## 7、处理乱码问题

> **注意**：在 Servlet 阶段，是通过`request.setCharacterEncoding("UTF-8");`的方式解决乱码问题的。虽然 SpringMVC 中可以使用`HttpServletRequest`对象，但是没有效果。原因也很简单，是因为请求参数获取在前，设置编码格式在后

事实胜于雄辩，简单测试下

后台测试代码

```java
@RequestMapping("/testServletAPI3")
public String testServletAPI3(HttpServletRequest request) throws UnsupportedEncodingException {
    request.setCharacterEncoding("UTF-8");
    String username = request.getParameter("username");
    System.out.println("username=" + username);
    return "success";
}
```

前台测试代码

```html
<form th:action="@{/paramController/testServletAPI3}" method="post">
    用户名：<input type="text" name="username"><br/>
    <input type="submit" value="测试请求参数">
</form>
```

后台日志信息

```console
username=å¼ ä¸
```

可能你会说，上面的测试都是`post`请求，如果是`get`请求呢？~~问得好，下次不要问了~~

```html
<a th:href="@{/paramController/testServletAPI3(username='张三')}">通过setCharacterEncoding设置编码</a><br/>
```

后台日志信息

```
username=张三
```

> **Q**：这是为什么呢？怎么`get`请求还搞特殊？
>
> **A**：这是因为 Tomcat 的 conf 目录下的 `server.xml`中配置了`URIEncoding="UTF-8"`的原因。这样`get`请求的乱码问题就可以一次性解决了
>
> ```xml
> <Connector port="8080" protocol="HTTP/1.1" connectionTimeout="20000" redirectPort="8443" URIEncoding="UTF-8"/>
> ```
>
> 如果一开始就没有配置，那`get`请求也会乱码，所以拜托不是`get`请求搞特殊了喂！
>
> **Q**：既然在`server.xml`配置下编码格式就行了，为什么只支持`get`请求啊？还说不是搞特殊？
>
> **A**：...你赢了
>
> **Q**：退一步来说，`post`请求能不能在请求参数获取之后再去处理也可以吧，只要知道其本身的编码
>
> **A**：试一下咯

我们先通过 [在线乱码恢复](http://www.mytju.com/classcode/tools/messycoderecover.asp) 看下，乱码的文本实际编码是什么

![image-20220319150515999](https://s2.loli.net/2022/03/19/Heg6x9wvX82aj5Y.png)

很显然，乱码本身为`ISO-8859-1`格式，我们转换为`UTF-8`编码格式即可

后台测试代码

```java
// 对其进行iso-8859-1解码并重新UTF-8编码
username = new String(username.getBytes("ISO-8859-1"), "UTF-8");
System.out.println("username=" + username);
```

后台日志信息

```console
username=张三
```

> 有上述测试可知，要想处理乱码问题，思路有二：
>
> 1. 获取请求参数之后，手动解码编码。但是这种方式要求每次处理`post`请求的请求参数都要手动处理，太不人性化了吧。~~你嫌烦，我还嫌烦呢~~（❌）
> 2. 获取请求参数之前“做手脚”：发送请求之前，也就是在`Servlet`处理请求之前（👌）
>
> 那什么组件时在`Servlet`之前执行的呢？
>
> 众所周知 ~~（我不知道）~~，JavaWeb 服务器中三大组件：监听器、过滤器、`Servlet`。很显然，监听器和过滤器都在`Servlet`之前
>
> - `ServletContextListener`监听器：只是来监听`ServletContext`的创建和销毁，都是只执行一次
> - `Filter`过滤器：只要设置了过滤路径，只要当前所访问的请求地址满足过滤路径，那么都会被过滤器过滤
>
> 很显然，用过滤器就可以做到在发送请求之前“做手脚”，这样所有请求都要经过过滤器的处理，再交给`DispatherServlet`处理
>
> 但是，这个过滤不需要我们写，SpringMVC 已为我们准备好了，只要再`web.xml`中进行配置即可

我们先对`web.xml`进行配置

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
```

重启后测试，看下后台日志信息

```console
username=张三
```

Perfect! 配置很简单，测试结果立竿见影，乱码问题得到了解决

![img](https://s2.loli.net/2022/03/19/Ie5RJkGOADrYzx3.gif)

> “知其然，知其所以然”

这个神奇的`CharacterEncodingFilter`到底干了什么？我们一起来看下源码一探究竟

![image-20220319160141358](https://s2.loli.net/2022/03/19/krTeLAUEmiNR9a3.png)

我们知道，在`<init-param>`标签中配置的属性值，其实就是为对应类进行的属性注入。这里可以很清楚地看到`encoding`和`forceResponseEncoding`两个属性值，同时注意到`encoding`被`@Nullable`注解修饰，表示其可以为空，`forceResponseEncoding`默认为`false`，即不配置不生效

另外，看一个`Filter`最重要的找它的`doFilter()`方法

![image-20220319160550784](https://s2.loli.net/2022/03/19/GudNs7mcepnOTSP.png)

可以看到，`CharacterEncodingFilter`类中并没有`doFilter()`方法，那去它的基类中找找吧~

![image-20220319160644342](https://s2.loli.net/2022/03/19/8vWNJEQTldkn2YC.png)

直接读源码

```java
@Override
public final void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
    throws ServletException, IOException {
    // 就是判断是不是http请求和相应，不管
    if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
        throw new ServletException("OncePerRequestFilter just supports HTTP requests");
    }
    // 拿到一些了对象和参数，继续往下看看有什么用处
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;
    String alreadyFilteredAttributeName = getAlreadyFilteredAttributeName();
    boolean hasAlreadyFilteredAttribute = request.getAttribute(alreadyFilteredAttributeName) != null;
    // 三个条件判断，一一看下
    // 1、跳过转发或不过滤的，就直接进行过滤链的下一个请求（确信）
    if (skipDispatch(httpRequest) || shouldNotFilter(httpRequest)) {
        filterChain.doFilter(request, response);
    }
    // 2、已经过滤属性的，多了一层判断，貌似时处理错误转发的？（不太懂）
    else if (hasAlreadyFilteredAttribute) {
        if (DispatcherType.ERROR.equals(request.getDispatcherType())) {
            doFilterNestedErrorDispatch(httpRequest, httpResponse, filterChain);
            return;
        }
        filterChain.doFilter(request, response);
    }
    // 3、其他情况（应该就是正常情况吧）：设置下属性，再走 doFilterInternal 方法（还有印象吗？这个方法我们在其子类CharacterEncodingFilter中看到过的，那就顺藤摸瓜）
    else {
        request.setAttribute(alreadyFilteredAttributeName, Boolean.TRUE);
        try {
            doFilterInternal(httpRequest, httpResponse, filterChain);
        }
        finally {
            request.removeAttribute(alreadyFilteredAttributeName);
        }
    }
}
```

回到`CharacterEncodingFilter`中看下`doFilterInternal()`方法

```java
@Override
protected void doFilterInternal(
    HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException {
	// 获取编码格式，很明显就是获取web.xml中的配置值了（确信）
    String encoding = getEncoding();
    if (encoding != null) {
        // 二选一：配置了forceRequestEncoding为true或者请求的字符编码没有被设置，就给请求对象设置编码格式
        if (isForceRequestEncoding() || request.getCharacterEncoding() == null) {
            request.setCharacterEncoding(encoding);
        }
        // 配置了forceResponseEncoding为true，就给响应对象设置编码格式
        if (isForceResponseEncoding()) {
            response.setCharacterEncoding(encoding);
        }
    }
    filterChain.doFilter(request, response);
}
```

可以看出

- 没有在`web.xml`设置编码格式就不管了，爱咋咋地
- 设置了`encoding`就看下是请求对象还是响应对象
  - 请求对象：如果打开了`forceRequestEncoding`即*强制请求编码*开关，就给设置下编码；就算没打开这个开关，只要请求对象还没有设置过字符编码格式，那就给它设置下
  - 响应对象：只有打开了`forceResponseEncoding`即*强制响应编码*开关，才给设置编码



## 总结

获取请求的方式有两种：

- 通过 Servlet API 获取（不推荐）
- 通过控制器方法获取（就是要用它，不然学 SpringMVC 干什么，~~不是~~）

SpringMVC 获取请求参数的注解：`@RequestParam`、`@RequestHeader`、`@CookieValue`

- 都是作用在控制器方法上的形参的（就是获取请求参数的，还能作用在别的地方？）
- 都有三个属性：`value`/`name`、`required`、`defaultValue`（这不是四个吗？~~呸~~）
- 主要解决形参和请求参数名不同名的问题，其次是必填问题，最后是缺省值的问题（顺序确定对吗？~~别误人子弟了，不是，我错了~~）

如果请求参数与控制器方法形参同名，就可以不用上述的`@RequestParam`注解

如果请求参数有多个值，通过字符串类型或字符数组类型都可以获取

如果请求参数与控制器方法形参对象属性同名，同理。即满足同名条件时，SpringMVC 中允许通过实体类接收请求参数

> **Q**：那请求头和`Cookie`呢，同名也可以不加注解么？
>
> **A**：大哥，这刚开始我是没想到的，不过我又回头试了下，确信不可以！

最后附上导图，仅供参考

![02-SpringMVC 获取请求参数](https://s2.loli.net/2022/03/19/FInHqy8QMuk6oD4.png)
