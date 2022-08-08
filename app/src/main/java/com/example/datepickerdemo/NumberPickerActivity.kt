package com.example.datepickerdemo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.datepickerdemo.numberpicker.OnValueChangeListener
import com.example.datepickerdemo.numberpicker.WheelPicker

class NumberPickerActivity : AppCompatActivity() {

    private lateinit var year: WheelPicker

    companion object {
        const val TAG = "NumberPickerActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_number_picker)
        year = findViewById(R.id.year)

        Log.d(TAG, "currentItem: ${year.getCurrentItem()}")
        year.setOnValueChangedListener(object : OnValueChangeListener{
            override fun onValueChange(picker: WheelPicker, oldVal: String, newVal: String) {
                Log.d(TAG, "onValueChange: $oldVal $newVal")
            }

        })
    }
}