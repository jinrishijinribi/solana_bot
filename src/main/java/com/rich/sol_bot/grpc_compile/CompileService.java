package com.rich.sol_bot.grpc_compile;

import com.google.gson.Gson;
import com.rich.sol_bot.sol.Base58;
import com.rich.sol_bot.sol.controller.RouteScanDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class CompileService {

    public static String base64ToBase58(String base64) {
        return Base58.encode(Base64.getDecoder().decode(base64));
    }

    public static RouteScanDTO compile(String msg) {
        //msg 是整个交易json字符串
        MyTransaction myTransaction = new Gson().fromJson(msg, MyTransaction.class);
        MyConfirmedTransaction transaction = myTransaction.getUpdateOneof().getTransaction().getTransaction();
        List<MyConfirmedTransaction.Instruction> instructionList = new ArrayList<>(transaction.getTransaction().getMessage().getInstructions());
        for (InnerInstructions innerInstructions : transaction.getMeta().getInner_instructions()) {
            instructionList.addAll(innerInstructions.getInstructions());
        }
        List<String> accountKeys = transaction.getTransaction().getMessage().getAccount_keys();
        accountKeys.addAll(transaction.getMeta().getLoaded_writable_addresses());
        accountKeys.addAll(transaction.getMeta().getLoaded_readonly_addresses());
        accountKeys = accountKeys.stream().map(CompileService::base64ToBase58).toList();
        RouteScanDTO result = null;
        for (MyConfirmedTransaction.Instruction instruction : instructionList) {
            if (accountKeys.get(instruction.getProgram_id_index()).equals("675kPX9MHTjS2zt1qfr1NYHuzeLXfQM9H24wFSUt1Mp8")) {
                byte[] accountsIndex = Base64.getDecoder().decode(instruction.getAccounts());
                byte[] dataBytes = Base64.getDecoder().decode(instruction.getData());
                if (dataBytes[0] == 1) {
                    String id = accountKeys.get(0xff & accountsIndex[4]);
                    String baseMint = accountKeys.get(0xff & accountsIndex[8]);
                    String quoteMint = accountKeys.get(0xff & accountsIndex[9]);
                    //todo 此处处理扫描到的新建池子
                    result = RouteScanDTO.builder().baseMint(baseMint).id(id).quoteMint(quoteMint).build();
                }
            }
        }
        return result;
    }

//    public static void main(String[] args) {
////        compile("");
//    }
}
