package fgd.showroom.logic.network

import fgd.showroom.logic.model.PlayFile
import retrofit2.Call
import retrofit2.http.GET

interface FileListService {

    @GET("nasfile")
    fun getFileList(): Call<List<PlayFile>>

}