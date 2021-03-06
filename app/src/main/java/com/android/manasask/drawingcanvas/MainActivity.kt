package com.android.manasask.drawingcanvas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //implementing canvas description page
        //setContentView(R.layout.activity_main)
//        val myCanvasView=MyCanvasView(this)
//        setContentView(myCanvasView)

        val myCanvasView=MyCanvasView(this)
        myCanvasView.systemUiVisibility= SYSTEM_UI_FLAG_FULLSCREEN
        myCanvasView.contentDescription=getString(R.string.canvasContentDescription)
        setContentView(myCanvasView)
    }
}