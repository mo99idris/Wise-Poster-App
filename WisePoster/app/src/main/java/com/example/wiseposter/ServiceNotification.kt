package com.example.wiseposter

import android.app.IntentService
import android.content.Intent
import android.util.Log

import android.widget.Toast
import com.android.volley.Request

import com.android.volley.toolbox.JsonObjectRequest

import com.android.volley.toolbox.Volley


class ServiceNotification : IntentService("ServiceNotification") {

    var notifyID:Int = 0
    var url:String = AppInfo.webLocal + "php_files/checkNotify.php"


    override fun onHandleIntent(p0: Intent?) {
        var msg:String = "There's new Announce!!"


        try {


            val rq = Volley.newRequestQueue(this)
            val jr = JsonObjectRequest(Request.Method.GET, url, null, { response ->

                if (response.toString().isNotEmpty())
                {
                    notifyID = response.getInt("post_id")

                }

            }, { error ->

            })

            rq.add(jr)

            while (true)
            {
                val rq = Volley.newRequestQueue(this)
                val jr = JsonObjectRequest(Request.Method.GET, url, null, { response ->

                    if (response.toString().isNotEmpty())
                    {

                        if (response.getInt("post_id") > notifyID && response.getString("admin_id") != AppInfo.user_id)
                        {
                            val i = Intent()
                            i.action = "com.example.Broadcast"
                            i.putExtra("msg", msg)
                            sendBroadcast(i)
                            notifyID = response.getInt("post_id")

                        }

                    }

                }, { error ->

                })

                rq.add(jr)

                Thread.sleep(5000)

            }

        }catch (e: InterruptedException) {
            Thread.currentThread().interrupt()

        }

    }


}