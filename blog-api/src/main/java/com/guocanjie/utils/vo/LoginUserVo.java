package com.guocanjie.utils.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class LoginUserVo {
    //    防止前端精度丢失
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String account;
    private String nickname;
    private String avatar;
}
