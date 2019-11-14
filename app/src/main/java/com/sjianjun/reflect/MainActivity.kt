package com.sjianjun.reflect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sjianjun.reflects.Fields
import com.sjianjun.reflects.Methods
import sjj.alog.Log

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.e("staticField " + Fields.getStaticValue(TestField::class.java, Fields.getField(TestField::class.java, "staticField")))
        Log.e("staticField " + Fields.getStaticValue(TestField::class.java, "staticField"))
        Log.e("staticField " + Fields.getStaticValue("com.sjianjun.reflect.TestField", "staticField"))
        Log.e("staticField " + Fields.getField("com.sjianjun.reflect.TestField", "staticField")?.name)

        Log.e("==========test set field value")
        val field = TestField()
        Log.e(field.field)
        Log.e("Field class ${Fields.getField(field::class.java, "field")?.toString()} ${Fields.getField(field::class.java, "staticField")}")
        Log.e(Fields.setValue(field, Fields.getField(field::class.java, "field"), 123))
        Log.e(Fields.setStaticValue(Fields.getField(field::class.java, "staticField"), "new staticField value"))
        Log.e("${field.field}   ${TestField.staticField}")

        val value = Fields.getStaticValue("com.android.internal.R\$dimen", "status_bar_height")
        Log.e("status bar height :${resources.getDimensionPixelSize(value.toString().toInt())}")

        Log.e(Methods.getMethod(this::class.java,"setTheme", arrayOf(Int::class.java)))

        Log.e(Methods.getMethod(TestField::class.java,"testM", arrayOf(Integer::class.java,Int::class.java)))
        Methods.invokeStatic(Methods.getMethod(TestField::class.java, "testStatic", arrayOf(String::class.java, String::class.java)), arrayOf("aaaabbb", "ccccdddd"))
        Log.e(Methods.newInstance("com.sjianjun.reflect.TestField",null))
    }

}
