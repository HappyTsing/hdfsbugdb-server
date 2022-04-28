package com.wang.service;

import java.util.HashMap;

public interface LabelService {

     /**
      * key为id，value为name。{1=Vital}
      */
     HashMap<Integer, String> getLabelMap();

     /**
      * key为name，value为id。{Vital=1}
      */
     HashMap<String, Integer> getLabelMapReverse();
}