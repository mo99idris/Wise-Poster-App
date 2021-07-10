package com.example.wiseposter



import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.view.*
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.post_layout.view.*
import kotlinx.android.synthetic.main.post_no_image_layout.view.*
import kotlinx.android.synthetic.main.profile_post_admin.view.*
import kotlinx.android.synthetic.main.profile_post_admin_pn.view.*


class ProfileAdp(var con:Context, var listModel: ArrayList<PostData>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var i = Intent(con, AddPostAct::class.java)

    override fun getItemViewType(position: Int): Int {

        if (listModel[position].photo != "null")
            return 1
        else
            return 2

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if(viewType == 1)
            return PIHA(LayoutInflater.from(con).inflate(R.layout.profile_post_admin, parent, false))

        else
            return PNHA(LayoutInflater.from(con).inflate(R.layout.profile_post_admin_pn, parent, false))

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            if (AppInfo.user_type == "Student")
            {
                if (listModel[position].photo != "null")
                {
                    (holder as PIHA).show(listModel[position].profile_img, listModel[position].user_name,
                            listModel[position].date, listModel[position].tweet, listModel[position].photo)


                    (holder).itemView.popup_menu_PIHA.setImageResource(R.drawable.baseline_bookmark_remove_black_24)

                    (holder).itemView.popup_menu_PIHA.setOnClickListener {

                        unSaveStudent(position)

                    }
                }
                else
                {
                    (holder as PNHA).show(listModel[position].profile_img, listModel[position].user_name,
                            listModel[position].date, listModel[position].tweet)

                    (holder).itemView.popup_menu_PNHA.setImageResource(R.drawable.baseline_bookmark_remove_black_24)


                    (holder).itemView.popup_menu_PNHA.setOnClickListener {

                        unSaveStudent(position)

                    }

                }
            }
        else
            {

                if (listModel[position].photo != "null")
                {
                    (holder as PIHA).show(listModel[position].profile_img, listModel[position].user_name,
                            listModel[position].date, listModel[position].tweet, listModel[position].photo)


                    val popopMenu = PopupMenu(con, (holder).itemView.popup_menu_PIHA)


                    popopMenu.menuInflater.inflate(R.menu.popup_menu, popopMenu.menu)

                    popopMenu.setOnMenuItemClickListener {
                        MenuItem ->

                        val id = MenuItem.itemId

                        if (id == R.id.edit_menu)
                        {

                           AppInfo.tweet_update = listModel[position].tweet
                           AppInfo.photo_update = listModel[position].photo
                           AppInfo.post_id = listModel[position].post_id
                            AppInfo.flagEdit = true
                            con.startActivity(i)

                        }

                        else if(id == R.id.delete_menu)
                        {
                           deletePostAdmin(position)

                        }


                        false
                    }


                    (holder).itemView.popup_menu_PIHA.setOnClickListener {
                        popopMenu.show()

                    }

                }
                else
                {
                    (holder as PNHA).show(listModel[position].profile_img, listModel[position].user_name,
                            listModel[position].date, listModel[position].tweet)



                    val popopMenu = PopupMenu(con, (holder).itemView.popup_menu_PNHA)


                    popopMenu.menuInflater.inflate(R.menu.popup_menu, popopMenu.menu)

                    popopMenu.setOnMenuItemClickListener {
                        MenuItem ->

                        val id = MenuItem.itemId

                        if (id == R.id.edit_menu)
                        {

                            AppInfo.tweet_update = listModel[position].tweet
                            AppInfo.post_id = listModel[position].post_id
                            AppInfo.flagEdit = true
                            AppInfo.photo_update = ""
                            con.startActivity(i)

                        }

                        else if(id == R.id.delete_menu)
                        {
                            deletePostAdmin(position)
                        }


                        false
                    }


                    (holder).itemView.popup_menu_PNHA.setOnClickListener {
                        popopMenu.show()
                    }

                }
            }



    }

    override fun getItemCount(): Int {
        return listModel.size
    }


    class PIHA(itemView: View): RecyclerView.ViewHolder(itemView){

        fun show(profile_img:String, user_name:String, date:String, tweet:String, photo:String){

            var ph:String = profile_img.replace(" ", "%20")
            Picasso.get().load(AppInfo.webLocal + "php_files/uploads/" + ph).into(itemView.tweet_PIHA_profile_img)
            itemView.tweet_PIHA_username.text = user_name
            itemView.tweet_PIHA_date_txt.text = date
            itemView.tweet_PIHA_txt.text = tweet
            ph = photo.replace(" ", "%20")
            Picasso.get().load(AppInfo.webLocal + "php_files/uploads/" + ph).into(itemView.tweet_PIHA_photo)

        }

    }

    class PNHA(itemView: View): RecyclerView.ViewHolder(itemView){

        fun show(profile_img: String, user_name: String, date: String, tweet: String)
        {
            var ph = profile_img.replace(" ", "%20")
            Picasso.get().load(AppInfo.webLocal + "php_files/uploads/" + ph).into(itemView.tweet_PNHA_profile_img)
            itemView.tweet_PNHA_username.text = user_name
            itemView.tweet_PNHA_date_txt.text = date
            itemView.tweet_PNHA_txt.text = tweet
        }
    }

    fun unSaveStudent(position: Int)
    {
        val pd = ProgressDialog(con)
        pd.setMessage("Please wait..")
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        pd.show()

        val url:String = AppInfo.webLocal + "php_files/unSavedStudent.php"

        val rq = Volley.newRequestQueue(con)
        val sr = object :StringRequest(Request.Method.POST, url, {
                response -> pd.dismiss()

            listModel.removeAt(position)
            notifyDataSetChanged()
        }, {
                error -> pd.dismiss()
            con.startActivity(Intent(con, ProfileAct::class.java))
            (con as ProfileAct).finish()
        })
        {
            override fun getParams(): MutableMap<String, String> {
                val map = HashMap<String,String>()

                map.put("Fpost_id", listModel[position].post_id.toString())
                map.put("Fuser_id", AppInfo.user_id)

                return map
            }
        }

        rq.add(sr)
    }

    fun deletePostAdmin(position: Int)
    {
        val pd = ProgressDialog(con)
        pd.setMessage("Please wait..")
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        pd.show()

        val url:String = AppInfo.webLocal + "php_files/adminDelete.php"

        val rq = Volley.newRequestQueue(con)
        val sr = object :StringRequest(Request.Method.POST, url, {
                response -> pd.dismiss()

            listModel.removeAt(position)
            notifyDataSetChanged()
        }, {
                error -> pd.dismiss()
            con.startActivity(Intent(con, ProfileAct::class.java))
            (con as ProfileAct).finish()
        })
        {
            override fun getParams(): MutableMap<String, String> {
                val map = HashMap<String,String>()

                map.put("post_id", listModel[position].post_id.toString())
                map.put("path_pic", "uploads/" + listModel[position].photo)

                return map
            }
        }

        rq.add(sr)
    }
}
