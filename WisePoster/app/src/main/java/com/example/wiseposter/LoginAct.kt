package com.example.wiseposter

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley

import kotlinx.android.synthetic.main.activity_login.*

open class LoginAct : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


       checkPermission()

        val sp = getSharedPreferences("cookie", MODE_PRIVATE)
        val editor = sp.edit()


        if (sp.getString("user_id", "") != ""
                && sp.getString("pwd", "") != ""
                && sp.getString("name", "") != ""
                && sp.getString("user_type", "") != "")
                {
                    if (sp.getString("user_id", "") == "000"
                        && sp.getString("pwd", "") == "000")
                    {
                        startActivity(Intent(this, ReportACT::class.java))
                        finish()
                    }

                    else
                    {
                        AppInfo.user_id = sp.getString("user_id", "").toString()
                        AppInfo.user_type = sp.getString("user_type", "").toString()
                        AppInfo.name = sp.getString("name", "").toString()

                        startActivity(Intent(this, DashboardACT::class.java))
                        finish()
                    }

                }


    //    forceLtR()

       login_btn.setOnClickListener {



           val pd = ProgressDialog(this)
           pd.setMessage("Please wait..")
           pd.setProgressStyle(ProgressDialog.STYLE_SPINNER)
           pd.show()

           val url:String = AppInfo.webLocal + "php_files/login.php"

           val rq = Volley.newRequestQueue(this)
           val jar = JsonArrayRequest(Request.Method.POST, url, null, {
               response -> pd.dismiss()

               var pwd:String = ""
              var flag:Boolean = true

               if (login_userID_et.text.toString() != ""
                       && login_pwd_et.text.toString() != "") {
                   for (x in 0..response.length() - 1)
                   {
                       if (login_userID_et.text.toString() == response.getJSONObject(x).getString("id")
                               && login_pwd_et.text.toString() == response.getJSONObject(x).getString("password")) {

                           pwd = response.getJSONObject(x).getString("password")
                           AppInfo.user_id = response.getJSONObject(x).getString("id")

                           AppInfo.name = response.getJSONObject(x).getString("name")


                          flag = false


                           if (response.getJSONObject(x).getString("user-type") == "admin")
                           {
                               AppInfo.user_type = "Admin"
                           }
                           else
                           {
                               AppInfo.user_type = "Student"
                           }

                           if (login_remember_ch.isChecked)
                           {
                               editor.putString("name", AppInfo.name)
                               editor.putString("user_type", AppInfo.user_type)
                               editor.putString("user_id", AppInfo.user_id)
                               editor.putString("pwd", pwd)
                               editor.commit()
                           }

                           if (AppInfo.user_id == "000"
                               && pwd == "000")
                           {
                               startActivity(Intent(this, ReportACT::class.java))
                               finish()
                               break
                           }

                           startActivity(Intent(this, DashboardACT::class.java))
                           finish()
                           break
                       }

                      else if (login_userID_et.text.toString() == response.getJSONObject(x).getString("id")
                               && login_pwd_et.text.toString() != response.getJSONObject(x).getString("password"))
                       {
                           Toast.makeText(this, "Your Password is wrong", Toast.LENGTH_LONG).show()
                           login_pwd_et.setText("")
                           flag = false
                           break
                       }

                   }
                    if (flag == true)
                    {
                        Toast.makeText(this, "ID not found!", Toast.LENGTH_LONG).show()
                        login_pwd_et.setText("")
                        login_userID_et.setText("")
                    }

               }
               else if (login_userID_et.text.toString() != ""
                       && login_pwd_et.text.toString() == "")
               {
                   Toast.makeText(this, "Enter Password ", Toast.LENGTH_LONG).show()

               }
               else if (login_userID_et.text.toString() == ""
                       && login_pwd_et.text.toString() != "")
               {
                   Toast.makeText(this, "Enter ID ", Toast.LENGTH_LONG).show()

               }
               else
               {
                   Toast.makeText(this, "Enter ID and Password", Toast.LENGTH_LONG).show()


               }
           }, {
               error -> pd.dismiss()
               Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
           })

           rq.add(jar)

       }
    }

    private fun checkPermission(){

        if(Build.VERSION.SDK_INT > 22){
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED)

                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_NETWORK_STATE), 111)

        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 111)
        {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED
                    && grantResults[1] == PackageManager.PERMISSION_DENIED)
                Toast.makeText(this, "Need Permissions To Use Features App", Toast.LENGTH_LONG).show()

        }
    }


//    private fun forceLtR() {
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
//            window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR
//        }
//    }
}