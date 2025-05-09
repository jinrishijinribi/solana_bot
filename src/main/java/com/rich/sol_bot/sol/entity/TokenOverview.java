package com.rich.sol_bot.sol.entity;


/**
 * @author wangqiyun
 * @since 2024/3/17 15:22
 */

/* 币USDC的返回示例
    {
  "data": {
    "address": "EPjFWdd5AufqSSqeM2qN1xzybapC8G4wEGGkZwyTDt1v",
    "decimals": 6,
    "symbol": "USDC",
    "name": "USD Coin",
    "extensions": {
      "coingeckoId": "usd-coin",
      "serumV3Usdt": "77quYg4MGneUdjgXCunt9GgM1usmrxKY31twEy3WHwcS",
      "website": "https://www.circle.com/en/usdc",
      "telegram": null,
      "twitter": "https://twitter.com/circle",
      "description": "USDC is a fully collateralized US dollar stablecoin. USDC is the bridge between dollars and trading on cryptocurrency exchanges. The technology behind CENTRE makes it possible to exchange value between people, businesses and financial institutions just like email between mail services and texts between SMS providers. We believe by removing artificial economic borders, we can create a more inclusive global economy.",
      "github": "https://github.com/centrehq/centre-tokens",
      "medium": "https://medium.com/centre-blog",
      "discord": "https://discord.com/invite/buildoncircle"
    },
    "logoURI": "https://img.fotofolio.xyz/?url=https%3A%2F%2Fraw.githubusercontent.com%2Fsolana-labs%2Ftoken-list%2Fmain%2Fassets%2Fmainnet%2FEPjFWdd5AufqSSqeM2qN1xzybapC8G4wEGGkZwyTDt1v%2Flogo.png",
    "liquidity": 30364096958.058765,
    "price": 1,
    "history30mPrice": 1.00003557,
    "priceChange30mPercent": -0.0015059464335031312,
    "history1hPrice": 0.99996259,
    "priceChange1hPercent": 0.005792216686819612,
    "history2hPrice": 1.00002274,
    "priceChange2hPercent": -0.00022299492909450848,
    "history4hPrice": 1.00010657,
    "priceChange4hPercent": -0.008605082956320339,
    "history6hPrice": 0.99995953,
    "priceChange6hPercent": 0.00609824679603556,
    "history8hPrice": 0.99999842,
    "priceChange8hPercent": 0.002209003490211875,
    "history12hPrice": 0.9999162,
    "priceChange12hPercent": 0.010431874191045742,
    "history24hPrice": 1.00007209,
    "priceChange24hPercent": -0.005157628186589132,
    "uniqueWallet30m": 8208,
    "uniqueWalletHistory30m": 8395,
    "uniqueWallet30mChangePercent": -2.2275163787969032,
    "uniqueWallet1h": 14257,
    "uniqueWalletHistory1h": 13885,
    "uniqueWallet1hChangePercent": 2.6791501620453726,
    "uniqueWallet2h": 23775,
    "uniqueWalletHistory2h": 20167,
    "uniqueWallet2hChangePercent": 17.890613378291267,
    "uniqueWallet4h": 37219,
    "uniqueWalletHistory4h": 38664,
    "uniqueWallet4hChangePercent": -3.7373267121870475,
    "uniqueWallet6h": 51260,
    "uniqueWalletHistory6h": 59178,
    "uniqueWallet6hChangePercent": -13.379972286998546,
    "uniqueWallet8h": 65406,
    "uniqueWalletHistory8h": 84206,
    "uniqueWallet8hChangePercent": -22.326200033251787,
    "uniqueWallet12h": 95437,
    "uniqueWalletHistory12h": 121785,
    "uniqueWallet12hChangePercent": -21.634848298230487,
    "uniqueWallet24h": 182854,
    "uniqueWalletHistory24h": 167795,
    "uniqueWallet24hChangePercent": 8.974641675854466,
    "lastTradeUnixTime": 1710658923,
    "lastTradeHumanTime": "2024-03-17T07:02:03.000Z",
    "supply": 2221907808.9524,
    "mc": 2221953380.281562,
    "trade30m": 69136,
    "tradeHistory30m": 62356,
    "trade30mChangePercent": 10.873051510680607,
    "sell30m": 34794,
    "sellHistory30m": 31353,
    "sell30mChangePercent": 10.975026313271458,
    "buy30m": 34342,
    "buyHistory30m": 31003,
    "buy30mChangePercent": 10.769925491081509,
    "v30m": 60094313.99180099,
    "v30mUSD": 60094756.53875305,
    "vHistory30m": 49143660.493754,
    "vHistory30mUSD": 49144064.23143569,
    "v30mChangePercent": 22.282942271747924,
    "vBuy30m": 31796398.942957,
    "vBuy30mUSD": 31796645.0457204,
    "vBuyHistory30m": 24914075.546491,
    "vBuyHistory30mUSD": 24914267.128997084,
    "vBuy30mChangePercent": 27.624237486248344,
    "vSell30m": 28297915.048844,
    "vSell30mUSD": 28298111.49303265,
    "vSellHistory30m": 24229584.947263,
    "vSellHistory30mUSD": 24229797.102438603,
    "vSell30mChangePercent": 16.790754403907208,
    "trade1h": 131580,
    "tradeHistory1h": 121943,
    "trade1hChangePercent": 7.902872653616853,
    "sell1h": 66191,
    "sellHistory1h": 63377,
    "sell1hChangePercent": 4.440096564999921,
    "buy1h": 65389,
    "buyHistory1h": 58566,
    "buy1hChangePercent": 11.650104155994946,
    "v1h": 109395972.863834,
    "v1hUSD": 109396835.54872972,
    "vHistory1h": 77607368.772277,
    "vHistory1hUSD": 77608221.68293801,
    "v1hChangePercent": 40.960806421403326,
    "vBuy1h": 56818371.206301,
    "vBuy1hUSD": 56818818.448381774,
    "vBuyHistory1h": 38011310.057474,
    "vBuyHistory1hUSD": 38011717.04101116,
    "vBuy1hChangePercent": 49.477540027929265,
    "vSell1h": 52577601.657533,
    "vSell1hUSD": 52578017.100347936,
    "vSellHistory1h": 39596058.714803,
    "vSellHistory1hUSD": 39596504.641926855,
    "vSell1hChangePercent": 32.78493709748147,
    "trade2h": 253586,
    "tradeHistory2h": 213105,
    "trade2hChangePercent": 18.99580019239342,
    "sell2h": 129614,
    "sellHistory2h": 108879,
    "sell2hChangePercent": 19.044076451841036,
    "buy2h": 123972,
    "buyHistory2h": 104226,
    "buy2hChangePercent": 18.94536871797824,
    "v2h": 187605669.040481,
    "v2hUSD": 187607426.90575913,
    "vHistory2h": 139119003.22516102,
    "vHistory2hUSD": 139123400.5287103,
    "v2hChangePercent": 34.85265469940536,
    "vBuy2h": 95137555.291912,
    "vBuy2hUSD": 95138416.13001545,
    "vBuyHistory2h": 70575197.284975,
    "vBuyHistory2hUSD": 70577400.77439956,
    "vBuy2hChangePercent": 34.803102154651945,
    "vSell2h": 92468113.748569,
    "vSell2hUSD": 92469010.77574368,
    "vSellHistory2h": 68543805.940186,
    "vSellHistory2hUSD": 68545999.75431073,
    "vSell2hChangePercent": 34.903675802975236,
    "trade4h": 466733,
    "tradeHistory4h": 544326,
    "trade4hChangePercent": -14.254876673170122,
    "sell4h": 238514,
    "sellHistory4h": 279405,
    "sell4hChangePercent": -14.635028005941198,
    "buy4h": 228219,
    "buyHistory4h": 264921,
    "buy4hChangePercent": -13.853941363651806,
    "v4h": 326758389.18964005,
    "v4hUSD": 326764544.04691267,
    "vHistory4h": 438145672.07447004,
    "vHistory4hUSD": 438144394.6735126,
    "v4hChangePercent": -25.422431393068262,
    "vBuy4h": 165746972.712408,
    "vBuy4hUSD": 165750036.8487445,
    "vBuyHistory4h": 215051072.259763,
    "vBuyHistory4hUSD": 215050296.01304388,
    "vBuy4hChangePercent": -22.92669319397763,
    "vSell4h": 161011416.477232,
    "vSell4hUSD": 161014507.19816813,
    "vSellHistory4h": 223094599.814707,
    "vSellHistory4hUSD": 223094098.6604687,
    "vSell4hChangePercent": -27.828187409752942,
    "trade6h": 712551,
    "tradeHistory6h": 838144,
    "trade6hChangePercent": -14.984656574526573,
    "sell6h": 365372,
    "sellHistory6h": 426113,
    "sell6hChangePercent": -14.254669536015093,
    "buy6h": 347179,
    "buyHistory6h": 412031,
    "buy6hChangePercent": -15.739592409309008,
    "v6h": 487982104.71381295,
    "v6hUSD": 487990168.78409123,
    "vHistory6h": 748514790.862879,
    "vHistory6hUSD": 748508283.705138,
    "v6hChangePercent": -34.80661829657729,
    "vBuy6h": 244224276.730137,
    "vBuy6hUSD": 244228254.35525706,
    "vBuyHistory6h": 376836288.525604,
    "vBuyHistory6hUSD": 376832880.9645573,
    "vBuy6hChangePercent": -35.19088151364615,
    "vSell6h": 243757827.983676,
    "vSell6hUSD": 243761914.42883417,
    "vSellHistory6h": 371678502.337275,
    "vSellHistory6hUSD": 371675402.7405807,
    "vSell6hChangePercent": -34.41702265511149,
    "trade8h": 1011313,
    "tradeHistory8h": 1111886,
    "trade8hChangePercent": -9.045261834396692,
    "sell8h": 518132,
    "sellHistory8h": 562040,
    "sell8hChangePercent": -7.812255355490712,
    "buy8h": 493181,
    "buyHistory8h": 549846,
    "buy8hChangePercent": -10.305612844323683,
    "v8h": 765885180.817294,
    "v8hUSD": 765890048.5347857,
    "vHistory8h": 1037851910.6522839,
    "vHistory8hUSD": 1037839736.6456242,
    "v8hChangePercent": -26.20477228432912,
    "vBuy8h": 381195473.256581,
    "vBuy8hUSD": 381197757.43141776,
    "vBuyHistory8h": 529267502.050482,
    "vBuyHistory8hUSD": 529261237.2563542,
    "vBuy8hChangePercent": -27.976784559838276,
    "vSell8h": 384689707.560713,
    "vSell8hUSD": 384692291.10336804,
    "vSellHistory8h": 508584408.601802,
    "vSellHistory8hUSD": 508578499.38926995,
    "vSell8hChangePercent": -24.360695873807803,
    "trade12h": 1549828,
    "tradeHistory12h": 1632241,
    "trade12hChangePercent": -5.049070572299066,
    "sell12h": 790919,
    "sellHistory12h": 828334,
    "sell12hChangePercent": -4.516897773120505,
    "buy12h": 758909,
    "buyHistory12h": 803907,
    "buy12hChangePercent": -5.597413631178731,
    "v12h": 1236178890.149184,
    "v12hUSD": 1236180499.0871139,
    "vHistory12h": 1431028726.909961,
    "vHistory12hUSD": 1431019439.6857166,
    "v12hChangePercent": -13.616067455299714,
    "vBuy12h": 621156622.448359,
    "vBuy12hUSD": 621157226.9642547,
    "vBuyHistory12h": 720755121.430897,
    "vBuyHistory12hUSD": 720750264.8868632,
    "vBuy12hChangePercent": -13.818632156897836,
    "vSell12h": 615022267.700825,
    "vSell12hUSD": 615023272.1228591,
    "vSellHistory12h": 710273605.479064,
    "vSellHistory12hUSD": 710269174.7988535,
    "vSell12hChangePercent": -13.4105135040171,
    "trade24h": 3182363,
    "tradeHistory24h": 3066754,
    "trade24hChangePercent": 3.7697513396901092,
    "sell24h": 1619462,
    "sellHistory24h": 1593390,
    "sell24hChangePercent": 1.6362597982916924,
    "buy24h": 1562901,
    "buyHistory24h": 1473364,
    "buy24hChangePercent": 6.077045455162472,
    "v24h": 2667306306.687799,
    "v24hUSD": 2667298555.0931263,
    "vHistory24h": 2533254922.9115124,
    "vHistory24hUSD": 2533298423.8351316,
    "v24hChangePercent": 5.291665776068802,
    "vBuy24h": 1341845186.077161,
    "vBuy24hUSD": 1341840899.119189,
    "vBuyHistory24h": 1261085350.941328,
    "vBuyHistory24hUSD": 1261106407.3818862,
    "vBuy24hChangePercent": 6.403994390669151,
    "vSell24h": 1325461120.610638,
    "vSell24hUSD": 1325457655.973937,
    "vSellHistory24h": 1272169571.970184,
    "vSellHistory24hUSD": 1272192016.4532456,
    "vSell24hChangePercent": 4.189028712416241,
    "watch": null,
    "view30m": 0,
    "viewHistory30m": 0,
    "view30mChangePercent": null,
    "view1h": 0,
    "viewHistory1h": 0,
    "view1hChangePercent": null,
    "view2h": 0,
    "viewHistory2h": 0,
    "view2hChangePercent": null,
    "view4h": 0,
    "viewHistory4h": 0,
    "view4hChangePercent": null,
    "view6h": 0,
    "viewHistory6h": 0,
    "view6hChangePercent": null,
    "view8h": 0,
    "viewHistory8h": 0,
    "view8hChangePercent": null,
    "view12h": 0,
    "viewHistory12h": 0,
    "view12hChangePercent": null,
    "view24h": 0,
    "viewHistory24h": 0,
    "view24hChangePercent": null,
    "uniqueView30m": 0,
    "uniqueViewHistory30m": 0,
    "uniqueView30mChangePercent": null,
    "uniqueView1h": 0,
    "uniqueViewHistory1h": 0,
    "uniqueView1hChangePercent": null,
    "uniqueView2h": 0,
    "uniqueViewHistory2h": 0,
    "uniqueView2hChangePercent": null,
    "uniqueView4h": 0,
    "uniqueViewHistory4h": 0,
    "uniqueView4hChangePercent": null,
    "uniqueView6h": 0,
    "uniqueViewHistory6h": 0,
    "uniqueView6hChangePercent": null,
    "uniqueView8h": 0,
    "uniqueViewHistory8h": 0,
    "uniqueView8hChangePercent": null,
    "uniqueView12h": 0,
    "uniqueViewHistory12h": 0,
    "uniqueView12hChangePercent": null,
    "uniqueView24h": 0,
    "uniqueViewHistory24h": 0,
    "uniqueView24hChangePercent": null,
    "numberMarkets": 26248
  },
  "success": true
}
*/

