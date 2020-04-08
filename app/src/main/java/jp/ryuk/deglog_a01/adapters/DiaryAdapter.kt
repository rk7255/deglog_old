package jp.ryuk.deglog_a01.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jp.ryuk.deglog_a01.database.Diary
import jp.ryuk.deglog_a01.databinding.DiaryItemBinding

class DiaryAdapter(val clickListener: DiaryListener) : ListAdapter<Diary, DiaryAdapter.ViewHolder>(DiaryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    class ViewHolder private constructor(val binding: DiaryItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Diary, clickListener: DiaryListener) {
            binding.diary = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = DiaryItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

class DiaryDiffCallback : DiffUtil.ItemCallback<Diary>() {
    override fun areItemsTheSame(oldItem: Diary, newItem: Diary): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Diary, newItem: Diary): Boolean {
        return oldItem == newItem
    }
}

class DiaryListener(val clickListener: (id: Long) -> Unit) {
    fun onClick(diary: Diary) = clickListener(diary.id)
}