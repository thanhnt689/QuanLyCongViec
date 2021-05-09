package com.ntt.quanlycongviec.model

import android.icu.util.Calendar
import android.widget.TimePicker
import java.io.Serializable
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class Job(
    var title: String,
    var content: String,
    var day: String,
    var time: String,
    var note: String
) : Serializable
