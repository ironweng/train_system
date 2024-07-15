package com.zhaopei.train.business.controller.admin;

import com.zhaopei.train.business.req.StationQueryReq;
import com.zhaopei.train.business.req.StationSaveReq;
import com.zhaopei.train.business.resp.StationQueryResp;
import com.zhaopei.train.business.service.StationService;
import com.zhaopei.train.common.resp.CommonResp;
import com.zhaopei.train.common.resp.PageResp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/station")
public class StationAdminController {

    @Autowired
    private StationService stationService;
    
    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody StationSaveReq req){
        stationService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<StationQueryResp>> queryList(@Valid StationQueryReq req){
        PageResp<StationQueryResp> list = stationService.queryList(req);
        return new CommonResp<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id){
        stationService.delete(id);
        return new CommonResp<>();
    }

    @GetMapping("/query-all")
    public CommonResp<List<StationQueryResp>> queryList(){
        List<StationQueryResp> list = stationService.queryAll();
        return new CommonResp<>(list);
    }
}
