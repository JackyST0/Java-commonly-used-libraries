package com.example.testspringboot.entity.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2025/2/10 15:18
 */
@Data
@ApiModel("ws通知返回对象")
public class NoticeWebsocketResp<T> {

    @ApiModelProperty(value = "通知类型")
    private T noticeType;

    @ApiModelProperty(value = "通知内容")
    private T noticeInfo;

}
