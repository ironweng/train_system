package com.zhaopei.train.member.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import com.zhaopei.train.common.util.SnowUtil;
import com.zhaopei.train.member.domain.Passenger;
import com.zhaopei.train.member.mapper.PassengerMapper;
import com.zhaopei.train.member.req.PassengerSaveReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PassengerService {

    @Autowired
    private PassengerMapper passengerMapper;

    public void save(PassengerSaveReq req){
        DateTime now=DateTime.now();
        Passenger passenger= BeanUtil.copyProperties(req,Passenger.class);
        passenger.setId(SnowUtil.getSnowflakeNextId());
        passenger.setCreateTime(now);
        passenger.setUpdateTime(now);
        passengerMapper.insert(passenger);
    }
}
