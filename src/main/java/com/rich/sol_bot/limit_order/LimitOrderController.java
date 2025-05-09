package com.rich.sol_bot.limit_order;


import com.rich.sol_bot.limit_order.param.LimitOrderPxParam;
import com.rich.sol_bot.limit_order.px_subscribe.PxSubscribeSchedule;
import com.rich.sol_bot.limit_order.px_subscribe.PxSubscribeService;
import com.rich.sol_bot.limit_order.service.LimitOrderPxQueueService;
import com.rich.sol_bot.limit_order.service.LimitOrderPxService;
import com.rich.sol_bot.system.common.ThreadAsyncUtil;
import com.rich.sol_bot.trade.mapper.TokenBaseInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "限价单")
@RestController
@RequestMapping("/api/limit")
@Slf4j
public class LimitOrderController {

    @Autowired
    private LimitOrderPxQueueService limitOrderPxQueueService;

    @Operation(summary = "限价单价格通知")
    @PostMapping("/ray/px")
    public Boolean limitOrderPx(@RequestBody LimitOrderPxParam param) {
        log.info("{}:{}:{}", param.getAmmkey(), param.getMint(), param.getAmount());
        limitOrderPxService.cacheAmount(param.getAmmkey(), param.getMint(), param.getAmount());
        if(!param.getMint().equalsIgnoreCase(TokenBaseInfo.mainAddress())) {
            // 不是主币，就触发
            limitOrderPxQueueService.triggerTask(param.getAmmkey(), param.getMint());
        }
        ThreadAsyncUtil.execAsync(() -> {
            pxSubscribeService.updateAmount(param.getAmmkey(), param.getMint(), Long.valueOf(param.getAmount()));
        });
        return true;
    }


    @Resource
    private LimitOrderPxService limitOrderPxService;
    @Resource
    private PxSubscribeService pxSubscribeService;
}
