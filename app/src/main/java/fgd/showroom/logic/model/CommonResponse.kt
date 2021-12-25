package fgd.showroom.logic.model

data class CommonResponse(val success: Int, val message: String)

data class DevStatus(val devno: Int, val state: Int)