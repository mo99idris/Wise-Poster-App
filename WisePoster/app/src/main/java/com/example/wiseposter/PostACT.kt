package com.example.wiseposter

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.wiseposter.AppInfo.Companion.user_type
import kotlinx.android.synthetic.main.activity_post_a_c_t.*
import kotlinx.android.synthetic.main.search_toolbar.*

class PostACT : AppCompatActivity() {
    lateinit var adp: PostAdp
    var listModel = ArrayList<PostData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_a_c_t)





        if(user_type == "Student")
            fab.visibility = View.INVISIBLE

        else
            fab.visibility = View.VISIBLE


        fab.setOnClickListener {
            startActivity(Intent(this, AddPostAct::class.java))
            AppInfo.tweet_update = ""
            AppInfo.photo_update = ""
            AppInfo.flagEdit = false

        }




        val pd = ProgressDialog(this)
        pd.setMessage("Please wait..")
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        pd.show()


        val url = AppInfo.webLocal + "php_files/retrievePosts.php"
        val rq = Volley.newRequestQueue(this)
        val jar = JsonArrayRequest(Request.Method.POST, url, null, {
                response -> pd.dismiss()


            for (x in 0..response.length()-1)
            {


                listModel.add(PostData(response.getJSONObject(x).getString("profile_img"),
                    response.getJSONObject(x).getString("name"),
                    response.getJSONObject(x).getString("p_date"),
                    response.getJSONObject(x).getString("p_text"),
                    response.getJSONObject(x).getString("image"),
                    response.getJSONObject(x).getInt("post_id")))

            }


            adp = PostAdp(this, listModel)
            post_rv.adapter = adp
            post_rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        }, {
                error -> pd.dismiss()


            Toast.makeText(this, error.message.toString(), Toast.LENGTH_LONG).show()
        })

        rq.add(jar)



        post_swp.setOnRefreshListener {
            startActivity(Intent(this, PostACT::class.java))
            finish()

        }

        search_field_et.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable) {
                filter(p0.toString())
            }
        })


    }

    private fun filter(text: String) {
        val filteredList: ArrayList<PostData> = ArrayList()
        for (item in listModel) {
            if (item.user_name.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item)
            }
        }
        adp.filterList(filteredList)
    }

}