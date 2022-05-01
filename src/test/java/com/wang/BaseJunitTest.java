package com.wang;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 注解说明：
 * 1)@RunWith：用于指定junit运行环境，是junit提供给其他框架测试环境接口扩展，为了便于使用spring的依赖注入，spring提供了org.springframework.test.context.junit4.SpringJUnit4ClassRunner作为Junit测试环境
 * 2)@ContextConfiguration：导入配置文件，一定要向示例一样一个一个导入。
 * <p>
 * 注意：下面这种写法是不行的！虽然看起来可以，但其实是【不行】的！！天坑！！@ContextConfiguration("classpath:spring-*.xml")
 * update：在整合redis时，发现@ContextConfiguration({"classpath:spring/applicationContext.xml"})是可行的！
 * 使用方法：此后所有的类，只需要 public class className extends BaseJunitTest{} 即可进行单元测试使用注解，无需重复添加@RunWith等注解了。
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
//@ContextConfiguration({"classpath:spring/spring-service.xml", "classpath:spring/spring-mybatis.xml"})
@ContextConfiguration({"classpath:spring/applicationContext.xml"})
public class BaseJunitTest {
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        System.out.println("================================================单元测试开始================================================");
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        System.out.println("================================================单元测试结束================================================");
    }
}