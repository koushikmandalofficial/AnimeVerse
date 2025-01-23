package com.sekho.animeverse.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sekho.animeverse.databinding.ItemProducerBinding
import com.sekho.animeverse.model.Producer

class ProducersAdapter(private val producers: List<Producer>) : RecyclerView.Adapter<ProducersAdapter.ProducerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProducerViewHolder {
        val binding = ItemProducerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProducerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProducerViewHolder, position: Int) {
        val producer = producers[position]
        holder.bind(producer)
    }

    override fun getItemCount(): Int = producers.size

    inner class ProducerViewHolder(private val binding: ItemProducerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(producer: Producer) {
            binding.producerNameTextView.text = producer.name
        }
    }
}
