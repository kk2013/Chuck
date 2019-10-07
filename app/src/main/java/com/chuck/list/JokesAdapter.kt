package com.chuck.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chuck.R
import com.chuck.model.Joke
import kotlinx.android.synthetic.main.joke_item_row.view.*


class JokesAdapter(private val jokes: List<Joke>) : RecyclerView.Adapter<JokesAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = jokes[position].run {
        holder.updateText(this)
    }

    override fun getItemCount(): Int = jokes.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return LayoutInflater.from(parent.context).inflate(R.layout.joke_item_row, parent, false).run {
            ViewHolder(this)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        internal fun updateText(joke: Joke) {
//            itemView.joke_text.text = joke.jokeString
        }
    }


}