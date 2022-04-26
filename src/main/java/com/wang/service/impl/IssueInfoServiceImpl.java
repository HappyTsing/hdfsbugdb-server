package com.wang.service.impl;

import com.wang.dao.ClassifyMapper;
import com.wang.dao.IssueInfoMapper;
import com.wang.pojo.IssueInfo;
import com.wang.service.IssueInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IssueInfoServiceImpl implements IssueInfoService {
    private static  final Logger logger = LoggerFactory.getLogger(IssueInfoServiceImpl.class);
    @Autowired
    private IssueInfoMapper issueInfoMapper;
    @Override
    public IssueInfo  getdata(){
        logger.info("进入getdata");
        IssueInfo issueInfo = issueInfoMapper.selectByPrimaryKey(1);
        System.out.println(issueInfo);
        return issueInfo;
    }

}
