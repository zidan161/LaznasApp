package com.zidan.laznasapp

import android.content.Context

class MainPreferences(private val context: Context) {

    private val preferences = context.getSharedPreferences("myPref", Context.MODE_PRIVATE)

    fun setFirst(first: Boolean, name: String, email: String){
        preferences.edit()
            .putBoolean("isFirst", first)
            .putString("name", name)
            .putString("email", email)
            .apply()
    }

    fun getFirst() = preferences.getBoolean("isFirst", true)

    fun getName() = preferences.getString("name", "")

    fun getEmail() = preferences.getString("email", "")
}