> 笔记来源：[【尚硅谷】SpringMVC教程丨一套快速上手spring mvc](https://www.bilibili.com/video/BV1Ry4y1574R)

[TOC]

# SpringMVC 执行流程

## 1、SpringMVC 常用组件

- `DispatcherServlet`：<mark>前端控制器</mark>，不需要工程师开发，由框架提供
  - 作用：统一处理请求和响应，整个流程控制的中心，由它调用其它组件处理用户的请求
- `HandlerMapping`：<mark>处理器映射器</mark>，不需要工程师开发，由框架提供
  - 作用：根据请求的 url、method 等信息查找`Handler`，即控制器方法
- `Handler`：<mark>处理器</mark>，需要工程师开发
  - 作用：在`DispatcherServlet`的控制下`Handler`对具体的用户请求进行处理
- `HandlerAdapter`：<mark>处理器适配器</mark>，不需要工程师开发，由框架提供
  - 作用：通过`HandlerAdapter`对处理器（控制器方法）进行执行
- `ViewResolver`：<mark>视图解析器</mark>，不需要工程师开发，由框架提供
  - 作用：进行视图解析，得到相应的视图，例如：`ThymeleafView`、`InternalResourceView`、`RedirectView`
- `View`：<mark>视图</mark>
  - 作用：将模型数据通过页面展示给用户



## 2、DispatcherServlet 初始化过程

`DispatcherServlet`本质上是一个 Servlet，所以天然的遵循 Servlet 的生命周期，宏观上是 Servlet 生命周期来进行调度

![image-20220406004101035](https://s2.loli.net/2022/04/06/n8YyI5fqm6CkcvF.png)

![image-20220406004325136](https://s2.loli.net/2022/04/06/ghZoEWdTHiQ6Owe.png)

### 2.1、初始化 WebApplicationContext

所在类：`org.springframework.web.servlet.FrameworkServlet`

```java
protected WebApplicationContext initWebApplicationContext() {
    WebApplicationContext rootContext =
        WebApplicationContextUtils.getWebApplicationContext(getServletContext());
    WebApplicationContext wac = null;
    if (this.webApplicationContext != null) {
        wac = this.webApplicationContext;
        if (wac instanceof ConfigurableWebApplicationContext) {
            ConfigurableWebApplicationContext cwac = (ConfigurableWebApplicationContext) wac;
            if (!cwac.isActive()) {
                if (cwac.getParent() == null) {
                    cwac.setParent(rootContext);
                }
                configureAndRefreshWebApplicationContext(cwac);
            }
        }
    }
    if (wac == null) {
        wac = findWebApplicationContext();
    }
    if (wac == null) {
        // 创建ApplicationContext
        wac = createWebApplicationContext(rootContext);
    }
    if (!this.refreshEventReceived) {
        synchronized (this.onRefreshMonitor) {
            // 刷新ApplicationContext
            onRefresh(wac);
        }
    }
    if (this.publishContext) {
        String attrName = getServletContextAttributeName();
        // 将IOC容器在应用域共享
        getServletContext().setAttribute(attrName, wac);
    }
    return wac;
}
```

### 2.2、创建 WebApplicationContext

所在类：`org.springframework.web.servlet.FrameworkServlet`

```java
protected WebApplicationContext createWebApplicationContext(@Nullable ApplicationContext parent) {
    Class<?> contextClass = getContextClass();
    if (!ConfigurableWebApplicationContext.class.isAssignableFrom(contextClass)) {
        throw new ApplicationContextException(
            "Fatal initialization error in servlet with name '" + getServletName() +
            "': custom WebApplicationContext class [" + contextClass.getName() +
            "] is not of type ConfigurableWebApplicationContext");
    }
    // 反射创建IOC容器对象
    ConfigurableWebApplicationContext wac =
        (ConfigurableWebApplicationContext) BeanUtils.instantiateClass(contextClass);
    wac.setEnvironment(getEnvironment());
    // 设置父容器：将Spring上下文对象作为SpringMVC上下文对象的父容器
    wac.setParent(parent);
    String configLocation = getContextConfigLocation();
    if (configLocation != null) {
        wac.setConfigLocation(configLocation);
    }
    configureAndRefreshWebApplicationContext(wac);
    return wac;
}
```

### 2.3、DispatcherServlet 初始化策略

`FrameworkServlet`创建`WebApplicationContext`后，调用`onRefresh(wac)`刷新容器

此方法在`DispatcherServlet`中进行了重写，调用了`initStrategies(context)`方法，初始化策略，即初始化`DispatcherServlet`的各个组件

所在类：`org.springframework.web.servlet.DispatcherServlet`

```java
protected void initStrategies(ApplicationContext context) {
    // 初始化文件上传解析器
    initMultipartResolver(context);
    initLocaleResolver(context);
    initThemeResolver(context);
    // 初始化处理器映射
    initHandlerMappings(context);
    // 初始化处理器适配器
    initHandlerAdapters(context);
    // 初始化处理器异常处理器
    initHandlerExceptionResolvers(context);
    initRequestToViewNameTranslator(context);
    // 初始化视图解析器
    initViewResolvers(context);
    initFlashMapManager(context);
}
```



## 3、DispatcherServlet 调用组件处理请求

### 3.1、processRequest

`FrameworkServlet`重写`HttpServlet`中的`service()`和`doXxx()`，这些方法中调用了`processRequest(request，response)`

所在类：`org.springframework.web.servlet.FrameworkServlet`

```java
protected final void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    long startTime = System.currentTimeMillis();
    Throwable failureCause = null;
    
    LocaleContext previousLocaleContext = LocaleContextHolder.getLocaleContext();
    LocaleContext localeContext = buildLocaleContext(request);
    
    RequestAttributes previousAttributes = RequestContextHolder.getRequestAttributes();
    ServletRequestAttributes requestAttributes = buildRequestAttributes(request, response, previousAttributes);
    
    WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);
    asyncManager.registerCallableInterceptor(FrameworkServlet.class.getName(), new RequestBindingInterceptor());
    
    initContextHolders(request, localeContext, requestAttributes);
    try {
        // 重点关注
        doService(request, response);
    }
    catch (ServletException | IOException ex) {
        failureCause = ex;
        throw ex;
    }
    catch (Throwable ex) {
        failureCause = ex;
        throw new NestedServletException("Request processing failed", ex);
    }
    finally {
        resetContextHolders(request, previousLocaleContext, previousAttributes);
        if (requestAttributes != null) {
            requestAttributes.requestCompleted();
        }
        logResult(request, response, failureCause, asyncManager);
        publishRequestHandledEvent(request, response, startTime, failureCause);
    }
}
```

### 3.2、doService

所在类：`org.springframework.web.servlet.DispatcherServlet`

```java
protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
    logRequest(request);
    Map<String, Object> attributesSnapshot = null;
    if (WebUtils.isIncludeRequest(request)) {
        attributesSnapshot = new HashMap<>();
        Enumeration<?> attrNames = request.getAttributeNames();
        while (attrNames.hasMoreElements()) {
            String attrName = (String) attrNames.nextElement();
            if (this.cleanupAfterInclude || attrName.startsWith(DEFAULT_STRATEGIES_PREFIX)) {
                attributesSnapshot.put(attrName, request.getAttribute(attrName));
            }
        }
    }
    
    request.setAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE, getWebApplicationContext());
    request.setAttribute(LOCALE_RESOLVER_ATTRIBUTE, this.localeResolver);
    request.setAttribute(THEME_RESOLVER_ATTRIBUTE, this.themeResolver);
    request.setAttribute(THEME_SOURCE_ATTRIBUTE, getThemeSource());
    
    if (this.flashMapManager != null) {
        FlashMap inputFlashMap = this.flashMapManager.retrieveAndUpdate(request, response);
        if (inputFlashMap != null) {
            request.setAttribute(INPUT_FLASH_MAP_ATTRIBUTE, Collections.unmodifiableMap(inputFlashMap));
        }
        request.setAttribute(OUTPUT_FLASH_MAP_ATTRIBUTE, new FlashMap());
        request.setAttribute(FLASH_MAP_MANAGER_ATTRIBUTE, this.flashMapManager);
    }
    RequestPath previousRequestPath = null;
    if (this.parseRequestPath) {
        previousRequestPath = (RequestPath) request.getAttribute(ServletRequestPathUtils.PATH_ATTRIBUTE);
        ServletRequestPathUtils.parseAndCache(request);
    }
    try {
        // 重点关注
        doDispatch(request, response);
    }
    finally {
        if (!WebAsyncUtils.getAsyncManager(request).isConcurrentHandlingStarted()) {
            if (attributesSnapshot != null) {
                restoreAttributesAfterInclude(request, attributesSnapshot);
            }
        }
        if (this.parseRequestPath) {
            ServletRequestPathUtils.setParsedRequestPath(previousRequestPath, request);
        }
    }
}
```

### 3.3、doDispatch

所在类：`org.springframework.web.servlet.DispatcherServlet`

```java
protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
    HttpServletRequest processedRequest = request;
    HandlerExecutionChain mappedHandler = null;
    boolean multipartRequestParsed = false;

    WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);
    try {
        ModelAndView mv = null;
        Exception dispatchException = null;
        try {
            processedRequest = checkMultipart(request);
            multipartRequestParsed = (processedRequest != request);
			// 包含handler、interceptorList、interceptorIndex 
            // 		handler：浏览器发送的请求所匹配的控制器方法
            // 		interceptorList：处理控制器方法的所有拦截器集合 
            // 		interceptorIndex：拦截器索引，控制拦截器aftercompletion()的执行
            mappedHandler = getHandler(processedRequest);
            if (mappedHandler == null) {
                noHandlerFound(processedRequest, response);
                return;
            }
			// 创建处理器适配器，负责调用控制器方法
            HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());
            String method = request.getMethod();
            boolean isGet = HttpMethod.GET.matches(method);
            if (isGet || HttpMethod.HEAD.matches(method)) {
                long lastModified = ha.getLastModified(request, mappedHandler.getHandler());
                if (new ServletWebRequest(request, response).checkNotModified(lastModified) && isGet) {
                    return;
                }
            }
            // 调用拦截器preHandle方法，正序执行
            if (!mappedHandler.applyPreHandle(processedRequest, response)) {
                return;
            }
            // 由处理器适配器调用具体的控制器方法，最终获得ModelAndview对象
            mv = ha.handle(processedRequest, response, mappedHandler.getHandler());
            if (asyncManager.isConcurrentHandlingStarted()) {
                return;
            }
            applyDefaultViewName(processedRequest, mv);
            // 调用拦截器postHandle方法，反序执行
            mappedHandler.applyPostHandle(processedRequest, response, mv);
        }
        catch (Exception ex) {
            dispatchException = ex;
        }
        catch (Throwable err) {
            dispatchException = new NestedServletException("Handler dispatch failed", err);
        }
        // 后续处理：处理模型数据和渲染视图
        processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchException);
    }
    catch (Exception ex) {
        triggerAfterCompletion(processedRequest, response, mappedHandler, ex);
    }
    catch (Throwable err) {
        triggerAfterCompletion(processedRequest, response, mappedHandler,
                               new NestedServletException("Handler processing failed", err));
    }
    finally {
        if (asyncManager.isConcurrentHandlingStarted()) {
            if (mappedHandler != null) {
                mappedHandler.applyAfterConcurrentHandlingStarted(processedRequest, response);
            }
        }
        else {
            if (multipartRequestParsed) {
                cleanupMultipart(processedRequest);
            }
        }
    }
}
```

### 3.4、processDispatchResult

所在类：`org.springframework.web.servlet.DispatcherServlet`

```java
private void processDispatchResult(HttpServletRequest request, HttpServletResponse response,
                                   @Nullable HandlerExecutionChain mappedHandler, @Nullable ModelAndView mv,
                                   @Nullable Exception exception) throws Exception {
    boolean errorView = false;
    if (exception != null) {
        if (exception instanceof ModelAndViewDefiningException) {
            logger.debug("ModelAndViewDefiningException encountered", exception);
            mv = ((ModelAndViewDefiningException) exception).getModelAndView();
        }
        else {
            Object handler = (mappedHandler != null ? mappedHandler.getHandler() : null);
            mv = processHandlerException(request, response, handler, exception);
            errorView = (mv != null);
        }
    }
    if (mv != null && !mv.wasCleared()) {
        // 处理模型数据和渲染视图
        render(mv, request, response);
        if (errorView) {
            WebUtils.clearErrorRequestAttributes(request);
        }
    }
    else {
        if (logger.isTraceEnabled()) {
            logger.trace("No view rendering, null ModelAndView returned.");
        }
    }
    if (WebAsyncUtils.getAsyncManager(request).isConcurrentHandlingStarted()) {
        return;
    }
    if (mappedHandler != null) {
        // 调用拦截器afterCompletion方法，反序执行
        mappedHandler.triggerAfterCompletion(request, response, null);
    }
}
```



## 4、SpringMVC 执行流程

1）用户向服务器发送请求，请求被 SpringMVC 前端控制器`DispatcherServlet`捕获

2）`DispatcherServlet`对请求 URL 进行解析，得到请求资源标识符 URI ，判断请求 URI 对应的映射是否存在：

若不存在，再判断是否配置了`mvc:default-servlet-handler` 

- i.如果没配置，则控制台报映射查找不到，客户端展示 404 错误

```console
DEBUG org.springframework.web.servlet.Dispatcherservlet - GET "/springMVC/testHaha", parameters={}
WARN org.springframework.web.servlet.PageNotFound - No mapping for GET /springMVC/testHaha 
DEBUG org.springframework.web.servlet.Dispatcherservlet - Completed 404 NOT_FOUND
```

- ii.如果有配置，则访问目标资源（一般为静态资源，如：JS，CSS，HTML），找不到客户端也会展示404错误

```console
  DEBUG org.springframework.web.servlet.Dispatcherservlet - GET "/springMVC/testHaha", parameters={}
  handler.SimpleUrlHandlerMapping Mapped to org.springframework.web.servlet.resource.DefaultServletHttpRequestHandlerDispatcherservlet - Completed 404 NOT_FOUND
  ```

若存在，则执行一下流程

3）根据该 URI，调用`HandlerMapping`获得该`Handler`配置的所有相关的对象（包括`Handler`对象以及`Handler`对象对应的拦截器），最后以`HandlerExecutionChain`执行链对象的形式返回

4）`DispatcherServlet`根据获得的`Handler`，选择一个合适的`HandlerAdapter`

5）如果成功获得`HandlerAdapter`，此时将开始执行拦截器的`preHandler()`方法【正向】

6）提取 Request 中的模型数据，填充`Handler`入参，开始执行`Handler`（Controller）方法，处理请求。在填充`Handler`的入参过程中，根据你的配置，Spring 将帮你做一些额外的工作：

- a）`HttpMessageConveter`：将请求消息（如 json、xml 等数据）转换成一个对象，将对象转换为指定的响应信息
- b）数据转换：对请求消息进行数据转换。如 String 转换成 Integer、Double 等
- c）数据格式化：对请求消息进行数据格式化。如将字符串转换成格式化数字或格式化日期等
- d）数据验证：验证数据的有效性（长度、格式等），验证结果存储到 BindingResult 或 Error 中

7）`Handler`执行完成后，向`DispatcherServlet`返回一个`ModelAndView`对象

8）此时将开始执行拦截器的`postHandle()`方法【逆向】

9）根据返回的`ModelAndView`（此时会判断是否存在异常，如果存在异常，则执行`HandlerExceptionResolver`进行异常处理）选择一个适合的`ViewResolver`进行视图解析，根据`Model`和`View`，来渲染视图

10）渲染视图完毕执行拦截器的`afterCompletion()`方法【逆向】

11）将渲染结果返回给客户端



## 总结

本届重点掌握

- SpringMVC 常用组件
- DispatcherServlet 的初始化过程和调用方法
- SpringMVC 执行流程

附上导图，仅供参考

![09-SpringMVC 执行流程](https://s2.loli.net/2022/04/06/tR6wpBsdvfbiUHe.png)
