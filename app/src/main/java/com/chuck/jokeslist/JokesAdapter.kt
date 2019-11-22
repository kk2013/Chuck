package com.chuck.jokeslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.chuck.R
import com.chuck.data.NetworkState
import com.chuck.model.Joke

class JokesAdapter
    : PagedListAdapter<Joke, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    private var networkState: NetworkState? = NetworkState.LOADING

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.joke_item_row -> (holder as JokeViewHolder).bind(getItem(position))
            R.layout.progress_item_row -> (holder as ProgressViewHolder)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            val joke = getItem(position)
            (holder as JokeViewHolder).bind(joke)
        } else {
            onBindViewHolder(holder, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.joke_item_row -> JokeViewHolder.create(parent)
            R.layout.progress_item_row -> ProgressViewHolder.create(parent)
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.progress_item_row
        } else {
            R.layout.joke_item_row
        }
    }

    override fun getItemCount(): Int = super.getItemCount() + if (hasExtraRow()) 1 else 0

    private fun hasExtraRow(): Boolean = networkState != null && networkState != NetworkState.SUCCESS

    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Joke>() {
            override fun areItemsTheSame(oldItem: Joke, newItem: Joke) = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Joke, newItem: Joke) = oldItem == newItem
        }
    }
}