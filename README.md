# 风景窗口 - Android TV 应用

## 📺 项目简介

将你的电视变成一扇动态的风景画窗口！这是一个专为Android TV设计的应用，可以播放精选的风景视频（大海、山景、湖景），让电视成为家中的艺术装饰。

## ✨ 核心功能

- **沉浸式播放**：全屏无干扰播放，就像真实的窗户
- **循环播放**：视频自动循环，持续展示美景
- **精选场景**：
  - 🌊 宁静的海洋
  - ⛰️ 壮丽山景
  - 🏞️ 静谧湖泊
- **TV遥控器支持**：完美适配电视遥控器导航
- **优雅界面**：简洁的场景选择界面

## 🛠️ 技术栈

- **开发语言**：Kotlin
- **视频播放**：ExoPlayer 2.19.1
- **图片加载**：Glide 4.16.0
- **UI框架**：Android Leanback Library
- **最低支持**：Android 5.0 (API 21)

## 📁 项目结构

```
SceneryWindowTV/
├── app/
│   ├── src/main/
│   │   ├── java/com/scenery/window/tv/
│   │   │   ├── MainActivity.kt           # 主界面（场景选择）
│   │   │   ├── PlayerActivity.kt         # 播放器界面
│   │   │   ├── model/
│   │   │   │   └── SceneryVideo.kt       # 数据模型
│   │   │   └── adapter/
│   │   │       └── SceneryAdapter.kt     # 场景列表适配器
│   │   ├── res/
│   │   │   ├── layout/                   # 布局文件
│   │   │   ├── drawable/                 # 图形资源
│   │   │   └── values/                   # 颜色、字符串、主题
│   │   └── AndroidManifest.xml
│   └── build.gradle
├── build.gradle
└── settings.gradle
```

## 🚀 构建与安装

### 前置要求

- Android Studio Hedgehog (2023.1.1) 或更高版本
- JDK 8 或更高版本
- Android TV 设备或模拟器

### 构建步骤

1. **克隆或打开项目**
   ```bash
   cd /Users/sean/Documents/stepfun/SceneryWindowTV
   ```

2. **使用Android Studio打开项目**
   - 打开 Android Studio
   - 选择 "Open an Existing Project"
   - 选择 SceneryWindowTV 文件夹

3. **同步Gradle**
   - Android Studio会自动提示同步Gradle
   - 等待依赖下载完成

4. **连接Android TV设备**
   - 通过USB连接TV设备，或
   - 使用Android TV模拟器

5. **运行应用**
   - 点击 Run 按钮 (绿色三角形)
   - 选择目标设备
   - 等待应用安装并启动

### 命令行构建

```bash
# 构建Debug版本
./gradlew assembleDebug

# 构建Release版本
./gradlew assembleRelease

# 安装到连接的设备
./gradlew installDebug
```

生成的APK位置：
- Debug: `app/build/outputs/apk/debug/app-debug.apk`
- Release: `app/build/outputs/apk/release/app-release.apk`

## 📱 使用说明

1. **启动应用**：在Android TV主屏幕找到"风景窗口"图标
2. **选择场景**：使用遥控器方向键浏览场景，按确认键选择
3. **观看风景**：视频将全屏循环播放
4. **退出播放**：按遥控器返回键退出到场景选择界面

## 🎨 自定义配置

### 添加自己的风景视频

编辑 `SceneryVideo.kt` 文件，修改视频URL：

```kotlin
SceneryVideo(
    id = 7,
    title = "你的风景标题",
    description = "描述",
    thumbnailUrl = "缩略图URL",
    videoUrl = "视频URL",  // 支持MP4、HLS等格式
    category = "分类"
)
```

### 使用本地视频

1. 将视频文件放入 `app/src/main/res/raw/` 目录
2. 修改视频URL为：`"android.resource://" + packageName + "/" + R.raw.your_video`

### 调整布局

- 修改网格列数：`activity_main.xml` 中的 `GridLayoutManager(this, 3)`
- 调整卡片大小：`item_scenery.xml` 中的高度值
- 更改主题颜色：`colors.xml` 文件

## 🔧 故障排除

### 视频无法播放
- 检查网络连接
- 确认视频URL有效
- 查看Logcat日志：`adb logcat | grep ExoPlayer`

### 遥控器无响应
- 确保设备支持Android TV
- 检查焦点设置：`android:focusable="true"`

### 构建失败
- 清理项目：`./gradlew clean`
- 删除 `.gradle` 和 `build` 文件夹后重新构建

## 📝 开发笔记

### Demo版本说明
- 当前使用的是示例视频URL（Google测试视频）
- 实际部署时需要替换为真实的风景视频
- 建议使用CDN托管视频以获得更好的播放体验

### 后续优化方向
- [ ] 添加视频分类筛选
- [ ] 支持播放列表自动切换
- [ ] 添加环境音效选项
- [ ] 实现视频下载功能（离线播放）
- [ ] 添加定时关闭功能
- [ ] 支持4K视频播放

## 📄 许可证

本项目为Demo演示版本，仅供学习和测试使用。

## 🤝 贡献

欢迎提交Issue和Pull Request！

---

**享受你的风景窗口！** 🌅
