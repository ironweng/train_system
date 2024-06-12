package com.zhaopei.train.member.controller;

import com.zhaopei.train.common.context.LoginMemberContext;
import com.zhaopei.train.common.resp.CommonResp;
import com.zhaopei.train.common.resp.PageResp;
import com.zhaopei.train.member.req.${Domain}QueryReq;
import com.zhaopei.train.member.req.${Domain}SaveReq;
import com.zhaopei.train.member.resp.${Domain}QueryResp;
import com.zhaopei.train.member.service.${Domain}Service;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/${domain}")
public class ${Domain}Controller {

    @Autowired
    private ${Domain}Service ${domain}Service;
    
    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody ${Domain}SaveReq req){
        ${domain}Service.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<${Domain}QueryResp>> queryList(@Valid ${Domain}QueryReq req){
        req.setMemberId(LoginMemberContext.getId());
        PageResp<${Domain}QueryResp> list = ${domain}Service.queryList(req);
        return new CommonResp<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id){
        ${domain}Service.delete(id);
        return new CommonResp<>();
    }
}
