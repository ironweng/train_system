package com.zhaopei.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhaopei.train.common.resp.PageResp;
import com.zhaopei.train.common.util.SnowUtil;
import com.zhaopei.train.business.domain.DailyTrainCarriage;
import com.zhaopei.train.business.domain.DailyTrainCarriageExample;
import com.zhaopei.train.business.mapper.DailyTrainCarriageMapper;
import com.zhaopei.train.business.req.DailyTrainCarriageQueryReq;
import com.zhaopei.train.business.req.DailyTrainCarriageSaveReq;
import com.zhaopei.train.business.resp.DailyTrainCarriageQueryResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class DailyTrainCarriageService {

    @Autowired
    private DailyTrainCarriageMapper dailyTrainCarriageMapper;

    public void save(DailyTrainCarriageSaveReq req){
        DateTime now=DateTime.now();
        DailyTrainCarriage dailyTrainCarriage= BeanUtil.copyProperties(req,DailyTrainCarriage.class);
        // if中是新增保存
        if(ObjUtil.isNull(dailyTrainCarriage.getId())){
            //直接通过TreadLocal线程本地变量获取当前登录的会员id
            dailyTrainCarriage.setId(SnowUtil.getSnowflakeNextId());
            dailyTrainCarriage.setCreateTime(now);
            dailyTrainCarriage.setUpdateTime(now);
            dailyTrainCarriageMapper.insert(dailyTrainCarriage);
            //else是编辑保存
        }else {
            dailyTrainCarriage.setUpdateTime(now);
            dailyTrainCarriageMapper.updateByPrimaryKey(dailyTrainCarriage);
        }
    }

    public PageResp<DailyTrainCarriageQueryResp> queryList(DailyTrainCarriageQueryReq req){
        DailyTrainCarriageExample dailyTrainCarriageExample=new DailyTrainCarriageExample();
        dailyTrainCarriageExample.setOrderByClause("id desc");
        DailyTrainCarriageExample.Criteria criteria = dailyTrainCarriageExample.createCriteria();


        log.info("req查询页码:{}",req.getPage());
        log.info("req每页条数:{}",req.getSize());
        PageHelper.startPage(req.getPage(), req.getSize());
        List<DailyTrainCarriage> dailyTrainCarriageList = dailyTrainCarriageMapper.selectByExample(dailyTrainCarriageExample);

        //PageInfo的底层就是select ... count()... 即返回总条数
        PageInfo<DailyTrainCarriage> pageInfo=new PageInfo<>(dailyTrainCarriageList);
        log.info("resp总条数:{}",pageInfo.getTotal());
        log.info("总页数:{}",pageInfo.getPages());

        List<DailyTrainCarriageQueryResp> list = BeanUtil.copyToList(dailyTrainCarriageList, DailyTrainCarriageQueryResp.class);
        PageResp<DailyTrainCarriageQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    public void delete(Long id){
        dailyTrainCarriageMapper.deleteByPrimaryKey(id);
    }
}
