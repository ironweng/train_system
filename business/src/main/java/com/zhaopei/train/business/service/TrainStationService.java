package com.zhaopei.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhaopei.train.business.domain.TrainStation;
import com.zhaopei.train.business.domain.TrainStationExample;
import com.zhaopei.train.business.mapper.TrainStationMapper;
import com.zhaopei.train.business.req.TrainStationQueryReq;
import com.zhaopei.train.business.req.TrainStationSaveReq;
import com.zhaopei.train.business.resp.TrainStationQueryResp;
import com.zhaopei.train.common.exception.BusinessException;
import com.zhaopei.train.common.exception.BusinessExceptionEnum;
import com.zhaopei.train.common.resp.PageResp;
import com.zhaopei.train.common.util.SnowUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TrainStationService {

    @Autowired
    private TrainStationMapper trainStationMapper;

    public void save(TrainStationSaveReq req){
        DateTime now=DateTime.now();
        TrainStation trainStation= BeanUtil.copyProperties(req,TrainStation.class);
        // if中是新增保存
        if(ObjUtil.isNull(trainStation.getId())){

            // 保存之前，先校验唯一键是否存在
            TrainStation trainStationDB = selectByUnique(req.getTrainCode(), req.getIndex());
            if (ObjectUtil.isNotEmpty(trainStationDB)) {
                throw new BusinessException(BusinessExceptionEnum.BUSINESS_TRAIN_STATION_INDEX_UNIQUE_ERROR);
            }
            // 保存之前，先校验唯一键是否存在
            trainStationDB = selectByUnique(req.getTrainCode(), req.getName());
            if (ObjectUtil.isNotEmpty(trainStationDB)) {
                throw new BusinessException(BusinessExceptionEnum.BUSINESS_TRAIN_STATION_NAME_UNIQUE_ERROR);
            }

            //直接通过TreadLocal线程本地变量获取当前登录的会员id
            trainStation.setId(SnowUtil.getSnowflakeNextId());
            trainStation.setCreateTime(now);
            trainStation.setUpdateTime(now);
            trainStationMapper.insert(trainStation);
            //else是编辑保存
        }else {
            trainStation.setUpdateTime(now);
            trainStationMapper.updateByPrimaryKey(trainStation);
        }
    }

    private TrainStation selectByUnique(String trainCode, Integer index) {
        TrainStationExample trainStationExample = new TrainStationExample();
        trainStationExample.createCriteria()
                .andTrainCodeEqualTo(trainCode)
                .andIndexEqualTo(index);
        List<TrainStation> list = trainStationMapper.selectByExample(trainStationExample);
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }

    private TrainStation selectByUnique(String trainCode, String name) {
        TrainStationExample trainStationExample = new TrainStationExample();
        trainStationExample.createCriteria()
                .andTrainCodeEqualTo(trainCode)
                .andNameEqualTo(name);
        List<TrainStation> list = trainStationMapper.selectByExample(trainStationExample);
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public PageResp<TrainStationQueryResp> queryList(TrainStationQueryReq req){
        TrainStationExample trainStationExample=new TrainStationExample();
        trainStationExample.setOrderByClause("train_code asc, `index` asc");
        TrainStationExample.Criteria criteria = trainStationExample.createCriteria();
        //如果某个参数(TrainCode)有值，就按这个参数来查询
        if(ObjUtil.isNotEmpty(req.getTrainCode())){
            criteria.andTrainCodeEqualTo(req.getTrainCode());
        }

        log.info("req查询页码:{}",req.getPage());
        log.info("req每页条数:{}",req.getSize());
        PageHelper.startPage(req.getPage(), req.getSize());
        List<TrainStation> trainStationList = trainStationMapper.selectByExample(trainStationExample);

        //PageInfo的底层就是select ... count()... 即返回总条数
        PageInfo<TrainStation> pageInfo=new PageInfo<>(trainStationList);
        log.info("resp总条数:{}",pageInfo.getTotal());
        log.info("总页数:{}",pageInfo.getPages());

        List<TrainStationQueryResp> list = BeanUtil.copyToList(trainStationList, TrainStationQueryResp.class);
        PageResp<TrainStationQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    public void delete(Long id){
        trainStationMapper.deleteByPrimaryKey(id);
    }

    //service之间的调用可以另外写一个方法,就不需要copy变成resp形式的数据,直接返回数据库的实体。
    public List<TrainStation> selectByTrainCode(String trainCode) {
        TrainStationExample trainStationExample = new TrainStationExample();
        trainStationExample.setOrderByClause("`index` asc");
        trainStationExample.createCriteria().andTrainCodeEqualTo(trainCode);
        return trainStationMapper.selectByExample(trainStationExample);
    }
}
