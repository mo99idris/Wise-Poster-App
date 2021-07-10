package com.example.wiseposter

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.print.PrintAttributes
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.marginLeft
import kotlinx.android.synthetic.main.activity_lng_a_c_t.*
import kotlinx.android.synthetic.main.edit_toolbar.*
import java.util.*

class LngACT : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lng_a_c_t)




        val lng_title = title_toolbar
        lng_title.text = getText(R.string.lng_title)
        arrow_back_toolbar.setOnClickListener {
            onBackPressed()
        }

        tv_ar.setOnClickListener {
            changeLng("ar")

        }

        tv_en.setOnClickListener {
            changeLng("en")

        }

    }
    fun changeLng(lng: String){

        val loc = Locale(lng)
        Locale.setDefault(loc)
        val conf = Configuration()
        conf.locale = loc

        baseContext.resources.updateConfiguration(conf, baseContext.resources.displayMetrics)

        val i = Intent(this, DashboardACT::class.java)
        startActivity(i)
        finishAffinity()

    }


}