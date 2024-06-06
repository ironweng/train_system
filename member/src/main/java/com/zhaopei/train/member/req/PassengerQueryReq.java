package com.zhaopei.train.member.req;

import com.zhaopei.train.common.req.PageReq;

public class PassengerQueryReq extends PageReq {
//继承分页参数的类,因为基本上每个模块都会用到分页参数,所以把该参数定义在common里,供其他模块继承
    private Long memberId;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PassengerQueryReq{");
        sb.append("memberId=").append(memberId);
        sb.append('}');
        return sb.toString();
    }
}