@lombok.Data
public class TokenOverview {
    private Boolean success;
    private Data data;

    @lombok.Data
    public static class Data {
        private String address;//币地址
        private Integer decimals;//币小数位数
        private String symbol;//币简称
        private String name;//币全称
        private Extensions extensions;//币的扩展信息/媒体信息
        private String logoURI;//币logo
        private String liquidity;//币的发行量
        private String price;//币价格
        private String history30mPrice;
        private String priceChange30mPercent;
        private String history1hPrice;
        private String priceChange1hPercent;
        private String history2hPrice;
        private String priceChange2hPercent;
        private String history4hPrice;
        private String priceChange4hPercent;
        private String history6hPrice;
        private String priceChange6hPercent;
        private String history8hPrice;
        private String priceChange8hPercent;
        private String history12hPrice;
        private String priceChange12hPercent;
        private String history24hPrice;
        private String priceChange24hPercent;
        private String uniqueWallet30m;
        private String uniqueWalletHistory30m;
        private String uniqueWallet30mChangePercent;
        private String uniqueWallet1h;
        private String uniqueWalletHistory1h;
        private String uniqueWallet1hChangePercent;
        private String uniqueWallet2h;
        private String uniqueWalletHistory2h;
        private String uniqueWallet2hChangePercent;
        private String uniqueWallet4h;
        private String uniqueWalletHistory4h;
        private String uniqueWallet4hChangePercent;
        private String uniqueWallet6h;
        private String uniqueWalletHistory6h;
        private String uniqueWallet6hChangePercent;
        private String uniqueWallet8h;
        private String uniqueWalletHistory8h;
        private String uniqueWallet8hChangePercent;
        private String uniqueWallet12h;
        private String uniqueWalletHistory12h;
        private String uniqueWallet12hChangePercent;
        private String uniqueWallet24h;
        private String uniqueWalletHistory24h;
        private String uniqueWallet24hChangePercent;
        private String lastTradeUnixTime;
        private String lastTradeHumanTime;
        private String supply;
        private String mc;
        private String trade30m;
        private String tradeHistory30m;
        private String trade30mChangePercent;
        private String sell30m;
        private String sellHistory30m;
        private String sell30mChangePercent;
        private String buy30m;
        private String buyHistory30m;
        private String buy30mChangePercent;
        private String v30m;
        private String v30mUSD;
        private String vHistory30m;
        private String vHistory30mUSD;
        private String v30mChangePercent;
        private String vBuy30m;
        private String vBuy30mUSD;
        private String vBuyHistory30m;
        private String vBuyHistory30mUSD;
        private String vBuy30mChangePercent;
        private String vSell30m;
        private String vSell30mUSD;
        private String vSellHistory30m;
        private String vSellHistory30mUSD;
        private String vSell30mChangePercent;
        private String trade1h;
        private String tradeHistory1h;
        private String trade1hChangePercent;
        private String sell1h;
        private String sellHistory1h;
        private String sell1hChangePercent;
        private String buy1h;
        private String buyHistory1h;
        private String buy1hChangePercent;
        private String v1h;
        private String v1hUSD;
        private String vHistory1h;
        private String vHistory1hUSD;
        private String v1hChangePercent;
        private String vBuy1h;
        private String vBuy1hUSD;
        private String vBuyHistory1h;
        private String vBuyHistory1hUSD;
        private String vBuy1hChangePercent;
        private String vSell1h;
        private String vSell1hUSD;
        private String vSellHistory1h;
        private String vSellHistory1hUSD;
        private String vSell1hChangePercent;
        private String trade2h;
        private String tradeHistory2h;
        private String trade2hChangePercent;
        private String sell2h;
        private String sellHistory2h;
        private String sell2hChangePercent;
        private String buy2h;
        private String buyHistory2h;
        private String buy2hChangePercent;
        private String v2h;
        private String v2hUSD;
        private String vHistory2h;
        private String vHistory2hUSD;
        private String v2hChangePercent;
        private String vBuy2h;
        private String vBuy2hUSD;
        private String vBuyHistory2h;
        private String vBuyHistory2hUSD;
        private String vBuy2hChangePercent;
        private String vSell2h;
        private String vSell2hUSD;
        private String vSellHistory2h;
        private String vSellHistory2hUSD;
        private String vSell2hChangePercent;
        private String trade4h;
        private String tradeHistory4h;
        private String trade4hChangePercent;
        private String sell4h;
        private String sellHistory4h;
        private String sell4hChangePercent;
        private String buy4h;
        private String buyHistory4h;
        private String buy4hChangePercent;
        private String v4h;
        private String v4hUSD;
        private String vHistory4h;
        private String vHistory4hUSD;
        private String v4hChangePercent;
        private String vBuy4h;
        private String vBuy4hUSD;
        private String vBuyHistory4h;
        private String vBuyHistory4hUSD;
        private String vBuy4hChangePercent;
        private String vSell4h;
        private String vSell4hUSD;
        private String vSellHistory4h;
        private String vSellHistory4hUSD;
        private String vSell4hChangePercent;
        private String trade6h;
        private String tradeHistory6h;
        private String trade6hChangePercent;
        private String sell6h;
        private String sellHistory6h;
        private String sell6hChangePercent;
        private String buy6h;
        private String buyHistory6h;
        private String buy6hChangePercent;
        private String v6h;
        private String v6hUSD;
        private String vHistory6h;
        private String vHistory6hUSD;
        private String v6hChangePercent;
        private String vBuy6h;
        private String vBuy6hUSD;
        private String vBuyHistory6h;
        private String vBuyHistory6hUSD;
        private String vBuy6hChangePercent;
        private String vSell6h;
        private String vSell6hUSD;
        private String vSellHistory6h;
        private String vSellHistory6hUSD;
        private String vSell6hChangePercent;
        private String trade8h;
        private String tradeHistory8h;
        private String trade8hChangePercent;
        private String sell8h;
        private String sellHistory8h;
        private String sell8hChangePercent;
        private String buy8h;
        private String buyHistory8h;
        private String buy8hChangePercent;
        private String v8h;
        private String v8hUSD;
        private String vHistory8h;
        private String vHistory8hUSD;
        private String v8hChangePercent;
        private String vBuy8h;
        private String vBuy8hUSD;
        private String vBuyHistory8h;
        private String vBuyHistory8hUSD;
        private String vBuy8hChangePercent;
        private String vSell8h;
        private String vSell8hUSD;
        private String vSellHistory8h;
        private String vSellHistory8hUSD;
        private String vSell8hChangePercent;
        private String trade12h;
        private String tradeHistory12h;
        private String trade12hChangePercent;
        private String sell12h;
        private String sellHistory12h;
        private String sell12hChangePercent;
        private String buy12h;
        private String buyHistory12h;
        private String buy12hChangePercent;
        private String v12h;
        private String v12hUSD;
        private String vHistory12h;
        private String vHistory12hUSD;
        private String v12hChangePercent;
        private String vBuy12h;
        private String vBuy12hUSD;
        private String vBuyHistory12h;
        private String vBuyHistory12hUSD;
        private String vBuy12hChangePercent;
        private String vSell12h;
        private String vSell12hUSD;
        private String vSellHistory12h;
        private String vSellHistory12hUSD;
        private String vSell12hChangePercent;
        private String trade24h;
        private String tradeHistory24h;
        private String trade24hChangePercent;
        private String sell24h;
        private String sellHistory24h;
        private String sell24hChangePercent;
        private String buy24h;
        private String buyHistory24h;
        private String buy24hChangePercent;
        private String v24h;
        private String v24hUSD;
        private String vHistory24h;
        private String vHistory24hUSD;
        private String v24hChangePercent;
        private String vBuy24h;
        private String vBuy24hUSD;
        private String vBuyHistory24h;
        private String vBuyHistory24hUSD;
        private String vBuy24hChangePercent;
        private String vSell24h;
        private String vSell24hUSD;
        private String vSellHistory24h;
        private String vSellHistory24hUSD;
        private String vSell24hChangePercent;
        private String watch;
        private String view30m;
        private String viewHistory30m;
        private String view30mChangePercent;
        private String view1h;
        private String viewHistory1h;
        private String view1hChangePercent;
        private String view2h;
        private String viewHistory2h;
        private String view2hChangePercent;
        private String view4h;
        private String viewHistory4h;
        private String view4hChangePercent;
        private String view6h;
        private String viewHistory6h;
        private String view6hChangePercent;
        private String view8h;
        private String viewHistory8h;
        private String view8hChangePercent;
        private String view12h;
        private String viewHistory12h;
        private String view12hChangePercent;
        private String view24h;
        private String viewHistory24h;
        private String view24hChangePercent;
        private String uniqueView30m;
        private String uniqueViewHistory30m;
        private String uniqueView30mChangePercent;
        private String uniqueView1h;
        private String uniqueViewHistory1h;
        private String uniqueView1hChangePercent;
        private String uniqueView2h;
        private String uniqueViewHistory2h;
        private String uniqueView2hChangePercent;
        private String uniqueView4h;
        private String uniqueViewHistory4h;
        private String uniqueView4hChangePercent;
        private String uniqueView6h;
        private String uniqueViewHistory6h;
        private String uniqueView6hChangePercent;
        private String uniqueView8h;
        private String uniqueViewHistory8h;
        private String uniqueView8hChangePercent;
        private String uniqueView12h;
        private String uniqueViewHistory12h;
        private String uniqueView12hChangePercent;
        private String uniqueView24h;
        private String uniqueViewHistory24h;
        private String uniqueView24hChangePercent;
        private String numberMarkets;
    }

    @lombok.Data
    public static class Extensions {
        private String coingeckoId;
        private String serumV3Usdt;
        private String website;
        private String telegram;
        private String twitter;
        private String description;
        private String github;
        private String medium;
        private String discord;
    }
}
