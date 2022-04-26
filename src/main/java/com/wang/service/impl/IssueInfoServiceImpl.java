package com.wang.service.impl;

import com.wang.dao.ClassifyMapper;
import com.wang.dao.IssueInfoMapper;
import com.wang.pojo.IssueInfo;
import com.wang.service.IssueInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IssueInfoServiceImpl implements IssueInfoService {
    @Autowired
    private IssueInfoMapper issueInfoMapper;
    @Override
    public IssueInfo  getdata(){
        System.out.println("进入getdata");
        IssueInfo issueInfo = issueInfoMapper.selectByPrimaryKey(1);
        System.out.println(issueInfo);
        return issueInfo;
    }

}
