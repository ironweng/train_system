package com.zhaopei.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhaopei.train.common.resp.PageResp;
import com.zhaopei.train.common.util.SnowUtil;
import com.zhaopei.train.business.domain.DailyTrain;
import com.zhaopei.train.business.domain.DailyTrainExample;
import com.zhaopei.train.business.mapper.DailyTrainMapper;
import com.zhaopei.train.business.req.DailyTrainQueryReq;
import com.zhaopei.train.business.req.DailyTrainSaveReq;
import com.zhaopei.train.business.resp.DailyTrainQueryResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class DailyTrainService {

    @Autowired
    private DailyTrainMapper dailyTrainMapper;

    public void save(DailyTrainSaveReq req){
        DateTime now=DateTime.now();
        DailyTrain dailyTrain= BeanUtil.copyProperties(req,DailyTrain.class);
        // if中是新增保存
        if(ObjUtil.isNull(dailyTrain.getId())){
            //直接通过TreadLocal线程本地变量获取当前登录的会员id
            dailyTrain.setId(SnowUtil.getSnowflakeNextId());
            dailyTrain.setCreateTime(now);
            dailyTrain.setUpdateTime(now);
            dailyTrainMapper.insert(dailyTrain);
            //else是编辑保存
        }else {
            dailyTrain.setUpdateTime(now);
            dailyTrainMapper.updateByPrimaryKey(dailyTrain);
        }
    }

    public PageResp<DailyTrainQueryResp> queryList(DailyTrainQueryReq req){
        DailyTrainExample dailyTrainExample=new DailyTrainExample();
        dailyTrainExample.setOrderByClause("id desc");
        DailyTrainExample.Criteria criteria = dailyTrainExample.createCriteria();


        log.info("req查询页码:{}",req.getPage());
        log.info("req每页条数:{}",req.getSize());
        PageHelper.startPage(req.getPage(), req.getSize());
        List<DailyTrain> dailyTrainList = dailyTrainMapper.selectByExample(dailyTrainExample);

        //PageInfo的底层就是select ... count()... 即返回总条数
        PageInfo<DailyTrain> pageInfo=new PageInfo<>(dailyTrainList);
        log.info("resp总条数:{}",pageInfo.getTotal());
        log.info("总页数:{}",pageInfo.getPages());

        List<DailyTrainQueryResp> list = BeanUtil.copyToList(dailyTrainList, DailyTrainQueryResp.class);
        PageResp<DailyTrainQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    public void delete(Long id){
        dailyTrainMapper.deleteByPrimaryKey(id);
    }
}
