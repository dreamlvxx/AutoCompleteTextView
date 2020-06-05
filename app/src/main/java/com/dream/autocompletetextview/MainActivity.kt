package com.dream.autocompletetextview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.util.Rfc822Tokenizer
import android.widget.ArrayAdapter
import android.widget.MultiAutoCompleteTextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    val data = arrayOf("lvsadf@163.com", "xxx@126.com", "ccc@kingsoft.com", "asdsad@163.com", "uuu")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, data)
        actv_content.setAdapter(adapter)
        val tt = CommonTokenizer(';')
        actv_content.setTokenizer(tt)
    }
}