package com.example.myapplication.sharedHelper

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.myapplication.dataModel.Listdata
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject

class sharedHelper(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)
    fun remove(key: String): Boolean {
        return prefs.edit().remove(key).commit()
    }
    fun getString(key: String): String? {
        return prefs.getString(key, null)
    }
    fun setString(key: String, str: String) {
        prefs.edit().putString(key, str).apply()
    }
    fun getList(key: String): MutableSet<String>? {
        return prefs.getStringSet(key, null)
    }
    fun setList(key: String, lst: MutableSet<String>) {
        return prefs.edit().putStringSet(key, lst).apply();
    }
    fun getreList(): List<Listdata> {
        val obj = getList("listdata")
        Log.d("오비제이", "$obj")
        if (obj != null) {
            return obj.map {
                val data = JSONObject(it)
                Listdata(
                    imageLink = data.getString("imageLink"),
                    name = data.getString("name"),
                    title = data.getString("title")
                )
            }.toList()
        } else {
            return listOf()
        }
    }
    fun setreList(list: List<Listdata>) {
        val obj = list.map {
            Gson().toJson(it)
        }
        setList("listdata", obj.toMutableSet());

    }

}
