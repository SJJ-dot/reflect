package com.sjianjun.reflect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sjianjun.reflects.Fields
import sjj.alog.Log

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.e("staticField "+Fields.getStaticValue(TestField::class.java,Fields.getField(TestField::class.java,"staticField")))
        Log.e("staticField "+Fields.getStaticValue(TestField::class.java,"staticField"))
        Log.e("staticField "+Fields.getStaticValue("com.sjianjun.reflect.TestField","staticField"))
        Log.e("staticField "+Fields.getField("com.sjianjun.reflect.TestField","staticField")?.name)

        Log.e("==========test set field value")
        val field = TestField()
        Log.e(field.field)
        Log.e("Field class ${Fields.getField(field::class.java,"field")?.toString()} ${Fields.getField(field::class.java,"staticField")}")
        Log.e(Fields.setValue(field,Fields.getField(field::class.java,"field"),123))
        Log.e(Fields.setStaticValue(Fields.getField(field::class.java,"staticField"),"new staticField value"))
        Log.e("${field.field}   ${TestField.staticField}")

    }

}
