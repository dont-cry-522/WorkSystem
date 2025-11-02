package com.worksystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.worksystem.entity.Position;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PositionMapper<PositionQueryRequest> extends BaseMapper<Position> {

    List<Position> selectPositionList(@Param("query") PositionQueryRequest request);
}