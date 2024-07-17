package com.zhaopei.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhaopei.train.business.domain.TrainCarriage;
import com.zhaopei.train.business.domain.TrainCarriageExample;
import com.zhaopei.train.business.enums.SeatColEnum;
import com.zhaopei.train.business.mapper.TrainCarriageMapper;
import com.zhaopei.train.business.req.TrainCarriageQueryReq;
import com.zhaopei.train.business.req.TrainCarriageSaveReq;
import com.zhaopei.train.business.resp.TrainCarriageQueryResp;
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
public class TrainCarriageService {

    @Autowired
    private TrainCarriageMapper trainCarriageMapper;

    public void save(TrainCarriageSaveReq req){
        DateTime now=DateTime.now();

        //自动计算出该车厢列数和总座位数
        List<SeatColEnum> seatColEnums=SeatColEnum.getColsByType(req.getSeatType());
        req.setColCount(seatColEnums.size());
        req.setSeatCount(req.getColCount()*req.getRowCount());

        TrainCarriage trainCarriage= BeanUtil.copyProperties(req,TrainCarriage.class);
        // if中是新增保存
        if(ObjUtil.isNull(trainCarriage.getId())){

            // 保存之前，先校验唯一键是否存在
            TrainCarriage trainCarriageDB = selectByUnique(req.getTrainCode(), req.getIndex());
            if (ObjectUtil.isNotEmpty(trainCarriageDB)) {
                throw new BusinessException(BusinessExceptionEnum.BUSINESS_TRAIN_CARRIAGE_INDEX_UNIQUE_ERROR);
            }

            //直接通过TreadLocal线程本地变量获取当前登录的会员id
            trainCarriage.setId(SnowUtil.getSnowflakeNextId());
            trainCarriage.setCreateTime(now);
            trainCarriage.setUpdateTime(now);
            trainCarriageMapper.insert(trainCarriage);
            //else是编辑保存
        }else {
            trainCarriage.setUpdateTime(now);
            trainCarriageMapper.updateByPrimaryKey(trainCarriage);
        }
    }

    private TrainCarriage selectByUnique(String trainCode,Integer index) {
        TrainCarriageExample trainCarriageExample=new TrainCarriageExample();
        trainCarriageExample.createCriteria()
                .andTrainCodeEqualTo(trainCode)
                .andIndexEqualTo(index);
        List<TrainCarriage> list = trainCarriageMapper.selectByExample(trainCarriageExample);
        if(CollUtil.isNotEmpty(list)){
            return list.get(0);
        } else {
            return null;
        }
    }

    public PageResp<TrainCarriageQueryResp> queryList(TrainCarriageQueryReq req){
        TrainCarriageExample trainCarriageExample=new TrainCarriageExample();
        trainCarriageExample.setOrderByClause("train_code asc, `index` asc");
        TrainCarriageExample.Criteria criteria = trainCarriageExample.createCriteria();

        //如果某个参数(TrainCode)有值，就按这个参数来查询
        if(ObjUtil.isNotEmpty(req.getTrainCode())){
            criteria.andTrainCodeEqualTo(req.getTrainCode());
        }

        log.info("req查询页码:{}",req.getPage());
        log.info("req每页条数:{}",req.getSize());
        PageHelper.startPage(req.getPage(), req.getSize());
        List<TrainCarriage> trainCarriageList = trainCarriageMapper.selectByExample(trainCarriageExample);

        //PageInfo的底层就是select ... count()... 即返回总条数
        PageInfo<TrainCarriage> pageInfo=new PageInfo<>(trainCarriageList);
        log.info("resp总条数:{}",pageInfo.getTotal());
        log.info("总页数:{}",pageInfo.getPages());

        List<TrainCarriageQueryResp> list = BeanUtil.copyToList(trainCarriageList, TrainCarriageQueryResp.class);
        PageResp<TrainCarriageQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    public void delete(Long id){
        trainCarriageMapper.deleteByPrimaryKey(id);
    }

    public List<TrainCarriage> selectByTrainCode(String trainCode){
        TrainCarriageExample trainCarriageExample=new TrainCarriageExample();
        trainCarriageExample.setOrderByClause("`index` asc");
        TrainCarriageExample.Criteria criteria = trainCarriageExample.createCriteria();
        criteria.andTrainCodeEqualTo(trainCode);
        return trainCarriageMapper.selectByExample(trainCarriageExample);
    }
}
