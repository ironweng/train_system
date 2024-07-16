package com.zhaopei.train.business.controller.admin;

import com.zhaopei.train.business.req.TrainSeatQueryReq;
import com.zhaopei.train.business.req.TrainSeatSaveReq;
import com.zhaopei.train.business.resp.trainSeatQueryResp;
import com.zhaopei.train.business.service.TrainSeatService;
import com.zhaopei.train.common.resp.CommonResp;
import com.zhaopei.train.common.resp.PageResp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/train-seat")
public class TrainSeatAdminController {

    @Autowired
    private TrainSeatService trainSeatService;
    
    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody TrainSeatSaveReq req){
        trainSeatService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<trainSeatQueryResp>> queryList(@Valid TrainSeatQueryReq req){
        PageResp<trainSeatQueryResp> list = trainSeatService.queryList(req);
        return new CommonResp<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id){
        trainSeatService.delete(id);
        return new CommonResp<>();
    }
}
