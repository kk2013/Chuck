package com.chuck.jokes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.chuck.databinding.JokeItemRowBinding
import com.chuck.model.Joke
import kotlinx.android.synthetic.main.joke_item_row.view.joke_text


class JokesAdapter :
    PagedListAdapter<Joke, JokesAdapter.JokeViewHolder>(DIFF_CALLBACK) {
/*
    var jokes: List<Joke> = emptyList()

    fun loadJokes(jokes: PagedList<Joke>) {
        this.jokes = jokes
        notifyDataSetChanged()
    }*/

    override fun onBindViewHolder(holder: JokeViewHolder, position: Int) {
        val joke: Joke? = getItem(position)
        holder.bind(joke)
    }

//    override fun getItemCount(): Int = jokes.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JokeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = JokeItemRowBinding.inflate(inflater)
        return JokeViewHolder(binding)
    }

    inner class JokeViewHolder(binding: JokeItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(joke: Joke?) {
            with(itemView) {
                joke_text.text = joke?.joke
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Joke>() {
            // The ID property identifies when items are the same.
            override fun areItemsTheSame(oldItem: Joke, newItem: Joke) =
                oldItem.id == newItem.id

            // If you use the "==" operator, make sure that the object implements
            // .equals(). Alternatively, write custom data comparison logic here.
            override fun areContentsTheSame(
                oldItem: Joke, newItem: Joke
            ) = oldItem == newItem
        }
    }
}