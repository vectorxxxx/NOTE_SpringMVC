> 笔记来源：[【尚硅谷】SpringMVC教程丨一套快速上手spring mvc](https://www.bilibili.com/video/BV1Ry4y1574R)

[TOC]

# 拦截器和异常处理

## 1、拦截器

### 1.1、拦截器的配置

SpringMVC 中的拦截器用于拦截控制器方法的执行，需要实现`HandlerInterceptor`，并在 SpringMVC 的配置文件中进行配置

#### 方式一

后台测试代码

```java
public class MyInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("MyInterceptor==>preHandle");
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("MyInterceptor==>postHandle");
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("MyInterceptor==>afterCompletion");
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
```

配置拦截器

```xml
<mvc:interceptors>
    <bean id="myInterceptor" class="com.vectorx.springmvc.s00_helloworld.interceptor.MyInterceptor"></bean>
</mvc:interceptors>
```

访问任意路径，后台日志信息

```console
MyInterceptor==>preHandle
MyInterceptor==>postHandle
MyInterceptor==>afterCompletion
```

#### 方式二

后台测试代码

```java
@Component
public class MyInterceptor implements HandlerInterceptor {
    // 略同
}
```

配置拦截器

```xml
<mvc:interceptors>
    <ref bean="myInterceptor"></ref>
</mvc:interceptors>
```

访问任意路径，后台日志信息

```console
MyInterceptor==>preHandle
MyInterceptor==>postHandle
MyInterceptor==>afterCompletion
```

#### 方式三

后台测试代码

```java
@Component
public class MyInterceptor implements HandlerInterceptor {
    // 略同
}
```

配置拦截器

```xml
<mvc:interceptors>
    <mvc:interceptor>
        <mvc:mapping path="/**"/>
        <mvc:exclude-mapping path="/"/>
        <ref bean="myInterceptor"></ref>
    </mvc:interceptor>
</mvc:interceptors>
```

此时除了访问`/`即首页时不会走拦截器，其他请求都会走拦截器，其后台日志信息

```console
MyInterceptor==>preHandle
MyInterceptor==>postHandle
MyInterceptor==>afterCompletion
```

> **注意**：这里的`path`路径配置的是`/**`，表示任意层路径的请求，符合`Ant`风格的路径。这与`web.xml`中`url-pattern`的`/*`匹配效果一致，但写法不同

#### 小结

- 方式一和方式二都是对`DispatcherServlet`所处理的所有请求进行拦截
- 方式三可以有选择拦截请求，通过`ref`或`bean`设置拦截器，通过`mvc:mapping`设置需要拦截的请求，通过`mvc:exclude-mapping`设置需要排除（即不需要拦截）的请求

### 1.2、拦截器的三个抽象方法

- `preHandle`：控制器方法执行之前执行`preHandle()`，其 boolean 类型的返回值表示是否拦截或放行
  - 返回 true 表示放行，即调用控制器方法
  - 返回 false 表示拦截，即不调用控制器方法
- `postHandle`：控制器方法执行之后执行`postHandle()`
- `afterCompletion`：处理完视图和模型数据，渲染视图完毕之后执行`afterCompletion()`

### 1.3、多个拦截器的执行顺序

#### 若每个拦截器的preHandle()都返回true

此时多个拦截器的执行顺序和拦截器在 SpringMVC 的配置文件的配置顺序有关：

`preHandle()`会按照配置的顺序执行，而`postHandle()`和`afterComplation()`会按照配置的反序执行

配置顺序

```xml
<mvc:interceptors>
    <ref bean="myInterceptor"></ref>
    <ref bean="myInterceptor2"></ref>
</mvc:interceptors>
```

后台日志信息

```console
MyInterceptor==>preHandle
MyInterceptor2==>preHandle
MyInterceptor2==>postHandle
MyInterceptor==>postHandle
MyInterceptor2==>afterCompletion
MyInterceptor==>afterCompletion
```

可以看出，`preHandle`顺序执行，`postHandle`和`afterCompletion`反序执行

#### 若某个拦截器的preHandle()返回了false

`preHandle()`返回 false 和它之前的拦截器的`preHandle()`都会执行，`postHandle()`都不执行，返回 false 的拦截器之前的拦截器的`afterComplation()`会执行

后台测试代码

```java
@Component
public class MyInterceptor2 implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("MyInterceptor2==>preHandle");
        return false;
    }
    // 略同
}
```

后台日志信息

```console
MyInterceptor==>preHandle
MyInterceptor2==>preHandle
MyInterceptor==>afterCompletion
```



## 2、异常处理

### 2.1、基于配置的异常处理

SpringMVC 提供了一个处理控制器方法执行过程中所出现异常的接口：`HandlerExceptionResolver`

`HandlerExceptionResolver`接口的实现类有：`DefaultHandlerExceptionResolver`和`SimpleMappingExceptionResolver`

![image-20220404180625663](https://s2.loli.net/2022/04/04/mDSwizT9Fsgj4lU.png)

SpringMVC 提供了自定义的异常处理器`SimpleMappingExceptionResolver`

配置文件

```xml
<bean id="simpleMappingExceptionResolver"
      class="org.springframework.web.servlet.handler.SimpleMappingExceptionResolver">
    <property name="exceptionMappings">
        <props>
            <!--
                properties的键表示处理器方法执行过程中出现的异常
                properties的值表示若出现指定异常，设置一个新的视图名称，跳转到指定页面
            -->
            <prop key="java.lang.ArithmeticException">error</prop>
        </props>
    </property>
    <!--设置一个属性名，将出现的异常信息共享在请求域中-->
    <property name="exceptionAttribute" value="ex"></property>
</bean>
```

后台测试代码

```java
@RequestMapping("testException")
public String testException() {
    int i = 1 / 0;
    return "success";
}
```

测试结果

![image-20220404183816999](https://s2.loli.net/2022/04/04/QVLYgqk5r8Z7wAv.png)

### 2.2、基于注解的异常处理

- `@ControllerAdvice`标识当前类为异常处理的组件
- `@ExceptionHandler`设置所标识方法处理的异常

配置注解

```java
// ControllerAdvice 标识当前类为异常处理的组件
@ControllerAdvice
public class ExceptionController {
    // ExceptionHandler 设置所标识方法处理的异常
    @ExceptionHandler(value = {ArithmeticException.class, NullPointerException.class})
    // ex 表示当前请求处理过程中出现的异常对象
    public String testException(Exception ex, Model model) {
        model.addAttribute("ex", ex);
        return "error";
    }
}
```

测试结果

![image-20220404183816999](https://s2.loli.net/2022/04/04/QVLYgqk5r8Z7wAv.png)



## 总结

本节重点掌握

- 拦截器的自定义方法、三种配置方式、三个抽象方法及执行顺序
- 异常处理解析器的两种方式：基于配置和基于注解

附上导图，仅供参考

![07-拦截器和异常处理](C:/Users/Archimedes/Pictures/Test/07-%E6%8B%A6%E6%88%AA%E5%99%A8%E5%92%8C%E5%BC%82%E5%B8%B8%E5%A4%84%E7%90%86.png)

