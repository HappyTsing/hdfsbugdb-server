package com.wang.service.impl;

import com.wang.dao.ClassifyMapper;
import com.wang.dao.IssueInfoMapper;
import com.wang.dao.ResearchMapper;
import com.wang.pojo.IssueInfo;
import com.wang.pojo.Research;
import com.wang.service.IssueInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
@Service
public class IssueInfoServiceImpl implements IssueInfoService {
    private static  final Logger logger = LoggerFactory.getLogger(IssueInfoServiceImpl.class);
    @Autowired
    private IssueInfoMapper issueInfoMapper;

    @Autowired
    private ResearchMapper researchMapper;
    @Override
    public IssueInfo  getdata(){
        logger.info("进入getdata");
        IssueInfo issueInfo = issueInfoMapper.selectByPrimaryKey(1);
//        List<Research> researchs = researchMapper.findAll();
//        for(Research r:researchs){
//            System.out.println(r);
//            System.out.println(r.getIssueinfo());
//        }

        return issueInfo;
    }

}
