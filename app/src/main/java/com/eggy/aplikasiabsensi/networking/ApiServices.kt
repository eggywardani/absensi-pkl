package com.eggy.aplikasiabsensi.networking

import com.eggy.aplikasiabsensi.model.ActionResponse
import com.eggy.aplikasiabsensi.model.LoginResponse
import com.eggy.aplikasiabsensi.model.PresensiResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiServices {

    @FormUrlEncoded
    @POST("login.php")
    fun login(
        @Field("email") email:String,
        @Field("password") password:String
    ):Call<LoginResponse>

    @FormUrlEncoded
    @POST("register.php")
    fun register(
        @Field("nama") nama:String,
        @Field("jekel") jekel:String,
        @Field("alamat") alamat:String,
        @Field("asal_sekolah") sekolah:String,
        @Field("no_hp") no_hp:String,
        @Field("email") email:String,
        @Field("password") password:String
    ):Call<ActionResponse>



    @Multipart
    @POST("absensi.php")
    fun send(
        @Part ("user_id") userId:Int,
        @Part ("status") status:String,
        @Part("keterangan") ket:String,
        @Part ("device_name") deviceName:String,
        @Part ("waktu") waktu:String,
        @Part photo:MultipartBody.Part?
    ):Call<ActionResponse>

    @GET("getPresensiByUser.php")
    fun getPresensi(
        @Query("user_id") userId: Int,
        @Query("from_date") fromDate:String,
        @Query("to_date") toDate:String

    ):Call<PresensiResponse>

}