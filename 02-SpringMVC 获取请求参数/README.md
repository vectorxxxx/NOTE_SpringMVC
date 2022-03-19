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



## 2、通过控制器方法形参获取参数

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

