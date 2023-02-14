package com.example.myapplication

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import com.example.lostarkapp.retrofitAPI
import com.example.myapplication.dataModel.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun getJSONNewsDate(cList: MutableList<newsData>, ctx: Context) {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://developer-lostark.game.onstove.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitAPIMethod = retrofit.create(retrofitAPI::class.java)

    val call: Call<List<newsData>> = retrofitAPIMethod.getEvent()

    call.enqueue(object : Callback<List<newsData>?> {
        override fun onResponse(call: Call<List<newsData>?>, response: Response<List<newsData>?>) {
            Log.d("아아","${response.code()}")
            if(response.isSuccessful) {
                var list: List<newsData> = response.body()!!
                Log.d("아아","${list}")
                if(cList.isNotEmpty()) {
                    cList.clear()
                }
                cList.addAll(list)

            } else if(response.code() == 429){

            } else {

            }
        }

        override fun onFailure(call: Call<List<newsData>?>, t: Throwable) {

        }
    })
}
fun getJSONProfile(cList: MutableList<charterProfile>, ctx: Context, username: String) {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://developer-lostark.game.onstove.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitAPIMethod = retrofit.create(retrofitAPI::class.java)

    val call: Call<charterProfile> = retrofitAPIMethod.getCharterProfile(username)

    call.enqueue(object : Callback<charterProfile?> {
        override fun onResponse(call: Call<charterProfile?>, response: Response<charterProfile?>) {
            Log.d("아아","${response.code()}")
            if(response.isSuccessful) {
                var cp: charterProfile = response.body()!!
                Log.d("아아","${cp}\nㅁㅁㅁㅁ")
                if(cList.isNotEmpty()) {
                    cList.clear()
                }
                cList.add(cp)

            } else if(response.code() == 429){

            } else {

            }
        }

        override fun onFailure(call: Call<charterProfile?>, t: Throwable) {
            t.printStackTrace()
        }
    })
}
fun getJSONProfileAvatars(cList: MutableList<charterAvatars>, ctx: Context, username: String) {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://developer-lostark.game.onstove.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitAPIMethod = retrofit.create(retrofitAPI::class.java)

    val call: Call<List<charterAvatars>> = retrofitAPIMethod.getCharterAvatars(username)

    call.enqueue(object : Callback<List<charterAvatars>?> {
        override fun onResponse(call: Call<List<charterAvatars>?>, response: Response<List<charterAvatars>?>) {
            Log.d("아아","${response.code()}")
            if(response.isSuccessful) {
                var cp: List<charterAvatars> = response.body()!!
                Log.d("아아","${cp}\nㅁㅁㅁㅁ")
                if(cList.isNotEmpty()) {
                    cList.clear()
                }
                cList.addAll(cp)

            } else if(response.code() == 429){

            } else {

            }
        }

        override fun onFailure(call: Call<List<charterAvatars>?>, t: Throwable) {
            t.printStackTrace()
        }
    })
}
fun getJSONProfileEquipment(cList: MutableList<charterEquipment>, ctx: Context, username: String) {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://developer-lostark.game.onstove.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitAPIMethod = retrofit.create(retrofitAPI::class.java)

    val call: Call<List<charterEquipment>> = retrofitAPIMethod.getCharterEquipment(username)

    call.enqueue(object : Callback<List<charterEquipment>?> {
        override fun onResponse(call: Call<List<charterEquipment>?>, response: Response<List<charterEquipment>?>) {
            Log.d("장비코드","${response.code()}")
            if(response.isSuccessful) {
                var cp: List<charterEquipment> = response.body()!!
                Log.d("장비","${cp}\nㅁㅁㅁㅁ")
                if(cList.isNotEmpty()) {
                    cList.clear()
                }
                cList.addAll(cp)

            } else if(response.code() == 429){

            } else {

            }
        }

        override fun onFailure(call: Call<List<charterEquipment>?>, t: Throwable) {
            Log.d("장비코드","${t.message}")
        }
    })
}
fun getJSONProfileCombatSkills(cList: MutableList<charterCombatSkills>, ctx: Context, username: String) {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://developer-lostark.game.onstove.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitAPIMethod = retrofit.create(retrofitAPI::class.java)
    val call: Call<List<charterCombatSkills>> = retrofitAPIMethod.getCharterCombatSkills(username)
    call.enqueue(object : Callback<List<charterCombatSkills>?> {
        override fun onResponse(call: Call<List<charterCombatSkills>?>, response: Response<List<charterCombatSkills>?>) {
            Log.d("스킬스킬1","${response.code()}")
            if(response.isSuccessful) {
                var cp: List<charterCombatSkills> = response.body()!!
                Log.d("스킬스킬2","${cp}\nㅁㅁㅁㅁ")
                if(cList.isNotEmpty()) {
                    cList.clear()
                }
                cList.addAll(cp)

            } else if(response.code() == 429){

            } else {

            }
        }

        override fun onFailure(call: Call<List<charterCombatSkills>?>, t: Throwable) {
            Log.d("스킬스킬", "${t.message}")
        }
    })
}
fun getJSONProfileEngravings(cList: MutableList<charterEngravings>, ctx: Context, username: String) {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://developer-lostark.game.onstove.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitAPIMethod = retrofit.create(retrofitAPI::class.java)

    val call: Call<charterEngravings> = retrofitAPIMethod.getCharterEngravings(username)

    call.enqueue(object : Callback<charterEngravings?> {
        override fun onResponse(call: Call<charterEngravings?>, response: Response<charterEngravings?>) {
            Log.d("이펙트","${response.code()}")
            if(response.isSuccessful) {
                var cp: charterEngravings = response.body()!!
                Log.d("이펙트","${cp}\nㅁㅁㅁㅁ")
                if(cList.isNotEmpty()) {
                    cList.clear()
                }
                cList.add(cp)

            } else if(response.code() == 429){

            } else {

            }
        }

        override fun onFailure(call: Call<charterEngravings?>, t: Throwable) {
            t.printStackTrace()
        }
    })
}
fun getJSONProfileCards(cList: MutableState<charterCards?>, ctx: Context, username: String) {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://developer-lostark.game.onstove.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitAPIMethod = retrofit.create(retrofitAPI::class.java)

    val call: Call<charterCards> = retrofitAPIMethod.getCharterCards(username)

    call.enqueue(object : Callback<charterCards?> {
        override fun onResponse(call: Call<charterCards?>, response: Response<charterCards?>) {
            Log.d("카드","${response.code()}")
            if(response.isSuccessful) {
                var cp: charterCards = response.body()!!
                Log.d("카드","${cp}\nㅁㅁㅁㅁ")
                cList.value = cp

            } else if(response.code() == 429){

            } else {

            }
        }

        override fun onFailure(call: Call<charterCards?>, t: Throwable) {
            t.printStackTrace()
        }

    })
}
fun getJSONProfileGems(cList: MutableList<charterGems>, ctx: Context, username: String) {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://developer-lostark.game.onstove.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitAPIMethod = retrofit.create(retrofitAPI::class.java)

    val call: Call<charterGems> = retrofitAPIMethod.getCharterGems(username)

    call.enqueue(object : Callback<charterGems?> {
        override fun onResponse(call: Call<charterGems?>, response: Response<charterGems?>) {
            Log.d("젬","${response.code()}")
            if(response.isSuccessful) {
                var cp: charterGems? = response.body()
                Log.d("젬","${cp}\nㅁㅁㅁㅁ")
                if(cList.isNotEmpty()) {
                    cList.clear()
                }
                if (cp != null) {
                    cList.add(cp)
                }

            } else if(response.code() == 429){

            } else {

            }
        }

        override fun onFailure(call: Call<charterGems?>, t: Throwable) {
            t.printStackTrace()
        }
    })
}
fun getJSONMarkets(cList: MutableList<marketsData>, ctx: Context) {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://developer-lostark.game.onstove.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitAPIMethod = retrofit.create(retrofitAPI::class.java)

    val call: Call<marketsData> = retrofitAPIMethod.getMarkets()

    call.enqueue(object : Callback<marketsData?> {
        override fun onResponse(call: Call<marketsData?>, response: Response<marketsData?>) {
            Log.d("아아","${response.code()}")
            if(response.isSuccessful) {
                var cp: marketsData = response.body()!!
                Log.d("아아","${cp}\nㅁㅁㅁㅁ")
                if(cList.isNotEmpty()) {
                    cList.clear()
                }
                cList.add(cp)

            } else if(response.code() == 429){

            } else {

            }
        }

        override fun onFailure(call: Call<marketsData?>, t: Throwable) {
            t.printStackTrace()
        }
    })
}
fun getJSONAuctions(cList: MutableList<auctionsData>, ctx: Context) {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://developer-lostark.game.onstove.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitAPIMethod = retrofit.create(retrofitAPI::class.java)

    val call: Call<auctionsData> = retrofitAPIMethod.getAuctions()

    call.enqueue(object : Callback<auctionsData?> {
        override fun onResponse(call: Call<auctionsData?>, response: Response<auctionsData?>) {
            Log.d("아아","${response.code()}")
            if(response.isSuccessful) {
                var cp: auctionsData = response.body()!!
                Log.d("아아","${cp}\nㅁㅁㅁㅁ")
                if(cList.isNotEmpty()) {
                    cList.clear()
                }
                cList.add(cp)

            } else if(response.code() == 429){

            } else {

            }
        }

        override fun onFailure(call: Call<auctionsData?>, t: Throwable) {
            t.printStackTrace()
        }
    })
}
fun getJSONProfileColosseums(cList: MutableList<charterColosseums>, ctx: Context, username: String) {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://developer-lostark.game.onstove.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitAPIMethod = retrofit.create(retrofitAPI::class.java)

    val call: Call<charterColosseums> = retrofitAPIMethod.getCharterColosseums(username)

    call.enqueue(object : Callback<charterColosseums?> {
        override fun onResponse(call: Call<charterColosseums?>, response: Response<charterColosseums?>) {
            Log.d("아아","${response.code()}")
            if(response.isSuccessful) {
                var cp: charterColosseums = response.body()!!
                Log.d("아아","${cp}\nㅁㅁㅁㅁ")
                if(cList.isNotEmpty()) {
                    cList.clear()
                }
                cList.add(cp)

            } else if(response.code() == 429){

            } else {

            }
        }

        override fun onFailure(call: Call<charterColosseums?>, t: Throwable) {
            t.printStackTrace()
        }
    })
}
fun getJSONProfileCollectibles(cList: MutableList<charterCollectibles>, ctx: Context, username: String) {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://developer-lostark.game.onstove.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitAPIMethod = retrofit.create(retrofitAPI::class.java)

    val call: Call<List<charterCollectibles>> = retrofitAPIMethod.getCharterCollectibles(username)

    call.enqueue(object : Callback<List<charterCollectibles>?> {
        override fun onResponse(call: Call<List<charterCollectibles>?>, response: Response<List<charterCollectibles>?>) {
            Log.d("컬렉션","${response.code()}")
            if(response.isSuccessful) {
                var cp: List<charterCollectibles> = response.body()!!
                Log.d("컬렉션","${cp}\nㅁㅁㅁㅁ")
                if(cList.isNotEmpty()) {
                    cList.clear()
                }
                cList.addAll(cp)

            } else if(response.code() == 429){

            } else {

            }
        }

        override fun onFailure(call: Call<List<charterCollectibles>?>, t: Throwable) {
            t.printStackTrace()
        }
    })
}