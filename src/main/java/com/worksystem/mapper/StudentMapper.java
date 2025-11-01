package com.worksystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worksystem.entity.Student;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StudentMapper extends BaseMapper<Student> {
    // MyBatis-Plus 已提供基础CRUD
}