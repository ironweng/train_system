package com.zhaopei.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhaopei.train.common.resp.PageResp;
import com.zhaopei.train.common.util.SnowUtil;
import com.zhaopei.train.business.domain.trainSeat;
import com.zhaopei.train.business.domain.trainSeatExample;
import com.zhaopei.train.business.mapper.TrainSeatMapper;
import com.zhaopei.train.business.req.TrainSeatQueryReq;
import com.zhaopei.train.business.req.TrainSeatSaveReq;
import com.zhaopei.train.business.resp.trainSeatQueryResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class TrainSeatService {

    @Autowired
    private TrainSeatMapper trainSeatMapper;

    public void save(TrainSeatSaveReq req){
        DateTime now=DateTime.now();
        trainSeat trainSeat= BeanUtil.copyProperties(req,trainSeat.class);
        // if中是新增保存
        if(ObjUtil.isNull(trainSeat.getId())){
            //直接通过TreadLocal线程本地变量获取当前登录的会员id
            trainSeat.setId(SnowUtil.getSnowflakeNextId());
            trainSeat.setCreateTime(now);
            trainSeat.setUpdateTime(now);
            trainSeatMapper.insert(trainSeat);
            //else是编辑保存
        }else {
            trainSeat.setUpdateTime(now);
            trainSeatMapper.updateByPrimaryKey(trainSeat);
        }
    }

    public PageResp<trainSeatQueryResp> queryList(TrainSeatQueryReq req){
        trainSeatExample trainSeatExample=new trainSeatExample();
        trainSeatExample.setOrderByClause("train_code asc,carriage_index asc,carriage_seat_index asc");
        trainSeatExample.Criteria criteria = trainSeatExample.createCriteria();

        //如果某个参数(TrainCode)有值，就按这个参数来查询
        if(ObjUtil.isNotEmpty(req.getTrainCode())){
            criteria.andTrainCodeEqualTo(req.getTrainCode());
        }

        log.info("req查询页码:{}",req.getPage());
        log.info("req每页条数:{}",req.getSize());
        PageHelper.startPage(req.getPage(), req.getSize());
        List<trainSeat> trainSeatList = trainSeatMapper.selectByExample(trainSeatExample);

        //PageInfo的底层就是select ... count()... 即返回总条数
        PageInfo<trainSeat> pageInfo=new PageInfo<>(trainSeatList);
        log.info("resp总条数:{}",pageInfo.getTotal());
        log.info("总页数:{}",pageInfo.getPages());

        List<trainSeatQueryResp> list = BeanUtil.copyToList(trainSeatList, trainSeatQueryResp.class);
        PageResp<trainSeatQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    public void delete(Long id){
        trainSeatMapper.deleteByPrimaryKey(id);
    }
}
