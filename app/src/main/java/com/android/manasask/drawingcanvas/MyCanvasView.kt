package com.android.manasask.drawingcanvas

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.RectShape
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.content.res.ResourcesCompat

class MyCanvasView(context: Context): View(context) {
//    private val drawable: ShapeDrawable = run {
//        val x = 10
//        val y = 10
//        val width = 300
//        val height = 50
//        contentDescription = context.resources.getString(R.string.canvasContentDescription)
//
//        ShapeDrawable(OvalShape()).apply {
//            // If the color isn't set, the shape uses black as the default.
//            paint.color = 0xff74AC23.toInt()
//            // If the bounds aren't set, the shape can't be drawn.
//            setBounds(x, y, x + width, y + height)
//        }
//        ShapeDrawable(RectShape()).apply {
//            // If the color isn't set, the shape uses black as the default.
//            paint.color = 0xff74AC23.toInt()
//            // If the bounds aren't set, the shape can't be drawn.
//            setBounds(x+width, y+width, x + 2*width, y + 2*height)
//        }
//    }
//
//    override fun onDraw(canvas: Canvas) {
//        drawable.draw(canvas)
//    }
//

    //bitmap and canvas for caching what has been drawn before.
    private lateinit var extraBitmap:Bitmap
    private lateinit var extraCanvas: Canvas


    //caching the x and y coordinates of the current touch event (the MotionEvent coordinates).
    private var motionTouchEventX = 0f
    private var motionTouchEventY = 0f

    //variables to cache the latest x and y values. After the user stops moving and lifts their touch, these are the starting point for the next path (the next segment of the line to draw).
    private var currentX = 0f
    private var currentY = 0f

    //whether user has barely moved his hand or drawn something on screen
    private val touchTolerence=ViewConfiguration.get(context).scaledTouchSlop

    //set background color
    private val backgroundColor=ResourcesCompat.getColor(resources,R.color.colorBackground,null)

    //holding the color to draw with and initialize it with the colorPaint resource you defined earlier.
    private val drawColor = ResourcesCompat.getColor(resources, R.color.colorPaint, null)

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

    // Set up the paint with which to draw. how things are styled
    private val paint = Paint().apply {
        color = drawColor
        // Smooths out edges of what is drawn without affecting shape.
        isAntiAlias = true
        // Dithering affects how colors with higher-precision than the device are down-sampled.
        isDither = true
        style = Paint.Style.STROKE // default: FILL , type of painting
        strokeJoin = Paint.Join.ROUND // default: MITER , curve and line segment join
        strokeCap = Paint.Cap.ROUND // default: BUTT , strokes shapes end of the line
        strokeWidth = Companion.STROKE_WIDTH // default: Hairline-width (really thin)
    }


    //object to store the path that is being drawn when following the user's touch on the screen. Import android.graphics.Path for the Path.
    //whats been drawn
    private var path= Path()

    companion object {
        private const val STROKE_WIDTH=12f //always float value
    }

    //called when user first touches screen
    private fun touchStart() {
        path.reset()
        path.moveTo(motionTouchEventX, motionTouchEventY)
        currentX = motionTouchEventX
        currentY = motionTouchEventY
    }

    //called when user starts drawing
    private fun touchMove() {
        val dx=Math.abs(motionTouchEventX-currentX)
        val dy=Math.abs(motionTouchEventY-currentY)
        if(dx>=touchTolerence && dy>=touchTolerence){
            // QuadTo() adds a quadratic bezier from the last point,
            // approaching control point (x1,y1), and ending at (x2,y2).
            path.quadTo(currentX, currentY, (motionTouchEventX + currentX) / 2, (motionTouchEventY + currentY) / 2)
            currentX = motionTouchEventX
            currentY = motionTouchEventY
            // Draw the path in the extra bitmap to cache it.
            extraCanvas.drawPath(path, paint)
        }
        invalidate()
    }

    //called when user lifts up his hand
    private fun touchUp() {
        path.reset() // Reset the path so it doesn't get drawn again.
    }

    //rotate the device, the screen is cleared, because the drawing state is not saved. For this sample app, this is by design, to give the user a simple way to clear the screen.


    override fun onTouchEvent(event: MotionEvent): Boolean {
        motionTouchEventX = event.x
        motionTouchEventY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> touchStart()
            MotionEvent.ACTION_MOVE -> touchMove()
            MotionEvent.ACTION_UP -> touchUp()
        }
        return true
    }
}