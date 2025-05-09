package com.rich.sol_bot.wallet;

import com.rich.sol_bot.sol.SolOperator;
import com.rich.sol_bot.sol.entity.CreateWallet;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UserWalletGenerateService {

    public CreateWallet generateWallet() {
        try {
            return solOperator.createWallet();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public CreateWallet generateWalletByPri(String priKey) {
        try {
            return solOperator.getAddress(priKey);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }



    @Resource
    private SolOperator solOperator;
}
