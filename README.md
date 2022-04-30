# 简介

该项目作为毕设的后端。

# 项目依赖

mysql和redis运行在腾讯云的ubuntu服务器上，开发在mac上进行，tomcat版本必须是9，若为10则无法启动。

```shell
tomcat 9.0.56
ubuntu 16.04
mysql  Ver 8.0.28-0ubuntu0.20.04.3 for Linux on x86_64 ((Ubuntu))
redis  redis_version:7.0.0
```

## mysql

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

#TODO sql语句 和 数据转换脚本

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

>  TIP：使用vim时，输入 `/`即可进入查询模式，输入想要查询的内容后， 回车，然后 `n`下一条，`N`上一条。

## tomcat

tomcat 9安装在mac上，`brew install tomcat@9` ，千万不要尝试tomcat10，会变得不幸！

Idea配置时选择 `Tomcat Server Local`，然后Tomcat Home选择 `/opt/homebrew/Cellar/tomcat@9/9.0.56_1/libexec`。

```shell
# 安装
brew install tomcat@9

# 查看Tomcat Home
brew ls tocat@9

# 找到libexec结尾
/opt/homebrew/Cellar/tomcat@9/9.0.56_1/libexec/
```

# 前置知识

## Spring

Spring的主要功能就像一个对象工厂，帮助你完成对象的实例化。

Spring主要有两个概念：控制反转IoC和切片

### 依赖注入

例如School类有很多属性，主要分为两类，如String和int等基础类型，还有对象类型，如Person本身也是一个Java类。

```java
import com.Person
public Class School{
		private String address;
  	private Person person;
}
```

同时，我们知道，一个Java类可以有多个构造函数：

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

首先我们谈论**有参构造**，此时可以说，School这个类，依赖两个东西：

- 基础类型String的address
- 同样为对象的Person的实例

因此，当我们使用有参构造来实例化School类的时候，需要传入这两个东西：

```java
public class test(){
  	public static void main(String[] args) {
      String address = "电子科技大学";
      Person person = new Person("leqing",18);
      School school = new School(address,person);
  }
}
```

这样，我们就成功实例化了school对象，并且使用 `构造函数`为它 `注入`了两个依赖：address和person。

此时，需要插入一个概念：Java Bean

- 所有属性都为private
- 至少提供提供默认构造方法，即无参构造
- 提供get和set。因为所有属性都是private，因此需要get获取属性，set设置属性值
- 实现serializable接口

如果属性不是private的，我们可以直接通过实例化的对象school，通过 `school.address`来获取对象属性，也可以通过 `school.address="xxx"`来设置属性值。

这样显然是不安全的，因此建议都采用Java Bean！

于是我们，添加set和get方法：

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

此次，我们在实例化School的时候，即便采用无参构造来实例化，也可以安全的通过set来为它注入依赖：

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

在一个庞大的项目中，我们可能需要 `new School`无数次，并且重复的为它注入对象，显然是十分麻烦的，此时引入新的概念：`控制反转`，即把对象创建的控制权，交给spring，不再由自己手动 `new`了。

> 约定：下文把诸如school这种实例对象，都称为bean实例对象。

通过上述内容，我们可以知道，spring所需要做的事情其实就两件：

- 我们在spring中管理bean实例，而不是自己new bean实例对象。
- 当手动new的时候，我们就获取了bean实例对象。在spring中管理后，我们也需要一种方式来获取实例对象。

### 一、配置文件XML方式

#### ①管理bean实例

共有两种方式：

- 构造器注入：正如我们使用有参构造来实例化对象一样，首先需要在类中提供一个有参构造函数。
- set注入：无需有参构造函数，但需要set方法。

每种注入方法又有许多细节，在此不赘述。

下面是个使用set注入方式的示例：

> 注意：为了代码的美观，删去了spring配置文件头部的重复内容。

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

已知new的时候可以传入不同的值，当然此处可以实现，id唯一，但name可以有多种：

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

#### ②获取bean实例

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

#### ①管理bean实例

管理bean实例其实有两个步骤：

- **步骤一**：`<bean id="school" class="com.wang.School">`：将类交给spring管理
- **步骤二**：`<property name="address" value="电子科技大学">`：为该类注入依赖

因此在使用注解的方式开发时，也要同时实现这两个步骤。

- **步骤一**：类管理注解：@Component、@Repository、@Service、@Controller。效果相同，区分只是为了更易读。

  - 设置id：@Component(value="idValue")，根据java注解的语法，`value=`可以省略：@Component("idValue")。

    ​				如果直接使用@Component()，默认idValue为当前类名，且首字母小写。

  - 设置class：无需设置，因为你在哪个类使用该注解，当然就是哪个类的class了！

- **步骤二**：注入数据注解

  - bean类型数据：
    - @Autowired：@Autowired只使用byType方式进行装配，若是同时存在相同class类型的bean，则无法装配！不过在纯注解开发中，不会同时存在class类型的bean。只有xml和注解方式同时使用时才会出现。示例见OneNote。
    - @Qualifier：使用@Qualifier指定对应bean的id进行辅助精确选择！
    - @Resource：@Resource结合了@Autowired和@Qualifier的方法，不过他是默认以byname的方式实现！且不是spring提供的，而是java自带的注解。
  - 基本类型和String类型：@Value
  - 集合类型：只能用XML进行配置！
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

#### ②获取类实例

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

# 文件

- controller
- dao
- dto
- pojo
- service
- utils
- resources
  - mapper
  - 
- web.xml



# 项目经验

## pom依赖

非常非常重要！比如 `spring-data-redis 2.6.0` ，它依赖于 `spring-context 5.3.13`，但之前我用的确是其他版本的，因此二者冲突，无法启动。

因此当出现错误时，首先要检查导入的新包是否有问题，是否与之前的包的依赖冲突！

## xx.properties

注意，spring只能使用一次：

```xml

<context:property-placeholder location="classpath:properties/*.properties"/>
```

如果在多个地方使用，会找不到文件。

## 移动文件

当你移动文件到某个地方时，Idea会自动修改一些路径。 但是！当你用 `/**/*` 等匹配符匹配了3个文件，但你修改了其中某个文件的位置。 idea会自动将通配符改成具体文件的名字，此时你其他文件就没有引用了！因此一定要注意！