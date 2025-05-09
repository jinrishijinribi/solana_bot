package com.rich.sol_bot.sol;

import com.google.gson.Gson;
import com.rich.sol_bot.sol.entity.*;
import com.rich.sol_bot.system.http.HttpClientTool;
import com.rich.sol_bot.system.tool.MapTool;
import jakarta.annotation.Resource;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.net.http.HttpClient.Version.HTTP_1_1;

/**
 * @author wangqiyun
 * @since 2024/3/16 16:28
 */

@Service
public class SolOperator {


    private static final Logger log = LoggerFactory.getLogger(SolOperator.class);

    /**
     * 获取Raydium交易所交易对信息
     *
     * @param ammKey 路由中获取的ammKey
     * @return 交易对信息
     */
    public RaydiumMint raydiumMint(String ammKey) throws Exception {
        return new Gson().fromJson(httpClientTool.doPostWithBody(url + "/api/raydium/mint", new Gson().toJson(MapTool.Map().put("ammKey", ammKey))).getContent(), RaydiumMint.class);
    }


    /**
     * 创建钱包
     *
     * @return address 地址,secret 私钥
     */
    public CreateWallet createWallet() throws Exception {
        return new Gson().fromJson(httpClientTool.doPost(url + "/api/general").getContent(), CreateWallet.class);
    }


    /**
     * 获取钱包地址
     *
     * @param secret 私钥
     * @return address 地址,secret 私钥
     */
    public CreateWallet getAddress(String secret) throws Exception {
        return new Gson().fromJson(httpClientTool.doPostWithBody(url + "/api/address", new Gson().toJson(MapTool.Map().put("secret", secret))).getContent(), CreateWallet.class);
    }

    /**
     * 获取交易详情
     *
     * @param txid 交易哈希
     * @return
     */
    public Transaction getTransaction(String txid) throws Exception {
        return new Gson().fromJson(httpClientTool.doPostWithBody(url + "/api/get/transaction", new Gson().toJson(MapTool.Map().put("txid", txid))).getContent(), Transaction.class);
    }


    /**
     * 创建主币(sol)转账交易
     *
     * @param from  转出地址
     * @param to    转入地址
     * @param value 主币数量,整数(sol的decimal为9)
     * @param payer 出手续费地址,可于from或者to相同
     * @return transaction 创建的未签名的base64交易,签名时需传入from和payer的私钥,如果from和payer相同,那么传一个私钥即可
     */
    public CreateTransaction solTransfer(String from, String to, Long value, String payer) throws Exception {
        return new Gson().fromJson(httpClientTool.doPostWithBody(url + "/api/sol/transfer", new Gson().toJson(
                MapTool.Map().put("from", from).put("to", to).put("value", value).put("payer", payer))).getContent(), CreateTransaction.class);
    }

    /**
     * 获取用户代币账户
     *
     * @param mint  代币地址
     * @param owner 用户主地址
     * @return amount 用户该代币的余额,整数
     */
    public TokenAccount tokenAccount(String mint, String owner){
        try {
            return new Gson().fromJson(httpClientTool.doPostWithBody(url + "/api/token/account",
                    new Gson().toJson(MapTool.Map().put("mint", mint).put("owner", owner))).getContent(), TokenAccount.class);
        }catch (Exception e) {
            log.error("query tokenAccount", e);
        }
        return null;
    }

    /**
     * 获取代币信息
     *
     * @param mint 代币地址
     * @return mintAuthority 铸造权限地址
     * - supply 发行量,整数
     * - decimals 小数位数
     * - freezeAuthority 冻结权限地址
     */
    public MintAccount mintAccount(String mint) throws Exception {
        return new Gson().fromJson(httpClientTool.doPostWithBody(url + "/api/token/mint",
                new Gson().toJson(MapTool.Map().put("mint", mint))).getContent(), MintAccount.class);
    }


    /**
     * 通过umi获取代币信息
     *
     * @param mint 代币地址
     * @return 代币信息
     */
    public MintUmi mintUmi(String mint) throws Exception {
        return new Gson().fromJson(httpClientTool.doPostWithBody(url + "/api/mint/umi",
                new Gson().toJson(MapTool.Map().put("mint", mint))).getContent(), MintUmi.class);
    }

    /**
     * 获取主币余额
     *
     * @param owner 用户地址
     * @return balance 用户的sol余额,整数,注意sol的decimal为9
     */
    public SolBalance solBalance(String owner)  {
        try {
            return new Gson().fromJson(httpClientTool.doPostWithBody(url + "/api/sol/balance",
                    new Gson().toJson(MapTool.Map().put("owner", owner))).getContent(), SolBalance.class);
        }catch (Exception e) {
            log.error("query solBalance", e);
        }
        return null;
    }


    /**
     * @param secret      可能用到的密钥数组
     * @param transaction base64格式的交易
     * @return signTransaction base64格式签名交易
     * - signTransaction58 base58格式签名交易
     * @deprecated 签名
     */
    public SignResult sign(List<String> secret, String transaction) throws Exception {
        return new Gson().fromJson(httpClientTool.doPostWithBody(url + "/api/sign",
                new Gson().toJson(MapTool.Map().put("secret", secret).put("transaction", transaction))).getContent(), SignResult.class);
    }

    /**
     * @param secret      可能用到的密钥数组
     * @param transaction base64格式的交易
     * @return 签名完成的交易以及交易哈希
     * 签名并发送交易
     */
    public SignSendResult signSend(List<String> secret, String transaction, Boolean dedicated){
        try {
            return new Gson().fromJson(httpClientTool.doPostWithBody(url + "/api/sign/send",
                    new Gson().toJson(MapTool.Map().put("secret", secret).put("transaction", transaction).put("dedicated", dedicated))).getContent(), SignSendResult.class);
        } catch (Exception e) {
            log.error("signSend error", e);
        }
        return null;
    }

