package com.zhaopei.train.business.controller.admin;

import com.zhaopei.train.business.req.DailyTrainQueryReq;
import com.zhaopei.train.business.req.DailyTrainSaveReq;
import com.zhaopei.train.business.resp.DailyTrainQueryResp;
import com.zhaopei.train.business.service.DailyTrainService;
import com.zhaopei.train.common.resp.CommonResp;
import com.zhaopei.train.common.resp.PageResp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/admin/daily-train")
public class DailyTrainAdminController {

    @Autowired
    private DailyTrainService dailyTrainService;
    
    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody DailyTrainSaveReq req){
        dailyTrainService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<DailyTrainQueryResp>> queryList(@Valid DailyTrainQueryReq req){
        PageResp<DailyTrainQueryResp> list = dailyTrainService.queryList(req);
        return new CommonResp<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id){
        dailyTrainService.delete(id);
        return new CommonResp<>();
    }

    @GetMapping("/gen-daily/{date}")
    public CommonResp<Object> genDaily(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        dailyTrainService.genDaily(date);
        return new CommonResp<>();
    }
}
