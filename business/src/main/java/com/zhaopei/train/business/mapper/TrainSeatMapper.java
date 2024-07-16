package com.zhaopei.train.business.mapper;

import com.zhaopei.train.business.domain.trainSeat;
import com.zhaopei.train.business.domain.trainSeatExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TrainSeatMapper {
    long countByExample(trainSeatExample example);

    int deleteByExample(trainSeatExample example);

    int deleteByPrimaryKey(Long id);

    int insert(trainSeat record);

    int insertSelective(trainSeat record);

    List<trainSeat> selectByExample(trainSeatExample example);

    trainSeat selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") trainSeat record, @Param("example") trainSeatExample example);

    int updateByExample(@Param("record") trainSeat record, @Param("example") trainSeatExample example);

    int updateByPrimaryKeySelective(trainSeat record);

    int updateByPrimaryKey(trainSeat record);
}