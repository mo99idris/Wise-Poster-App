package com.example.wiseposter

import android.app.ProgressDialog
import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.fragment_feedback.*
import kotlinx.android.synthetic.main.fragment_feedback.view.*
import java.text.DateFormat
import java.util.*
import kotlin.collections.HashMap

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FeedbackFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FeedbackFragment : DialogFragment()  {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_feedback, container, false)

                dialog?.setTitle("Type a Feedback")
        v.send_feedback_btn.setOnClickListener {

            if (feedback_et.text.toString() != "") {

                val currentTime = Calendar.getInstance().time
                val formatedDate:String = DateFormat.getInstance().format(currentTime)

                val pd = ProgressDialog(activity)
                pd.setMessage("Please wait..")
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                pd.show()

                val url: String = AppInfo.webLocal + "php_files/report_insert.php"

                val rq = Volley.newRequestQueue(activity)
                val sr = object : StringRequest(Request.Method.POST, url, { response ->
                    pd.dismiss()
                    Toast.makeText(activity, "Sent successfully", Toast.LENGTH_LONG).show()
                    dismiss()
                }, { error ->
                    pd.dismiss()
                    Toast.makeText(activity, error.message, Toast.LENGTH_LONG).show()
                }) {
                    override fun getParams(): MutableMap<String, String> {
                        val map = HashMap<String, String>()
                        map.put("report_text", v.feedback_et.text.toString())
                        map.put("Ruser_id", AppInfo.user_id)
                        map.put("Rdate", formatedDate)
                        return map
                    }
                }
                rq.add(sr)

            } else
            {
                Toast.makeText(activity, "field is Empty", Toast.LENGTH_LONG).show()
            }
        }
        return v
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FeedbackFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                FeedbackFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}