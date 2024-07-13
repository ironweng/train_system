package com.zhaopei.train.business.controller.admin;

import com.zhaopei.train.business.req.TrainStationQueryReq;
import com.zhaopei.train.business.req.TrainStationSaveReq;
import com.zhaopei.train.business.resp.TrainStationQueryResp;
import com.zhaopei.train.business.service.TrainStationService;
import com.zhaopei.train.common.resp.CommonResp;
import com.zhaopei.train.common.resp.PageResp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/train-station")
public class TrainStationAdminController {

    @Autowired
    private TrainStationService trainStationService;
    
    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody TrainStationSaveReq req){
        trainStationService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<TrainStationQueryResp>> queryList(@Valid TrainStationQueryReq req){
        PageResp<TrainStationQueryResp> list = trainStationService.queryList(req);
        return new CommonResp<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id){
        trainStationService.delete(id);
        return new CommonResp<>();
    }
}
