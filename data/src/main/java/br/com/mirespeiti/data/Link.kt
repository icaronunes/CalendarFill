package br.com.mirespeiti.data


import com.google.gson.annotations.SerializedName

data class Link(
    @SerializedName("type")
    val type: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("suggested_link_text")
    val suggestedLinkText: String
)