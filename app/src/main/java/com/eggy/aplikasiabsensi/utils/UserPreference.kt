package com.eggy.aplikasiabsensi.utils

import android.content.Context
import android.content.SharedPreferences

class UserPreference (context:Context){

    companion object{
        private const val PREFS_NAME = "user_prefs"

        private const val STATUS_USER = "status_user"
        private const val ID_USER = "id_user"
        private const val NAME_USER = "name_user"
        private const val JEKEL_USER = "jekel_user"
        private const val ALAMAT_USER = "alamat_user"
        private const val SEKOLAH_USER = "seklolah_user"
        private const val HP_USER = "hp_user"
        private const val EMAIL_USER = "email_user"




    }


    private val preference = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private var editor = preference.edit()
    fun setStatusUser(value:Boolean){
        editor.putBoolean(STATUS_USER, value)
        editor.apply()
    }
    fun getStatusUser():Boolean{
        return preference.getBoolean(STATUS_USER, false)
    }

    fun setName(value:String){
        editor.putString(NAME_USER, value)
        editor.apply()
    }
    fun getId():Int?{
        return preference.getInt(ID_USER, 0)
    }

    fun setId(value:Int){
        editor.putInt(ID_USER, value)
        editor.apply()
    }


    fun getName():String?{
        return preference.getString(NAME_USER, "")
    }

    fun setJekel(value:String){
        editor.putString(JEKEL_USER, value)
        editor.apply()
    }

    fun getJekel():String?{
        return preference.getString(JEKEL_USER, "")
    }
    fun setAlamat(value:String){
        editor.putString(ALAMAT_USER, value)
        editor.apply()
    }

    fun getAlamat():String?{
        return preference.getString(ALAMAT_USER, "")
    }

    fun setSekolah(value:String){
        editor.putString(SEKOLAH_USER, value)
        editor.apply()
    }

    fun getSekolah():String?{
        return preference.getString(SEKOLAH_USER, "")
    }
    fun setHP(value:String){
        editor.putString(HP_USER, value)
        editor.apply()
    }

    fun getHP():String?{
        return preference.getString(HP_USER, "")
    }
    fun setEmail(value:String){
        editor.putString(EMAIL_USER, value)
        editor.apply()
    }

    fun getEmail():String?{
        return preference.getString(EMAIL_USER, "")
    }







    fun logout(){
        editor.remove(STATUS_USER)
        editor.remove(ID_USER)
        editor.remove(NAME_USER)
        editor.remove(JEKEL_USER)
        editor.remove(ALAMAT_USER)
        editor.remove(SEKOLAH_USER)
        editor.remove(HP_USER)
        editor.remove(EMAIL_USER)
        editor.apply()
    }
    fun clear(){
        editor.remove(NAME_USER)
        editor.remove(JEKEL_USER)
        editor.remove(ALAMAT_USER)
        editor.remove(SEKOLAH_USER)
        editor.remove(HP_USER)
        editor.remove(EMAIL_USER)
        editor.apply()
    }




}