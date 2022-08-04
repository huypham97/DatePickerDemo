package com.example.datepickerdemo

data class DateSelect(
    var date: Long = 0,
    var isDisplay: Boolean = false,
    var isFullyBooked: Boolean = false,
    var textColor: Int = 0,
    var available: Boolean = false
)
