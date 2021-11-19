package com.example.faststudy.model

data class AlarmDisplayModel(
    val hour: Int,
    val minute: Int,
    var onOff : Boolean
){
    val timeText: String
    get(){
        // "$02d" 두자리숫까지 Int로 올 수 있고
        val h = "%02d".format(if(hour < 12)hour else hour - 12)
        val m = "%02d".format(minute)

        return "$h:$m"
    }

    val ampmText: String
    get(){
        return if(hour < 12 ) "AM" else "PM"
    }

    val onOffText: String
    get(){
        return if(onOff)"알람 끄기" else "알람 켜기"
    }

    fun makeDataForDB() : String {
        return "$hour:$minute"
    }

}
