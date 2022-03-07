package fgd.showroom.logic.model

data class CommonResponse(val success: Int, val message: String)

data class DevStatus(val devno: Int, val state: Int)

data class Action(val action: Int, val memo: String)

data class PlayFile(val queue: Int, val filename: String)

data class DevType(val typeid: Int, val typename: String)

data class Device(val typeid: Int, val devno: Int, val typename: String, val devname: String)

data class Step(val stepno: Int, val stepname: String, val posx: Int, val posy: Int)

//StepAction == Wizard
data class StepAction(
    val id: Int?,
    val stepno: Int,
    val stepidx: Int,
    val devtype: Int,
    val action: Int,
    val devno: Int,
    val filename: String,
    val intv: Int
)