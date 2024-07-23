package com.zhaopei.train.business.req;

import com.zhaopei.train.common.req.PageReq;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class DailyTrainQueryReq extends PageReq {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    private String code;

    public String getTrainCode() {
        return code;
    }

    public void setTrainCode(String code) {
        this.code = code;
    }

}
