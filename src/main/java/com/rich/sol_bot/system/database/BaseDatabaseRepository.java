package com.rich.sol_bot.system.database;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rich.sol_bot.system.exception.AppException;
import com.rich.sol_bot.system.exception.DefaultAppExceptionCode;

import java.util.function.Supplier;


public class BaseDatabaseRepository<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> {
    public boolean exist(Wrapper<T> queryWrapper) {
        return super.count(queryWrapper) > 0;
    }

    public void executeOrElseThrow(Supplier<Boolean> supplier) throws AppException {
        Boolean success = supplier.get();
        if (!success) {
            throw DefaultAppExceptionCode.DATABASE_ERROR.toException();
        }
    }
}
