package com.zhaopei.train.business.req;

import com.zhaopei.train.common.req.PageReq;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

@Data
public class DailyTrainCarriageQueryReq extends PageReq {

    /**
     * 日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    /**
     * 车次编号
     */
    private String trainCode;

}
