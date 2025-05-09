package com.rich.sol_bot.sol;

import com.google.gson.Gson;
import com.rich.sol_bot.sol.entity.CreateTransaction;
import com.rich.sol_bot.sol.entity.RayComputeAmountOut;
import com.rich.sol_bot.sol.entity.SignSendResult;
import com.rich.sol_bot.system.http.HttpClientTool;
import com.rich.sol_bot.system.tool.MapTool;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.http.HttpClient;
import java.util.List;

/**
 * @author wangqiyun
 * @since 2024/3/23 17:07
 */

@Service
public class RaydiumOperator {


    /**
     * 计算输出币的数量
     *
     * @param inputMint   输入币地址
     * @param outputMint  输出币地址
     * @param ammKey      交易对key
     * @param inputAmount 输入币数量,整数
     * @param slippage    滑点
     * @return 输出币数量计算
     */
    public RayComputeAmountOut computeAmountOut(String inputMint, String outputMint, String ammKey, Long inputAmount, Integer slippage) throws Exception {
        return new Gson().fromJson(httpClientTool.doPostWithBody(url + "/api/raydium/computeAmountOut",
                new Gson().toJson(MapTool.Map().put("inputMint", inputMint).put("outputMint", outputMint)
                        .put("ammKey", ammKey).put("inputAmount", inputAmount.toString())
                        .put("slippage", slippage))).getContent(), RayComputeAmountOut.class);
    }


    /**
     * 计算ray交易
     *
     * @param userPublicKey 用户地址
     * @param priorityFee   优先费用,整数,单位10^-9 sol,建议 20000000以上
     * @param feeAccount    平台收取手续费地址
     * @param fee           平台收取手续费,整数,单位10^-9 sol
     * @param inputMint     输入币地址
     * @param outputMint    输出币地址
     * @param ammKey        交易对key
     * @param inputAmount   输入币数量,整数
     * @param minAmountOut  上一步计算出的输出币最小数量,整数
     * @return base64的交易体
     */
    public String rayCalc(String userPublicKey, Long priorityFee, String feeAccount, Long fee, String inputMint, String outputMint, String ammKey, Long inputAmount, Long minAmountOut) throws Exception {
        return new Gson().fromJson(httpClientTool.doPostWithBody(url + "/api/raydium/calc",
                new Gson().toJson(MapTool.Map().put("owner", userPublicKey)
                        .put("priorityFee", priorityFee).put("feeAccount", feeAccount).put("fee", fee)
                        .put("inputMint", inputMint).put("outputMint", outputMint)
                        .put("ammKey", ammKey).put("inputAmount", inputAmount.toString()).put("minAmountOut", minAmountOut.toString())
                )).getContent(), CreateTransaction.class).getTransaction();
    }


    /**
     * 发送ray交易
     *
     * @param secret        用户密钥
     * @param userPublicKey 用户地址
     * @param priorityFee   优先费用,整数,单位10^-9 sol,建议 20000000以上
     * @param feeAccount    平台收取手续费地址
     * @param fee           平台收取手续费,整数,单位10^-9 sol
     * @param inputMint     输入币地址
     * @param outputMint    输出币地址
     * @param ammKey        交易对key
     * @param inputAmount   输入币数量,整数
     * @param minAmountOut  上一步计算出的输出币最小数量,整数
     * @return 交易哈希
     */
    public String raySend(List<String> secret, String userPublicKey, Long priorityFee, String feeAccount,
                          Long fee, String inputMint, String outputMint, String ammKey, Long inputAmount,
                          Long minAmountOut, Boolean dedicated) throws Exception {
        return new Gson().fromJson(httpClientTool.doPostWithBody(url + "/api/raydium/send",
                new Gson().toJson(MapTool.Map().put("secret", secret).put("owner", userPublicKey)
                        .put("priorityFee", priorityFee).put("feeAccount", feeAccount).put("fee", fee)
                        .put("inputMint", inputMint).put("outputMint", outputMint)
                        .put("ammKey", ammKey).put("inputAmount", inputAmount.toString())
                        .put("minAmountOut", minAmountOut.toString()).put("dedicated", dedicated)
                )).getContent(), SignSendResult.class).getTxid();
    }


    @Resource
    private HttpClientTool httpClientTool;
    @Value("${sol.url}")
    private String url;
    @Resource
    private HttpClient httpClientProxy;
}
