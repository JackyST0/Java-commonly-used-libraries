package com.example.testspringboot.mapstruct;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2024/9/25 10:28
 */
public class Test7 {

    public static void main(String[] args) {
       Map map = new HashMap();
       map.put("张三丰", new Date());
       map.put("张无忌", new Date());
       Map newMap = MapMapper.INSTANCE.toMap(map);
       System.out.println(newMap);
    }
}
