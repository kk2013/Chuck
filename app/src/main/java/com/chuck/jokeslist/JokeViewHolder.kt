package com.chuck.jokeslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chuck.R
import com.chuck.model.Joke
import kotlinx.android.synthetic.main.joke_item_row.view.joke_text

class JokeViewHolder(view: View) :
    RecyclerView.ViewHolder(view) {

    fun bind(joke: Joke?) {
        with(itemView) {
            joke_text.text = joke?.joke
        }
    }

    companion object {
        fun create(parent: ViewGroup): JokeViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.joke_item_row, parent, false)
            return JokeViewHolder(view)
        }
    }
}