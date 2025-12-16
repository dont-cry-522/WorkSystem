package com.worksystem.controller;

// controller/PositionController.java

import com.worksystem.dto.response.CommonResponse;
import com.worksystem.entity.Position;
import com.worksystem.mapper.PositionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/positions")
public class PositionController {

    @Autowired
    private PositionMapper positionMapper;

    /**
     * 获取岗位列表（只显示招聘中的岗位）
     */
    /**
     * 获取岗位列表（只显示招聘中的岗位）
     */
    @GetMapping
    public CommonResponse getPositionList() {
        List<Position> positions = positionMapper.selectList(null);

        // 过滤只显示招聘中的岗位
        List<Position> recruitingPositions = positions.stream()
                .filter(p -> "recruiting".equals(p.getRecruitmentStatus()))
                .collect(Collectors.toList());

        return CommonResponse.success(recruitingPositions);
    }


    /**
     * 获取岗位详情
     */
    @GetMapping("/{id}")
    public CommonResponse getPositionDetail(@PathVariable Long id) {
        Position position = (Position) positionMapper.selectById(id);
        if (position == null) {
            return CommonResponse.error(404, "岗位不存在");
        }
        return CommonResponse.success(position);
    }

    /**
     * 测试接口
     */
    @GetMapping("/test")
    public CommonResponse test() {
        return CommonResponse.success("岗位服务正常");
    }
}