package com.example.testspringboot.mapstruct;

import org.mapstruct.MapMapping;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Date;
import java.util.Map;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2024/9/25 10:26
 */
@Mapper
public interface MapMapper {

    MapMapper INSTANCE = Mappers.getMapper(MapMapper.class);

    @MapMapping(valueDateFormat = "yyyy-MM-dd")
    Map<String,String> toMap(Map<String, Date> map);
}
