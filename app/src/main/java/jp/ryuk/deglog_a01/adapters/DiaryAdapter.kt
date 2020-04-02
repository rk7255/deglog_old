package jp.ryuk.deglog_a01.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jp.ryuk.deglog_a01.database.Animal
import jp.ryuk.deglog_a01.databinding.DairyItemBinding

class DiaryAdapter(val clickListener: AnimalListener): ListAdapter<Animal, DiaryAdapter.ViewHolder>(DiaryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    class ViewHolder private constructor(val binding: DairyItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: Animal,
            clickListener: AnimalListener
        ) {
            binding.animal = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = DairyItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

class DiaryDiffCallback : DiffUtil.ItemCallback<Animal>() {
    override fun areItemsTheSame(oldItem: Animal, newItem: Animal): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Animal, newItem: Animal): Boolean {
        return oldItem == newItem
    }
}

class AnimalListener(val clickListener: (id: Long) -> Unit) {
    fun onClick(animal: Animal) = clickListener(animal.id)
}