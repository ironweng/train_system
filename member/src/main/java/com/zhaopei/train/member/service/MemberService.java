package com.zhaopei.train.member.service;


import cn.hutool.core.collection.CollUtil;
import com.zhaopei.train.common.exception.BusinessException;
import com.zhaopei.train.common.exception.BusinessExceptionEnum;
import com.zhaopei.train.common.util.SnowUtil;
import com.zhaopei.train.member.domain.Member;
import com.zhaopei.train.member.domain.MemberExample;
import com.zhaopei.train.member.mapper.MemberMapper;
import com.zhaopei.train.member.req.MemberRegisterReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    @Autowired
    private MemberMapper memberMapper;

    public int count(){
        return Math.toIntExact(memberMapper.countByExample(null));
    }

    public long register (MemberRegisterReq req){
        String mobile=req.getMobile();
        //MemberExample相当于是一个工具，它为每个Member类中的字段设置了很多条件查询的方法
        //例如下面的andMobileEqualTo方法就是为Mobile字段设置的一个条件查询方法
        MemberExample memberExample=new MemberExample();
        memberExample.createCriteria().andMobileEqualTo(mobile);
        List<Member> list=memberMapper.selectByExample(memberExample);

        if(CollUtil.isNotEmpty(list)){
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_EXIST);
        }

        Member member =new Member();
        member.setId(SnowUtil.getSnowflakeNextId());
        member.setMobile(mobile);
        memberMapper.insert(member);
        return member.getId();
    }
}
