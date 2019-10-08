package com.chuck.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chuck.R
import com.chuck.databinding.JokeItemRowBinding
import com.chuck.model.Joke
import kotlinx.android.synthetic.main.joke_item_row.view.*


class JokesAdapter() :
    RecyclerView.Adapter<JokesAdapter.ViewHolder>() {

    var jokes: List<Joke> = emptyList()

    fun loadJokes(jokes: List<Joke>) {
        this.jokes = jokes
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(jokes[position])

    override fun getItemCount(): Int = jokes.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = JokeItemRowBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    inner class ViewHolder(binding: JokeItemRowBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(joke: Joke) {
            with(itemView) {
                joke_text.text = joke.joke
            }
        }
    }

}