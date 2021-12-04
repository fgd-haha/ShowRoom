package fgd.showroom.logic.dao

import android.content.Context
import androidx.core.content.edit
import fgd.showroom.ShowRoomApplication

object ServerUrlDao {

    fun saveUrl(url: String) {
        sharedPreferences().edit {
            putString("server_url", url)
        }
    }

    fun getSavedUrl(): String? {
        return sharedPreferences().getString("server_url", "")
    }

    fun isUrlSaved() = sharedPreferences().contains("server_url")

    private fun sharedPreferences() =
        ShowRoomApplication.context.getSharedPreferences("show_room", Context.MODE_PRIVATE)

}