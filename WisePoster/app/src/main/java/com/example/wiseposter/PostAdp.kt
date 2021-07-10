package com.example.wiseposter



import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.post_layout.view.*
import kotlinx.android.synthetic.main.post_no_image_layout.view.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class PostAdp(var con: Context, var listModel: ArrayList<PostData>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mainList = listModel
    private val searchList = ArrayList<PostData>(listModel)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == 1)
            return PIH(LayoutInflater.from(con).inflate(R.layout.post_layout, parent, false))
        else
            return PNH(
                LayoutInflater.from(con).inflate(R.layout.post_no_image_layout, parent, false)
            )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


        if (listModel[position].photo != "null") {
            (holder as PIH).show(
                listModel[position].profile_img, listModel[position].user_name,
                listModel[position].date, listModel[position].tweet, listModel[position].photo
            )


            if (AppInfo.user_type == "Student") {


                val url = AppInfo.webLocal + "php_files/checkSave.php?Fuser_id=" + AppInfo.user_id
                val rq = Volley.newRequestQueue(con)
                val jar = JsonArrayRequest(Request.Method.GET, url, null, { response ->

                    if (response != null) {
                        for (x in 0 until response.length()) {
                            if (response.getJSONObject(x)
                                    .getString("Fpost_id") == listModel[position].post_id.toString()
                            ) {
                                (holder).itemView.tweet_btn.isEnabled = false
                                (holder).itemView.tweet_btn.text = con.getText(R.string.saved_btn)
                                (holder).itemView.tweet_btn.setTextColor(Color.GREEN)
                            }
                        }
                    }

                }, { error ->

                    Toast.makeText(con, error.message.toString(), Toast.LENGTH_LONG).show()
                })

                rq.add(jar)

                (holder).itemView.tweet_btn.setOnClickListener {

                    savePost(position)
                }


            } else {
                (holder).itemView.tweet_btn.visibility = View.INVISIBLE

            }

        } else {
            (holder as PNH).show(
                listModel[position].profile_img, listModel[position].user_name,
                listModel[position].date, listModel[position].tweet
            )



            if (AppInfo.user_type == "Student") {


                val url = AppInfo.webLocal + "php_files/checkSave.php?Fuser_id=" + AppInfo.user_id
                val rq = Volley.newRequestQueue(con)
                val jar = JsonArrayRequest(Request.Method.GET, url, null, { response ->

                    if (response != null) {
                        for (x in 0 until response.length()) {
                            if (response.getJSONObject(x)
                                    .getString("Fpost_id") == listModel[position].post_id.toString()
                            ) {
                                (holder).itemView.tweet_PNH_btn.isEnabled = false
                                (holder).itemView.tweet_PNH_btn.text = con.getText(R.string.saved_btn)
                                (holder).itemView.tweet_PNH_btn.setTextColor(Color.GREEN)
                            }
                        }
                    }

                }, { error ->

                    Toast.makeText(con, error.message.toString(), Toast.LENGTH_LONG).show()
                })

                rq.add(jar)

                (holder).itemView.tweet_PNH_btn.setOnClickListener {

                    savePost(position)

                }


            } else {
                (holder).itemView.tweet_PNH_btn.visibility = View.INVISIBLE

            }


        }

    }

    override fun getItemCount(): Int {
        return listModel.size
    }

    override fun getItemViewType(position: Int): Int {
        if (listModel[position].photo != "null")
            return 1
        else
            return 2
    }

    class PIH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun show(
            profile_img: String,
            user_name: String,
            date: String,
            tweet: String,
            photo: String
        ) {

            var ph: String = profile_img.replace(" ", "%20")
            Picasso.get().load(AppInfo.webLocal + "php_files/uploads/" + ph)
                .into(itemView.tweet_profile_img)
            itemView.tweet_username.text = user_name
            itemView.tweet_date_txt.text = date
            itemView.tweet_txt.text = tweet
            ph = photo.replace(" ", "%20")
            Picasso.get().load(AppInfo.webLocal + "php_files/uploads/" + ph)
                .into(itemView.tweet_photo)

        }

    }

    class PNH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun show(profile_img: String, user_name: String, date: String, tweet: String) {
            var ph = profile_img.replace(" ", "%20")
            Picasso.get().load(AppInfo.webLocal + "php_files/uploads/" + ph)
                .into(itemView.tweet_PNH_profile_img)
            itemView.tweet_PNH_userName.text = user_name
            itemView.tweet_PNH_date.text = date
            itemView.tweet_PNH_text.text = tweet
        }
    }

    private fun savePost(position: Int) {

        val pd = ProgressDialog(con)
        pd.setTitle("Please wait..")
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        pd.show()

        val url: String = AppInfo.webLocal + "php_files/save_post.php"

        val rq = Volley.newRequestQueue(con)
        val sr = object : StringRequest(Request.Method.POST, url, Response.Listener { response ->
            pd.dismiss()
            notifyItemChanged(position)
        },
            Response.ErrorListener { error ->
                pd.dismiss()
                Toast.makeText(con, error.message, Toast.LENGTH_LONG).show()
            }) {
            override fun getBodyContentType(): String {
                return "application/x-www-form-urlencoded; charset=UTF-8"
            }

            override fun getParams(): MutableMap<String, String> {
                val map = HashMap<String, String>()

                map.put("Fpost_id", listModel[position].post_id.toString())
                map.put("Fuser_id", AppInfo.user_id)

                return map
            }

        }


        rq.add(sr)

    }

    fun filterList(filteredList: ArrayList<PostData>) {
        listModel = filteredList
        notifyDataSetChanged()
    }

}