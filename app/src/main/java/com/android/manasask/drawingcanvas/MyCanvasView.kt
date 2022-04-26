package com.android.manasask.drawingcanvas

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import androidx.core.content.res.ResourcesCompat

class MyCanvasView(context: Context): View(context) {

    //bitmap and canvas for caching what has been drawn before.
    private lateinit var extraBitmap:Bitmap
    private lateinit var extraCanvas: Canvas

    //set background color
    private val backgroundColor=ResourcesCompat.getColor(resources,R.color.colorBackground,null)


    //In MyCanvasView, override the onSizeChanged() method. This callback method is called by the Android system with the changed screen dimensions, that is, with a new width and height (to change to) and the old width and height (to change from).
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //recycle before creating new bitmap to avoid memory leak
        if (::extraBitmap.isInitialized) extraBitmap.recycle()
        //Inside onSizeChanged(), create an instance of Bitmap with the new width and height, which are the screen size, and assign it to extraBitmap. The third argument is the bitmap color configuration. ARGB_8888 stores each color in 4 bytes and is recommended.
        extraBitmap= Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888)
        extraCanvas= Canvas(extraBitmap)
        extraCanvas.drawColor(backgroundColor)
    }

    //Override onDraw() and draw the contents of the cached extraBitmap on the canvas associated with the view. The drawBitmap() Canvas method comes in several versions. In this code, you provide the bitmap, the x and y coordinates (in pixels) of the top left corner, and null for the Paint, as you'll set that later.
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(extraBitmap,0f,0f,null)
    }
}