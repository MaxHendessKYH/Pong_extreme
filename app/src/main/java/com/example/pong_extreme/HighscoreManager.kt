package com.example.pong_extreme

import android.content.SharedPreferences
import com.google.gson.Gson

// Manager defined as object so that there can be only 1 Manager ( Singleton pattern)
object HighscoreManager {
    var classicHighScores:MutableList<Highscore> = mutableListOf(
        Highscore("Max" , "500", "classic"))
    var timedHighScores:MutableList<Highscore> = mutableListOf(
        Highscore("Dennis" , "500", "timed"))
    fun getHighScores( gameMode: String, prefs: SharedPreferences): MutableList<Highscore> {
//      deleteAllHighScores(prefs)
        when (gameMode) {
            "classic" -> {
                classicHighScores.clear()
                setDummyData(classicHighScores, "classic")
                val allPrefs: Map<String, *> = prefs.getAll()
                for (key in allPrefs.keys) {
                    val gson = Gson()
                    val json = prefs.getString(key, "")
                    val highscore = gson.fromJson(json, Highscore::class.java)
                    if (highscore.gameMode == "classic") {
                        classicHighScores.add(highscore)
                    }
                }
                return classicHighScores
            }

            "timed" -> {
               timedHighScores.clear()
                setDummyData(timedHighScores, "timed")
                val allPrefs: Map<String, *> = prefs.getAll()
                for (key in allPrefs.keys) {
                    val gson = Gson()
                    val json = prefs.getString(key, "")
                    val highscore = gson.fromJson(json, Highscore::class.java)
                    if(highscore.gameMode == "timed") {
                        timedHighScores.add(highscore)
                    }
                }
                return timedHighScores
            }
        }
        return classicHighScores // maybe change or add default return
    }
    fun addHighScores( newHighscore: Highscore,  prefs: SharedPreferences)
    {
        val editor: SharedPreferences.Editor = prefs.edit()
        // convert Highscore object to json String
       val gson = Gson()
       val json: String = gson.toJson(newHighscore)
        // save highscore in sharedprefs
        editor.putString(prefs.all.size.toString(), json)
        // apply changes
        editor.apply()
    }
 fun setDummyData(list: MutableList<Highscore>, gameMode: String): MutableList<Highscore>
 {
     list.add(Highscore("Max" , "500", gameMode))
     list.add(Highscore("Mehdi" , "500", gameMode))
     list.add(Highscore("Dennis" , "500", gameMode))
     list.add(Highscore("Juhee" , "500", gameMode))
     return list
 }
    fun deleteAllHighScores(prefs: SharedPreferences)
    {
        val prefsEditor = prefs.edit()
        prefsEditor.clear()
        prefsEditor.commit()
    }
}