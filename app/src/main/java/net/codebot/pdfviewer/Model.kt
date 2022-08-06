package net.codebot.pdfviewer

import android.graphics.*

import android.graphics.Region
import android.view.WindowInsetsAnimation.Bounds
import java.util.*

var annos: ArrayList<Anno> = ArrayList()
// the annotations for all the pages
var pages: ArrayList<ArrayList<Anno>> = ArrayList()
class Anno(paths: Path, tool: String, visibility: Boolean) {
    var paths: Path
    var tool: String
    var visibility: Boolean
    init {
        this.paths = paths
        this.tool = tool
        this.visibility = visibility
    }
}

fun createReligion (path: Path, region: Region, rect: RectF){
    path.computeBounds(rect, true)
    if (rect.top == rect.bottom) {
        region.set(
            Rect(
                rect.left.toInt(),
                rect.top.toInt(),
                rect.right.toInt(),
                (rect.bottom + 0.2).toInt()
            )
        )
    } else if (rect.left == rect.right) {
        region.set(
            Rect(
                rect.left.toInt(),
                rect.top.toInt(),
                (rect.right + 0.2).toInt(),
                rect.bottom.toInt()
            )
        )
    } else {
        region.setPath(
            path,
            Region(
                rect.left.toInt(),
                rect.top.toInt(),
                rect.right.toInt(),
                rect.bottom.toInt()
            )
        )
    }
}
var Total = 55
var CURRENT = 0
var Tool = "Hand"

var undo: Stack<Anno> = Stack()
var redo: Stack<Anno> = Stack()

