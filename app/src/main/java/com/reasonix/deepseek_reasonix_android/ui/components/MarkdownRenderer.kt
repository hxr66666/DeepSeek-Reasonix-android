package com.reasonix.deepseek_reasonix_android.ui.components

import android.content.Context
import android.text.method.LinkMovementMethod
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import coil.ImageLoader
import com.reasonix.deepseek_reasonix_android.ui.markdown.DefaultGrammarLocator
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.PrecomputedTextSetterCompat
import io.noties.markwon.core.MarkwonTheme
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.ext.tables.TableTheme
import io.noties.markwon.ext.tasklist.TaskListPlugin
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.image.ImagesPlugin
import io.noties.markwon.image.coil.CoilImagesPlugin
import io.noties.markwon.inlineparser.MarkwonInlineParserPlugin
import io.noties.markwon.linkify.LinkifyPlugin
import io.noties.markwon.syntax.Prism4jThemeDarkula
import io.noties.markwon.syntax.SyntaxHighlightPlugin
import io.noties.markwon.utils.NoCopySpannableFactory
import io.noties.prism4j.Prism4j
import java.util.concurrent.Executors

// ═══════════════════════════════════════════════════════════════════
//  Reasonix 暗色主题色板（与 index.html CSS 变量对齐）
// ═══════════════════════════════════════════════════════════════════
private val ACCENT = 0xFFEA8800.toInt()   // --accent
private val FG = 0xFFF5F2F0.toInt()   // --fg
private val FG2 = 0xFFCCC5C0.toInt()   // --fg-2  (code text)
private val BORDER = 0xFF3D3938.toInt()   // --border
private val BG2 = 0xFF222022.toInt()   // --bg-2  (inline code bg)
private val PANEL2 = 0xFF2E2C2E.toInt()   // --panel-2 (code block bg)

/**
 * 安卓原生 Markdown 渲染器 —— 基于 Markwon v4 + AndroidView。
 *
 * 覆盖的 Markdown 特性：
 * - 粗体/斜体/删除线  • 有序/无序列表  • 标题 H1~H6
 * - 引用块  • 内联代码 & 代码块  • 表格  • 任务列表
 * - 链接（可点击）  • 图片（Coil 异步加载）  • HTML 标签
 * - 语法高亮（Prism4j，当前无语言语法文件故退化为纯文本渲染）
 *
 * @param markdown   原始 Markdown 文本
 * @param modifier   外部修饰符
 * @param linkColor  链接颜色（默认使用 Reasonix 主题色）
 */
@Composable
fun MarkdownRenderer(
    markdown: String,
    modifier: Modifier = Modifier,
    codeBackground: Color = Color(BG2),
    codeTextColor: Color = Color(FG2),
    linkColor: Color = Color(ACCENT),
) {
    val context = LocalContext.current

    // 使用 applicationContext 避免泄露 Activity
    val markwon = remember { buildMarkwon(context.applicationContext) }

    AndroidView(
        factory = { ctx ->
            // HorizontalScrollView 包裹 TextView，支持表格左右滑动
            HorizontalScrollView(ctx).apply {
                isHorizontalScrollBarEnabled = false
                overScrollMode = View.OVER_SCROLL_NEVER
                isFillViewport = false
                addView(
                    TextView(ctx).apply {
                        setTextColor(FG)
                        setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
                        setLineSpacing(4f, 1f)
                        movementMethod = LinkMovementMethod.getInstance()
                        isClickable = true
                        // 流式更新时不复制 Spannable，防止闪烁
                        setSpannableFactory(NoCopySpannableFactory.getInstance())
                    }, ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                )
            }
        },
        update = { scrollView ->
            val textView = scrollView.getChildAt(0) as TextView
            markwon.setMarkdown(textView, markdown)
        },
        modifier = modifier,
    )
}

// ═══════════════════════════════════════════════════════════════════
//  Markwon 实例构建（全局缓存，避免重复初始化）
// ═══════════════════════════════════════════════════════════════════

@Volatile
private var MARKWON_INSTANCE: Markwon? = null

private val LOCK = Any()

