package fgd.showroom.logic.dao

import android.content.Context
import android.util.Patterns
import androidx.core.content.edit
import fgd.showroom.ShowRoomApplication

object ServerUrlDao {

    fun saveUrl(url: String): Boolean {
        return if (Patterns.WEB_URL.matcher(url).matches()) {
            sharedPreferences().edit {
                putString("server_url", url)
            }
            true
        } else {
            false
        }
    }

    fun getSavedUrl(): String? {
        return sharedPreferences().getString("server_url", "")
    }

    fun isUrlSaved() = sharedPreferences().contains("server_url")

    private fun sharedPreferences() =
        ShowRoomApplication.context.getSharedPreferences("show_room", Context.MODE_PRIVATE)

}