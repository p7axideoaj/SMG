package com.example.lostarkapp

import com.example.myapplication.dataModel.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface retrofitAPI {
    companion object {
        const val url = "armories/characters/{username}"
        const val token = "Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IktYMk40TkRDSTJ5NTA5NWpjTWk5TllqY2lyZyIsImtpZCI6IktYMk40TkRDSTJ5NTA5NWpjTWk5TllqY2lyZyJ9.eyJpc3MiOiJodHRwczovL2x1ZHkuZ2FtZS5vbnN0b3ZlLmNvbSIsImF1ZCI6Imh0dHBzOi8vbHVkeS5nYW1lLm9uc3RvdmUuY29tL3Jlc291cmNlcyIsImNsaWVudF9pZCI6IjEwMDAwMDAwMDAwMjgxMTgifQ.Jpx5u8fJqzGPCc-qBB8IBKAcatzqkbyonQFG8O1-Qdy6zqx172pIjfSyBTkcxVktXnubknJ9E2-6fuaV4AFunq_ig5xQzrXeQjY5NXH8rD5yRizcCst6GZbE2uz3Zvk6gjDnzaKt_vD_bElA5T-MgTHOXpV6AOmrNL217mSdJn9ocYbGCk67KjbYlaxBJOfrdZMMZe_cY-hedFY0KJthIRPuU_pxP8-DGTeOUoIf5vN4D3YtdqEoIneUW7kF2f82qxRyg4ut2YRwiQxbWNx2RYX_T41COnbXtwQ54HoGsx7ozdTf0WVBbSpao-ePFwrJkuEoqf8eGZ9SS42g_xk4UA"
    }
    @Headers("$token")
    @GET("news/events")
    fun getEvent(): Call<List<newsData>>
    @Headers("$token")
    @GET("auctions/options")
    fun getAuctions(): Call<auctionsData>
    @Headers("$token")
    @GET("markets/options")
    fun getMarkets(): Call<marketsData>
    @Headers("$token")
    @GET("$url/profiles")
    fun getCharterProfile(@Path("username") username:String): Call<charterProfile>
    @Headers("$token")
    @GET("$url/equipment")
    fun getCharterEquipment(@Path("username") username:String): Call<List<charterEquipment>>
    @Headers("$token")
    @GET("$url/avatars")
    fun getCharterAvatars(@Path("username") username:String): Call<List<charterAvatars>>
    @Headers("$token")
    @GET("$url/combat-skills")
    fun getCharterCombatSkills(@Path("username") username:String): Call<List<charterCombatSkills>>
    @Headers("$token")
    @GET("$url/engravings")
    fun getCharterEngravings(@Path("username") username:String): Call<charterEngravings>
    @Headers("$token")
    @GET("$url/cards")
    fun getCharterCards(@Path("username") username:String): Call<charterCards>
    @Headers("$token")
    @GET("$url/gems")
    fun getCharterGems(@Path("username") username:String): Call<charterGems>
    @Headers("$token")
    @GET("$url/colosseums")
    fun getCharterColosseums(@Path("username") username:String): Call<charterColosseums>
    @Headers("$token")
    @GET("$url/collectibles")
    fun getCharterCollectibles(@Path("username") username:String): Call<List<charterCollectibles>>
}