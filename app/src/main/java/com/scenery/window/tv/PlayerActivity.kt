package com.scenery.window.tv

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.scenery.window.tv.model.SceneryRepository
import com.scenery.window.tv.model.SceneryVideo

class PlayerActivity : AppCompatActivity() {

    private var player: ExoPlayer? = null
    private lateinit var playerView: StyledPlayerView
    private lateinit var loadingOverlay: FrameLayout
    private lateinit var loadingIcon: ImageView
    private lateinit var loadingText: TextView
    private lateinit var switchHint: TextView
    private lateinit var sceneryTitleHud: TextView

    private val videoList = SceneryRepository.getSceneryVideos()
    private var currentIndex: Int = 0
    private val handler = Handler(Looper.getMainLooper())
    private var isSwitching = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        hideSystemUI()

        playerView = findViewById(R.id.playerView)
        loadingOverlay = findViewById(R.id.loadingOverlay)
        loadingIcon = findViewById(R.id.loadingIcon)
        loadingText = findViewById(R.id.loadingText)
        switchHint = findViewById(R.id.switchHint)
        sceneryTitleHud = findViewById(R.id.sceneryTitleHud)

        // 根据传入的 resId 定位当前索引
        val startResId = intent.getIntExtra("VIDEO_RES_ID", 0)
        currentIndex = videoList.indexOfFirst { it.rawResId == startResId }.coerceAtLeast(0)

        showLoading(videoList[currentIndex].title)
        startLoadingAnimation()
    }

    // ==================== 方向键切换 ====================

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                switchScenery(-1)
                return true
            }
            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                switchScenery(1)
                return true
            }
            KeyEvent.KEYCODE_DPAD_UP -> {
                switchScenery(-1)
                return true
            }
            KeyEvent.KEYCODE_DPAD_DOWN -> {
                switchScenery(1)
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun switchScenery(direction: Int) {
        if (isSwitching) return
        isSwitching = true

        val newIndex = (currentIndex + direction + videoList.size) % videoList.size
        if (newIndex == currentIndex) {
            isSwitching = false
            return
        }
        currentIndex = newIndex
        val scenery = videoList[currentIndex]

        // 显示切换提示
        showSceneryTitleHud(scenery.title, scenery.category)

        // 释放旧播放器，显示加载，播放新视频
        releasePlayer()
        showLoading(scenery.title)
        startLoadingAnimation()

        // 短暂延迟后初始化新播放器，让 UI 有时间刷新
        handler.postDelayed({
            initializePlayer()
            isSwitching = false
        }, 300)
    }

    private fun showSceneryTitleHud(title: String, category: String) {
        sceneryTitleHud.text = "$category · $title"
        sceneryTitleHud.visibility = View.VISIBLE
        sceneryTitleHud.alpha = 1f

        // 3 秒后淡出
        handler.removeCallbacksAndMessages("hud")
        handler.postDelayed({
            val fadeOut = AlphaAnimation(1f, 0f).apply {
                duration = 600
                fillAfter = true
                setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(a: Animation?) {}
                    override fun onAnimationRepeat(a: Animation?) {}
                    override fun onAnimationEnd(a: Animation?) {
                        sceneryTitleHud.visibility = View.GONE
                    }
                })
            }
            sceneryTitleHud.startAnimation(fadeOut)
        }, 3000)
    }

    // ==================== 加载动画 ====================

    private fun showLoading(title: String) {
        loadingOverlay.clearAnimation()
        loadingOverlay.visibility = View.VISIBLE
        loadingOverlay.alpha = 1f
        loadingText.text = "正在准备「$title」..."
    }

    private fun startLoadingAnimation() {
        val breathe = AlphaAnimation(0.3f, 1.0f).apply {
            duration = 1200
            repeatMode = Animation.REVERSE
            repeatCount = Animation.INFINITE
        }
        loadingIcon.startAnimation(breathe)

        val textFade = AlphaAnimation(0.5f, 1.0f).apply {
            duration = 1500
            repeatMode = Animation.REVERSE
            repeatCount = Animation.INFINITE
        }
        loadingText.startAnimation(textFade)
    }

    private fun fadeOutLoading() {
        loadingIcon.clearAnimation()
        loadingText.clearAnimation()

        val fadeOut = AlphaAnimation(1.0f, 0.0f).apply {
            duration = 800
            fillAfter = true
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(a: Animation?) {}
                override fun onAnimationRepeat(a: Animation?) {}
                override fun onAnimationEnd(a: Animation?) {
                    loadingOverlay.visibility = View.GONE
                }
            })
        }
        loadingOverlay.startAnimation(fadeOut)
    }

    // ==================== 播放器 ====================

    private fun initializePlayer() {
        if (player != null) return
        if (currentIndex < 0 || currentIndex >= videoList.size) return

        val resId = videoList[currentIndex].rawResId

        player = ExoPlayer.Builder(this).build().also { exoPlayer ->
            playerView.player = exoPlayer

            val videoUri = Uri.parse("android.resource://$packageName/$resId")
            val mediaItem = MediaItem.fromUri(videoUri)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.repeatMode = Player.REPEAT_MODE_ONE

            exoPlayer.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    if (playbackState == Player.STATE_READY) {
                        fadeOutLoading()
                        // 首次播放时短暂显示切换提示
                        showSwitchHint()
                    }
                }
            })

            exoPlayer.prepare()
            exoPlayer.playWhenReady = true
        }
    }

    private fun showSwitchHint() {
        switchHint.visibility = View.VISIBLE
        switchHint.alpha = 1f

        handler.postDelayed({
            val fadeOut = AlphaAnimation(1f, 0f).apply {
                duration = 1000
                fillAfter = true
                setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(a: Animation?) {}
                    override fun onAnimationRepeat(a: Animation?) {}
                    override fun onAnimationEnd(a: Animation?) {
                        switchHint.visibility = View.GONE
                    }
                })
            }
            switchHint.startAnimation(fadeOut)
        }, 4000)
    }

    // ==================== 系统 UI ====================

    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.let {
                it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
            )
        }
    }

    // ==================== 生命周期 ====================

    override fun onStart() {
        super.onStart()
        initializePlayer()
    }

    override fun onResume() {
        super.onResume()
        hideSystemUI()
        player?.playWhenReady = true
    }

    override fun onPause() {
        super.onPause()
        player?.playWhenReady = false
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    private fun releasePlayer() {
        player?.release()
        player = null
    }
}
