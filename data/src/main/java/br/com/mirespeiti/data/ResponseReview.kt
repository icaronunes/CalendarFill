package br.com.mirespeiti.data


import com.google.gson.annotations.SerializedName

data class ResponseReview(
    @SerializedName("status")
    val status: String,
    @SerializedName("copyright")
    val copyright: String,
    @SerializedName("has_more")
    val hasMore: Boolean,
    @SerializedName("num_results")
    val numResults: Int,
    @SerializedName("results")
    val results: List<Result>?
)