private fun buildMarkwon(context: Context): Markwon {
    MARKWON_INSTANCE?.let { return it }

    synchronized(LOCK) {
        MARKWON_INSTANCE?.let { return it }

        // Prism4j — 语法高亮引擎（暂无 language grammar）
        val prism4j = Prism4j(DefaultGrammarLocator())

        // Prism4j 主题（基于 Darkula，覆盖背景色以匹配 Reasonix）
        val prismTheme = Prism4jThemeDarkula.create()

        // Coil 2.x ImageLoader（兼容 Markwon image-coil 插件）
        val coilLoader = ImageLoader.Builder(context)
            .crossfade(true)
            .build()

        // dp → px
        val density = context.resources.displayMetrics.density
        val cellPaddingPx = (8f * density).toInt()
        val borderWidthPx = if (density >= 3f) 3 else if (density >= 1.5f) 2 else 1

        val tableTheme = TableTheme.emptyBuilder()
            .tableBorderColor(BORDER)                         // 边框色 #3D3938
            .tableBorderWidth(borderWidthPx)                  // 实线边框（密度自适应）
            .tableCellPadding(cellPaddingPx)                  // 8dp 内边距
            .tableHeaderRowBackgroundColor(PANEL2)            // 表头 #2E2C2E
            .tableEvenRowBackgroundColor(0x00000000)          // 偶数行 透明
            .tableOddRowBackgroundColor(0x0DFFFFFF.toInt())   // 奇数行 微白 5%
            .build()

        val markwon = Markwon.builder(context)

            // ① 语法高亮（最先注册；同时设置 code 文字/背景色）
            .usePlugin(SyntaxHighlightPlugin.create(prism4j, prismTheme))
            // ② HTML 支持
            .usePlugin(HtmlPlugin.create())
            // ③ 图片 SchemeHandler 注册
            .usePlugin(ImagesPlugin.create())
            // ④ 图片异步加载 — Coil 2.x（显式传入 ImageLoader）
            .usePlugin(CoilImagesPlugin.create(context, coilLoader))
            // ⑤ 删除线扩展
            .usePlugin(StrikethroughPlugin.create())
            // ⑥ 表格支持
            .usePlugin(TablePlugin.create(tableTheme))
            // ⑦ 任务列表
            .usePlugin(TaskListPlugin.create(context))
            // ⑧ 自动链接
            .usePlugin(LinkifyPlugin.create())

            .usePlugin(MarkwonInlineParserPlugin.create())

            // ⑨ 主题覆盖（必须在 SyntaxHighlightPlugin 之后注册，以覆盖内联代码背景色）
            .usePlugin(ReasonixThemePlugin())
            .textSetter(PrecomputedTextSetterCompat.create(Executors.newCachedThreadPool()))
            .build()
        MARKWON_INSTANCE = markwon
        return markwon
    }
}

/**
 * Reasonix 暗色主题插件 —— 覆盖核心颜色以匹配 index.html 色板。
 */
private class ReasonixThemePlugin : AbstractMarkwonPlugin() {

    override fun configureTheme(builder: MarkwonTheme.Builder) {
        builder
            // 链接
            .linkColor(ACCENT)
            .isLinkUnderlined(true)
            // 内联代码（覆盖 SyntaxHighlightPlugin 设置的值）
            .codeBackgroundColor(BG2)
            // 代码块
            .codeBlockBackgroundColor(PANEL2)
            .codeBlockTextColor(FG2)
            // 引用块
            .blockQuoteColor(BORDER)
            // 标题分割线
            .headingBreakColor(BORDER)
            // 水平分割线
            .thematicBreakColor(BORDER)
            // 列表项
            .listItemColor(FG)
    }
}

/** 判断字符串是否不含 Markdown 语法。纯文本可跳过完整渲染管线。 */
fun isPlainText(text: String): Boolean {
    val patterns = listOf(
        "\\*\\*", "\\*", "__", "_", "```", "`", "##", "> ", "- ", "\\d+\\. ",
        "!\\[", "\\[", "\\|",
    )
    return patterns.none { Regex(it).containsMatchIn(text) }
}
