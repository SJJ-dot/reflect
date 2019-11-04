package com.sjianjun.reflect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sjianjun.reflects.Fields
import sjj.alog.Log

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.e(Fields.getField(B::class.java,"field",true))
    }

    open class A {
        var field = "ceShi"
    }

    class B : A() {

    }

}
