package com.zhaopei.train.business.controller.admin;

import com.zhaopei.train.common.context.LoginMemberContext;
import com.zhaopei.train.common.resp.CommonResp;
import com.zhaopei.train.common.resp.PageResp;
import com.zhaopei.train.business.req.TrainCarriageQueryReq;
import com.zhaopei.train.business.req.TrainCarriageSaveReq;
import com.zhaopei.train.business.resp.TrainCarriageQueryResp;
import com.zhaopei.train.business.service.TrainCarriageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/train-carriage")
public class TrainCarriageAdminController {

    @Autowired
    private TrainCarriageService trainCarriageService;
    
    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody TrainCarriageSaveReq req){
        trainCarriageService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<TrainCarriageQueryResp>> queryList(@Valid TrainCarriageQueryReq req){
        PageResp<TrainCarriageQueryResp> list = trainCarriageService.queryList(req);
        return new CommonResp<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id){
        trainCarriageService.delete(id);
        return new CommonResp<>();
    }
}
