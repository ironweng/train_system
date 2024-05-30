package com.zhaopei.train.member.service;


import cn.hutool.core.collection.CollUtil;
import com.zhaopei.train.member.domain.Member;
import com.zhaopei.train.member.domain.MemberExample;
import com.zhaopei.train.member.mapper.MemberMapper;
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

    public long register (String mobile){
        //MemberExample相当于是一个工具，它为每个Member类中的字段设置了很多条件查询的方法
        //例如下面的andMobileEqualTo方法就是为Mobile字段设置的一个条件查询方法
        MemberExample memberExample=new MemberExample();
        memberExample.createCriteria().andMobileEqualTo(mobile);
        List<Member> list=memberMapper.selectByExample(memberExample);

        if(CollUtil.isNotEmpty(list)){
            throw new RuntimeException("手机号已注册");
        }

        Member member =new Member();
        member.setId(System.currentTimeMillis());
        member.setMobile(mobile);
        memberMapper.insert(member);
        return member.getId();
    }
}
