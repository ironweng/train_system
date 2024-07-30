package com.zhaopei.train.member.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhaopei.train.common.context.LoginMemberContext;
import com.zhaopei.train.common.resp.PageResp;
import com.zhaopei.train.common.util.SnowUtil;
import com.zhaopei.train.member.domain.Passenger;
import com.zhaopei.train.member.domain.PassengerExample;
import com.zhaopei.train.member.mapper.PassengerMapper;
import com.zhaopei.train.member.req.PassengerQueryReq;
import com.zhaopei.train.member.req.PassengerSaveReq;
import com.zhaopei.train.member.resp.PassengerQueryResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class PassengerService {

    @Autowired
    private PassengerMapper passengerMapper;

    public void save(PassengerSaveReq req){
        DateTime now=DateTime.now();
        Passenger passenger= BeanUtil.copyProperties(req,Passenger.class);
        // if中是新增保存
        if(ObjUtil.isNull(passenger.getId())){
            //直接通过TreadLocal线程本地变量获取当前登录的会员id
            passenger.setMemberId(LoginMemberContext.getId());
            passenger.setId(SnowUtil.getSnowflakeNextId());
            passenger.setCreateTime(now);
            passenger.setUpdateTime(now);
            passengerMapper.insert(passenger);
            //else是编辑保存
        }else {
            passenger.setUpdateTime(now);
            passengerMapper.updateByPrimaryKey(passenger);
        }
    }

    public PageResp<PassengerQueryResp> queryList(PassengerQueryReq req){
        PassengerExample passengerExample=new PassengerExample();
        passengerExample.setOrderByClause("id desc");
        PassengerExample.Criteria criteria = passengerExample.createCriteria();
        //如果某个参数(MemberId)有值，就按这个参数来查询
        if(ObjUtil.isNotNull(req.getMemberId())){
            criteria.andMemberIdEqualTo(req.getMemberId());
        }

        log.info("req查询页码:{}",req.getPage());
        log.info("req每页条数:{}",req.getSize());
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Passenger> passengerList = passengerMapper.selectByExample(passengerExample);

        //PageInfo的底层就是select ... count()... 即返回总条数
        PageInfo<Passenger> pageInfo=new PageInfo<>(passengerList);
        log.info("resp总条数:{}",pageInfo.getTotal());
        log.info("总页数:{}",pageInfo.getPages());

        List<PassengerQueryResp> list = BeanUtil.copyToList(passengerList, PassengerQueryResp.class);
        PageResp<PassengerQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    public void delete(Long id){
        passengerMapper.deleteByPrimaryKey(id);
    }

    /**
     * 查询我的所有乘客
     */
    public List<PassengerQueryResp> queryMine() {
        PassengerExample passengerExample = new PassengerExample();
        passengerExample.setOrderByClause("name asc");
        PassengerExample.Criteria criteria = passengerExample.createCriteria();
        criteria.andMemberIdEqualTo(LoginMemberContext.getId());
        List<Passenger> list = passengerMapper.selectByExample(passengerExample);
        return BeanUtil.copyToList(list, PassengerQueryResp.class);
    }
}
