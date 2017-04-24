package com.huelvadevelopers.proyectozero

/**
 * Created by DrAP on 24/04/2017.
 */

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.main_activity.*

public class MainActivity: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)
        myMessage.setText("Hello Proyecto Zero")

    }
}