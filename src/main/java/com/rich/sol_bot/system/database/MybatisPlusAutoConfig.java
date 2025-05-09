package com.rich.sol_bot.system.database;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusPropertiesCustomizer;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.handlers.MybatisEnumTypeHandler;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.rich.sol_bot.system.common.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.LocalCacheScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author T.J
 * @date 2021/11/24 11:47
 */
@Slf4j
@Configuration
public class MybatisPlusAutoConfig {
    @Value("${system.sql.select-max-limit:500}")
    private long selectMaxLimit = 500;

    @Bean
    public IdentifierGenerator identifierGenerator() {
        log.info("配置MybatisPlus的ID生成器Bean");
        return new IdentifierGenerator() {
            @Override
            public Number nextId(Object entity) {
                return IdUtil.nextId();
            }

            @Override
            public String nextUUID(Object entity) {
                return IdUtil.uuid();
            }
        };
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        log.info("配置MybatisPlus的拦截器Bean");
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 防全表更新与删除插件
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        log.info("配置MybatisPlus启用防全表更新与删除插件");
        // 乐观锁拦截器
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        log.info("配置MybatisPlus启用乐观锁插件");
        // 分页拦截器
        final PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        paginationInnerInterceptor.setMaxLimit(selectMaxLimit);
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        log.info("配置MybatisPlus启用分页插件");
        return interceptor;
    }

    @Bean
    public MybatisPlusPropertiesCustomizer mybatisPlusPropertiesCustomizer() {
        log.info("配置MybatisPlus的属性Bean");
        return properties -> {
            MybatisConfiguration configuration = new MybatisConfiguration();
            // 默认的枚举处理器
            configuration.setDefaultEnumTypeHandler(MybatisEnumTypeHandler.class);
            log.info("配置MybatisPlus配置默认枚举处理器" + MybatisEnumTypeHandler.class.getName());
            // 服务要多进程部署，关闭一级缓存
            configuration.setLocalCacheScope(LocalCacheScope.STATEMENT);
            log.info("配置MybatisPlus关闭一级缓存");
            // 服务要多进程部署，关闭二级缓存
            configuration.setCacheEnabled(false);
            log.info("配置MybatisPlus关闭二级缓存");
            properties.setConfiguration(configuration);
        };
    }
}
