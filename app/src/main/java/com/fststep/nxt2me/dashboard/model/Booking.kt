package com.fststep.nxt2me.dashboard.model

data class Booking(
    var id: Int,
    var name: String,
    var date: String,
    var time: String,
    var contact: String,
    var email: String
) {

    fun getTimeVal(): String {
        return time.split(" ")[0]
    }
}