package com.rich.sol_bot.iceberg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface IcebergTaskMapper extends BaseMapper<IcebergTask> {

    @Update("update `iceberg_task` set `success_count` = `success_count` + 1 where `id` = #{id}")
    long successOne(Long id);

    @Update("update `iceberg_task` set `fail_count` = `fail_count` + 1 where `id` = #{id}")
    long failOne(Long id);

    @Update("update `iceberg_task` set `submit_count` = `submit_count` + 1 where `id` = #{id} ")
    long submitOne(Long id);
}
