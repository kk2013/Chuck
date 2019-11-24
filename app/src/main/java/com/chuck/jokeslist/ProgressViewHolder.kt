package com.chuck.jokeslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chuck.R

class ProgressViewHolder(view: View) :
    RecyclerView.ViewHolder(view) {

    companion object {
        fun create(parent: ViewGroup): ProgressViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.progress_item_row, parent, false)
            return ProgressViewHolder(view)
        }
    }
}
