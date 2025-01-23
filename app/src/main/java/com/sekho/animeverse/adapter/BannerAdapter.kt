package com.sekho.animeverse.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sekho.animeverse.R
import com.sekho.animeverse.model.BannerModel
import com.sekho.animeverse.ui.TrailerActivity

class BannerAdapter(
    private val context: Context,
    private val bannerList: List<BannerModel>
) : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_banner, parent, false)
        return BannerViewHolder(view)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        val banner = bannerList[position]
        Glide.with(context).load(banner.banLink).error(R.drawable.land_load).placeholder(R.drawable.land_load).into(holder.imageView)
        holder.bannertxt.text = banner.contName
        if(banner.episodes == "1") {
            holder.catagorytxt.text = banner.episodes + " Episode | " + banner.score+" Rating"
        }
        else {

            holder.catagorytxt.text = banner.episodes + " Episodes | " + " ⭐ "+banner.score
        }

        holder.watchnowBtn.setOnClickListener {
            val intent = Intent(context, TrailerActivity::class.java)
            intent.putExtra("contentId", banner.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = bannerList.size

    inner class BannerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val watchnowBtn: CardView = view.findViewById(R.id.watchnowBtn)
        val  bannertxt: TextView = view.findViewById(R.id.bannertxt)
        val  catagorytxt: TextView = view.findViewById(R.id.catagorytxt)

    }
}
