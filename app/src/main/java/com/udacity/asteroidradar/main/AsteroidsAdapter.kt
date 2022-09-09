package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.ItemMainBinding
import com.udacity.asteroidradar.domain.Asteroid

class AsteroidsAdapter(val onClickListener: OnClickListener) : RecyclerView.Adapter<AsteroidsAdapter.AsteroidHolder>() {

//    private lateinit var asteroids: List<Asteroid>
    var asteroids = listOf<Asteroid>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

//    fun setData(list: List<Asteroid>) {
//        asteroids = list
//        notifyDataSetChanged()
//    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidHolder {
        return AsteroidHolder(ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: AsteroidHolder, position: Int) {
        var asteroid = asteroids[position]
        holder.itemView.setOnClickListener{
            onClickListener.onClick(asteroid)
        }
        holder.bind(asteroid)
    }
    override fun getItemCount(): Int {
        return asteroids.size
    }

    class AsteroidHolder(private var binding: ItemMainBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(asteroid: Asteroid) {
            binding.asteroid = asteroid
            binding.executePendingBindings()
        }
    }

    class OnClickListener(val clickListener: (asteroid:Asteroid) -> Unit) {
        fun onClick(asteroid: Asteroid) = clickListener(asteroid)
    }
}