package com.huelvadevelopers.proyectozero

import android.graphics.Point
import android.view.View



/**
 * Created by DrAP on 04/07/2017.
 */

class CustomDragShadowBuilder(internal var v: View) : View.DragShadowBuilder(v) {

    override fun onProvideShadowMetrics(shadowSize: Point, touchPoint: Point) {
        shadowSize.set(v.width, v.height)
        touchPoint.set(v.width /4, v.height /2)
    }
}