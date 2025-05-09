package com.rich.sol_bot.trade.operator;

import com.google.gson.Gson;
import com.rich.sol_bot.sol.DexscreenerService;
import com.rich.sol_bot.sol.RaydiumOperator;
import com.rich.sol_bot.sol.SolOperator;
import com.rich.sol_bot.sol.entity.*;
import com.rich.sol_bot.system.common.ThreadAsyncUtil;
import com.rich.sol_bot.system.http.HttpClientTool;
import com.rich.sol_bot.system.tool.MapTool;
import com.rich.sol_bot.trade.dto.OHLCVTDTO;
import com.rich.sol_bot.trade.dto.UriInfoDTO;
import com.rich.sol_bot.trade.enums.OHLCVTypeEnum;
import com.rich.sol_bot.trade.params.OHLCVTParam;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class SolQueryService {
    public SolBalance solBalance(String owner) {
        try {
            return solOperator.solBalance(owner);
        } catch (Exception e) {
            e.printStackTrace();;
        }
        return null;
    }

    public MintAccount mintAccount(String mint) {
        try {
            return solOperator.mintAccount(mint);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public MintUmi mintUmi(String mint) {
        try {
            return solOperator.mintUmi(mint);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public RaydiumMint raydiumMint(String ammKey) {
        try {
            return solOperator.raydiumMint(ammKey);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public BigDecimal tokenBalance(String tokenAddress, String walletAddress) {
        try {
            TokenAccount account = solOperator.tokenAccount(tokenAddress, walletAddress);
            return new BigDecimal(account.getAmount());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    public String tokenAccount(String mint, String owner) {
        try {
            TokenAccount account = solOperator.tokenAccount(mint, owner);
            return account.getAssociatedTokenAddress();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public QuoteResponse tradeRoute(String inputMint, String outputMint, Long amount, Integer slippageBps, Integer platformFeeBps) {
        try {
            return solOperator.route(inputMint, outputMint, amount, slippageBps, platformFeeBps);
        } catch (Exception e) {
            e.printStackTrace();;
        }
        return null;
    }

//    public SwapResult swap(QuoteResponse quoteResponse, String userPublicKey, String feeAccount){
//        try {
//            return solOperator.swap(quoteResponse, userPublicKey, feeAccount, null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

//    public SignSendResult signSend(List<String> secret, String transaction) {
//        try {
//            return solOperator.signSend(secret, transaction);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public String instructions(QuoteResponse quoteResponse, String userPublicKey){
        try {
            return solOperator.instructions(quoteResponse, userPublicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String jupSend(List<String> secret, String userPublicKey, Long priorityFee, String feeAccount, Long fee, String instructions) {
        try {
            return solOperator.jupSend(secret, userPublicKey, priorityFee, feeAccount, fee, instructions);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Transaction getTransaction(String txid) {
        try {
            return solOperator.getTransaction(txid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public DexscreenerTokenPair.Pairs pairOfRay(String mint) {
        try {
            DexscreenerTokenPair result = dexscreenerService.getDexPairInfo(mint);
            if(result.getPairs() == null) return null;
            List<DexscreenerTokenPair.Pairs> list = result.getPairs().stream().filter(o ->
                    Objects.equals(o.getDexId(), "raydium") && "So11111111111111111111111111111111111111112".equals(o.getQuoteToken().getAddress())
            ).sorted((o1, o2) -> o2.getLiquidity().getQuote().compareTo(o1.getLiquidity().getQuote())).toList();
            if(list.isEmpty()) return null;
            return list.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public RayComputeAmountOut computeAmountOut(String inputMint, String outputMint, String ammKey, Long inputAmount, Integer slippage) {
        try {
            return raydiumOperator.computeAmountOut(inputMint, outputMint, ammKey, inputAmount, slippage);
        } catch (Exception e) {
            return null;
        }
    }

    public String raySend(List<String> secret, String userPublicKey, Long priorityFee, String feeAccount, Long fee, String inputMint,
                          String outputMint, String ammKey, Long inputAmount, Long minAmountOut, Boolean dedicated) {
        try {
            return raydiumOperator.raySend(secret, userPublicKey, priorityFee, feeAccount, fee, inputMint, outputMint, ammKey, inputAmount, minAmountOut, dedicated);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public UriInfoDTO getUriInfo(String uri) {
        try {
            return new Gson().fromJson(httpClientTool.doGet(uri).getContent(), UriInfoDTO.class);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return UriInfoDTO.builder().build();
    }

    public LargestAccounts largestAccounts(String mint) {
        try {
            return solOperator.largestAccounts(mint);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return LargestAccounts.builder().accounts(new ArrayList<>()).build();
    }

    public List<OHLCVTDTO> result(OHLCVTParam param) {
        try {

//            httpClientTool.

//            return new Gson().fromJson(httpClientTool.doGet(uri).getContent(), UriInfoDTO.class);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void generatePageView(Long uid, Long tokenId, Long walletId) {
        ThreadAsyncUtil.execAsync(() -> {
            try {
                httpClientTool.doPostWithBody(url,
                        new Gson().toJson(MapTool.Map().put("uid", uid.toString()).put("tokenId", tokenId.toString()).put("walletId", walletId.toString())));
            }catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    @Value("${sol.viewurl}")
    private String url;






    @Resource
    private SolOperator solOperator;
    @Resource
    private DexscreenerService dexscreenerService;
    @Resource
    private RaydiumOperator raydiumOperator;
    @Resource
    private HttpClientTool httpClientTool;
}
