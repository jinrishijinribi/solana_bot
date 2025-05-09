package com.rich.sol_bot.system.query;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class PageResult<T> {
    long count;
    List<T> rows;

    public PageResult(long count, List<T> rows) {
        this.count = count;
        this.rows = rows;
    }

    public PageResult(PageResult<T> pageResult) {
        this.count = pageResult.getCount();
        this.rows = pageResult.getRows();
    }
}
