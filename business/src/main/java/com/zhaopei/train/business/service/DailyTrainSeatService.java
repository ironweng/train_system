package com.zhaopei.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhaopei.train.common.resp.PageResp;
import com.zhaopei.train.common.util.SnowUtil;
import com.zhaopei.train.business.domain.DailyTrainSeat;
import com.zhaopei.train.business.domain.DailyTrainSeatExample;
import com.zhaopei.train.business.mapper.DailyTrainSeatMapper;
import com.zhaopei.train.business.req.DailyTrainSeatQueryReq;
import com.zhaopei.train.business.req.DailyTrainSeatSaveReq;
import com.zhaopei.train.business.resp.DailyTrainSeatQueryResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class DailyTrainSeatService {

    @Autowired
    private DailyTrainSeatMapper dailyTrainSeatMapper;

    public void save(DailyTrainSeatSaveReq req){
        DateTime now=DateTime.now();
        DailyTrainSeat dailyTrainSeat= BeanUtil.copyProperties(req,DailyTrainSeat.class);
        // if中是新增保存
        if(ObjUtil.isNull(dailyTrainSeat.getId())){
            //直接通过TreadLocal线程本地变量获取当前登录的会员id
            dailyTrainSeat.setId(SnowUtil.getSnowflakeNextId());
            dailyTrainSeat.setCreateTime(now);
            dailyTrainSeat.setUpdateTime(now);
            dailyTrainSeatMapper.insert(dailyTrainSeat);
            //else是编辑保存
        }else {
            dailyTrainSeat.setUpdateTime(now);
            dailyTrainSeatMapper.updateByPrimaryKey(dailyTrainSeat);
        }
    }

    public PageResp<DailyTrainSeatQueryResp> queryList(DailyTrainSeatQueryReq req){
        DailyTrainSeatExample dailyTrainSeatExample=new DailyTrainSeatExample();
        dailyTrainSeatExample.setOrderByClause("train_code asc,carriage_index asc,carriage_seat_index asc");
        DailyTrainSeatExample.Criteria criteria = dailyTrainSeatExample.createCriteria();

        if (ObjUtil.isNotEmpty(req.getTrainCode())) {
            criteria.andTrainCodeEqualTo(req.getTrainCode());
        }

        log.info("req查询页码:{}",req.getPage());
        log.info("req每页条数:{}",req.getSize());
        PageHelper.startPage(req.getPage(), req.getSize());
        List<DailyTrainSeat> dailyTrainSeatList = dailyTrainSeatMapper.selectByExample(dailyTrainSeatExample);

        //PageInfo的底层就是select ... count()... 即返回总条数
        PageInfo<DailyTrainSeat> pageInfo=new PageInfo<>(dailyTrainSeatList);
        log.info("resp总条数:{}",pageInfo.getTotal());
        log.info("总页数:{}",pageInfo.getPages());

        List<DailyTrainSeatQueryResp> list = BeanUtil.copyToList(dailyTrainSeatList, DailyTrainSeatQueryResp.class);
        PageResp<DailyTrainSeatQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    public void delete(Long id){
        dailyTrainSeatMapper.deleteByPrimaryKey(id);
    }
}
