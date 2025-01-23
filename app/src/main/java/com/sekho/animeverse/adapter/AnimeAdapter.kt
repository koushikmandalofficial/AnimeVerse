package com.sekho.animeverse.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.makeramen.roundedimageview.RoundedImageView
import com.sekho.animeverse.R
import com.sekho.animeverse.model.AnimeModel
import com.sekho.animeverse.ui.TrailerActivity

class AnimeAdapter(
    private val context: Context,
    private val vodList: List<AnimeModel>
) : RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_anime, parent, false)
        return AnimeViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnimeViewHolder, position: Int) {
        val animeModel = vodList[position]
        holder.bind(animeModel)

        if(animeModel.episodes == "1") {
            holder.episodeTxt.text = animeModel.episodes+" Episode"
        }
        else {
            holder.episodeTxt.text = animeModel.episodes + " Episodes"
        }

        holder.ratingTxt.text = " ⭐ "+animeModel.score+" "

        holder.nameTxt.text = animeModel.contName

        holder.itemView.setOnClickListener {
            val intent = Intent(context, TrailerActivity::class.java)
            intent.putExtra("contentId", animeModel.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = vodList.size

    inner class AnimeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val animeImage: RoundedImageView = view.findViewById(R.id.animeImage)
        val ratingTxt: TextView = view.findViewById(R.id.ratingTxt)
        val episodeTxt: TextView = view.findViewById(R.id.episodeTxt)
        val nameTxt: TextView = view.findViewById(R.id.nameTxt)

        fun bind(animeModel: AnimeModel) {
            Glide.with(context).load(animeModel.posterImage).error(R.drawable.land_load).placeholder(R.drawable.land_load).into(animeImage)
        }
    }
}
