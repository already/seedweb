seedweb
=======

# What's the SeedWeb

&nbsp;&nbsp;&nbsp;&nbsp;eedWeb生长在spring beans 和aop 上，分为E模式（embedded type）和C模式（Container type）. E模式（embedded type）是独立jar包运行，提供web服务，独立完成业务需求。C模式（Container type） 可以部署在Tomcat、Resin等服务器，完成业务需求。

&nbsp;&nbsp;&nbsp;&nbsp;SeedWeb是一个简单的web框架。它主要通过注解，完成地址到业务handler的映射，并支持rest,视图层面暂时支持velocity。 同时自定义了一套Steps的概念，通过注解和简单编码，可以对业务的前置检测提供灵活、粒度更小的工作支持。 针对E模式，单独提供filter完成来自于jar的js、css、图片的读取。它有两个重要注解 @requestMapping ,@Step来作为业务数据流转的桥梁。<br>

工程目录结构<br>
seedweb/<br>
├── seedweb-frame/<br>
├── seedweb-onejar/<br>
└── seedweb-ex/<br>

进入项目根目录,输入以下命令或点击install.bat

`mvn clean install -Dmaven.test.skip`

&nbsp;&nbsp;&nbsp;&nbsp;seed-onejar/target 下产生可执行jar包(E模式),在windows下点击seed-onejar/run.bat 即可启动一个独立web服务。seedweb-ex/target 下产生war包（c模式）

&nbsp;&nbsp;&nbsp;&nbsp;框架包(seed-frame)中类20个左右，核心的8个。SeedWeb 的请求上下文的载体是ThreadLocal,其中ThreadLocalUtils、WebContextUtils 支持request被线程处理期间的上下文工作。SeedWeb对请求和回传的处理交由AOP来拦截处理，由切面RequestAspectj、 枚举ResponseType来支持，其中ResponseType封装了请求后的反馈动作。SeedWeb的注解后业务方法、以及相关的 初始化工作由WebApplicationContext、MethodContext来处理。以及处理工具类WebUtils、RestfulUtils(支持restful的请求处理)
