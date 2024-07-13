package com.zhaopei.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhaopei.train.common.resp.PageResp;
import com.zhaopei.train.common.util.SnowUtil;
import com.zhaopei.train.business.domain.Train;
import com.zhaopei.train.business.domain.TrainExample;
import com.zhaopei.train.business.mapper.TrainMapper;
import com.zhaopei.train.business.req.TrainQueryReq;
import com.zhaopei.train.business.req.TrainSaveReq;
import com.zhaopei.train.business.resp.TrainQueryResp;
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

    public PageResp<TrainQueryResp> queryList(TrainQueryReq req){
        TrainExample trainExample=new TrainExample();
        trainExample.setOrderByClause("id desc");
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
}
