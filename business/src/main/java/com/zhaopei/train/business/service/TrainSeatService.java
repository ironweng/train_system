package com.zhaopei.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhaopei.train.business.domain.TrainCarriage;
import com.zhaopei.train.business.domain.TrainSeat;
import com.zhaopei.train.business.domain.TrainSeatExample;
import com.zhaopei.train.business.enums.SeatColEnum;
import com.zhaopei.train.business.mapper.TrainSeatMapper;
import com.zhaopei.train.business.req.TrainSeatQueryReq;
import com.zhaopei.train.business.req.TrainSeatSaveReq;
import com.zhaopei.train.business.resp.TrainSeatQueryResp;
import com.zhaopei.train.common.resp.PageResp;
import com.zhaopei.train.common.util.SnowUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class TrainSeatService {

    @Autowired
    private TrainSeatMapper trainSeatMapper;

    @Resource
    private TrainCarriageService trainCarriageService;

    public void save(TrainSeatSaveReq req) {
        DateTime now = DateTime.now();
        TrainSeat trainSeat = BeanUtil.copyProperties(req, TrainSeat.class);
        // if中是新增保存
        if (ObjUtil.isNull(trainSeat.getId())) {
            //直接通过TreadLocal线程本地变量获取当前登录的会员id
            trainSeat.setId(SnowUtil.getSnowflakeNextId());
            trainSeat.setCreateTime(now);
            trainSeat.setUpdateTime(now);
            trainSeatMapper.insert(trainSeat);
            //else是编辑保存
        } else {
            trainSeat.setUpdateTime(now);
            trainSeatMapper.updateByPrimaryKey(trainSeat);
        }
    }

    public PageResp<TrainSeatQueryResp> queryList(TrainSeatQueryReq req) {
        TrainSeatExample trainSeatExample = new TrainSeatExample();
        trainSeatExample.setOrderByClause("train_code asc,carriage_index asc,carriage_seat_index asc");
        TrainSeatExample.Criteria criteria = trainSeatExample.createCriteria();

        //如果某个参数(TrainCode)有值，就按这个参数来查询
        if (ObjUtil.isNotEmpty(req.getTrainCode())) {
            criteria.andTrainCodeEqualTo(req.getTrainCode());
        }

        log.info("req查询页码:{}", req.getPage());
        log.info("req每页条数:{}", req.getSize());
        PageHelper.startPage(req.getPage(), req.getSize());
        List<TrainSeat> trainSeatList = trainSeatMapper.selectByExample(trainSeatExample);

        //PageInfo的底层就是select ... count()... 即返回总条数
        PageInfo<TrainSeat> pageInfo = new PageInfo<>(trainSeatList);
        log.info("resp总条数:{}", pageInfo.getTotal());
        log.info("总页数:{}", pageInfo.getPages());

        List<TrainSeatQueryResp> list = BeanUtil.copyToList(trainSeatList, TrainSeatQueryResp.class);
        PageResp<TrainSeatQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    public void delete(Long id) {
        trainSeatMapper.deleteByPrimaryKey(id);
    }

    @Transactional
    public void genTrainSeat(String trainCode) {
        DateTime now=DateTime.now();
        //先清空当前车次下的所有座位记录
        TrainSeatExample trainSeatExample = new TrainSeatExample();
        TrainSeatExample.Criteria criteria = trainSeatExample.createCriteria();
        criteria.andTrainCodeEqualTo(trainCode);
        trainSeatMapper.deleteByExample(trainSeatExample);
        //查找当前车次下的所有车厢
        List<TrainCarriage> carriageList = trainCarriageService.selectByTrainCode(trainCode);
        log.info("当前车次下的车厢数:{}",carriageList.size());
        //循环生成每个车厢的座位
        for (TrainCarriage trainCarriage : carriageList) {
            //拿到车厢数据:行数,座位类型(得到列数)
            Integer rowCount = trainCarriage.getRowCount();
            String seatType = trainCarriage.getSeatType();
            int seatIndex=1;
            //根据车厢的座位类型,筛选出所有的列,比如车厢类型是一等座,则筛选columnList={ACDF}
            List<SeatColEnum> colEnumList = SeatColEnum.getColsByType(seatType);
            log.info("根据车厢的座位类型,筛选出所有的列:{}",colEnumList);
            //循环行数,注意行数是从1开始
            for (int row = 1; row <= rowCount; row++) {
                //循环列数
                for(SeatColEnum seatColEnum:colEnumList){
                    //构造座位数据并保存数据库
                    TrainSeat trainSeat = new TrainSeat();
                    trainSeat.setId(SnowUtil.getSnowflakeNextId());
                    trainSeat.setTrainCode(trainCode);
                    trainSeat.setCarriageIndex(trainCarriage.getIndex());
                    trainSeat.setRow(StrUtil.fillBefore(String.valueOf(row), '0', 2));
                    trainSeat.setCol(seatColEnum.getCode());
                    trainSeat.setSeatType(seatType);
                    trainSeat.setCarriageSeatIndex(seatIndex++);
                    trainSeat.setCreateTime(now);
                    trainSeat.setUpdateTime(now);
                    trainSeatMapper.insert(trainSeat);
                }
            }
        }
    }

    //service之间的调用可以另外写一个方法,就不需要copy变成resp形式的数据,直接返回数据库的实体。
    public List<TrainSeat> selectByTrainCode(String trainCode) {
        TrainSeatExample trainSeatExample = new TrainSeatExample();
        trainSeatExample.setOrderByClause("`id` asc");
        trainSeatExample.createCriteria().andTrainCodeEqualTo(trainCode);
        return trainSeatMapper.selectByExample(trainSeatExample);
    }
}
