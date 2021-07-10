package com.example.wiseposter



import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.report_layout.view.*


class ReportAdp(var con: Context, var listModel: ArrayList<ReportData>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

            return RH(LayoutInflater.from(con).inflate(R.layout.report_layout, parent, false))

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        (holder as RH).show(listModel[position].Ruser_name, listModel[position].Ruser_id, listModel[position].Rtext
        , listModel[position].Rdate)

    }

    override fun getItemCount(): Int {
        return listModel.size
    }


    class RH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun show(Ruser_name:String, Ruser_id:String, Rtext:String, Rdate:String) {

            itemView.Ruser_name_tv.text = Ruser_name
            itemView.Ruser_id_tv.text = Ruser_id
            itemView.Rtext_tv.text = Rtext
            itemView.Rdate_tv.text = Rdate

        }

    }





}