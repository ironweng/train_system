package com.zhaopei.train.business.controller.admin;

import com.zhaopei.train.common.context.LoginMemberContext;
import com.zhaopei.train.common.resp.CommonResp;
import com.zhaopei.train.common.resp.PageResp;
import com.zhaopei.train.business.req.trainSeatQueryReq;
import com.zhaopei.train.business.req.trainSeatSaveReq;
import com.zhaopei.train.business.resp.trainSeatQueryResp;
import com.zhaopei.train.business.service.trainSeatService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/train-seat")
public class trainSeatAdminController {

    @Autowired
    private trainSeatService trainSeatService;
    
    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody trainSeatSaveReq req){
        trainSeatService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<trainSeatQueryResp>> queryList(@Valid trainSeatQueryReq req){
        PageResp<trainSeatQueryResp> list = trainSeatService.queryList(req);
        return new CommonResp<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id){
        trainSeatService.delete(id);
        return new CommonResp<>();
    }
}
