package com.example.android.politicalpreparedness.election.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.databinding.ElectionListItemBinding
import com.example.android.politicalpreparedness.network.models.Election

class ElectionListAdapter(private val clickListener: ElectionListener): ListAdapter<Election,
        ElectionViewHolder>(ElectionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
        return ElectionViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        val electionItem = getItem(position) as Election
        holder.itemView.setOnClickListener{
            clickListener.onClick(electionItem)
        }
        holder.bind(electionItem)
    }

}

class ElectionViewHolder(private val binding: ElectionListItemBinding) : RecyclerView.ViewHolder
(binding.root){


    fun bind(item: Election) {
        binding.election = item
        binding.executePendingBindings()
    }


    companion object {
        fun from(parent: ViewGroup): ElectionViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ElectionListItemBinding.inflate(layoutInflater, parent, false)
            return ElectionViewHolder(binding)
        }
    }
}

class ElectionDiffCallback : DiffUtil.ItemCallback<Election>() {


    override fun areItemsTheSame(oldItem: Election, newItem: Election): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Election, newItem: Election): Boolean {
        return oldItem == newItem
    }
}
interface ElectionListener {

    fun onClick(election: Election)
}