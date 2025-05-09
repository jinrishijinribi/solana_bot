package com.rich.sol_bot.sol.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author wangqiyun
 * @since 2024/3/19 00:20
 */

@Data
public class Transaction {
    private Long blockTime;
    private Meta meta;
    private TransactionDetail transaction;


    @Data
    public static class Meta {
        private Long computeUnitsConsumed;
        private Long fee;//交易手续费
        private Map<String, Object> err;//交易成功时 err==null
        private List<Long> postBalances;//交易后每个账户主币情况
        private List<Long> preBalances;//交易前每个账户主币情况
        private List<TokenBalances> postTokenBalances;//交易后账户代币情况
        private List<TokenBalances> preTokenBalances;//交易后账户主币情况
        private List<String> logMessages;

        @Data
        public static class TokenBalances {
            private Integer accountIndex;
            private String mint;
            private String owner;
            private String programId;
            private UiTokenAmount uiTokenAmount;

            @Data
            public static class UiTokenAmount {
                private String amount;
                private Integer decimals;
                private Double uiAmount;
                private String uiAmountString;
            }
        }
    }

    @Data
    public static class TransactionDetail {
        private Message message;

        @Data
        public static class Message {
            private String recentBlockhash;
            private List<AccountKeys> accountKeys;

            @Data
            public static class AccountKeys {
                private String pubkey;
                private Boolean signer;
                private String source;
                private Boolean writable;
            }
        }
    }

}
/*
示例:

{
  "blockTime": 1710776873,
  "meta": {
    "computeUnitsConsumed": 150206,
    "err": null,
    "fee": 1636424,
    "innerInstructions": [
      {
        "index": 1,
        "instructions": [
          {
            "parsed": {
              "info": {
                "extensionTypes": [
                  "immutableOwner"
                ],
                "mint": "So11111111111111111111111111111111111111112"
              },
              "type": "getAccountDataSize"
            },
            "program": "spl-token",
            "programId": "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA",
            "stackHeight": 2
          },
          {
            "parsed": {
              "info": {
                "lamports": 2039280,
                "newAccount": "DUHSGV3w85352tSa6ucWNddco7vFqgHzQmYkeuYFCbcd",
                "owner": "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA",
                "source": "AmDp48FLbRN76uQHG9rEjozoc28TJNcVGrvDEJvDuWBa",
                "space": 165
              },
              "type": "createAccount"
            },
            "program": "system",
            "programId": "11111111111111111111111111111111",
            "stackHeight": 2
          },
          {
            "parsed": {
              "info": {
                "account": "DUHSGV3w85352tSa6ucWNddco7vFqgHzQmYkeuYFCbcd"
              },
              "type": "initializeImmutableOwner"
            },
            "program": "spl-token",
            "programId": "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA",
            "stackHeight": 2
          },
          {
            "parsed": {
              "info": {
                "account": "DUHSGV3w85352tSa6ucWNddco7vFqgHzQmYkeuYFCbcd",
                "mint": "So11111111111111111111111111111111111111112",
                "owner": "AmDp48FLbRN76uQHG9rEjozoc28TJNcVGrvDEJvDuWBa"
              },
              "type": "initializeAccount3"
            },
            "program": "spl-token",
            "programId": "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA",
            "stackHeight": 2
          }
        ]
      },
      {
        "index": 2,
        "instructions": [
          {
            "parsed": {
              "info": {
                "authority": "AmDp48FLbRN76uQHG9rEjozoc28TJNcVGrvDEJvDuWBa",
                "destination": "59v2cSbCsnyaWymLnsq6TWzE6cEN5KJYNTBNrcP4smRH",
                "mint": "EPjFWdd5AufqSSqeM2qN1xzybapC8G4wEGGkZwyTDt1v",
                "source": "5eUrKE7F3dKHvTPZqpsK6r2QfTxT6BvwDwbxKEeL2QA9",
                "tokenAmount": {
                  "amount": "9938497",
                  "decimals": 6,
                  "uiAmount": 9.938497,
                  "uiAmountString": "9.938497"
                }
              },
              "type": "transferChecked"
            },
            "program": "spl-token",
            "programId": "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA",
            "stackHeight": 2
          },
          {
            "accounts": [
              "HTvjzsfX3yU6BUodCjZ5vZkUrAxMDTrBs3CJaq43ashR",
              "LBUZKhRxPF3XUpBCjp4YzTKgLccjZhTSDM9YuVaPwxo",
              "H7j5NPopj3tQvDg4N8CxwtYciTn3e8AEV6wSVrxpyDUc",
              "HbYjRzx7teCxqW3unpXBEcNHhfVZvW2vW9MQ99TkizWt",
              "59v2cSbCsnyaWymLnsq6TWzE6cEN5KJYNTBNrcP4smRH",
              "7x4VcEX8aLd3kFsNWULTp1qFgVtDwyWSxpTGQkoMM6XX",
              "So11111111111111111111111111111111111111112",
              "EPjFWdd5AufqSSqeM2qN1xzybapC8G4wEGGkZwyTDt1v",
              "EgEYXef2FCoEYLHJJW74dMbom1atLXo6KwPuA6mSATYA",
              "LBUZKhRxPF3XUpBCjp4YzTKgLccjZhTSDM9YuVaPwxo",
              "6U91aKa8pmMxkJwBCfPTmUEfZi6dHe7DcFq2ALvB2tbB",
              "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA",
              "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA",
              "D1ZN9Wj1fRSUQfCjhvnu1hqDMT7hzjzBBpi12nVniYD6",
              "LBUZKhRxPF3XUpBCjp4YzTKgLccjZhTSDM9YuVaPwxo",
              "dMDeK1jxR52iSMvR9gqmjchkXFAbEcDa7mPmsxaePdb",
              "DF6A3F6ViTFAnndsrVPS4ew2CrWujYifpaS2rrL73ege",
              "2DBZwgVP2faWTMq5nJWqZXUg3BRn3FYo9AtwUyRuALud"
            ],
            "data": "PgQWtn8oziwxtrDykf8vPDJmktjicNKiP",
            "programId": "LBUZKhRxPF3XUpBCjp4YzTKgLccjZhTSDM9YuVaPwxo",
            "stackHeight": 2
          },
          {
            "parsed": {
              "info": {
                "authority": "6U91aKa8pmMxkJwBCfPTmUEfZi6dHe7DcFq2ALvB2tbB",
                "destination": "HbYjRzx7teCxqW3unpXBEcNHhfVZvW2vW9MQ99TkizWt",
                "mint": "EPjFWdd5AufqSSqeM2qN1xzybapC8G4wEGGkZwyTDt1v",
                "source": "59v2cSbCsnyaWymLnsq6TWzE6cEN5KJYNTBNrcP4smRH",
                "tokenAmount": {
                  "amount": "9938497",
                  "decimals": 6,
                  "uiAmount": 9.938497,
                  "uiAmountString": "9.938497"
                }
              },
              "type": "transferChecked"
            },
            "program": "spl-token",
            "programId": "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA",
            "stackHeight": 3
          },
          {
            "parsed": {
              "info": {
                "authority": "HTvjzsfX3yU6BUodCjZ5vZkUrAxMDTrBs3CJaq43ashR",
                "destination": "7x4VcEX8aLd3kFsNWULTp1qFgVtDwyWSxpTGQkoMM6XX",
                "mint": "So11111111111111111111111111111111111111112",
                "source": "H7j5NPopj3tQvDg4N8CxwtYciTn3e8AEV6wSVrxpyDUc",
                "tokenAmount": {
                  "amount": "49107593",
                  "decimals": 9,
                  "uiAmount": 0.049107593,
                  "uiAmountString": "0.049107593"
                }
              },
              "type": "transferChecked"
            },
            "program": "spl-token",
            "programId": "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA",
            "stackHeight": 3
          },
          {
            "accounts": [
              "D1ZN9Wj1fRSUQfCjhvnu1hqDMT7hzjzBBpi12nVniYD6"
            ],
            "data": "yCGxBopjnVNQkNP5usq1PpVPviYoH78qx2JCYWGFStt5m6D1u8ivt7uHmx9UpdgBTUKM4XbMKxZZdmFDFRkNephTwvh6swMt2s7Bb5xFAs9e6rwP3CATJDzquwoBrEszxBrsi5bFrPPbvrX3GUQDoUufWT4CgoaDKDnQQZd26RuhJtoarFpbVHud4bSrYyAfMXFyGj",
            "programId": "LBUZKhRxPF3XUpBCjp4YzTKgLccjZhTSDM9YuVaPwxo",
            "stackHeight": 3
          },
          {
            "accounts": [
              "D8cy77BBepLMngZx6ZukaTff5hCt1HrWyKk3Hnd9oitf"
            ],
            "data": "QMqFu4fYGGeUEysFnenhAvBobXTzswhLdvQq6s8axxcbKUPRksm2543pJNNNHVd1VLAi4qh5j7rwZtCSWuuB1aXiEonpsPqhURndSP2a5qqFkRE9kMrY6YUvs2zqJ7SKhNAc8dJ7qzmhWSXQSUNrNT5ai62bhNgy7jnR73w3DyaP6DM",
            "programId": "JUP6LkbZbjS1jKKwapdHNy74zcZ3tLUZoi5QNyVTaV4",
            "stackHeight": 2
          },
          {
            "parsed": {
              "info": {
                "authority": "6U91aKa8pmMxkJwBCfPTmUEfZi6dHe7DcFq2ALvB2tbB",
                "destination": "DUHSGV3w85352tSa6ucWNddco7vFqgHzQmYkeuYFCbcd",
                "mint": "So11111111111111111111111111111111111111112",
                "source": "7x4VcEX8aLd3kFsNWULTp1qFgVtDwyWSxpTGQkoMM6XX",
                "tokenAmount": {
                  "amount": "49107593",
                  "decimals": 9,
                  "uiAmount": 0.049107593,
                  "uiAmountString": "0.049107593"
                }
              },
              "type": "transferChecked"
            },
            "program": "spl-token",
            "programId": "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA",
            "stackHeight": 2
          }
        ]
      }
    ],
    "logMessages": [
      "Program ComputeBudget111111111111111111111111111111 invoke [1]",
      "Program ComputeBudget111111111111111111111111111111 success",
      "Program ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL invoke [1]",
      "Program log: CreateIdempotent",
      "Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA invoke [2]",
      "Program log: Instruction: GetAccountDataSize",
      "Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA consumed 1569 of 794445 compute units",
      "Program return: TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA pQAAAAAAAAA=",
      "Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA success",
      "Program 11111111111111111111111111111111 invoke [2]",
      "Program 11111111111111111111111111111111 success",
      "Program log: Initialize the associated token account",
      "Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA invoke [2]",
      "Program log: Instruction: InitializeImmutableOwner",
      "Program log: Please upgrade to SPL Token 2022 for immutable owner support",
      "Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA consumed 1405 of 787858 compute units",
      "Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA success",
      "Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA invoke [2]",
      "Program log: Instruction: InitializeAccount3",
      "Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA consumed 3158 of 783976 compute units",
      "Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA success",
      "Program ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL consumed 19315 of 799850 compute units",
      "Program ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL success",
      "Program JUP6LkbZbjS1jKKwapdHNy74zcZ3tLUZoi5QNyVTaV4 invoke [1]",
      "Program log: Instruction: SharedAccountsRoute",
      "Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA invoke [2]",
      "Program log: Instruction: TransferChecked",
      "Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA consumed 6200 of 763474 compute units",
      "Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA success",
      "Program LBUZKhRxPF3XUpBCjp4YzTKgLccjZhTSDM9YuVaPwxo invoke [2]",
      "Program log: Instruction: Swap",
      "Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA invoke [3]",
      "Program log: Instruction: TransferChecked",
      "Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA consumed 6200 of 693336 compute units",
      "Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA success",
      "Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA invoke [3]",
      "Program log: Instruction: TransferChecked",
      "Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA consumed 6238 of 683702 compute units",
      "Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA success",
      "Program LBUZKhRxPF3XUpBCjp4YzTKgLccjZhTSDM9YuVaPwxo invoke [3]",
      "Program LBUZKhRxPF3XUpBCjp4YzTKgLccjZhTSDM9YuVaPwxo consumed 2132 of 674033 compute units",
      "Program LBUZKhRxPF3XUpBCjp4YzTKgLccjZhTSDM9YuVaPwxo success",
      "Program LBUZKhRxPF3XUpBCjp4YzTKgLccjZhTSDM9YuVaPwxo consumed 63714 of 734069 compute units",
      "Program LBUZKhRxPF3XUpBCjp4YzTKgLccjZhTSDM9YuVaPwxo success",
      "Program JUP6LkbZbjS1jKKwapdHNy74zcZ3tLUZoi5QNyVTaV4 invoke [2]",
      "Program JUP6LkbZbjS1jKKwapdHNy74zcZ3tLUZoi5QNyVTaV4 consumed 2021 of 667343 compute units",
      "Program JUP6LkbZbjS1jKKwapdHNy74zcZ3tLUZoi5QNyVTaV4 success",
      "Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA invoke [2]",
      "Program log: Instruction: TransferChecked",
      "Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA consumed 6238 of 660630 compute units",
      "Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA success",
      "Program JUP6LkbZbjS1jKKwapdHNy74zcZ3tLUZoi5QNyVTaV4 consumed 127676 of 780535 compute units",
      "Program return: JUP6LkbZbjS1jKKwapdHNy74zcZ3tLUZoi5QNyVTaV4 iVLtAgAAAAA=",
      "Program JUP6LkbZbjS1jKKwapdHNy74zcZ3tLUZoi5QNyVTaV4 success",
      "Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA invoke [1]",
      "Program log: Instruction: CloseAccount",
      "Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA consumed 2915 of 652859 compute units",
      "Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA success",
      "Program 11111111111111111111111111111111 invoke [1]",
      "Program 11111111111111111111111111111111 success"
    ],
    "postBalances": [
      1058152092,
      0,
      2039280,
      2039280,
      6676315,
      71437440,
      71437440,
      71437440,
      5078560,
      1,
      731913600,
      1,
      934087680,
      1141440,
      2033060000,
      0,
      7182720,
      41865719231,
      2039280,
      23385600,
      491386727358,
      242683120916,
      1141440,
      0
    ],
    "postTokenBalances": [
      {
        "accountIndex": 2,
        "mint": "EPjFWdd5AufqSSqeM2qN1xzybapC8G4wEGGkZwyTDt1v",
        "owner": "AmDp48FLbRN76uQHG9rEjozoc28TJNcVGrvDEJvDuWBa",
        "programId": "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA",
        "uiTokenAmount": {
          "amount": "0",
          "decimals": 6,
          "uiAmount": null,
          "uiAmountString": "0"
        }
      },
      {
        "accountIndex": 3,
        "mint": "EPjFWdd5AufqSSqeM2qN1xzybapC8G4wEGGkZwyTDt1v",
        "owner": "6U91aKa8pmMxkJwBCfPTmUEfZi6dHe7DcFq2ALvB2tbB",
        "programId": "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA",
        "uiTokenAmount": {
          "amount": "40309742",
          "decimals": 6,
          "uiAmount": 40.309742,
          "uiAmountString": "40.309742"
        }
      },
      {
        "accountIndex": 4,
        "mint": "So11111111111111111111111111111111111111112",
        "owner": "6U91aKa8pmMxkJwBCfPTmUEfZi6dHe7DcFq2ALvB2tbB",
        "programId": "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA",
        "uiTokenAmount": {
          "amount": "4637035",
          "decimals": 9,
          "uiAmount": 0.004637035,
          "uiAmountString": "0.004637035"
        }
      },
      {
        "accountIndex": 17,
        "mint": "So11111111111111111111111111111111111111112",
        "owner": "HTvjzsfX3yU6BUodCjZ5vZkUrAxMDTrBs3CJaq43ashR",
        "programId": "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA",
        "uiTokenAmount": {
          "amount": "41863679951",
          "decimals": 9,
          "uiAmount": 41.863679951,
          "uiAmountString": "41.863679951"
        }
      },
      {
        "accountIndex": 18,
        "mint": "EPjFWdd5AufqSSqeM2qN1xzybapC8G4wEGGkZwyTDt1v",
        "owner": "HTvjzsfX3yU6BUodCjZ5vZkUrAxMDTrBs3CJaq43ashR",
        "programId": "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA",
        "uiTokenAmount": {
          "amount": "13614374037",
          "decimals": 6,
          "uiAmount": 13614.374037,
          "uiAmountString": "13614.374037"
        }
      }
    ],
    "preBalances": [
      1011680923,
      0,
      2039280,
      2039280,
      6676315,
      71437440,
      71437440,
      71437440,
      4078560,
      1,
      731913600,
      1,
      934087680,
      1141440,
      2033060000,
      0,
      7182720,
      41914826824,
      2039280,
      23385600,
      491386727358,
      242683120916,
      1141440,
      0
    ],
    "preTokenBalances": [
      {
        "accountIndex": 2,
        "mint": "EPjFWdd5AufqSSqeM2qN1xzybapC8G4wEGGkZwyTDt1v",
        "owner": "AmDp48FLbRN76uQHG9rEjozoc28TJNcVGrvDEJvDuWBa",
        "programId": "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA",
        "uiTokenAmount": {
          "amount": "9938497",
          "decimals": 6,
          "uiAmount": 9.938497,
          "uiAmountString": "9.938497"
        }
      },
      {
        "accountIndex": 3,
        "mint": "EPjFWdd5AufqSSqeM2qN1xzybapC8G4wEGGkZwyTDt1v",
        "owner": "6U91aKa8pmMxkJwBCfPTmUEfZi6dHe7DcFq2ALvB2tbB",
        "programId": "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA",
        "uiTokenAmount": {
          "amount": "40309742",
          "decimals": 6,
          "uiAmount": 40.309742,
          "uiAmountString": "40.309742"
        }
      },
      {
        "accountIndex": 4,
        "mint": "So11111111111111111111111111111111111111112",
        "owner": "6U91aKa8pmMxkJwBCfPTmUEfZi6dHe7DcFq2ALvB2tbB",
        "programId": "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA",
        "uiTokenAmount": {
          "amount": "4637035",
          "decimals": 9,
          "uiAmount": 0.004637035,
          "uiAmountString": "0.004637035"
        }
      },
      {
        "accountIndex": 17,
        "mint": "So11111111111111111111111111111111111111112",
        "owner": "HTvjzsfX3yU6BUodCjZ5vZkUrAxMDTrBs3CJaq43ashR",
        "programId": "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA",
        "uiTokenAmount": {
          "amount": "41912787544",
          "decimals": 9,
          "uiAmount": 41.912787544,
          "uiAmountString": "41.912787544"
        }
      },
      {
        "accountIndex": 18,
        "mint": "EPjFWdd5AufqSSqeM2qN1xzybapC8G4wEGGkZwyTDt1v",
        "owner": "HTvjzsfX3yU6BUodCjZ5vZkUrAxMDTrBs3CJaq43ashR",
        "programId": "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA",
        "uiTokenAmount": {
          "amount": "13604435540",
          "decimals": 6,
          "uiAmount": 13604.43554,
          "uiAmountString": "13604.43554"
        }
      }
    ],
    "rewards": [],
    "status": {
      "Ok": null
    }
  },
  "slot": 254956897,
  "transaction": {
    "message": {
      "accountKeys": [
        {
          "pubkey": "AmDp48FLbRN76uQHG9rEjozoc28TJNcVGrvDEJvDuWBa",
          "signer": true,
          "source": "transaction",
          "writable": true
        },
        {
          "pubkey": "DUHSGV3w85352tSa6ucWNddco7vFqgHzQmYkeuYFCbcd",
          "signer": false,
          "source": "transaction",
          "writable": true
        },
        {
          "pubkey": "5eUrKE7F3dKHvTPZqpsK6r2QfTxT6BvwDwbxKEeL2QA9",
          "signer": false,
          "source": "transaction",
          "writable": true
        },
        {
          "pubkey": "59v2cSbCsnyaWymLnsq6TWzE6cEN5KJYNTBNrcP4smRH",
          "signer": false,
          "source": "transaction",
          "writable": true
        },
        {
          "pubkey": "7x4VcEX8aLd3kFsNWULTp1qFgVtDwyWSxpTGQkoMM6XX",
          "signer": false,
          "source": "transaction",
          "writable": true
        },
        {
          "pubkey": "dMDeK1jxR52iSMvR9gqmjchkXFAbEcDa7mPmsxaePdb",
          "signer": false,
          "source": "transaction",
          "writable": true
        },
        {
          "pubkey": "DF6A3F6ViTFAnndsrVPS4ew2CrWujYifpaS2rrL73ege",
          "signer": false,
          "source": "transaction",
          "writable": true
        },
        {
          "pubkey": "2DBZwgVP2faWTMq5nJWqZXUg3BRn3FYo9AtwUyRuALud",
          "signer": false,
          "source": "transaction",
          "writable": true
        },
        {
          "pubkey": "8xnhU7R4b7mNPohdVHvXbwToeJ5RTCDL8ghSCxLz9XjG",
          "signer": false,
          "source": "transaction",
          "writable": true
        },
        {
          "pubkey": "ComputeBudget111111111111111111111111111111",
          "signer": false,
          "source": "transaction",
          "writable": false
        },
        {
          "pubkey": "ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL",
          "signer": false,
          "source": "transaction",
          "writable": false
        },
        {
          "pubkey": "11111111111111111111111111111111",
          "signer": false,
          "source": "transaction",
          "writable": false
        },
        {
          "pubkey": "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA",
          "signer": false,
          "source": "transaction",
          "writable": false
        },
        {
          "pubkey": "JUP6LkbZbjS1jKKwapdHNy74zcZ3tLUZoi5QNyVTaV4",
          "signer": false,
          "source": "transaction",
          "writable": false
        },
        {
          "pubkey": "6U91aKa8pmMxkJwBCfPTmUEfZi6dHe7DcFq2ALvB2tbB",
          "signer": false,
          "source": "transaction",
          "writable": false
        },
        {
          "pubkey": "D8cy77BBepLMngZx6ZukaTff5hCt1HrWyKk3Hnd9oitf",
          "signer": false,
          "source": "transaction",
          "writable": false
        },
        {
          "pubkey": "HTvjzsfX3yU6BUodCjZ5vZkUrAxMDTrBs3CJaq43ashR",
          "signer": false,
          "source": "lookupTable",
          "writable": true
        },
        {
          "pubkey": "H7j5NPopj3tQvDg4N8CxwtYciTn3e8AEV6wSVrxpyDUc",
          "signer": false,
          "source": "lookupTable",
          "writable": true
        },
        {
          "pubkey": "HbYjRzx7teCxqW3unpXBEcNHhfVZvW2vW9MQ99TkizWt",
          "signer": false,
          "source": "lookupTable",
          "writable": true
        },
        {
          "pubkey": "EgEYXef2FCoEYLHJJW74dMbom1atLXo6KwPuA6mSATYA",
          "signer": false,
          "source": "lookupTable",
          "writable": true
        },
        {
          "pubkey": "So11111111111111111111111111111111111111112",
          "signer": false,
          "source": "lookupTable",
          "writable": false
        },
        {
          "pubkey": "EPjFWdd5AufqSSqeM2qN1xzybapC8G4wEGGkZwyTDt1v",
          "signer": false,
          "source": "lookupTable",
          "writable": false
        },
        {
          "pubkey": "LBUZKhRxPF3XUpBCjp4YzTKgLccjZhTSDM9YuVaPwxo",
          "signer": false,
          "source": "lookupTable",
          "writable": false
        },
        {
          "pubkey": "D1ZN9Wj1fRSUQfCjhvnu1hqDMT7hzjzBBpi12nVniYD6",
          "signer": false,
          "source": "lookupTable",
          "writable": false
        }
      ],
      "addressTableLookups": [
        {
          "accountKey": "8kejqRVs9JsHrXtUvVwqHsXkgAnd34WceNDieRQyJ1w4",
          "readonlyIndexes": [
            112,
            209,
            214,
            212
          ],
          "writableIndexes": [
            215,
            210,
            211,
            206
          ]
        }
      ],
      "instructions": [
        {
          "accounts": [],
          "data": "3ucy3QGhYkVd",
          "programId": "ComputeBudget111111111111111111111111111111",
          "stackHeight": null
        },
        {
          "parsed": {
            "info": {
              "account": "DUHSGV3w85352tSa6ucWNddco7vFqgHzQmYkeuYFCbcd",
              "mint": "So11111111111111111111111111111111111111112",
              "source": "AmDp48FLbRN76uQHG9rEjozoc28TJNcVGrvDEJvDuWBa",
              "systemProgram": "11111111111111111111111111111111",
              "tokenProgram": "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA",
              "wallet": "AmDp48FLbRN76uQHG9rEjozoc28TJNcVGrvDEJvDuWBa"
            },
            "type": "createIdempotent"
          },
          "program": "spl-associated-token-account",
          "programId": "ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL",
          "stackHeight": null
        },
        {
          "accounts": [
            "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA",
            "6U91aKa8pmMxkJwBCfPTmUEfZi6dHe7DcFq2ALvB2tbB",
            "AmDp48FLbRN76uQHG9rEjozoc28TJNcVGrvDEJvDuWBa",
            "5eUrKE7F3dKHvTPZqpsK6r2QfTxT6BvwDwbxKEeL2QA9",
            "59v2cSbCsnyaWymLnsq6TWzE6cEN5KJYNTBNrcP4smRH",
            "7x4VcEX8aLd3kFsNWULTp1qFgVtDwyWSxpTGQkoMM6XX",
            "DUHSGV3w85352tSa6ucWNddco7vFqgHzQmYkeuYFCbcd",
            "EPjFWdd5AufqSSqeM2qN1xzybapC8G4wEGGkZwyTDt1v",
            "So11111111111111111111111111111111111111112",
            "JUP6LkbZbjS1jKKwapdHNy74zcZ3tLUZoi5QNyVTaV4",
            "JUP6LkbZbjS1jKKwapdHNy74zcZ3tLUZoi5QNyVTaV4",
            "D8cy77BBepLMngZx6ZukaTff5hCt1HrWyKk3Hnd9oitf",
            "JUP6LkbZbjS1jKKwapdHNy74zcZ3tLUZoi5QNyVTaV4",
            "LBUZKhRxPF3XUpBCjp4YzTKgLccjZhTSDM9YuVaPwxo",
            "HTvjzsfX3yU6BUodCjZ5vZkUrAxMDTrBs3CJaq43ashR",
            "LBUZKhRxPF3XUpBCjp4YzTKgLccjZhTSDM9YuVaPwxo",
            "H7j5NPopj3tQvDg4N8CxwtYciTn3e8AEV6wSVrxpyDUc",
            "HbYjRzx7teCxqW3unpXBEcNHhfVZvW2vW9MQ99TkizWt",
            "59v2cSbCsnyaWymLnsq6TWzE6cEN5KJYNTBNrcP4smRH",
            "7x4VcEX8aLd3kFsNWULTp1qFgVtDwyWSxpTGQkoMM6XX",
            "So11111111111111111111111111111111111111112",
            "EPjFWdd5AufqSSqeM2qN1xzybapC8G4wEGGkZwyTDt1v",
            "EgEYXef2FCoEYLHJJW74dMbom1atLXo6KwPuA6mSATYA",
            "LBUZKhRxPF3XUpBCjp4YzTKgLccjZhTSDM9YuVaPwxo",
            "6U91aKa8pmMxkJwBCfPTmUEfZi6dHe7DcFq2ALvB2tbB",
            "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA",
            "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA",
            "D1ZN9Wj1fRSUQfCjhvnu1hqDMT7hzjzBBpi12nVniYD6",
            "LBUZKhRxPF3XUpBCjp4YzTKgLccjZhTSDM9YuVaPwxo",
            "dMDeK1jxR52iSMvR9gqmjchkXFAbEcDa7mPmsxaePdb",
            "DF6A3F6ViTFAnndsrVPS4ew2CrWujYifpaS2rrL73ege",
            "2DBZwgVP2faWTMq5nJWqZXUg3BRn3FYo9AtwUyRuALud",
            "JUP6LkbZbjS1jKKwapdHNy74zcZ3tLUZoi5QNyVTaV4"
          ],
          "data": "2U4BQZ7jhoZZJr4A1Gk14sJRw4HjK2MyHfqWqdbvFSu9eZgWb1",
          "programId": "JUP6LkbZbjS1jKKwapdHNy74zcZ3tLUZoi5QNyVTaV4",
          "stackHeight": null
        },
        {
          "parsed": {
            "info": {
              "account": "DUHSGV3w85352tSa6ucWNddco7vFqgHzQmYkeuYFCbcd",
              "destination": "AmDp48FLbRN76uQHG9rEjozoc28TJNcVGrvDEJvDuWBa",
              "owner": "AmDp48FLbRN76uQHG9rEjozoc28TJNcVGrvDEJvDuWBa"
            },
            "type": "closeAccount"
          },
          "program": "spl-token",
          "programId": "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA",
          "stackHeight": null
        },
        {
          "parsed": {
            "info": {
              "destination": "8xnhU7R4b7mNPohdVHvXbwToeJ5RTCDL8ghSCxLz9XjG",
              "lamports": 1000000,
              "source": "AmDp48FLbRN76uQHG9rEjozoc28TJNcVGrvDEJvDuWBa"
            },
            "type": "transfer"
          },
          "program": "system",
          "programId": "11111111111111111111111111111111",
          "stackHeight": null
        }
      ],
      "recentBlockhash": "8asxErtoXtSFkuErv2RVWaSDoeRJFehSU2a7LSx4ryhe"
    },
    "signatures": [
      "435cogDHkQZXaqK5Vr9mPrk6vYiNS8eGCe5fe2ar4wABMzBUEzgLXHtKxLoqbUbBtj3PCX5iW9xiGk6k8YwaLinD"
    ]
  },
  "version": 0
}
 */