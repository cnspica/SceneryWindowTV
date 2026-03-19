package com.scenery.window.tv

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scenery.window.tv.adapter.SceneryAdapter
import com.scenery.window.tv.model.SceneryRepository

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SceneryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecyclerView()
        loadSceneryVideos()
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.sceneryRecyclerView)
        // 根据屏幕宽度动态计算列数，TV 用 4 列，手机用 2 列
        val columns = if (resources.configuration.smallestScreenWidthDp >= 600) 4 else 2
        recyclerView.layoutManager = GridLayoutManager(this, columns)

        adapter = SceneryAdapter { sceneryVideo ->
            val intent = Intent(this, PlayerActivity::class.java).apply {
                putExtra("VIDEO_RES_ID", sceneryVideo.rawResId)
                putExtra("VIDEO_TITLE", sceneryVideo.title)
            }
            startActivity(intent)
        }

        recyclerView.adapter = adapter
    }

    private fun loadSceneryVideos() {
        val videos = SceneryRepository.getSceneryVideos()
        adapter.submitList(videos)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        // TV 遥控器确认键
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
            val focused = currentFocus
            focused?.performClick()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
