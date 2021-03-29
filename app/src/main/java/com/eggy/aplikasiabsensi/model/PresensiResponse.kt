package com.eggy.aplikasiabsensi.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PresensiResponse(

    @field:SerializedName("data")
    val data: List<HistoryItem?>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("isSuccess")
    val isSuccess: Boolean? = null
) : Parcelable

@Parcelize
data class HistoryItem(

    @field:SerializedName("device_name")
    val deviceName: String? = null,

    @field:SerializedName("user_id")
    val userId: String? = null,

    @field:SerializedName("waktu")
    val waktu: String? = null,

    @field:SerializedName("photo")
    val photo: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("status")
    val status: String? = null
) : Parcelable
