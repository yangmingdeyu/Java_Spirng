# Java_Spirngs
关于javaSpringAop的学习代码

 开发术语

- 连接点(Joinpoint)：连接点是程序类中客观存在的方法，可被Spring拦截并切入内容。
  - 说白了 类中的哪些方法 可以被增强 这些方法 就称为是 连接点
- 切入点(Pointcut)：被Spring切入连接点。
  - 真正被增强的方法 称为 切入点
- 通知、增强(Advice)：可以为切入点添加额外功能，分为：前置通知、后置通知、异常通知、环绕通知，最终通知等。
  - 实际增强的逻辑部分 称为通知（增强）
- 目标对象(Target)：代理的目标对象 真实对象
- 引介(Introduction)：一种特殊的增强，可在运行期为类动态添加Field和Method。
- 织入(Weaving)：把通知应用到具体的类，进而创建新的代理类的过程。
- 代理(Proxy)：被AOP织入通知后，产生的结果类。
- 切面(Aspect)：由切点和通知组成，将横切逻辑织入切面所指定的连接点中。 把通知 应用到 切入点的过程

![image](https://github.com/yangmingdeyu/Java_Spirng/assets/51312267/16b14b7b-25b5-4cfe-b1fa-4451a9519e11)

#### Schema-based 的 aop实现方式 了解

导入相关依赖

~~~xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-aspects</artifactId>
    <version>5.1.6.RELEASE</version>
</dependency>
~~~

原始类用上面的UserServiceImpl 

首先定义通知类

~~~java
/**
 * 基于Schema-based的aop实现方式
 * 测试前置增强
 *
 */
public class MyBeforeAdvice implements MethodBeforeAdvice {

    @Override
    public void before(Method method, Object[] objects, Object o) throws Throwable {
        System.out.println("前置通知正在执行");
    }
}
~~~

​	spring-context.xml引入AOP命名空间

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
       http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/aop
       http://www.springframework.org/schema/aop/spring-aop.xsd
       ">
</beans>
```

定义bean标签

~~~xml
 <!-- 目标类  -->
    <bean id="userService" class="com.qf.spring.aop.service.impl.UserServiceImpl"/>
    <!-- 增强类  -->
    <bean id="myAdvice" class="com.qf.spring.aop.advice.MyBeforeAdvice"/>
    <!-- 进行aop的配置   -->
    <aop:config>
        <!--  切点   expression里面写的是切入点表达式 , 表四切入到所有的save方法上-->
        <aop:pointcut id="myPointCut" expression="execution(* save())"/>
        <!--  绑定增强代码到切入点 advisor是增强的意思 advice-ref:增强类是什么,poingcut-ref:切入点是哪个   -->
        <aop:advisor advice-ref="myAdvice" pointcut-ref="myPointCut"/>
    <!--   完成上面的工作表示会在save方法上进行增强 -->
    </aop:config>
~~~

配置完成之后, 前置通知就配置完成了, 我们在使用这个方法的时候,就会看到前置通知被执行了

#### 基于 AspectJ 的 AOP 实现方式

基于xml的配置方式

导入依赖

~~~xml
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>5.1.6.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-aspects</artifactId>
            <version>5.1.6.RELEASE</version>
        </dependency>
~~~

首先自定义通知类

~~~java
import org.aspectj.lang.ProceedingJoinPoint;


/**
 * 就是一个普通的类,我想让这个类作为我的增强类
 */
public class MyAspectJAdvice {

    public void before(){
        System.out.println("前置增强代码");
    }

    public void after(){
        System.out.println("最终增强代码, 类似于finally,不管目标方法有没有异常都要执行");
    }

    public Object around(ProceedingJoinPoint jp) throws Throwable {
        System.out.println("环绕增强前面的代码");
        //让目标方法继续执行
        Object result = jp.proceed();
        System.out.println("环绕增强后面的代码");
        return result;
    }

    public void throwing(Exception e){
        System.out.println("异常增强抛出执行代码,只有在目标方法抛出异常时,才能执行,异常信息是"+e.getMessage());

    }

    public void afterReturning(){
        System.out.println("后置增强");
    }
}
~~~

xml文件配置

aop头部信息

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
       http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/aop
       http://www.springframework.org/schema/aop/spring-aop.xsd
       ">
~~~

目标类和增强类

~~~xml
    <!-- 目标类  -->
    <bean id="userService" class="com.qf.spring.aop.service.impl.UserServiceImpl"/>
    <!--   配置增强类 -->
    <bean id="advice" class="com.qf.spring.aop.advice.MyAspectJAdvice"/>
~~~

aop的xml配置

~~~xml
    <!-- 配置AOp   -->
    <aop:config>
        <!-- 配置切入点   -->
        <aop:pointcut id="pc" expression="execution(* save())"/>
        <!--  直接配置切面     ref就是我们增强类,有五种子标签 -->
        <aop:aspect ref="advice">
            <!--   前置增强  pointcut-ref是引用切入点   -->
            <aop:before method="before" pointcut-ref="pc"/>
            <!--   环绕增强    -->
            <aop:around method="around" pointcut-ref="pc"/>
            <!--   异常抛出增强    -->
            <aop:after-throwing method="throwing" pointcut-ref="pc" throwing="e"/>
            <!--   最终增强    -->
            <aop:after method="after" pointcut-ref="pc"/>
            <!--   后置增强    -->
            <aop:after-returning method="afterReturning" pointcut-ref="pc"/>
        </aop:aspect>
    </aop:config>
~~~

测试

~~~java
    @Test
    public void save02(){
        ApplicationContext context = new ClassPathXmlApplicationContext("/aspectj-beas.xml");
        UserService us = context.getBean(UserService.class);
        us.save();
    }
~~~

结果

~~~tex
前置增强代码
环绕增强前面的代码
新增数据成功
环绕增强后面的代码
最终增强代码, 类似于finally,不管目标方法有没有异常都要执行
后置增强
~~~

让目标方法抛出一个异常就可以看到异常增强代码执行

~~~java
public class UserServiceImpl implements UserService {
    @Override
    public int save() {
        System.out.println("新增数据成功");
        int a = 1/0;
        return 1;
    }
}
~~~

再运行

~~~java
前置增强代码
环绕增强前面的代码
新增数据成功
异常增强抛出执行代码,只有在目标方法抛出异常时,才能执行,异常信息是/ by zero
最终增强代码, 类似于finally,不管目标方法有没有异常都要执行
java.lang.ArithmeticException: / by zero
~~~



##### 异常增强补充

~~~java
    public void throwing(Exception e){
        System.out.println("异常增强抛出执行代码,只有在目标方法抛出异常时,才能执行,异常信息是"+e.getMessage());
  }
~~~

xml文件中

~~~xml
   			<!--   异常抛出增强    -->
            <aop:after-throwing method="throwing" pointcut-ref="pc" throwing="e"/>
~~~



后置增强也可以添加东西

~~~xml
       <!--   后置增强    -->
            <aop:after-returning method="afterReturning" pointcut-ref="pc" returning="resut"/>
~~~

~~~java
  public void afterReturning(Object resut){
        System.out.println("后置增强,返回值是"+resut);
~~~

~~~
前置增强代码
环绕增强前面的代码
新增数据成功
环绕增强后面的代码
最终增强代码, 类似于finally,不管目标方法有没有异常都要执行
后置增强,返回值是1
~~~

 

#### 切入点表达式

语法结构execution([权限修饰符] [返回值类型] [类全路径] [方法名称] ([参数列表]))

~~~java
<!--匹配参数-->
<aop:pointcut id="myPointCut" expression="execution(* *(com.qf.aaron.aop.basic.User))" />
<!--匹配方法名（无参）-->
<aop:pointcut id="myPointCut" expression="execution(* save())" />
<!--匹配方法名（任意参数）你没有看错,就是两个点-->
<aop:pointcut id="myPointCut" expression="execution(* save(..))" />
<!--匹配返回值类型-->
<aop:pointcut id="myPointCut" expression="execution(com.qf.aaron.aop.basic.User *(..))" />
<!--匹配类名-->
<aop:pointcut id="myPointCut" expression="execution(* com.qf.aaron.aop.basic.UserServiceImpl.*(..))" />
<!--匹配包名  这个包下面的任意类,任意方法-->
<aop:pointcut id="myPointCut" expression="execution(* com.qf.aaron.aop.basic.*.*(..))" />
<!--匹配包名、以及子包名下的所有类的所有方法-->
<aop:pointcut id="myPointCut" expression="execution(* com.qf.aaron.aop..*.*(..))" />
~~~



修改上面的代码

~~~xml
 <!-- 配置切入点   后面的表达式的意思是在这个包下的所有类的所有方法中返回值为任意的任意参数的方法-->
        <aop:pointcut id="pc" expression="execution(* com.qf.spring.aop.service..*.*(..) )"/>
~~~

