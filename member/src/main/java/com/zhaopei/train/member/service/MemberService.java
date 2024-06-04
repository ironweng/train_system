package com.zhaopei.train.member.service;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.jwt.JWTUtil;
import com.zhaopei.train.common.exception.BusinessException;
import com.zhaopei.train.common.exception.BusinessExceptionEnum;
import com.zhaopei.train.common.util.SnowUtil;
import com.zhaopei.train.member.domain.Member;
import com.zhaopei.train.member.domain.MemberExample;
import com.zhaopei.train.member.mapper.MemberMapper;
import com.zhaopei.train.member.req.MemberLoginReq;
import com.zhaopei.train.member.req.MemberRegisterReq;
import com.zhaopei.train.member.req.MemberSendCodeReq;
import com.zhaopei.train.member.resp.MemberLoginResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MemberService {

    @Autowired
    private MemberMapper memberMapper;

    public int count(){
        return Math.toIntExact(memberMapper.countByExample(null));
    }

    public long register (MemberRegisterReq req){
        String mobile=req.getMobile();
        Member memberDB = selectByMobile(mobile);

        if(ObjUtil.isNotNull(memberDB)){
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_EXIST);
        }

        Member member =new Member();
        member.setId(SnowUtil.getSnowflakeNextId());
        member.setMobile(mobile);
        memberMapper.insert(member);
        return member.getId();
    }

    public void sendCode (MemberSendCodeReq req){
        String mobile=req.getMobile();
        Member memberDB = selectByMobile(mobile);

        //如果手机号不存在，则插入一条记录
        if(ObjUtil.isNull(memberDB)){
            log.info("手机号不存在,可以插入数据库");
            Member member =new Member();
            member.setId(SnowUtil.getSnowflakeNextId());
            member.setMobile(mobile);
            memberMapper.insert(member);
        }else {
            log.info("手机号存在,无需插入数据库");
        }
        //生成验证码
//       String code= RandomUtil.randomString(4);
        String code= "8888";
        log.info("生成短信验证码:{}",code);

        //保存短信记录表:手机号，短信验证码，有效期，是否已使用，业务类型，发送时间，使用时间
        //对接短信通道，发送短信
        //以上两步应当是真实项目中需要做的，这里该部分功能省略。
    }

    public MemberLoginResp login (MemberLoginReq req){
        String mobile=req.getMobile();
        String code=req.getCode();
        Member memberDB = selectByMobile(mobile);

        //如果手机号不存在，则插入一条记录
        if(ObjUtil.isNull(memberDB)){
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_NOT_EXIST);
        }

        //如果手机号已存在,则需要校验短信验证码
        if(!"8888".equals(code)){
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_CODE_ERROR);
        }

        MemberLoginResp memberLoginResp = BeanUtil.copyProperties(memberDB, MemberLoginResp.class);
        //下面的map和key都是根据JWTUtil.createToken方法中需要的参数而定的。
        Map<String, Object> map = BeanUtil.beanToMap(memberLoginResp);
        String key="zhaopei12306";
        String token=JWTUtil.createToken(map,key.getBytes());
        memberLoginResp.setToken(token);
        return memberLoginResp;
    }

    private Member selectByMobile(String mobile) {
        //MemberExample相当于是一个工具，它为每个Member类中的字段设置了很多条件查询的方法
        //例如下面的andMobileEqualTo方法就是为Mobile字段设置的一个条件查询方法
        MemberExample memberExample=new MemberExample();
        memberExample.createCriteria().andMobileEqualTo(mobile);
        List<Member> list=memberMapper.selectByExample(memberExample);
        //如果手机号不存在，则插入一条记录
        if(CollUtil.isEmpty(list)){
            return null;
        }else {
            return list.get(0);
        }
    }

}
