package com.karina.carawicara.data

import com.google.gson.annotations.SerializedName

data class PredictResponse(

    @field:SerializedName("data")
    val data: Data? = null,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status: String
)

data class Data(

    @field:SerializedName("confidence")
    val confidence: Float,

    @field:SerializedName("prediction")
    val prediction: String
)
