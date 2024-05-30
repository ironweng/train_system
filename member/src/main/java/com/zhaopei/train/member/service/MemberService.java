package com.zhaopei.train.member.service;


import com.zhaopei.train.member.mapper.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    @Autowired
    private MemberMapper memberMapper;

    public int count(){
        return memberMapper.count();
    }
}
