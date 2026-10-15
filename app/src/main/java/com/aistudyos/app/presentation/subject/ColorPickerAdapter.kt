package com.aistudyos.app.presentation.subject

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aistudyos.app.databinding.ItemColorPickerBinding

class ColorPickerAdapter(
    private val colors: List<String>,
    private val onColorSelected: (String) -> Unit
) : RecyclerView.Adapter<ColorPickerAdapter.ColorVH>() {

    private var selectedPos = 0

    inner class ColorVH(private val b: ItemColorPickerBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(color: String, isSelected: Boolean) {
            b.viewColor.setBackgroundColor(Color.parseColor(color))
            b.ivCheck.visibility = if (isSelected) android.view.View.VISIBLE else android.view.View.GONE
            b.root.setOnClickListener {
                val prev = selectedPos
                selectedPos = adapterPosition
                notifyItemChanged(prev)
                notifyItemChanged(selectedPos)
                onColorSelected(color)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ColorVH(ItemColorPickerBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ColorVH, pos: Int) = holder.bind(colors[pos], pos == selectedPos)
    override fun getItemCount() = colors.size
}
