package com.wang.service.Impl;

import com.wang.BaseJunitTest;
import com.wang.dao.IssueInfoMapper;
import com.wang.pojo.IssueInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


public class IssueInfoServiceImplTest extends BaseJunitTest {
    private static  final Logger logger = LoggerFactory.getLogger(IssueInfoServiceImplTest.class);

//    @Autowired
//    private ResearchMapper researchMapper;
    @Autowired
    private IssueInfoMapper issueInfoMapper;

    @Test
    public void getdata(){
        logger.info("进入getdata");
        IssueInfo issueInfo = issueInfoMapper.selectByPrimaryKey(1);
        System.out.println(issueInfo);

    }
}
