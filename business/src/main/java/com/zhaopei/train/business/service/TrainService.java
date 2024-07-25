package com.zhaopei.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhaopei.train.business.domain.Train;
import com.zhaopei.train.business.domain.TrainExample;
import com.zhaopei.train.business.mapper.TrainMapper;
import com.zhaopei.train.business.req.TrainQueryReq;
import com.zhaopei.train.business.req.TrainSaveReq;
import com.zhaopei.train.business.resp.TrainQueryResp;
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
public class TrainService {

    @Autowired
    private TrainMapper trainMapper;

    public void save(TrainSaveReq req){
        DateTime now=DateTime.now();
        Train train= BeanUtil.copyProperties(req,Train.class);
        // if中是新增保存
        if(ObjUtil.isNull(train.getId())){

            // 保存之前，先校验唯一键是否存在
            Train trainDB = selectByUnique(req.getCode());
            if (ObjectUtil.isNotEmpty(trainDB)) {
                throw new BusinessException(BusinessExceptionEnum.BUSINESS_TRAIN_CODE_UNIQUE_ERROR);
            }

            //直接通过TreadLocal线程本地变量获取当前登录的会员id
            train.setId(SnowUtil.getSnowflakeNextId());
            train.setCreateTime(now);
            train.setUpdateTime(now);
            trainMapper.insert(train);
            //else是编辑保存
        }else {
            train.setUpdateTime(now);
            trainMapper.updateByPrimaryKey(train);
        }
    }

    private Train selectByUnique(String code) {
        TrainExample trainExample = new TrainExample();
        trainExample.createCriteria()
                .andCodeEqualTo(code);
        List<Train> list = trainMapper.selectByExample(trainExample);
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public PageResp<TrainQueryResp> queryList(TrainQueryReq req){
        TrainExample trainExample=new TrainExample();
        trainExample.setOrderByClause("code asc");
        TrainExample.Criteria criteria = trainExample.createCriteria();


        log.info("req查询页码:{}",req.getPage());
        log.info("req每页条数:{}",req.getSize());
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Train> trainList = trainMapper.selectByExample(trainExample);

        //PageInfo的底层就是select ... count()... 即返回总条数
        PageInfo<Train> pageInfo=new PageInfo<>(trainList);
        log.info("resp总条数:{}",pageInfo.getTotal());
        log.info("总页数:{}",pageInfo.getPages());

        List<TrainQueryResp> list = BeanUtil.copyToList(trainList, TrainQueryResp.class);
        PageResp<TrainQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    public void delete(Long id){
        trainMapper.deleteByPrimaryKey(id);
    }

    //查询所有车次信息,可用于做车次编号的下拉框
    public List<TrainQueryResp> queryAll(){
        TrainExample trainExample=new TrainExample();
        trainExample.setOrderByClause("code asc");
        List<Train> trainList = trainMapper.selectByExample(trainExample);
        return BeanUtil.copyToList(trainList, TrainQueryResp.class);
    }

    //service之间的调用可以另外写一个方法,就不需要copy变成resp形式的数据,直接返回数据库的实体。
    public List<Train> selectAll() {
        TrainExample trainExample = new TrainExample();
        trainExample.setOrderByClause("code asc");
        return trainMapper.selectByExample(trainExample);
    }
}
