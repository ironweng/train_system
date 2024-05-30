package com.zhaopei.train.member.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MemberRegisterReq {

    @NotBlank(message = "手机号不能为空")
    private String mobile;
}
