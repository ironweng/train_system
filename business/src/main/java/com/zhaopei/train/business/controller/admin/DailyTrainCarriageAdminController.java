package com.zhaopei.train.business.controller.admin;

import com.zhaopei.train.common.context.LoginMemberContext;
import com.zhaopei.train.common.resp.CommonResp;
import com.zhaopei.train.common.resp.PageResp;
import com.zhaopei.train.business.req.DailyTrainCarriageQueryReq;
import com.zhaopei.train.business.req.DailyTrainCarriageSaveReq;
import com.zhaopei.train.business.resp.DailyTrainCarriageQueryResp;
import com.zhaopei.train.business.service.DailyTrainCarriageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/daily-train-carriage")
public class DailyTrainCarriageAdminController {

    @Autowired
    private DailyTrainCarriageService dailyTrainCarriageService;
    
    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody DailyTrainCarriageSaveReq req){
        dailyTrainCarriageService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<DailyTrainCarriageQueryResp>> queryList(@Valid DailyTrainCarriageQueryReq req){
        PageResp<DailyTrainCarriageQueryResp> list = dailyTrainCarriageService.queryList(req);
        return new CommonResp<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id){
        dailyTrainCarriageService.delete(id);
        return new CommonResp<>();
    }
}
