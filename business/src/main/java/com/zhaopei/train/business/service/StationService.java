package com.zhaopei.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhaopei.train.business.domain.Station;
import com.zhaopei.train.business.domain.StationExample;
import com.zhaopei.train.business.mapper.StationMapper;
import com.zhaopei.train.business.req.StationQueryReq;
import com.zhaopei.train.business.req.StationSaveReq;
import com.zhaopei.train.business.resp.StationQueryResp;
import com.zhaopei.train.common.resp.PageResp;
import com.zhaopei.train.common.util.SnowUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class StationService {

    @Autowired
    private StationMapper stationMapper;

    public void save(StationSaveReq req){
        DateTime now=DateTime.now();
        Station station= BeanUtil.copyProperties(req,Station.class);
        // if中是新增保存
        if(ObjUtil.isNull(station.getId())){
            //直接通过TreadLocal线程本地变量获取当前登录的会员id
            station.setId(SnowUtil.getSnowflakeNextId());
            station.setCreateTime(now);
            station.setUpdateTime(now);
            stationMapper.insert(station);
            //else是编辑保存
        }else {
            station.setUpdateTime(now);
            stationMapper.updateByPrimaryKey(station);
        }
    }

    public PageResp<StationQueryResp> queryList(StationQueryReq req){
        StationExample stationExample=new StationExample();
        stationExample.setOrderByClause("id desc");
        StationExample.Criteria criteria = stationExample.createCriteria();


        log.info("req查询页码:{}",req.getPage());
        log.info("req每页条数:{}",req.getSize());
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Station> stationList = stationMapper.selectByExample(stationExample);

        //PageInfo的底层就是select ... count()... 即返回总条数
        PageInfo<Station> pageInfo=new PageInfo<>(stationList);
        log.info("resp总条数:{}",pageInfo.getTotal());
        log.info("总页数:{}",pageInfo.getPages());

        List<StationQueryResp> list = BeanUtil.copyToList(stationList, StationQueryResp.class);
        PageResp<StationQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    public void delete(Long id){
        stationMapper.deleteByPrimaryKey(id);
    }

    //查询所有车站信息,可用于做车站选项的下拉框
    public List<StationQueryResp> queryAll(){
        StationExample stationExample=new StationExample();
        stationExample.setOrderByClause("name_pinyin asc");
        List<Station> stationList = stationMapper.selectByExample(stationExample);
        return BeanUtil.copyToList(stationList, StationQueryResp.class);
    }
}
