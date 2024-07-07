package com.zhaopei.train.${module}.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhaopei.train.common.resp.PageResp;
import com.zhaopei.train.common.util.SnowUtil;
import com.zhaopei.train.${module}.domain.${Domain};
import com.zhaopei.train.${module}.domain.${Domain}Example;
import com.zhaopei.train.${module}.mapper.${Domain}Mapper;
import com.zhaopei.train.${module}.req.${Domain}QueryReq;
import com.zhaopei.train.${module}.req.${Domain}SaveReq;
import com.zhaopei.train.${module}.resp.${Domain}QueryResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class ${Domain}Service {

    @Autowired
    private ${Domain}Mapper ${domain}Mapper;

    public void save(${Domain}SaveReq req){
        DateTime now=DateTime.now();
        ${Domain} ${domain}= BeanUtil.copyProperties(req,${Domain}.class);
        // if中是新增保存
        if(ObjUtil.isNull(${domain}.getId())){
            //直接通过TreadLocal线程本地变量获取当前登录的会员id
            ${domain}.setId(SnowUtil.getSnowflakeNextId());
            ${domain}.setCreateTime(now);
            ${domain}.setUpdateTime(now);
            ${domain}Mapper.insert(${domain});
            //else是编辑保存
        }else {
            ${domain}.setUpdateTime(now);
            ${domain}Mapper.updateByPrimaryKey(${domain});
        }
    }

    public PageResp<${Domain}QueryResp> queryList(${Domain}QueryReq req){
        ${Domain}Example ${domain}Example=new ${Domain}Example();
        ${domain}Example.setOrderByClause("id desc");
        ${Domain}Example.Criteria criteria = ${domain}Example.createCriteria();


        log.info("req查询页码:{}",req.getPage());
        log.info("req每页条数:{}",req.getSize());
        PageHelper.startPage(req.getPage(), req.getSize());
        List<${Domain}> ${domain}List = ${domain}Mapper.selectByExample(${domain}Example);

        //PageInfo的底层就是select ... count()... 即返回总条数
        PageInfo<${Domain}> pageInfo=new PageInfo<>(${domain}List);
        log.info("resp总条数:{}",pageInfo.getTotal());
        log.info("总页数:{}",pageInfo.getPages());

        List<${Domain}QueryResp> list = BeanUtil.copyToList(${domain}List, ${Domain}QueryResp.class);
        PageResp<${Domain}QueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    public void delete(Long id){
        ${domain}Mapper.deleteByPrimaryKey(id);
    }
}
