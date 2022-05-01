# 简介

本科毕设后端，基于 Spring + SpringMVC + Mybatis + Redis + Mysql。

# 项目依赖

mysql 和 redis 运行在腾讯云的 ubuntu 服务器上，开发在 mac 上进行，**☆tomcat 版本必须是 9**，若为 10 则无法启动。

```shell
tomcat 9.0.56
ubuntu 16.04
mysql  Ver 8.0.28-0ubuntu0.20.04.3 for Linux on x86_64 ((Ubuntu))
redis  redis_version:7.0.0
```

## mysql

mysql 需要一个数据库名为 HDFSBUGDB，其下四个表格。

数据转换脚本(通过 db.json 和 Label.json 生成 mysql 数据)和 sql 语句（快速直接构建 mysql 数据）参见：`hdfsbugdb-script`

```mysql
 # 获取密码
sudo cat /etc/mysql/debian.cnf

# 创建用户 参考：https://blog.51cto.com/niuben/3027422
sudo mysql

mysql> CREATE USER 'leqing'@'%' IDENTIFIED BY 'Ww0810083142';
mysql> GRANT ALL ON *.* TO 'leqing'@'%';

mysql> use mysql;
mysql> select user,host from user;

# 开启远程服务
sudo vi /etc/mysql/mysql.conf.d/mysqld.cnf
# 注释掉 bind-address = 127.0.0.1

# 重启mysql
sudo service mysql restart

# 腾讯云防火墙打开3306端口
```

项目中对 mysql 的配置在`resources/properties/db.properties`中。

## redis

```shell
# 从源码安装
wget https://download.redis.io/redis-stable.tar.gz
tar -xzvf redis-stable.tar.gz
cd redis-stable
make

# 开启远程服务并设置密码
vim redis.conf
# 注释掉 bind 127.0.0.1... 这一行，
# 解除注释 requirepass 081008

# 使用配置文件启动
src/redis-server redis.conf

# 密码登录
src/redis-cli
> AUTH 081008

# 查看端口号是否开启 -ef 和 -aux等效
ps -ef | grep redis
ps -aux | grep redis

# 腾讯云防火墙打开端口6379
```

> TIP：使用 vim 时，输入 `/`即可进入查询模式，输入想要查询的内容后， 回车，然后 `n`下一条，`N`上一条。

项目中对 redis 的配置在`resources/properties/redis.properties`中。

## tomcat

tomcat 9 安装在 mac 上，`brew install tomcat@9` ，千万不要尝试 tomcat10，会变得不幸！

### IDEA

Idea 配置时选择 `Tomcat Server Local`，然后 `Tomcat Home`选择 `/opt/homebrew/Cellar/tomcat@9/9.0.56_1/libexec`。

```shell
# 安装
brew install tomcat@9

# 查看Tomcat Home
brew ls tomcat@9

# 找到libexec结尾
/opt/homebrew/Cellar/tomcat@9/9.0.56_1/libexec/
```

### war 包部署到 tomcat

当 war 包部署到`Tomcat Home/webapps`时，会自动解包，然后通过：`localhost:端口号/war包名`即可访问。

```shell
cd /opt/homebrew/Cellar/tomcat@9/9.0.56_1/libexec
vim conf/server.xml
# vim 通过 /8080 搜索到默认端口号，修改为任意你想要的端口，此处我们使用8081
```

首先我们需要将项目打成 war 包。IDEA 进入 File - Project Structure - Artifacts。

红框 ① 中，Exploded 是文件夹形式，Archive 就是 war 包形式，选择 Archive。红框 ② 的地方可以修改 war 包的名字，改为 hdfsbugdb-server。

