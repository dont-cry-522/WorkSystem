package com.worksystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worksystem.entity.User;
import org.apache.ibatis.annotations.Mapper; // 必须添加
import org.springframework.stereotype.Repository;

@Mapper
@Repository // 关键：标记为MyBatis Mapper接口
public interface UserMapper extends BaseMapper<User> { // 必须继承BaseMapper
    // 无需手动写selectOne方法，BaseMapper已提供
}