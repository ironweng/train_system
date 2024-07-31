package com.zhaopei.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhaopei.train.common.resp.PageResp;
import com.zhaopei.train.common.util.SnowUtil;
import com.zhaopei.train.business.domain.ConfirmOrder;
import com.zhaopei.train.business.domain.ConfirmOrderExample;
import com.zhaopei.train.business.mapper.ConfirmOrderMapper;
import com.zhaopei.train.business.req.ConfirmOrderQueryReq;
import com.zhaopei.train.business.req.ConfirmOrderSaveReq;
import com.zhaopei.train.business.resp.ConfirmOrderQueryResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class ConfirmOrderService {

    @Autowired
    private ConfirmOrderMapper confirmOrderMapper;

    public void save(ConfirmOrderSaveReq req){
        DateTime now=DateTime.now();
        ConfirmOrder confirmOrder= BeanUtil.copyProperties(req,ConfirmOrder.class);
        // if中是新增保存
        if(ObjUtil.isNull(confirmOrder.getId())){
            //直接通过TreadLocal线程本地变量获取当前登录的会员id
            confirmOrder.setId(SnowUtil.getSnowflakeNextId());
            confirmOrder.setCreateTime(now);
            confirmOrder.setUpdateTime(now);
            confirmOrderMapper.insert(confirmOrder);
            //else是编辑保存
        }else {
            confirmOrder.setUpdateTime(now);
            confirmOrderMapper.updateByPrimaryKey(confirmOrder);
        }
    }

    public PageResp<ConfirmOrderQueryResp> queryList(ConfirmOrderQueryReq req){
        ConfirmOrderExample confirmOrderExample=new ConfirmOrderExample();
        confirmOrderExample.setOrderByClause("id desc");
        ConfirmOrderExample.Criteria criteria = confirmOrderExample.createCriteria();


        log.info("req查询页码:{}",req.getPage());
        log.info("req每页条数:{}",req.getSize());
        PageHelper.startPage(req.getPage(), req.getSize());
        List<ConfirmOrder> confirmOrderList = confirmOrderMapper.selectByExample(confirmOrderExample);

        //PageInfo的底层就是select ... count()... 即返回总条数
        PageInfo<ConfirmOrder> pageInfo=new PageInfo<>(confirmOrderList);
        log.info("resp总条数:{}",pageInfo.getTotal());
        log.info("总页数:{}",pageInfo.getPages());

        List<ConfirmOrderQueryResp> list = BeanUtil.copyToList(confirmOrderList, ConfirmOrderQueryResp.class);
        PageResp<ConfirmOrderQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    public void delete(Long id){
        confirmOrderMapper.deleteByPrimaryKey(id);
    }
}