    public SignSendJitoResult jitoSignSend(List<String> secret, List<String> transaction, String payer, BigInteger fee) {
        try {
            return new Gson().fromJson(httpClientTool.doPostWithBody(url + "/api/jito/sign/send",
                    new Gson().toJson(MapTool.Map().put("secret", secret)
                            .put("transactions", transaction).put("payer", payer).put("fee", fee))).getContent(), SignSendJitoResult.class);
        } catch (Exception e) {
            log.error("signSend error", e);
        }
        return null;
    }

    /**
     * 获取路由
     *
     * @param inputMint      卖出代币
     * @param outputMint     买入代币
     * @param amount         数量,整数
     * @param slippageBps    滑点,slippageBps%
     * @param platformFeeBps 平台收取手续费,platformFeeBps%,废弃字段
     * @return 路由信息
     */
    public QuoteResponse route(String inputMint, String outputMint, Long amount, Integer slippageBps, Integer platformFeeBps) throws Exception {
        URIBuilder uriBuilder = new URIBuilder("https://quote-api.jup.ag/v6/quote")
                .addParameter("inputMint", inputMint)
                .addParameter("outputMint", outputMint)
                .addParameter("amount", amount.toString())
                .addParameter("slippageBps", slippageBps.toString())
                .addParameter("onlyDirectRoutes", "true");
        return new Gson().fromJson(httpClientProxy.send(HttpRequest.newBuilder().GET().uri(uriBuilder.build()).build(), HttpResponse.BodyHandlers.ofString()).body(), QuoteResponse.class);
    }

    /**
     * 创建兑换交易
     *
     * @param quoteResponse 路由
     * @param userPublicKey 用户地址
     * @return 生成的交易
     */
    public String instructions(QuoteResponse quoteResponse, String userPublicKey) throws Exception {
        MapTool body = MapTool.Map().put("quoteResponse", quoteResponse).put("userPublicKey", userPublicKey);
        return httpClientProxy.send(HttpRequest.newBuilder().uri(new URI("https://quote-api.jup.ag/v6/swap-instructions")).header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(body), StandardCharsets.UTF_8)).build(),
                HttpResponse.BodyHandlers.ofString()).body();
    }


    /**
     * 发送jup兑换交易
     *
     * @param secret        用户密钥
     * @param userPublicKey 用户地址
     * @param priorityFee   优先费用,整数,单位10^-9 sol,建议 20000000以上
     * @param feeAccount    平台收取手续费地址
     * @param fee           平台收取手续费,整数,单位10^-9 sol
     * @param instructions  创建兑换交易函数获得的交易
     * @return 交易哈希
     */
    public String jupSend(List<String> secret, String userPublicKey, Long priorityFee, String feeAccount, Long fee, String instructions) throws Exception {
        return new Gson().fromJson(httpClientTool.doPostWithBody(url + "/api/jup/send",
                new Gson().toJson(MapTool.Map().put("secret", secret).put("userAccount", userPublicKey)
                        .put("priorityFee", priorityFee).put("feeAccount", feeAccount)
                        .put("fee", fee).put("instructions", instructions))).getContent(), SignSendResult.class).getTxid();
    }

    /**
     * @param quoteResponse             路由信息
     * @param userPublicKey             用户地址
     * @param feeAccount                平台手续费地址
     * @param prioritizationFeeLamports 有限费用,整数,单位为10^-9 sol,传null视为不需要优先费用
     * @return swapTransaction 生成的交易的base64格式
     * @deprecated 生成币兑换交易
     */
    public SwapResult swap(QuoteResponse quoteResponse, String userPublicKey, String feeAccount, Long prioritizationFeeLamports) throws Exception {
        MapTool body = MapTool.Map().put("quoteResponse", quoteResponse).put("userPublicKey", userPublicKey);
//        if (StringUtils.hasLength(feeAccount))
//            body.put("feeAccount", feeAccount);
        if (prioritizationFeeLamports != null) {
            body.put("dynamicComputeUnitLimit", true);
            body.put("prioritizationFeeLamports", prioritizationFeeLamports);
        }
        return new Gson().fromJson(httpClientProxy.send(HttpRequest.newBuilder().uri(new URI("https://quote-api.jup.ag/v6/swap")).header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(body), StandardCharsets.UTF_8)).build(),
                HttpResponse.BodyHandlers.ofString()).body(), SwapResult.class);
    }

    public LargestAccounts largestAccounts(String mint) throws Exception {
        return new Gson().fromJson(httpClientTool.doPostWithBody(url + "/api/token/largestaccounts",
                new Gson().toJson(MapTool.Map().put("mint", mint))).getContent(), LargestAccounts.class);
    }

    public String calPump(PumpCal cal) throws Exception {
        return new Gson().fromJson(httpClientTool.doPostWithBody(url + "/api/pump/calc",
                new Gson().toJson(MapTool.Map().put("priorityFee", cal.getPriorityFee()).put("owner", cal.getOwner())
                        .put("mint", cal.getMint()).put("hold", cal.getHold())
                        .put("amount", cal.getAmount()).put("sol", cal.getSol())
                        .put("feeAccount", cal.getFeeAccount()).put("fee", cal.getFee())
                        .put("buy", cal.getBuy())
                )
        ).getContent(), PumpCalResult.class).getTransaction();
    }


    @Resource
    private HttpClientTool httpClientTool;
    @Value("${sol.url}")
    private String url;
    @Resource
    private HttpClient httpClientProxy;

}
