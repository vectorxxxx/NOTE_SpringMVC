> 笔记来源：[【尚硅谷】SpringMVC教程丨一套快速上手spring mvc](https://www.bilibili.com/video/BV1Ry4y1574R)

[TOC]

# 完全注解开发

## 1、AbstractAnnotationConfigDispatcherServletlnitializer

在 Servlet3.0 环境中，容器会在类路径中查找实现`javax.servlet.ServletContainerlnitializer`接口的类，如果找到的话就用它来配置 Servlet 容器

Spring 提供了这个接口的实现，名为`SpringServletContainerlnitializer`，这个类反过来又会查找实现`WebApplicationlnitializer`的类并将配置的任务交给它们来完成

Spring3.2 引入了一个便利的 `WebApplicationlnitializer`基础实现，名为`AbstractAnnotationConfigDispatcherServletlnitializer`

当类扩展了`AbstractAnnotationConfigDispatcherServletlnitializer`并将其部署到 Servlet3.0 容器时，容器会自动发现它，并用它来配置Servlet 上下文



## 2、初始化类

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



## 3、SpringMVC 配置类

SpringMVC 配置类清单

 * 1、扫描组件
 * 2、视图解析器
 * 3、view-controller
 * 4、default-servlet-handler
 * 5、MVC注解驱动
 * 6、文件上传解析器
 * 7、拦截器
 * 8、异常处理解析器

```java
// 标识为配置类
@Configuration
// ========== 1、扫描组件 ==========
@ComponentScan("com.vectorx.springmvc.controller")
// ========== 5、MVC注解驱动 ==========
@EnableWebMvc
public class WebConfig {}
```



## 4、视图解析器

```java
@Configuration
@ComponentScan("com.vectorx.springmvc.controller")
@EnableWebMvc
public class WebConfig {
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
}
```



## 5、WebMvcConfigurer

```java
@Configuration
@ComponentScan("com.vectorx.springmvc.controller")
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    // 略...

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
        properties.setProperty("java.lang.ArithmeticException", "error");
        exceptionResolver.setExceptionMappings(properties);
        exceptionResolver.setExceptionAttribute("ex");
        resolvers.add(exceptionResolver);
    }
}
```



## 6、文件上传解析器

```java
@Configuration
@ComponentScan("com.vectorx.springmvc.controller")
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    // 略...
    
    // ========== 6、文件上传解析器 ==========
    @Bean
    public MultipartResolver multipartResolver() {
        MultipartResolver multipartResolver = new CommonsMultipartResolver();
        return multipartResolver;
    }
}
```



## 测试

### 访问首页

![image-20220405173617166](https://s2.loli.net/2022/04/05/obe53BRuFjcJmH4.png)

### 测试异常

![image-20220405173640378](https://s2.loli.net/2022/04/05/aWnGC1gUd29tFMi.png)



## 附录：SpringMVC 配置类总览

```java
// 标识为配置类
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



## 总结

本届重点掌握

- web 工程初始化的配置方法
- SpringMVC 配置类的组件配置方法

附上导图，仅供参考

![08-完全注解开发](https://s2.loli.net/2022/04/05/SVJ7TmsadxzPenW.png)
