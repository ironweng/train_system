package com.zhaopei.train.member.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjUtil;
import com.github.pagehelper.PageHelper;
import com.zhaopei.train.common.context.LoginMemberContext;
import com.zhaopei.train.common.util.SnowUtil;
import com.zhaopei.train.member.domain.Passenger;
import com.zhaopei.train.member.domain.PassengerExample;
import com.zhaopei.train.member.mapper.PassengerMapper;
import com.zhaopei.train.member.req.PassengerQueryReq;
import com.zhaopei.train.member.req.PassengerSaveReq;
import com.zhaopei.train.member.resp.PassengerQueryResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PassengerService {

    @Autowired
    private PassengerMapper passengerMapper;

    public void save(PassengerSaveReq req){
        DateTime now=DateTime.now();
        Passenger passenger= BeanUtil.copyProperties(req,Passenger.class);
        //直接通过TreadLocal线程本地变量获取当前登录的会员id
        passenger.setMemberId(LoginMemberContext.getId());
        passenger.setId(SnowUtil.getSnowflakeNextId());
        passenger.setCreateTime(now);
        passenger.setUpdateTime(now);
        passengerMapper.insert(passenger);
    }

    public List<PassengerQueryResp> queryList(PassengerQueryReq req){
        PassengerExample passengerExample=new PassengerExample();
        PassengerExample.Criteria criteria = passengerExample.createCriteria();
        if(ObjUtil.isNotNull(req.getMemberId())){
            criteria.andMemberIdEqualTo(req.getMemberId());
        }
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Passenger> passengerList = passengerMapper.selectByExample(passengerExample);
        return BeanUtil.copyToList(passengerList, PassengerQueryResp.class);
    }
}
