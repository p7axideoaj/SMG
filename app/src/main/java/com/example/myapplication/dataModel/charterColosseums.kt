package com.example.myapplication.dataModel

import com.google.gson.annotations.SerializedName

data class charterColosseums(
    @SerializedName("Rank") val Rank: Int?,
    @SerializedName("PreRank") val PreRank: Int?,
    @SerializedName("Exp") val Exp: Int?,
    @SerializedName("Colosseums") val Colosseums: List<Colosseums>,
)
data class Colosseums(
    @SerializedName("SeasonName") val seasonName: String,
    @SerializedName("Competitive") val competitive: Competitive?,
    @SerializedName("TeamDeathmatch") val teamDeathmatch: TeamDeathmatch?,
    @SerializedName("Deathmatch") val deathmatch: Deathmatch?,
    @SerializedName("TeamElimination") val teamElimination: TeamDeathmatch?,
    @SerializedName("CoOpBattle") val coOpBattle: CoOpBattle?,
)
data class Competitive(
    @SerializedName("Rank") val rank: Int?,
    @SerializedName("RankName") val rankName: String?,
    @SerializedName("RankIcon") val rankIcon: String?,
    @SerializedName("RankLastMmr") val rankLastMmr: Int?,
    @SerializedName("PlayCount") val playCount: Int?,
    @SerializedName("VictoryCount") val victoryCount: Int?,
    @SerializedName("LoseCount") val loseCount: Int?,
    @SerializedName("TieCount") val tieCount: Int?,
    @SerializedName("KillCount") val killCount: Int?,
    @SerializedName("AceCount") val aceCount: Int?,
    @SerializedName("DeathCount") val deathCount: Int?,
)
data class TeamDeathmatch(
    @SerializedName("PlayCount") val playCount: Int?,
    @SerializedName("VictoryCount") val victoryCount: Int?,
    @SerializedName("LoseCount") val loseCount: Int?,
    @SerializedName("TieCount") val tieCount: Int?,
    @SerializedName("KillCount") val killCount: Int?,
    @SerializedName("AceCount") val aceCount: Int?,
    @SerializedName("DeathCount") val deathCount: Int?,
)
data class Deathmatch(
    @SerializedName("PlayCount") val playCount: Int?,
    @SerializedName("VictoryCount") val victoryCount: Int?,
    @SerializedName("LoseCount") val loseCount: Int?,
    @SerializedName("TieCount") val tieCount: Int?,
    @SerializedName("KillCount") val killCount: Int?,
    @SerializedName("AceCount") val aceCount: Int?,
    @SerializedName("DeathCount") val deathCount: Int?,
)
data class TeamElimination(
    @SerializedName("FirstWinCount") val firstWinCount: Int?,
    @SerializedName("SecondWinCount") val secondWinCount: Int?,
    @SerializedName("ThirdWinCount") val thirdWinCount: Int?,
    @SerializedName("FirstPlayCount") val firstPlayCount: Int?,
    @SerializedName("SecondPlayCount") val secondPlayCount: Int?,
    @SerializedName("ThirdPlayCount") val thirdPlayCount: Int?,
    @SerializedName("AllKillCount") val allKillCount: Int?,
    @SerializedName("PlayCount") val playCount: Int?,
    @SerializedName("VictoryCount") val victoryCount: Int?,
    @SerializedName("LoseCount") val loseCount: Int?,
    @SerializedName("TieCount") val tieCount: Int?,
    @SerializedName("KillCount") val killCount: Int?,
    @SerializedName("AceCount") val aceCount: Int?,
    @SerializedName("DeathCount") val deathCount: Int?,
)
data class CoOpBattle(
    @SerializedName("PlayCount") val playCount: Int?,
    @SerializedName("VictoryCount") val victoryCount: Int?,
    @SerializedName("LoseCount") val loseCount: Int?,
    @SerializedName("TieCount") val tieCount: Int?,
    @SerializedName("KillCount") val killCount: Int?,
    @SerializedName("AceCount") val aceCount: Int?,
    @SerializedName("DeathCount") val deathCount: Int?,
)