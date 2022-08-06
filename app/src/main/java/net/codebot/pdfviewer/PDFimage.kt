package net.codebot.pdfviewer

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import android.widget.ImageView


@SuppressLint("AppCompatCustomView")
class PDFimage // constructor
    (context: Context?) : ImageView(context){
    var path: Path? = null
    var bitmap: Bitmap? = null
    var paint = Paint(Color.BLUE)
    var pen = Paint()
    val bound = RectF()
    var highlighter = Paint()

    init {
        for (i in 0 until Total) {
            pages.add(ArrayList())
        }
        annos = pages[0]
        pen.isAntiAlias = true
        pen.style = Paint.Style.STROKE
        pen.strokeWidth = 6.5F
        pen.color = Color.GREEN
        highlighter.isAntiAlias = true
        highlighter.style = Paint.Style.STROKE
        highlighter.strokeWidth = 28.5F
        highlighter.setARGB(70,255,255,2)
    }
    var x1 = 0f
    var x2 = 0f
    var y1 = 0f
    var y2 = 0f
    var old_x1 = 0f
    var old_y1 = 0f
    var old_x2 = 0f
    var old_y2 = 0f
    var mid_x = -1f
    var mid_y = -1f
    var old_mid_x = -1f
    var old_mid_y = -1f
    var p1_id = 0
    var p1_index = 0
    var p2_id = 0
    var p2_index = 0

    var currentMatrix = Matrix()
    var inverse = Matrix()
    // capture touch events (down/move/up) to create a path
    // and use that to create a stroke that we can draw
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        var inverted = floatArrayOf()
        var pointer  = event.pointerCount
        when(event.pointerCount) {
            1 -> {
                inverse = Matrix()
                currentMatrix.invert(inverse)
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        path = Path()
                        path!!.moveTo(event.x, event.y)
                    }
                    MotionEvent.ACTION_MOVE -> {
                        path!!.lineTo(event.x, event.y)
                        if (Tool == "eraser") {
                            erase()
                        }
                    }
                    MotionEvent.ACTION_UP -> {
                        Log.d("LOGNAME", "Action up2");
                        if (Tool != "eraser" && Tool != "Hand") {
                            path?.let { Anno(it, Tool, true) }?.let { annos.add(it) }
                            path = null
                            undo.push(annos[annos.size - 1]);
                        }
                    }
                }
            }
            else -> {
                //using this code from the Android/zoompan
                Tool = "Hand"
                p1_id = event.getPointerId(0)
                p1_index = event.findPointerIndex(p1_id)

                // mapPoints returns values in-place
                inverted = floatArrayOf(event.getX(p1_index), event.getY(p1_index))
                inverse.mapPoints(inverted)

                // first pass, initialize the old == current value
                if (old_x1 < 0 || old_y1 < 0) {
                    x1 = inverted[0]
                    old_x1 = x1
                    y1 = inverted[1]
                    old_y1 = y1
                } else {
                    old_x1 = x1
                    old_y1 = y1
                    x1 = inverted[0]
                    y1 = inverted[1]
                }
                p2_id = event.getPointerId(1)
                p2_index = event.findPointerIndex(p2_id)
                inverted = floatArrayOf(event.getX(p2_index), event.getY(p2_index))
                inverse.mapPoints(inverted)
                if (old_x2 < 0 || old_y2 < 0) {
                    x2 = inverted[0]
                    old_x2 = x2
                    y2 = inverted[1]
                    old_y2 = y2
                } else {
                    old_x2 = x2
                    old_y2 = y2
                    x2 = inverted[0]
                    y2 = inverted[1]
                }
                mid_x = (x1 + x2) / 2
                mid_y = (y1 + y2) / 2
                old_mid_x = (old_x1 + old_x2) / 2
                old_mid_y = (old_y1 + old_y2) / 2
                val d_old =
                    Math.sqrt(Math.pow((old_x1 - old_x2).toDouble(), 2.0) + Math.pow((old_y1 - old_y2).toDouble(), 2.0))
                        .toFloat()
                val d = Math.sqrt(Math.pow((x1 - x2).toDouble(), 2.0) + Math.pow((y1 - y2).toDouble(), 2.0))
                    .toFloat()
                if (event.action == MotionEvent.ACTION_MOVE) {
                    val dx = mid_x - old_mid_x
                    val dy = mid_y - old_mid_y
                    currentMatrix.preTranslate(dx, dy)
                    var scale = d / d_old
                    scale = Math.max(0f, scale)
                    currentMatrix.preScale(scale, scale, mid_x, mid_y)
                    // reset on up
                } else if (event.action == MotionEvent.ACTION_UP) {
                    old_x1 = -1f
                    old_y1 = -1f
                    old_x2 = -1f
                    old_y2 = -1f
                    old_mid_x = -1f
                    old_mid_y = -1f
                }else {
                    Log.d("LOGNAME", "Action down222");
                }
            }
        }
        return true
    }

    fun setImage(bitmap: Bitmap?, i: Int) {
        this.bitmap = bitmap
        annos = pages[i]
    }

    fun setBrush(paint: Paint) {
        this.paint = paint

    }

    fun erase(){
        val eraseRegion = Region()
        path?.let { createReligion(it, eraseRegion, bound) }
        for (i in annos) {
            if (!i.visibility) continue
            val pathRegion = Region()
            createReligion(i.paths, pathRegion, bound)
            if (pathRegion.op(eraseRegion, Region.Op.INTERSECT)) {
                i.visibility = false
                undo.push(i)
            }
        }
    }


    override fun onDraw(canvas: Canvas) {
        if (bitmap != null) {
            setImageBitmap(bitmap)
        }
        canvas.concat(currentMatrix)
        for (i in annos) {
            if (i.visibility) {
               if (i.tool == "pen") {
                   canvas.drawPath(i.paths, pen)
                } else if (i.tool == "highlight") {
                    canvas.drawPath(i.paths, highlighter)
                }
            }
        }
        if (path != null && Tool != "eraser"&& Tool != "Hand") {
            if(Tool == "pen") {
                canvas.drawPath(path!!, pen)
            }else if(Tool == "highlight"){
                canvas.drawPath(path!!, highlighter)
            }
        }
        super.onDraw(canvas)
    }
}
