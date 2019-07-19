/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.marsrealestate.overview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.marsrealestate.data.local.MarsEntity
import com.example.android.marsrealestate.databinding.GridViewItemBinding

class PhotoGridAdapter(val onClickListener: OnClickListener) : ListAdapter<MarsEntity, PhotoGridAdapter.MarsPropertyViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            MarsPropertyViewHolder {
        return MarsPropertyViewHolder(GridViewItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: MarsPropertyViewHolder, position: Int) {
        val marsProperty = getItem(position)
        holder.bind(marsProperty)
        holder.itemView.setOnClickListener {
            onClickListener.clickListener(marsProperty)
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<MarsEntity>() {
        override fun areItemsTheSame(oldItem: MarsEntity, newItem: MarsEntity): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: MarsEntity, newItem: MarsEntity): Boolean {
            return oldItem.id == newItem.id
        }
    }

    class MarsPropertyViewHolder(private var binding: GridViewItemBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun bind(marsEntity: MarsEntity) {
            binding.property = marsEntity
            binding.executePendingBindings()
        }
    }

    class OnClickListener(val clickListener: (marsEntity: MarsEntity) -> Unit) {
        fun onClick(marsEntity: MarsEntity) = clickListener(marsEntity)
    }
}