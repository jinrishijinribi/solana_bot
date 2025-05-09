package com.rich.sol_bot.sol.pump;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PumpInfo {
    private String mint;
    private String name;
    private String symbol;
    private String description;
    @SerializedName(value = "image_uri")
    private String imageUri;
    @SerializedName(value = "metadata_uri")
    private String metadataUri;
    private String twitter;
    private String telegram;
    @SerializedName(value = "bonding_curve")
    private String bondingCurve;
    @SerializedName(value = "associated_bonding_curve")
    private String associatedBondingCurve;
    private String creator;
    @SerializedName(value = "created_timestamp")
    private Long createdTimestamp;
    @SerializedName(value = "raydium_pool")
    private String raydiumPool;
    private Boolean complete;
    @SerializedName(value = "virtual_sol_reserves")
    private String virtualSolReserves;
    @SerializedName(value = "virtual_token_reserves")
    private String virtualTokenReserves;
    @SerializedName(value = "total_supply")
    private String totalSupply;
    private String website;
    private String showName;
    @SerializedName(value = "king_of_the_hil_timestamp")
    private String kingOfTheHillTimestamp;
    @SerializedName(value = "market_cap")
    private String marketCap;
    @SerializedName(value = "reply_count")
    private String replyCount;
    @SerializedName(value = "last_reply")
    private String lastReply;
    private String nsfw;
    @SerializedName(value = "market_id")
    private String marketId;
    private String inverted;
    private String username;
    @SerializedName(value = "profile_image")
    private String profileImage;
    @SerializedName(value = "usd_market_cap")
    private String usdMarketCap;
}
