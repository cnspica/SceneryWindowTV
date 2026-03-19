package com.scenery.window.tv.adapter

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.scenery.window.tv.R
import com.scenery.window.tv.model.SceneryVideo

class SceneryAdapter(
    private val onItemClick: (SceneryVideo) -> Unit
) : RecyclerView.Adapter<SceneryAdapter.SceneryViewHolder>() {

    private var sceneryList: List<SceneryVideo> = emptyList()

    fun submitList(list: List<SceneryVideo>) {
        sceneryList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SceneryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_scenery, parent, false)
        return SceneryViewHolder(view)
    }

    override fun onBindViewHolder(holder: SceneryViewHolder, position: Int) {
        holder.bind(sceneryList[position])
    }

    override fun getItemCount(): Int = sceneryList.size

    inner class SceneryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val thumbnail: ImageView = itemView.findViewById(R.id.sceneryThumbnail)
        private val title: TextView = itemView.findViewById(R.id.sceneryTitle)
        private val category: TextView = itemView.findViewById(R.id.sceneryCategory)
        private val description: TextView = itemView.findViewById(R.id.sceneryDescription)

        fun bind(scenery: SceneryVideo) {
            title.text = scenery.title
            category.text = scenery.category
            description.text = scenery.description

            // 使用渐变色背景代替网络缩略图
            val gradient = GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                intArrayOf(scenery.gradientColors.first, scenery.gradientColors.second)
            )
            gradient.cornerRadius = 0f
            thumbnail.setImageDrawable(gradient)

            // 设置分类图标
            val iconRes = when (scenery.category) {
                "大海" -> R.drawable.ic_ocean
                "山景" -> R.drawable.ic_mountain
                "湖景" -> R.drawable.ic_lake
                "日落" -> R.drawable.ic_sunset
                else -> R.drawable.ic_ocean
            }
            thumbnail.setImageResource(iconRes)
            thumbnail.scaleType = ImageView.ScaleType.CENTER
            thumbnail.setBackgroundDrawable(gradient)

            itemView.setOnClickListener {
                onItemClick(scenery)
            }

            // TV 焦点处理
            itemView.isFocusable = true
            itemView.isFocusableInTouchMode = true
            itemView.setOnFocusChangeListener { v, hasFocus ->
                val scale = if (hasFocus) 1.08f else 1.0f
                v.animate().scaleX(scale).scaleY(scale).setDuration(200).start()
                v.elevation = if (hasFocus) 16f else 4f
            }
        }
    }
}
