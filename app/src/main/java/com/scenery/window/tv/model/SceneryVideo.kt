package com.scenery.window.tv.model

import com.scenery.window.tv.R

data class SceneryVideo(
    val id: Int,
    val title: String,
    val description: String,
    val rawResId: Int,
    val category: String,
    val gradientColors: Pair<Int, Int>  // 卡片背景渐变色
)

object SceneryRepository {
    fun getSceneryVideos(): List<SceneryVideo> {
        return listOf(
            SceneryVideo(
                id = 1,
                title = "碧海浪涌",
                description = "无人机俯瞰壮阔海浪涌向海岸",
                rawResId = R.raw.ocean,
                category = "大海",
                gradientColors = Pair(0xFF0D47A1.toInt(), 0xFF1565C0.toInt())
            ),
            SceneryVideo(
                id = 2,
                title = "雪山之巅",
                description = "白雪皑皑的山峰在云雾中若隐若现",
                rawResId = R.raw.mountain,
                category = "山景",
                gradientColors = Pair(0xFF1B5E20.toInt(), 0xFF2E7D32.toInt())
            ),
            SceneryVideo(
                id = 3,
                title = "静谧湖泊",
                description = "平静的湖面倒映着天空和远山",
                rawResId = R.raw.lake,
                category = "湖景",
                gradientColors = Pair(0xFF006064.toInt(), 0xFF00838F.toInt())
            ),
            SceneryVideo(
                id = 4,
                title = "日落余晖",
                description = "金色的阳光洒在波光粼粼的海面上",
                rawResId = R.raw.sunset,
                category = "日落",
                gradientColors = Pair(0xFFBF360C.toInt(), 0xFFE65100.toInt())
            )
        )
    }
}
