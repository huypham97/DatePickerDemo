package com.example.datepickerdemo

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView

class MainActivity : AppCompatActivity()/*, ViewTreeObserver.OnGlobalLayoutListener*/ {

    private lateinit var nsContent: NestedScrollView
    private lateinit var tvAnchor: View
    private lateinit var rlContainer: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        applyWindow(window)
        setContentView(R.layout.activity_main)
//        nsContent = findViewById(R.id.nsv)
//        tvAnchor = findViewById(R.id.tv)
//        rlContainer = findViewById(R.id.rl)
    }

//    override fun onResume() {
//        super.onResume()
//        rlContainer.viewTreeObserver.addOnGlobalLayoutListener(this)
//    }
//
//    override fun onPause() {
//        super.onPause()
//        rlContainer.viewTreeObserver.removeOnGlobalLayoutListener(this)
//    }
//
//    override fun onGlobalLayout() {
//        nsContent.smoothScrollTo(0, tvAnchor.bottom)
//    }
//
//    private fun applyWindow(window: Window) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            window.setDecorFitsSystemWindows(false)
//        } else {
//            window.decorView.systemUiVisibility =
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//        }
//        window.statusBarColor = Color.TRANSPARENT
//    }
}