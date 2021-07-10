package com.example.wiseposter

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.widget.Adapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.*
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_post_a_c_t.*
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.ByteArrayOutputStream


class ProfileAct : AppCompatActivity() {

    var encodeImageString:String = ""
    var pathImg: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)



        profile_swp.setOnRefreshListener {
            startActivity(Intent(this, ProfileAct::class.java))
            finish()
        }


        name_profile.text = AppInfo.name
        user_type_profile.text = AppInfo.user_type

            retrieveImage()

        if (user_type_profile.text == "Student")
                studentRetrievePosts()
        else
            adminRetrievePosts()


        img_profile.setOnClickListener {
             checkPermission()

        }

        feedback_img.setOnClickListener {
            val obj = FeedbackFragment()
            obj.show(supportFragmentManager, "feedback")

        }


    }

    private fun uploadImage()
    {

        val pd = ProgressDialog(this)
        pd.setMessage("Please wait..")
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        pd.show()

        val url:String = AppInfo.webLocal + "php_files/update_Pimg.php"

        val rq = Volley.newRequestQueue(this)
        val sr = object :StringRequest(Request.Method.POST, url, { response ->
            pd.dismiss()

        }, { error ->
            pd.dismiss()
            Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
        })
        {
            override fun getParams(): MutableMap<String, String> {
                val map = HashMap<String, String>()

                map.put("id", AppInfo.user_id)
                map.put("profile_img", encodeImageString)
                map.put("path_pic", "uploads/" + pathImg)
                return map
            }
        }
        rq.add(sr)
    }

    private fun checkPermission(){

        if(Build.VERSION.SDK_INT > 22){
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                        loadImage()


                else
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE), 222)
        }
        else
            loadImage()

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 222){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED ){
                loadImage()

            }
            else{
                Toast.makeText(this, "Can't uploud image", Toast.LENGTH_LONG).show()
            }
        }
    }

        private fun loadImage(){
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this)


        }

      override  fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && data != null && resultCode == RESULT_OK) {


                val result = CropImage.getActivityResult(data)
                val resultUri = result.uri
                img_profile.setImageURI(resultUri)
                bgImg_profile.setImageURI(resultUri)

                val inputStream = contentResolver.openInputStream(resultUri)
                val bitmap = BitmapFactory.decodeStream(inputStream)


                encodeBitmapImage(bitmap)
                uploadImage()

            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val result = CropImage.getActivityResult(data);
                val error:Exception = result.getError();
                Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
            }
      }

    private fun encodeBitmapImage(bitmap: Bitmap) {


        val byteArrayOutPutStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutPutStream)
        val byteOfImage = byteArrayOutPutStream.toByteArray()
            encodeImageString = Base64.encodeToString(byteOfImage, Base64.DEFAULT)

    }

    private fun retrieveImage() {

        var pd = ProgressDialog(this)
        pd.setMessage("Please wait..")
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        pd.show()

        val url:String = AppInfo.webLocal + "php_files/profile_img.php?id=" + AppInfo.user_id
        val rq = Volley.newRequestQueue(this)
        val jr = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            pd.dismiss()

             pathImg = response.getString("profile_img")

            if (pathImg != "null")
            {
                Picasso.get().load(AppInfo.webLocal + "php_files/uploads/" + pathImg).into(img_profile)
                Picasso.get().load(AppInfo.webLocal + "php_files/uploads/" + pathImg).into(bgImg_profile)
            }
            else
            {
                img_profile.setImageResource(R.drawable.person)
                bgImg_profile.setImageResource(R.drawable.gradient_bg)
            }
        }, { error ->
            pd.dismiss()
            Toast.makeText(this, error.message.toString(), Toast.LENGTH_LONG).show()
        })

        rq.add(jr)

    }


    private fun studentRetrievePosts()
  {
      val listModel = ArrayList<PostData>()

      val pd = ProgressDialog(this)
        pd.setMessage("Please wait..")
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        pd.show()


      val url = AppInfo.webLocal + "php_files/user_favo.php?Fuser_id=" + AppInfo.user_id
      val rq = Volley.newRequestQueue(this)
      val jar = JsonArrayRequest(Request.Method.GET, url, null, { response ->
            pd.dismiss()

            for (x in 0..response.length() - 1) {

                listModel.add(PostData(response.getJSONObject(x).getString("profile_img"),
                        response.getJSONObject(x).getString("name"),
                        response.getJSONObject(x).getString("p_date"),
                        response.getJSONObject(x).getString("p_text"),
                        response.getJSONObject(x).getString("image"),
                        response.getJSONObject(x).getInt("Fpost_id")))

            }

          val adp = ProfileAdp(this, listModel)
            profile_rv.adapter = adp
            profile_rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
          profile_rv.scrollToPosition(listModel.lastIndex)

      }, { error ->
            pd.dismiss()
            Toast.makeText(this, error.message.toString(), Toast.LENGTH_LONG).show()
        })

        rq.add(jar)




    }

    private fun adminRetrievePosts()
    {
        val listModel = ArrayList<PostData>()



        val pd = ProgressDialog(this)
        pd.setMessage("Please wait..")
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        pd.show()


        val url = AppInfo.webLocal + "php_files/admin_profile_posts.php?admin_id=" + AppInfo.user_id
        val rq = Volley.newRequestQueue(this)
        val jar = JsonArrayRequest(Request.Method.POST, url, null, { response ->
            pd.dismiss()

            for (x in 0..response.length() - 1) {


                listModel.add(PostData(response.getJSONObject(x).getString("profile_img"),
                        response.getJSONObject(x).getString("name"),
                        response.getJSONObject(x).getString("p_date"),
                        response.getJSONObject(x).getString("p_text"),
                        response.getJSONObject(x).getString("image"),
                        response.getJSONObject(x).getInt("post_id")))

            }

            val adp = ProfileAdp(this, listModel)
            profile_rv.adapter = adp
            profile_rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            profile_rv.scrollToPosition(listModel.lastIndex)

        }, { error ->
            pd.dismiss()
            Toast.makeText(this, error.message.toString(), Toast.LENGTH_LONG).show()
        })

        rq.add(jar)



    }

}

