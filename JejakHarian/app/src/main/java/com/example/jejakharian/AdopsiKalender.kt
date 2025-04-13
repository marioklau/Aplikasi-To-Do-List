package com.example.jejakharian

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.jejakharian.R
import com.example.jejakharian.Bantuan_Kalender
import com.example.jejakharian.Model_Kalender


class AdopsiKalender(private val context: Context, private val kalenderList: List<Model_Kalender>) : RecyclerView.Adapter<AdopsiKalender.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tv_Title)
        val tvDescription: TextView = itemView.findViewById(R.id.tv_description)
        val tvDate: TextView = itemView.findViewById(R.id.tv_date)
        val tvTime: TextView = itemView.findViewById(R.id.tv_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.note_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val kalender = kalenderList[position]
        holder.tvTitle.text = "Judul: ${kalender.judul}"
        holder.tvDescription.text = "Keterangan: ${kalender.deskripsi}"
        holder.tvDate.text = kalender.tanggal // Pastikan ini diisi dengan benar
        holder.tvTime.text = kalender.waktu // Pastikan ini diisi dengan benar
    }

    override fun getItemCount(): Int {
        return kalenderList.size
    }
}