package com.wang.dao;

import com.wang.BaseJunitTest;
import com.wang.pojo.Label;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

public class LabelMapperTest extends BaseJunitTest {
    @Autowired
    private LabelMapper labelMapper;

    @Test
    public void findAll(){
        List<Label> list = labelMapper.findAll();
        HashMap<Integer,String> map = new HashMap<>();
        for(Label label : list){
            map.put(label.getId(),label.getName());
        }
    }

}
