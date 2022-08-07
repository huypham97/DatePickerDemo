package com.example.datepickerdemo

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.datepickerdemo.wheelpicker.WheelPicker


class WheelPickerActivity : AppCompatActivity(), WheelPicker.OnItemSelectedListener,
    View.OnClickListener {

    private lateinit var wheelLeft: WheelPicker
    private lateinit var wheelCenter: WheelPicker
    private lateinit var wheelRight: WheelPicker

    private lateinit var gotoBtn: Button
    private var gotoBtnItemIndex: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wheel_picker)

        wheelLeft = findViewById(R.id.main_wheel_left)
        wheelLeft.setOnItemSelectedListener(this)
        wheelCenter = findViewById(R.id.main_wheel_center)
        wheelCenter.setOnItemSelectedListener(this)
        wheelRight = findViewById(R.id.main_wheel_right)
        wheelRight.setOnItemSelectedListener(this)

        gotoBtn = findViewById(R.id.goto_btn)
        randomlySetGotoBtnIndex()
        gotoBtn.setOnClickListener(this)
    }

    private fun randomlySetGotoBtnIndex() {
        gotoBtnItemIndex = (Math.random() * wheelCenter.data.size).toInt()
        gotoBtn.text = "Goto '" + wheelCenter.data.get(gotoBtnItemIndex!!) + "'"
    }

    override fun onItemSelected(picker: WheelPicker, data: Any, position: Int) {
        var text = ""
        when (picker.id) {
            R.id.main_wheel_left -> text = "Left:"
            R.id.main_wheel_center -> text = "Center:"
            R.id.main_wheel_right -> text = "Right:"
        }
        Toast.makeText(this, text + data.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onClick(v: View?) {
        wheelCenter.selectedItemPosition = gotoBtnItemIndex!!
        randomlySetGotoBtnIndex()
    }
}