![war包步骤1](https://gitee.com/happytsing/figure-bed/raw/master/img/202204302159101.png)

IDEA 点击 Build，选择 Build Artifacts...

![war包步骤2 build](https://gitee.com/happytsing/figure-bed/raw/master/img/202204302202131.png)

在跳出来的选项中选择 Archive，然后在编译出来的 out 文件夹下可以找到 war 包。

![war包位置](https://gitee.com/happytsing/figure-bed/raw/master/img/202204302203861.png)

> war 包已提供：hdfsbugdb-server/hdfsbugdb-server.war

我们将 war 包复制，粘贴到 `Tomcat Home/webapps` 。然后启动 tomcat：

```shell
cd /opt/homebrew/Cellar/tomcat@9/9.0.56_1/libexec/bin
./startup.sh # 启动
./shutdown.sh # 关闭
```

此时，可以通过`http://localhost:8081/hdfsbugdb-server`来访问 war 包了。

# 前置知识

## Spring

Spring 的主要功能就像一个对象工厂，帮助你完成对象的实例化。

Spring 主要有两个概念：控制反转 IoC 和切片

### 依赖注入

例如 School 类有很多属性，主要分为两类，如 String 和 int 等基础类型，还有对象类型，如 Person 本身也是一个 Java 类。

```java
import com.Person
public Class School{
		private String address;
  	private Person person;
}
```

同时，我们知道，一个 Java 类可以有多个构造函数：

```java
public Class School{
		private String address;
  	private Person person;
  	// 无参构造
  	public School(){}

  	// 有参构造
  	public School(String address,Person person){
      this.address = address;
      this.person = person;
    }
}
```

首先我们谈论**有参构造**，此时可以说，School 这个类，依赖两个东西：

- 基础类型 String 的 address
- 同样为对象的 Person 的实例

因此，当我们使用有参构造来实例化 School 类的时候，需要传入这两个东西：

```java
public class test(){
  	public static void main(String[] args) {
      String address = "电子科技大学";
      Person person = new Person("leqing",18);
      School school = new School(address,person);
  }
}
```

这样，我们就成功实例化了 school 对象，并且使用 `构造函数`为它 `注入`了两个依赖：address 和 person。

此时，需要插入一个概念：Java Bean

- 所有属性都为 private
- 至少提供提供默认构造方法，即无参构造
- 提供 get 和 set。因为所有属性都是 private，因此需要 get 获取属性，set 设置属性值
- 实现 serializable 接口

如果属性不是 private 的，我们可以直接通过实例化的对象 school，通过 `school.address`来获取对象属性，也可以通过 `school.address="xxx"`来设置属性值。

这样显然是不安全的，因此建议都采用 Java Bean！

于是我们，添加 set 和 get 方法：

```java
public Class School{
		private String address;
  	private Person person;
  	// 无参构造
  	public School(){}

    public String getAddress() {
        return address;
    }
    public void setCode(String address) {
        this.address = address;
    }

    public Person getPerson() {
        return person;
    }
    public void setPerson(Person person) {
        this.person = person;
    }
}
```

此次，我们在实例化 School 的时候，即便采用无参构造来实例化，也可以安全的通过 set 来为它注入依赖：

```java
public class test(){
  	public static void main(String[] args) {
      String address = "电子科技大学";
      Person person = new Person("leqing",18);

      School school = new School();
      school.setAddress(address);
      school.setPerson(person)
  }
}
```

**总结**：依赖注入，就是为实例化的对象的属性，注入值，这个值就是该对象的依赖。

在一个庞大的项目中，我们可能需要 `new School`无数次，并且重复的为它注入对象，显然是十分麻烦的，此时引入新的概念：`控制反转`，即把对象创建的控制权，交给 spring，不再由自己手动 `new`了。

> 约定：下文把诸如 school 这种实例对象，都称为 bean 实例对象。

通过上述内容，我们可以知道，spring 所需要做的事情其实就两件：

- 我们在 spring 中管理 bean 实例，而不是自己 new bean 实例对象。
- 当手动 new 的时候，我们就获取了 bean 实例对象。在 spring 中管理后，我们也需要一种方式来获取实例对象。

### 一、配置文件 XML 方式

#### ① 管理 bean 实例

共有两种方式：

- 构造器注入：正如我们使用有参构造来实例化对象一样，首先需要在类中提供一个有参构造函数。
- set 注入：无需有参构造函数，但需要 set 方法。

每种注入方法又有许多细节，在此不赘述。

下面是个使用 set 注入方式的示例：

> 注意：为了代码的美观，删去了 spring 配置文件头部的重复内容。

```xml
<beans>
    <bean id="personId" class="com.wang.Person">
        <property name="name" value="leqing"/>
      	<property name="age" value="18"/>
    </bean>
    	<bean id="school" class="com.wang.School">
        <property name="address" value="电子科技大学"/>
        <property name="person" ref="personId"/>
    </bean>
</beans>
```

已知 new 的时候可以传入不同的值，当然此处可以实现，id 唯一，但 name 可以有多种：

```xml
<beans>
  	<--! <bean personId> 同上-->
    <bean name="schoolName1" class="com.wang.School">
        <property name="address" value="电子科技大学"/>
        <property name="person" ref="personId"/>
    </bean>
      <bean name="SchoolName2" class="com.wang.School">
        <property name="address" value="四川大学"/>
        <property name="person" ref="personId"/>
    </bean>
</beans>
```

#### ② 获取 bean 实例

```java
public class test(){
  	public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        School schoolById = (School) applicationContext.getBean("school");
        School schoolByName1 = (School) applicationContext.getBean("schoolName1");
      	School schoolByName2 = (School) applicationContext.getBean("schoolName1");

      	schoolById.getAddress() //使用
  }
}
```

### 二、注解方式

在使用注解之前，必须开启注解：

```
<beans>
  <context:component-scan base-package="com.wang"/>
</beans>
```

#### ① 管理 bean 实例

管理 bean 实例其实有两个步骤：

- **步骤一**：`<bean id="school" class="com.wang.School">`：将类交给 spring 管理
- **步骤二**：`<property name="address" value="电子科技大学">`：为该类注入依赖

因此在使用注解的方式开发时，也要同时实现这两个步骤。

- **步骤一**：类管理注解：@Component、@Repository、@Service、@Controller。效果相同，区分只是为了更易读。

  - 设置 id：@Component(value="idValue")，根据 java 注解的语法，`value=`可以省略：@Component("idValue")。

    如果直接使用@Component()，默认 idValue 为当前类名，且首字母小写。

  - 设置 class：无需设置，因为你在哪个类使用该注解，当然就是哪个类的 class 了！

- **步骤二**：注入数据注解

  - bean 类型数据：
    - @Autowired：@Autowired 只使用 byType 方式进行装配，若是同时存在相同 class 类型的 bean，则无法装配！不过在纯注解开发中，不会同时存在 class 类型的 bean。只有 xml 和注解方式同时使用时才会出现。示例见 OneNote。
    - @Qualifier：使用@Qualifier 指定对应 bean 的 id 进行辅助精确选择！
    - @Resource：@Resource 结合了@Autowired 和@Qualifier 的方法，不过他是默认以 byname 的方式实现！且不是 spring 提供的，而是 java 自带的注解。
  - 基本类型和 String 类型：@Value
  - 集合类型：只能用 XML 进行配置！
  - 改变作用范围的：@Scope

```java
// School依赖于Person，因此Person必须要交给Spring管理。
@Component
public Class Person{
  // 注入依赖
  @Value("leqing")
  private String name;
  @Value(18)
  private int age;

  public String printPerson(){
    return name + age;
  }
}

// School交给spring管理
@Component
public Class School{

  	// 注入依赖
  	@Value("电子科技大学")
		private String address;
  	@Autowired
  	private Person person;

  	public String printSchool(){
      return person.printPerson() + address;
    }
}
```

#### ② 获取类实例

```java
public class test(){
  	@Autowired
  	private ApplicationContext applicationContext;
  	public static void main(String[] args) {
        School schoolById = (School) applicationContext.getBean("school");
      	schoolById.printSchool()
  }
}
```

## SpringMVC

首先明确 MVC 架构模式，**MVC 分别指指：Model 模型、View 视图、Controller 控件器：**:

- View：视图，可以理解为前端界面。
- Model：模型，分为两个部分：① 数据 ② 数据的行为，即处理数据。
- Controller：控制器，用于将用户请求转发给相应的 Model 进行处理。处理的结果在视图中显示。

在传统的 JavaWeb 中，MVC 的经典实现是 JSP+servlet+javaBean。其中 jsp 就是 View，javabean 就是 Model 来实现业务，servlet 就是 Controller 控制器。

此时前后端是杂糅在一个项目中的，jsp 文件都放在 web 文件夹下，且 web 项目都有一个`web.xml`配置文件，在其中我们需要配置若干对`<servlet>`和`<servlet-mapping>`，前者是一个继承了 Servlet 类的 java 类，后者是拦截路径，即拦截到 A 路径，就让 servletA 处理。

```xml
<!-- web.xml -->
<web-app>
    <servlet>
        <servlet-name>HelloServlet</servlet-name>
        <servlet-class>com.wang.servlet.HelloServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>HelloServlet</servlet-name>
        <url-pattern>/user</url-pattern>
    </servlet-mapping>
</web-app>
```

时间推进到 SpringMVC 的架构，且前后端分离。

此时的 View 就是前端，如 Vue 等框架，当然最后生成的都是 JavaScript、html、css 代码。

而后端的作用主要就是提供一个 api 接口，以 JSON 格式返回数据。

因此后端主要就两个部分：Model 和 Controller。

实现大概如下，service 层调用 dao 层查询数据库，拿到数据，然后在 service 层做业务处理，在 controller 将用户请求的结果返回。其中数据承载 bean 放在 pojo 文件夹中。

由于是 web 项目，我们还是需要配置`web.xml`，已知她有两个部分，`<servlet>`和`<servlet-mapping>`，此时我们只需要配置一对即可！

```xml
<!-- web.xml -->
<web-app>
    <servlet>
        <servlet-name>springmvc</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:spring/springmvc-servlet.xml</param-value>
        </init-param>
        <!--启动级别-1-->
        <load-on-startup>1</load-on-startup>
    </servlet>

    <!--/ 匹配所有的请求；（不包括.jsp）-->
    <!--/* 匹配所有的请求；（包括.jsp）-->

    <servlet-mapping>
        <servlet-name>springmvc</servlet-name>
        <!-- 这个/说明所有的请求都会经过这个servlet，也就是DispatcherServlet -->
        <url-pattern>/</url-pattern>
    </servlet-mapping>
</web-app>
```

此时我们的`<servlet-mapping>`会拦截所有的请求，交给 SpringMVC 的中央控制器 DispatcherServlet，其本质也是一个继承了 Servlet 类的 java 类罢了！

此时，我们就可以用 SpringMVC 来处理请求。

SpringMVC 的配置文件主要需要配置三个东西：

- 处理器映射器 HandlerMapping
- 处理器适配器 HandlerAdapter
- 视图解析器 ViewResolver

这三个都支持丰富的定制，详见 SpringMVC 笔记。

此处仅讲解 RestFul 前后端分离的项目，且使用注解开发的情况，配置如下：

```xml
<beans>
    <!-- 自动扫描包，让指定包下的注解生效,由IOC容器统一管理 -->
    <!-- 1. 激活已经在spring中注册的bean（包括XML和注解） 2. 自动扫描，让指定包下的注解生效（实例化bean、注入bean的依赖参数）-->
    <context:component-scan base-package="com.wang.controller"/>

    <!-- 让Spring MVC不处理静态资源 -->
    <mvc:default-servlet-handler/>

    <!-- Springmvc开启注解-->
    <mvc:annotation-driven/>
    <!--切换HandlerMapping和HandlerAdapter为注解版本，效果相当于以下两句：-->
    <!--<bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping"/>-->
    <!--<bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter"/>-->
</beans>
```

由于后端仅提供 api 即可，并不需要展示数据，因此我们无需配置视图解析器。

### 请求映射与参数注解

注解开发时，除了 Spring 的组件与依赖注入相关的注解，如@Component、@Autowired、@Value 等，上文都已经提及，还有 SpringMVC 的请求映射与参数的注解。

- 请求映射
  - @RequestMapping：基本注解，可通用，基于不同的请求类型有很多特定的子注解
    - @GetMapping
    - @PostMapping
    - ...
- 参数注解
  - @RequestParam
  - @PathVariable
- RestFul 注解：@ResponseBody、@RestController

当使用 RestFul 风格的时候，可以直接用@ResponseBody 注解类或方法，此时由容器自动处理 Java 对象的转换并返回。容器内部通过 HttpMessageConverter 将方法返回的 Java 对象转换为 JSON 字符串后写入**Response 对象的 body 数据区**。

当然，可以直接使用@RestController 来代替@ResponseBody 和@Controller。

# 文件夹作用解析

- **controller**：存放 SpringMVC 的控制器

  > BookController.java：控制器

- **pojo**：存放实体类

  > Book.java：实体类，对象关系映射，操作实体类就实现操作数据库表

- **dao**：存放实体类对应的 mapper 接口

  > BookMapper.java：接口

- **dto**：往往会构建一个 Result.java 类，作为 controller 的返回对象，此外，有时候当我们联表查询时，查询的结果显然无法用 pojo 的实体类接受，因此在此构建诸如 BookLibrary.java，作为联表（join on）查询 Book 和 Library 数据库的结果的接收对象。

- **service**：存放业务处理的接口及其实现类

  - **Impl**：实现类放在 Impl 文件夹下

  > BookService.java：接口，业务方法
  >
  > BookServiceImpl.java：接口实现类，通过调用 Dao 层的相关方法读取数据库内容，并使用这些数据进行对应的业务操作

- **utils**：工具类，封装序列化等工具

- **resources**

  - **mapper：**mybatis 的映射器文件，可以理解为对接口 BookMapper.java 的实现。原本我们使用 implements 来实现一个接口，但在此处我们使用 BookMapper.xml 来实现。

    > BookMapper.xml：实现对数据库的直接操作，如增删改查

  - **properties**：把配置的属性和值抽离出来，在配置文件中可以通过 `${key}`来取出`value`。

    > db.properties
    >
    > redis.properties

  - **spring**：存放 Spring、SpringMVC、mybatis、redis 等的配置文件

    > applicationContext.xml：用于导入\*properties，整合导入 spring-mybatis.xml、spring-redis.xml、spring-service.xml
    >
    > spring-mybatis.xml：mybatis 的核心配置文件
    >
    > spring-redis.xml：redis 的核心配置文件
    >
    > spring-service：用于开启 com.wang.service.impl 文件夹下的注解扫描
    >
    > springmvc-servlet.xml：Springmvc 的配置文件，① 开启 com.wang.controller 文件夹下的注解扫描 ② 启用 springmvc 的注解功能

    此外，还有其余的配置：generatorConfig.xml、logback.xml

- **web/WEB-INF/web.xml**：web 项目的文件配置

- **test**：用于单元测试的文件夹，一般会构建一个 `BaseJunitTest.java`文件，开启注解等功能。此后使用时，只需要继承该类，就可以免去配置。

# 项目经验

## module 未导入

初次启动时，会出现找不到依赖的情况，进入 File - Project Structure - Artifacts，点击 `Put into Output Root`。

![依赖导入](https://gitee.com/happytsing/figure-bed/raw/master/img/202204301704701.png)

## pom 依赖冲突

非常非常重要！比如 `spring-data-redis 2.6.0` ，它依赖于 `spring-context 5.3.13`，但之前我用的确是其他版本的，因此二者冲突，无法启动。

因此当出现错误时，首先要检查导入的新包是否有问题，是否与之前的包的依赖冲突！

## property-placeholder

注意，spring 只能使用一次 property-placeholder：

```xml
<context:property-placeholder location="classpath:properties/*.properties"/>
```

如果在多个地方使用，会找不到文件。

因此，我们新建一个 `applicationContext.xml`文件，用于整合 spring 的其他配置（如 mybatis、redis 等），并在这里使用通配符导入 `*.properties`。

## Ides 移动文件自动修改路径

当你移动文件到某个地方时，Idea 会自动修改一些路径。 但是！当你用 `/**/*` 等匹配符匹配了 3 个文件，但你修改了其中某个文件的位置。 idea 会自动将通配符改成具体文件的名字，此时你其他文件就没有引用了！因此一定要注意！

# 项目优化

- 项目部署，可以改为 docker 部署。
- 若项目设计增删改，需要在 redis 中添加删除逻辑，否则缓存与数据库中内容不一致。
