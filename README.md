# NOTE_SpringMVC

## 介绍

:sparkles: 【尚硅谷】SpringMVC教程丨一套快速上手spring mvc 学习笔记

## 更新

- :link: Github：[vectorxxxx/NOTE_SpringMVC: 尚硅谷SpringMVC教程丨一套快速上手spring mvc 学习笔记 (github.com)](https://github.com/vectorxxxx/NOTE_SpringMVC)
- :link: Gitee：[NOTE_SpringMVC: 尚硅谷SpringMVC教程丨一套快速上手spring mvc 学习笔记 (gitee.com)](https://gitee.com/vectorx/NOTE_SpringMVC)
- :link: GitCode：[VectorUx / NOTE_SpringMVC · GitCode](https://gitcode.net/qq_35925558/NOTE_SpringMVC)
- :link: 语雀：[SpringMVC从入门到精通 · 语雀 (yuque.com)](https://www.yuque.com/u21195183/fvry7k)
- :link: 博客园：[SpringMVC从入门到精通 - 随笔分类 - VectorX - 博客园 (cnblogs.com)](https://www.cnblogs.com/vectorx/category/2124029.html)
- :link: CSDN：[SpringMVC从入门到精通_VectorUx的博客-CSDN博客](https://blog.csdn.net/qq_35925558/category_11683845.html)
- :link: 掘金：[SpringMVC从入门到精通 - VectorX的专栏 - 掘金 (juejin.cn)](https://juejin.cn/column/7074468745569108004)

<mark>**整理不易，还望各位看官一键三连 :heart: :heart: :heart: **</mark>

<mark>**整理不易，还望各位看官一键三连 :heart: :heart: :heart: **</mark>

<mark>**整理不易，还望各位看官一键三连 :heart: :heart: :heart: **</mark>

:sparkles:下面开始吧~

---

## 一、目录

![SpringMVC目录](https://s2.loli.net/2022/04/06/VMKfQJ2GTz3BlgE.png)

### 0、简介

![00-SpringMVC 简介](https://s2.loli.net/2022/04/06/6ZsxiKhdY3GMrkQ.png)

### 1、@RequestMapping 注解

![01-@RequestMapping](https://s2.loli.net/2022/04/06/T9LA1YkstNaGCzB.png)

### 2、获取请求参数

![02-SpringMVC 获取请求参数](https://s2.loli.net/2022/04/06/Oi8heuAx679ZHVJ.png)

### 3、域对象共享数据

![03-域对象共享数据](https://s2.loli.net/2022/04/06/Z3UvDtkMBeno2ay.png)

### 4、视图

![04-SpringMVC 视图](https://s2.loli.net/2022/04/06/NWDOaBoTK6fCylg.png)

### 5、RESTful

![05-RESTful](https://s2.loli.net/2022/04/06/6EjpLKS9bNatVCG.png)

### 6、HttpMessageConverter

![06-HttpMessageConverter](https://s2.loli.net/2022/04/06/vJG6Od5CorwegEb.png)

### 7、拦截器和异常处理

![07-拦截器和异常处理](https://s2.loli.net/2022/04/06/k2mCghJQ96DRz4q.png)

### 8、完全注解开发

![08-完全注解开发](https://s2.loli.net/2022/04/06/eomNcLKUCaA8k6w.png)

### 9、执行流程

![09-SpringMVC 执行流程](https://s2.loli.net/2022/04/06/tR6wpBsdvfbiUHe.png)



## 二、配置文件

### 1、pom.xml

```xml
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
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.13.2</version>
</dependency>
<dependency>
    <groupId>commons-fileupload</groupId>
    <artifactId>commons-fileupload</artifactId>
    <version>1.4</version>
</dependency>
```

### 2、web.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">
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
    <!--配置 org.springframework.web.filter.HiddenHttpMethodFilter: 可以把 POST 请求转为 DELETE 或 POST 请求 -->
    <filter>
        <filter-name>HiddenHttpMethodFilter</filter-name>
        <filter-class>org.springframework.web.filter.HiddenHttpMethodFilter</filter-class>
    </filter>
    <filter-mapping>
        <filter-name>HiddenHttpMethodFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>

    <!--配置 SpringMVC 的前端控制器，对浏览器发送的请求统一进行处理-->
    <servlet>
        <servlet-name>springMVC</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <!--通过初始化参数指定 SpringMVC 配置文件的位置和名称-->
        <init-param>
            <!--contextConfigLocation 为固定值-->
            <param-name>contextConfigLocation</param-name>
            <!--使用 classpath: 表示从类路径查找配置文件，java 工程默认 src 下, Maven 工程默认 src/main/resources 下-->
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
</web-app>
```

### 3、springMVC.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd http://www.springframework.org/schema/mvc https://www.springframework.org/schema/mvc/spring-mvc.xsd">

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
    <!--InternalResourceViewResolver-->
    <!--<bean id="InternalResourceViewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver">-->
    <!--    <property name="prefix" value="/WEB-INF/templates/"/>-->
    <!--    <property name="suffix" value=".jsp"/>-->
    <!--</bean>-->

    <!--视图控制器：当前请求映射对应的控制器方法中没有其他请求过程的处理，只需设置一个视图名称时，就可以使用`view-controller`-->
    <mvc:view-controller path="/" view-name="index"></mvc:view-controller>
    <!--处理静态资源-->
    <mvc:default-servlet-handler/>
    <!--开启 MVC 的注解驱动-->
    <mvc:annotation-driven/>
    <!--配置文件上传解析器，将上传文件自动封装为MutilpartFile对象-->
    <bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver"></bean>

    <!--自定义拦截器-->
    <!--方式一-->
    <!--<mvc:interceptors>-->
    <!--    <bean id="myInterceptor" class="com.vectorx.springmvc.s00_helloworld.interceptor.MyInterceptor"></bean>-->
    <!--</mvc:interceptors>-->
    <!--方式二-->
    <!--<mvc:interceptors>-->
    <!--    <ref bean="myInterceptor"></ref>-->
    <!--</mvc:interceptors>-->
    <!--<mvc:interceptors>-->
    <!--    <ref bean="myInterceptor"></ref>-->
    <!--    <ref bean="myInterceptor2"></ref>-->
    <!--</mvc:interceptors>-->
    <!--方式三-->
    <!--<mvc:interceptors>-->
    <!--    <mvc:interceptor>-->
    <!--        <mvc:mapping path="/**"/>-->
    <!--        <mvc:exclude-mapping path="/"/>-->
    <!--        <ref bean="myInterceptor"></ref>-->
    <!--    </mvc:interceptor>-->
    <!--</mvc:interceptors>-->

    <!--异常处理解析器-->
    <!--<bean id="simpleMappingExceptionResolver"-->
    <!--      class="org.springframework.web.servlet.handler.SimpleMappingExceptionResolver">-->
    <!--    <property name="exceptionMappings">-->
    <!--        <props>-->
    <!--            &lt;!&ndash;-->
    <!--                properties的键表示处理器方法执行过程中出现的异常-->
    <!--                properties的值表示若出现指定异常，设置一个新的视图名称，跳转到指定页面-->
    <!--            &ndash;&gt;-->
    <!--            <prop key="java.lang.ArithmeticException">error</prop>-->
    <!--        </props>-->
    <!--    </property>-->
    <!--    &lt;!&ndash;设置一个属性名，将出现的异常信息共享在请求域中&ndash;&gt;-->
    <!--    <property name="exceptionAttribute" value="ex"></property>-->
    <!--</bean>-->
</beans>
```



## 三、完全注解开发

`WebInit`——`web.xml`

```java
/**
 * web工程的初始化类，代替web.xml
 */
public class WebInit extends AbstractAnnotationConfigDispatcherServletInitializer {
    /**
     * 指定Spring配置类
     *
     * @return
     */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{SpringConfig.class};
    }

    /**
     * 指定SpringMVC配置类
     *
     * @return
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebConfig.class};
    }

    /**
     * 指定SpringMVC的映射规则，即url-pattern
     *
     * @return
     */
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    /**
     * 注册过滤器
     *
     * @return
     */
    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceResponseEncoding(true);
        HiddenHttpMethodFilter hiddenHttpMethodFilter = new HiddenHttpMethodFilter();
        return new Filter[]{characterEncodingFilter, hiddenHttpMethodFilter};
    }
}
```

`WebConfig`——`springMVC.xml`

```java

/**
 * SpringMVC的配置类清单
 * 1、扫描组件
 * 2、视图解析器
 * 3、view-controller
 * 4、default-servlet-handler
 * 5、MVC注解驱动
 * 6、文件上传解析器
 * 7、拦截器
 * 8、异常处理解析器
 */
@Configuration
// ========== 1、扫描组件 ==========
@ComponentScan("com.vectorx.springmvc.controller")
// ========== 5、MVC注解驱动 ==========
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    // ========== 2、视图解析器 ==========
    /**
     * 生成模板解析器
     *
     * @return
     */
    @Bean
    public ITemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }

    /**
     * 生成模板引擎并注入模板解析器
     *
     * @param templateResolver
     * @return
     */
    @Bean
    public ISpringTemplateEngine templateEngine(ITemplateResolver templateResolver) {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }

    /**
     * 生成视图解析器并注入模板引擎
     *
     * @param templateEngine
     * @return
     */
    @Bean
    public ViewResolver viewResolver(ISpringTemplateEngine templateEngine) {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setOrder(1);
        viewResolver.setCharacterEncoding("UTF-8");
        viewResolver.setTemplateEngine(templateEngine);
        return viewResolver;
    }

    // ========== 3、view-controller ==========
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
    }

    // ========== 4、default-servlet-handler ==========
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    // ========== 6、文件上传解析器 ==========
    @Bean
    public MultipartResolver multipartResolver() {
        MultipartResolver multipartResolver = new CommonsMultipartResolver();
        return multipartResolver;
    }

    // ========== 7、拦截器 ==========
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TestInterceptor()).addPathPatterns("/**");
    }

    // ========== 8、异常处理解析器 ==========
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        SimpleMappingExceptionResolver exceptionResolver = new SimpleMappingExceptionResolver();
        Properties properties = new Properties();
        properties.setProperty(ArithmeticException.class.getCanonicalName(), "error");
        exceptionResolver.setExceptionMappings(properties);
        exceptionResolver.setExceptionAttribute("ex");
        resolvers.add(exceptionResolver);
    }
}
```



整理难免有误，欢迎大家批评指正！

---

> 署名 4.0 国际 (CC BY 4.0)。您可以自由地：共享 — 在任何媒介以任何形式复制、发行本作品；演绎 — 修改、转换或以本作品为基础进行创作；在任何用途下，甚至商业目的。只要你遵守许可协议条款，许可人就无法收回你的这些权利。惟须遵守下列条件：署名 — 您必须给出适当的署名，提供指向本许可协议的链接
