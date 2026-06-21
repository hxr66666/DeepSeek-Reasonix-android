package com.reasonix.deepseek_reasonix_android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ═══════════════════════════════════════════════════════════════════
//  Compose 原生 Markdown 渲染器  (表格 + 代码高亮)
// ═══════════════════════════════════════════════════════════════════

// ── 内联 Token ──

private sealed class InlinePart {
    data class Text(val value: String) : InlinePart()
    data class Bold(val children: List<InlinePart>) : InlinePart()
    data class Italic(val children: List<InlinePart>) : InlinePart()
    data class Code(val value: String) : InlinePart()
    data class Strikethrough(val children: List<InlinePart>) : InlinePart()
    data class Link(val text: String, val url: String) : InlinePart()
}

// ── 表格对齐 ──

private enum class ColAlign { LEFT, CENTER, RIGHT }

// ── 块类型 ──

private sealed class MdBlock {
    data class Header(val level: Int, val text: String) : MdBlock()
    data class Paragraph(val text: String) : MdBlock()
    data class CodeBlock(val code: String, val language: String = "") : MdBlock()
    data class Table(
        val headers: List<String>,
        val alignments: List<ColAlign>,
        val rows: List<List<String>>
    ) : MdBlock()
    data class UnorderedListItem(val text: String) : MdBlock()
    data class OrderedListItem(val number: Int, val text: String) : MdBlock()
    data class BlockQuote(val text: String) : MdBlock()
    data object BlankLine : MdBlock()
    data object HorizontalRule : MdBlock()
}

// ── 语法高亮 Token ──

private enum class TokenKind { KEYWORD, STRING, COMMENT, NUMBER, PUNCTUATION, TYPE, FUNCTION, OPERATOR, PLAIN }

private data class HiToken(val text: String, val kind: TokenKind)

// ═══════════════════════════════════════════════════════════════════
//  公开入口
// ═══════════════════════════════════════════════════════════════════

@Composable
fun MarkdownText(
    markdown: String,
    modifier: Modifier = Modifier,
    textColor: Color = Color(0xFFF5F2F0),
    secondaryColor: Color = Color(0xFFCCC5C0),
    codeBackground: Color = Color(0xFF222022),
    codeTextColor: Color = Color(0xFFCCC5C0),
    linkColor: Color = Color(0xFFEA8800),
    borderColor: Color = Color(0xFF3D3938),
    fontSize: Float = 15f,
    lineHeight: Float = 22f,
) {
    val blocks = remember(markdown) { parseBlocks(markdown) }

    Column(modifier = modifier.fillMaxWidth()) {
        blocks.forEachIndexed { _, block ->
            when (block) {
                is MdBlock.Header -> {
                    val sz = when (block.level) {
                        1 -> 22f; 2 -> 20f; 3 -> 18f
                        4 -> 17f; 5 -> 16f; else -> 15f
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    InlineText(
                        raw = block.text,
                        base = SpanStyle(color = textColor, fontWeight = FontWeight.Bold, fontSize = sz.sp),
                        linkColor = linkColor, codeBg = codeBackground, codeFg = codeTextColor,
                    )
                }

                is MdBlock.Paragraph -> {
                    Spacer(modifier = Modifier.height(4.dp))
                    InlineText(
                        raw = block.text,
                        base = SpanStyle(color = textColor, fontSize = fontSize.sp),
                        linkColor = linkColor, codeBg = codeBackground, codeFg = codeTextColor,
                        lh = lineHeight.sp,
                    )
                }

                is MdBlock.CodeBlock -> {
                    Spacer(modifier = Modifier.height(6.dp))
                    // ── 语言标签 ──
                    if (block.language.isNotEmpty()) {
                        Text(
                            text = block.language.uppercase(),
                            color = secondaryColor,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier
                                .padding(start = 12.dp, bottom = 2.dp)
                        )
                    }
                    Box(
                        Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp))
                            .background(codeBackground).padding(12.dp)
                            .horizontalScroll(rememberScrollState())
                    ) {
                        HighlightedCode(
                            code = block.code,
                            language = block.language,
                            baseColor = codeTextColor,
                            fontSize = 13.sp,
                            lineHeight = 19.sp,
                        )
                    }
                }

                is MdBlock.Table -> {
                    Spacer(modifier = Modifier.height(6.dp))
                    TableBlock(block, borderColor, textColor, secondaryColor, codeBackground, fontSize, lineHeight)
                }

                is MdBlock.UnorderedListItem -> {
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(Modifier.padding(start = 12.dp)) {
                        Text("•", color = secondaryColor, fontSize = fontSize.sp,
                            lineHeight = lineHeight.sp, modifier = Modifier.width(16.dp))
                        InlineText(
                            raw = block.text,
                            base = SpanStyle(color = textColor, fontSize = fontSize.sp),
                            linkColor = linkColor, codeBg = codeBackground, codeFg = codeTextColor,
                            lh = lineHeight.sp, modifier = Modifier.weight(1f),
                        )
                    }
                }

                is MdBlock.OrderedListItem -> {
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(Modifier.padding(start = 12.dp)) {
                        Text("${block.number}.", color = secondaryColor, fontSize = fontSize.sp,
                            lineHeight = lineHeight.sp, modifier = Modifier.width(24.dp))
                        InlineText(
                            raw = block.text,
                            base = SpanStyle(color = textColor, fontSize = fontSize.sp),
                            linkColor = linkColor, codeBg = codeBackground, codeFg = codeTextColor,
                            lh = lineHeight.sp, modifier = Modifier.weight(1f),
                        )
                    }
                }

                is MdBlock.BlockQuote -> {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(Modifier.fillMaxWidth()) {
                        Box(Modifier.width(3.dp).heightIn(min = 20.dp)
                            .background(borderColor, RoundedCornerShape(2.dp)))
                        Spacer(Modifier.width(8.dp))
                        InlineText(
                            raw = block.text,
                            base = SpanStyle(color = secondaryColor, fontStyle = FontStyle.Italic,
                                fontSize = (fontSize - 0.5f).sp),
                            linkColor = linkColor, codeBg = codeBackground, codeFg = codeTextColor,
                            lh = lineHeight.sp, modifier = Modifier.weight(1f),
                        )
                    }
                }

                is MdBlock.HorizontalRule -> {
                    Spacer(Modifier.height(6.dp))
                    Box(Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(1.dp).background(borderColor))
                    Spacer(Modifier.height(6.dp))
                }

                is MdBlock.BlankLine -> Spacer(Modifier.height(6.dp))
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════
//  表格渲染
// ═══════════════════════════════════════════════════════════════════

@Composable
private fun TableBlock(
    table: MdBlock.Table,
    borderColor: Color,
    textColor: Color,
    secondaryColor: Color,
    codeBg: Color,
    fontSize: Float,
    lineHeight: Float,
) {
    val scrollState = rememberScrollState()
    val cellPadding = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
    // 每列最小宽度，防止文字被挤成竖排
    val cellMinWidth = 72.dp

    val cellAlign: (Int) -> Alignment = { ci ->
        when (table.alignments.getOrElse(ci) { ColAlign.LEFT }) {
            ColAlign.CENTER -> Alignment.Center
            ColAlign.RIGHT -> Alignment.CenterEnd
            else -> Alignment.CenterStart
        }
    }

    Box(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp))
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .horizontalScroll(scrollState)
    ) {
        // IntrinsicSize.Max 统一所有行宽度，让 fillMaxWidth / weight 在正确的约束下工作
        Column(Modifier.width(IntrinsicSize.Max)) {
            // ── 表头行 ──
            Row(
                Modifier.fillMaxWidth().background(codeBg).height(IntrinsicSize.Min)
            ) {
                table.headers.forEachIndexed { ci, header ->
                    Box(
                        cellPadding.weight(1f).widthIn(min = cellMinWidth),
                        contentAlignment = cellAlign(ci)
                    ) {
                        Text(
                            text = header.trim(),
                            color = textColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = (fontSize - 1f).sp,
                            lineHeight = lineHeight.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            softWrap = true,
                        )
                    }
                }
            }

            // ── 表头/表体分隔线 ──
            Box(Modifier.fillMaxWidth().height(1.dp).background(borderColor))

            // ── 数据行 ──
            table.rows.forEachIndexed { ri, row ->
                Row(
                    Modifier.fillMaxWidth()
                        .background(if (ri % 2 == 0) Color.Transparent else codeBg.copy(alpha = 0.4f))
                        .height(IntrinsicSize.Min)
                ) {
                    row.forEachIndexed { ci, cell ->
                        Box(
                            cellPadding.weight(1f).widthIn(min = cellMinWidth),
                            contentAlignment = cellAlign(ci)
                        ) {
                            Text(
                                text = cell.trim(),
                                color = secondaryColor,
                                fontSize = (fontSize - 1f).sp,
                                lineHeight = lineHeight.sp,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis,
                                softWrap = true,
                            )
                        }
                    }
                }
                // 行间分隔线（最后一行不加）
                if (ri < table.rows.size - 1) {
                    Box(Modifier.fillMaxWidth().height(0.5.dp).background(borderColor.copy(alpha = 0.4f)))
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════
//  语法高亮代码渲染
// ═══════════════════════════════════════════════════════════════════

@Composable
private fun HighlightedCode(
    code: String,
    language: String,
    baseColor: Color,
    fontSize: TextUnit,
    lineHeight: TextUnit,
) {
    val tokens = remember(code, language) { tokenize(code, language) }

    Text(
        buildAnnotatedString {
            for (t in tokens) {
                val color = colorForKind(t.kind, baseColor)
                withStyle(SpanStyle(
                    color = color,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = if (t.kind == TokenKind.KEYWORD) FontWeight.Bold else FontWeight.Normal,
                )) {
                    append(t.text)
                }
            }
        },
        fontSize = fontSize,
        lineHeight = lineHeight,
    )
}

private fun colorForKind(kind: TokenKind, base: Color): Color = when (kind) {
    TokenKind.KEYWORD     -> Color(0xFFCC7832)  // 橙棕
    TokenKind.STRING      -> Color(0xFF6A8759)  // 绿
    TokenKind.COMMENT     -> Color(0xFF808080)  // 灰
    TokenKind.NUMBER      -> Color(0xFF6897BB)  // 蓝
    TokenKind.TYPE        -> Color(0xFFA9B7C6)  // 浅灰白 (类/类型名)
    TokenKind.FUNCTION    -> Color(0xFFFFC66D)  // 金黄
    TokenKind.OPERATOR    -> Color(0xFFA9B7C6)  // 浅灰白
    TokenKind.PUNCTUATION -> Color(0xFFA9B7C6)
    TokenKind.PLAIN       -> base
}

// ── 词法分析引擎 ──

/** 各语言的 keyword + 内建类型集合 */
private val KEYWORD_SETS: Map<String, Set<String>> = mapOf(
    "kotlin" to setOf(
        "fun","val","var","class","object","interface","enum","data","sealed",
        "if","else","when","for","while","do","return","try","catch","finally",
        "throw","import","package","as","is","in","!in","super","this","true",
        "false","null","typealias","companion","private","protected","public",
        "internal","override","abstract","open","final","const","lateinit",
        "suspend","inline","crossinline","noinline","reified","operator","infix",
        "tailrec","external","annotation","by","get","set","constructor","init",
        "break","continue","where","out","vararg","field","it"
    ),
    "java" to setOf(
        "abstract","assert","boolean","break","byte","case","catch","char","class",
        "const","continue","default","do","double","else","enum","extends","final",
        "finally","float","for","goto","if","implements","import","instanceof",
        "int","interface","long","native","new","package","private","protected",
        "public","return","short","static","strictfp","super","switch",
        "synchronized","this","throw","throws","transient","try","void","volatile",
        "while","true","false","null","var","record","sealed","permits","yield"
    ),
    "python" to setOf(
        "False","None","True","and","as","assert","async","await","break",
        "class","continue","def","del","elif","else","except","finally","for",
        "from","global","if","import","in","is","lambda","nonlocal","not","or",
        "pass","raise","return","try","while","with","yield","self","print"
    ),
    "javascript" to setOf(
        "async","await","break","case","catch","class","const","continue",
        "debugger","default","delete","do","else","export","extends","finally",
        "for","function","if","import","in","instanceof","let","new","of",
        "return","super","switch","this","throw","try","typeof","var","void",
        "while","with","yield","true","false","null","undefined","from","as"
    ),
    "typescript" to setOf(
        "async","await","break","case","catch","class","const","continue",
        "debugger","default","delete","do","else","enum","export","extends",
        "finally","for","function","if","implements","import","in","instanceof",
        "interface","let","new","of","return","super","switch","this","throw",
        "try","type","typeof","var","void","while","with","yield","true","false",
        "null","undefined","from","as","readonly","keyof","infer","never","unknown","any"
    ),
    "go" to setOf(
        "break","case","chan","const","continue","default","defer","else",
        "fallthrough","for","func","go","goto","if","import","interface","map",
        "package","range","return","select","struct","switch","type","var",
        "true","false","nil","int","string","bool","byte","error","float64","float32"
    ),
    "rust" to setOf(
        "as","break","const","continue","crate","else","enum","extern","false",
        "fn","for","if","impl","in","let","loop","match","mod","move","mut",
        "pub","ref","return","self","Self","static","struct","super","trait",
        "true","type","unsafe","use","where","while","async","await","dyn","union"
    ),
    "sql" to setOf(
        "SELECT","FROM","WHERE","AND","OR","NOT","IN","IS","NULL","LIKE",
        "INSERT","INTO","VALUES","UPDATE","SET","DELETE","CREATE","TABLE",
        "ALTER","DROP","INDEX","JOIN","LEFT","RIGHT","OUTER","INNER","ON",
        "GROUP","BY","ORDER","ASC","DESC","HAVING","LIMIT","OFFSET","UNION",
        "AS","DISTINCT","COUNT","SUM","AVG","MAX","MIN","BETWEEN","EXISTS",
        "CASE","WHEN","THEN","ELSE","END","PRIMARY","KEY","FOREIGN","REFERENCES",
        "CASCADE","DEFAULT","CHECK","UNIQUE","IF","TRUNCATE"
    ),
    "bash" to setOf(
        "if","then","else","elif","fi","for","while","do","done","case","esac",
        "in","function","return","exit","export","local","source","echo","read",
        "declare","unset","set","shift","break","continue","trap","eval","exec"
    ),
    "json" to emptySet(),
    "yaml" to setOf("true","false","null","yes","no","on","off"),
    "xml" to emptySet(),
    "html" to emptySet(),
    "css" to setOf(
        "!important","@media","@import","@keyframes","@font-face","@supports",
        "url","rgb","rgba","hsl","hsla"
    ),
    "c" to setOf(
        "auto","break","case","char","const","continue","default","do","double",
        "else","enum","extern","float","for","goto","if","inline","int","long",
        "register","restrict","return","short","signed","sizeof","static","struct",
        "switch","typedef","union","unsigned","void","volatile","while","NULL","true","false"
    ),
    "cpp" to setOf(
        "auto","break","case","char","const","continue","default","do","double",
        "else","enum","extern","float","for","goto","if","inline","int","long",
        "register","restrict","return","short","signed","sizeof","static","struct",
        "switch","typedef","union","unsigned","void","volatile","while","class",
        "namespace","template","typename","virtual","override","final","public",
        "private","protected","new","delete","this","nullptr","true","false",
        "try","catch","throw","noexcept","constexpr","decltype","using","operator",
        "explicit","friend","mutable","static_cast","dynamic_cast","reinterpret_cast",
        "const_cast","include","define","ifdef","ifndef","endif","pragma"
    ),
    "swift" to setOf(
        "class","struct","enum","protocol","extension","func","var","let","if",
        "else","guard","switch","case","default","for","while","repeat","do",
        "try","catch","throw","throws","rethrows","return","break","continue",
        "fallthrough","where","in","as","is","self","Self","super","true","false",
        "nil","import","public","private","internal","fileprivate","open","final",
        "override","mutating","nonmutating","lazy","weak","unowned","static",
        "deinit","init","subscript","associatedtype","typealias","some","any"
    ),
    "scala" to setOf(
        "val","var","def","class","object","trait","extends","with","import",
        "package","if","else","match","case","for","while","do","yield","return",
        "throw","try","catch","finally","new","this","super","true","false","null",
        "abstract","override","private","protected","implicit","lazy","sealed",
        "final","type","lazy","given","using","extension","export","enum","given"
    ),
    "dart" to setOf(
        "abstract","as","assert","async","await","break","case","catch","class",
        "const","continue","covariant","default","deferred","do","dynamic","else",
        "enum","export","extends","extension","external","factory","false","final",
        "finally","for","Function","get","hide","if","implements","import","in",
        "interface","is","late","library","mixin","new","null","on","operator",
        "part","required","rethrow","return","set","show","static","super","switch",
        "sync","this","throw","true","try","typedef","var","void","while","with","yield"
    ),
    "groovy" to setOf(
        "def","var","class","interface","enum","trait","extends","implements",
        "import","package","if","else","switch","case","default","for","while",
        "do","return","break","continue","throw","try","catch","finally","new",
        "this","super","true","false","null","as","in","instanceof","assert",
        "abstract","final","private","protected","public","static","synchronized"
    ),
    "markdown" to emptySet(),
    "plaintext" to emptySet(),
    "text" to emptySet(),
    "" to emptySet(),     // 无语言标签 → 纯文本
)

private fun tokenize(code: String, language: String): List<HiToken> {
    val lang = language.lowercase()
    val keywords = KEYWORD_SETS[lang] ?: emptySet()
    if (keywords.isEmpty() && lang !in listOf("json","xml","html","yaml","css","markdown","plaintext","text","")) {
        // 未知语言 → 退化为已知语言关键词检测 (尝试常见关键词)
        // 不做任何高亮，直接当纯文本
    }

    val tokens = mutableListOf<HiToken>()
    var i = 0
    val n = code.length

    // 状态机
    while (i < n) {
        val ch = code[i]

        when {
            // ── 单行注释 // ──
            i + 1 < n && ch == '/' && code[i + 1] == '/' -> {
                val start = i
                i += 2
                while (i < n && code[i] != '\n') i++
                tokens.add(HiToken(code.substring(start, i), TokenKind.COMMENT))
            }

            // ── 块注释 /* */ ──
            i + 1 < n && ch == '/' && code[i + 1] == '*' -> {
                val start = i
                i += 2
                while (i + 1 < n && !(code[i] == '*' && code[i + 1] == '/')) i++
                if (i + 1 < n) i += 2  // skip */
                tokens.add(HiToken(code.substring(start, i), TokenKind.COMMENT))
            }

            // ── # 注释 (Python / Bash / YAML / Ruby) ──
            ch == '#' -> {
                val start = i
                while (i < n && code[i] != '\n') i++
                tokens.add(HiToken(code.substring(start, i), TokenKind.COMMENT))
            }

            // ── SQL 注释 -- ──
            i + 1 < n && ch == '-' && code[i + 1] == '-' -> {
                val start = i
                i += 2
                while (i < n && code[i] != '\n') i++
                tokens.add(HiToken(code.substring(start, i), TokenKind.COMMENT))
            }

            // ── 双引号字符串 ──
            ch == '"' -> {
                val start = i
                i++ // skip opening "
                while (i < n && code[i] != '"') {
                    if (code[i] == '\\' && i + 1 < n) i++  // escape
                    i++
                }
                if (i < n) i++ // skip closing "
                tokens.add(HiToken(code.substring(start, i), TokenKind.STRING))
            }

            // ── 单引号字符串 ──
            ch == '\'' -> {
                val start = i
                i++ // skip opening '
                while (i < n && code[i] != '\'') {
                    if (code[i] == '\\' && i + 1 < n) i++
                    i++
                }
                if (i < n) i++ // skip closing '
                tokens.add(HiToken(code.substring(start, i), TokenKind.STRING))
            }

            // ── 反引号字符串 (JS/Go template literal) ──
            ch == '`' -> {
                val start = i
                i++
                while (i < n && code[i] != '`') {
                    if (code[i] == '\\' && i + 1 < n) i++
                    i++
                }
                if (i < n) i++
                tokens.add(HiToken(code.substring(start, i), TokenKind.STRING))
            }

            // ── 三引号字符串 (Python docstring) ──
            i + 2 < n && ((ch == '"' && code[i+1] == '"' && code[i+2] == '"') ||
                          (ch == '\'' && code[i+1] == '\'' && code[i+2] == '\'')) -> {
                val quote = code[i]
                val start = i
                i += 3
                while (i + 2 < n && !(code[i] == quote && code[i+1] == quote && code[i+2] == quote)) i++
                if (i + 2 < n) i += 3
                tokens.add(HiToken(code.substring(start, i), TokenKind.STRING))
            }

            // ── 数字字面量 ──
            ch.isDigit() || (ch == '.' && i + 1 < n && code[i + 1].isDigit()) -> {
                val start = i
                if (ch == '0' && i + 1 < n && (code[i+1] == 'x' || code[i+1] == 'X')) {
                    i += 2  // skip 0x
                    while (i < n && code[i].let { it.isDigit() || it in 'a'..'f' || it in 'A'..'F' }) i++
                } else {
                    while (i < n && (code[i].isDigit() || code[i] == '.' || code[i] == '_')) i++
                }
                // 后缀: f, d, L, ul, etc.
                while (i < n && code[i] in "fFdDlLuU") i++
                tokens.add(HiToken(code.substring(start, i), TokenKind.NUMBER))
            }

            // ── 标识符 & 关键字 ──
            ch.isLetter() || ch == '_' || ch == '$' -> {
                val start = i
                while (i < n && (code[i].isLetterOrDigit() || code[i] == '_' || code[i] == '$')) i++
                val word = code.substring(start, i)
                val kind = when {
                    word in keywords -> TokenKind.KEYWORD
                    word[0].isUpperCase() -> TokenKind.TYPE
                    i < n && code[i] == '(' -> TokenKind.FUNCTION
                    else -> TokenKind.PLAIN
                }
                tokens.add(HiToken(word, kind))
            }

            // ── 运算符 / 标点 ──
            ch in "+-*/%&|^~!=<>@:" || (ch == '.' && (i + 1 >= n || !code[i+1].isDigit())) -> {
                val start = i
                // 多字符运算符
                if (i + 1 < n && code.substring(i, i + 2) in setOf("==","!=","<=",">=","&&","||","++","--","->","::","=>","+=","-=","*=","/=","%=","&=","|=","^=","<<",">>","**","//","?.","!!","?:","?.")) {
                    i += 2
                } else {
                    i++
                }
                tokens.add(HiToken(code.substring(start, i), TokenKind.OPERATOR))
            }

            // ── 空白 / 换行 ──
            ch.isWhitespace() -> {
                val start = i
                while (i < n && code[i].isWhitespace()) i++
                tokens.add(HiToken(code.substring(start, i), TokenKind.PLAIN))
            }

            // ── 其他标点 ──
            else -> {
                tokens.add(HiToken(ch.toString(), TokenKind.PUNCTUATION))
                i++
            }
        }
    }

    return tokens
}

// ═══════════════════════════════════════════════════════════════════
//  内联文本渲染
// ═══════════════════════════════════════════════════════════════════

@Composable
private fun InlineText(
    raw: String,
    base: SpanStyle,
    linkColor: Color,
    codeBg: Color,
    codeFg: Color,
    lh: TextUnit = 22.sp,
    modifier: Modifier = Modifier,
) {
    val annotated = remember(raw) { buildInline(raw, base, linkColor, codeBg, codeFg) }
    val uri = LocalUriHandler.current
    val urls = annotated.getStringAnnotations("URL", 0, annotated.length)

    if (urls.isNotEmpty()) {
        ClickableText(annotated, modifier, style = TextStyle(lineHeight = lh)) { off ->
            annotated.getStringAnnotations("URL", off, off).firstOrNull()?.let { uri.openUri(it.item) }
        }
    } else {
        Text(annotated, modifier, lineHeight = lh)
    }
}

// ═══════════════════════════════════════════════════════════════════
//  内联解析引擎
// ═══════════════════════════════════════════════════════════════════

private fun buildInline(
    text: String,
    base: SpanStyle,
    linkColor: Color,
    codeBg: Color,
    codeFg: Color,
): AnnotatedString {
    val sb = StringBuilder()
    data class Slot(val pos: Int, val len: Int, val part: InlinePart)
    val slots = mutableListOf<Slot>()

    fun emit(part: InlinePart) {
        val start = sb.length
        when (part) {
            is InlinePart.Text -> sb.append(part.value)
            is InlinePart.Code -> sb.append(part.value)
            is InlinePart.Link -> sb.append(part.text)
            is InlinePart.Bold -> part.children.forEach { emit(it) }
            is InlinePart.Italic -> part.children.forEach { emit(it) }
            is InlinePart.Strikethrough -> part.children.forEach { emit(it) }
        }
        slots.add(Slot(start, sb.length - start, part))
    }

    val parts = parseInline(text)
    parts.forEach { emit(it) }

    return buildAnnotatedString {
        append(sb.toString())
        addStyle(base, 0, sb.length)

        for (slot in slots) {
            if (slot.len <= 0) continue
            val r = slot.pos until (slot.pos + slot.len)
            when (val p = slot.part) {
                is InlinePart.Bold -> addStyle(SpanStyle(fontWeight = FontWeight.Bold), r.first, r.last + 1)
                is InlinePart.Italic -> addStyle(SpanStyle(fontStyle = FontStyle.Italic), r.first, r.last + 1)
                is InlinePart.Strikethrough -> addStyle(SpanStyle(textDecoration = TextDecoration.LineThrough), r.first, r.last + 1)
                is InlinePart.Code -> {
                    addStyle(SpanStyle(
                        fontFamily = FontFamily.Monospace,
                        background = codeBg, color = codeFg,
                        fontSize = (base.fontSize ?: 15.sp) * 0.92f,
                    ), r.first, r.last + 1)
                }
                is InlinePart.Link -> {
                    addStyle(SpanStyle(color = linkColor, textDecoration = TextDecoration.Underline), r.first, r.last + 1)
                    addStringAnnotation("URL", p.url, r.first, r.last + 1)
                }
                is InlinePart.Text -> {}
            }
        }
    }
}

/** 解析内联 Markdown → [InlinePart] 列表 */
private fun parseInline(text: String): List<InlinePart> {
    val parts = mutableListOf<InlinePart>()
    var i = 0
    val n = text.length

    while (i < n) {
        when {
            // 内联代码 `...`
            text[i] == '`' -> {
                val end = text.indexOf('`', i + 1)
                if (end > i) {
                    parts.add(InlinePart.Code(text.substring(i + 1, end)))
                    i = end + 1
                } else {
                    parts.add(InlinePart.Text("`"))
                    i++
                }
            }

            // 链接 [text](url)
            text[i] == '[' -> {
                val close = text.indexOf(']', i + 1)
                val paren = if (close > i) text.indexOf('(', close + 1) else -1
                val parenClose = if (paren > close) text.indexOf(')', paren + 1) else -1
                if (close > i && paren == close + 1 && parenClose > paren) {
                    val linkText = text.substring(i + 1, close)
                    val linkUrl = text.substring(paren + 1, parenClose)
                    parts.add(InlinePart.Link(linkText, linkUrl))
                    i = parenClose + 1
                } else {
                    parts.add(InlinePart.Text("["))
                    i++
                }
            }

            // 粗体 **...**
            i + 1 < n && text[i] == '*' && text[i + 1] == '*' -> {
                val end = text.indexOf("**", i + 2)
                if (end > i) {
                    val inner = parseInline(text.substring(i + 2, end))
                    parts.add(InlinePart.Bold(inner))
                    i = end + 2
                } else {
                    parts.add(InlinePart.Text("**"))
                    i += 2
                }
            }

            // 斜体 *...*（单 *，不在 ** 中）
            text[i] == '*' -> {
                val end = text.indexOf('*', i + 1)
                if (end > i) {
                    val inner = parseInline(text.substring(i + 1, end))
                    parts.add(InlinePart.Italic(inner))
                    i = end + 1
                } else {
                    parts.add(InlinePart.Text("*"))
                    i++
                }
            }

            // 斜体 _..._
            text[i] == '_' -> {
                val end = text.indexOf('_', i + 1)
                if (end > i) {
                    val inner = parseInline(text.substring(i + 1, end))
                    parts.add(InlinePart.Italic(inner))
                    i = end + 1
                } else {
                    parts.add(InlinePart.Text("_"))
                    i++
                }
            }

            // 删除线 ~~...~~
            i + 1 < n && text[i] == '~' && text[i + 1] == '~' -> {
                val end = text.indexOf("~~", i + 2)
                if (end > i) {
                    val inner = parseInline(text.substring(i + 2, end))
                    parts.add(InlinePart.Strikethrough(inner))
                    i = end + 2
                } else {
                    parts.add(InlinePart.Text("~~"))
                    i += 2
                }
            }

            else -> {
                val start = i
                while (i < n && text[i] != '`' && text[i] != '[' &&
                    text[i] != '*' && text[i] != '_' && text[i] != '~') i++
                if (i > start) parts.add(InlinePart.Text(text.substring(start, i)))
            }
        }
    }
    return parts
}

// ═══════════════════════════════════════════════════════════════════
//  块级解析 (含表格)
// ═══════════════════════════════════════════════════════════════════

/** 判定一行是否可能是表格行 (至少含 2 个 `|`) */
private fun isTableLine(line: String): Boolean {
    val trimmed = line.trim()
    return trimmed.startsWith("|") && trimmed.count { it == '|' } >= 2
}

/** 判定是否为表格分隔行 (如 |---|---|) */
private fun isTableSeparator(line: String): Boolean {
    val trimmed = line.trim()
    if (!trimmed.startsWith("|") || !trimmed.endsWith("|")) return false
    val inner = trimmed.removeSurrounding("|").replace("|", "")
    return inner.all { it in setOf('-', ':', ' ') } && inner.any { it == '-' }
}

/** 从分隔行提取每列对齐 */
private fun parseAlignments(sepLine: String): List<ColAlign> {
    return sepLine.trim().removeSurrounding("|")
        .split("|").map { col ->
            val c = col.trim()
            val left = c.startsWith(":")
            val right = c.endsWith(":")
            when {
                left && right -> ColAlign.CENTER
                right -> ColAlign.RIGHT
                else -> ColAlign.LEFT
            }
        }
}

/** 解析一行表格单元格 */
private fun parseCells(line: String): List<String> {
    return line.trim().removeSurrounding("|")
        .split("|")  // 简单 split；不支持单元格内转义 |
        .map { it.trim() }
}

private fun parseBlocks(raw: String): List<MdBlock> {
    val blocks = mutableListOf<MdBlock>()
    var inCodeBlock = false
    var codeLang = ""
    val codeBuf = StringBuilder()

    // 表格缓冲：收集连续表格行，遇到非表格行时 flush
    val tableBuf = mutableListOf<String>()

    fun flushTable() {
        if (tableBuf.size < 2) {
            // 不足 2 行 → 当普通段落处理
            for (line in tableBuf) {
                blocks.add(MdBlock.Paragraph(line.trim()))
            }
            tableBuf.clear()
            return
        }
        // 第一行必须是表头，第二行必须是分隔行
        val headerLine = tableBuf[0]
        val sepLine = tableBuf[1]
        if (!isTableSeparator(sepLine)) {
            for (line in tableBuf) {
                blocks.add(MdBlock.Paragraph(line.trim()))
            }
            tableBuf.clear()
            return
        }

        val headers = parseCells(headerLine)
        val alignments = parseAlignments(sepLine)
        val rows = mutableListOf<List<String>>()
        for (i in 2 until tableBuf.size) {
            rows.add(parseCells(tableBuf[i]))
        }

        // 对齐列数：以表头为准，不足补齐空串
        val colCount = headers.size
        blocks.add(MdBlock.Table(
            headers = headers,
            alignments = List(colCount) { alignments.getOrElse(it) { ColAlign.LEFT } },
            rows = rows.map { row ->
                List(colCount) { row.getOrElse(it) { "" } }
            }
        ))
        tableBuf.clear()
    }

    for (line in raw.lines()) {
        // ── 代码块 ──
        if (line.trimStart().startsWith("```")) {
            // 先 flush 表格缓冲
            flushTable()

            if (inCodeBlock) {
                blocks.add(MdBlock.CodeBlock(codeBuf.toString().trimEnd(), codeLang))
                codeBuf.clear()
                codeLang = ""
                inCodeBlock = false
            } else {
                inCodeBlock = true
                codeLang = line.trimStart().removePrefix("```").trim()
            }
            continue
        }
        if (inCodeBlock) {
            if (codeBuf.isNotEmpty()) codeBuf.append('\n')
            codeBuf.append(line)
            continue
        }

        // ── 表格检测 ──
        if (isTableLine(line)) {
            tableBuf.add(line)
            continue
        } else if (tableBuf.isNotEmpty()) {
            flushTable()
        }

        // 空行
        if (line.isBlank()) {
            blocks.add(MdBlock.BlankLine)
            continue
        }

        val trimmed = line.trim()
        val noIndent = line.trimStart()

        // 标题
        val headerMatch = Regex("^(#{1,6})\\s+(.+)").find(trimmed)
        if (headerMatch != null) {
            blocks.add(MdBlock.Header(headerMatch.groupValues[1].length, headerMatch.groupValues[2]))
            continue
        }

        // 水平分割线
        if (Regex("^(-{3,}|\\*{3,}|_{3,})\\s*$").matches(trimmed)) {
            blocks.add(MdBlock.HorizontalRule)
            continue
        }

        // 无序列表
        val ulMatch = Regex("^[-*+]\\s+(.+)").find(noIndent)
        if (ulMatch != null) {
            blocks.add(MdBlock.UnorderedListItem(ulMatch.groupValues[1]))
            continue
        }

        // 有序列表
        val olMatch = Regex("^(\\d+)\\.\\s+(.+)").find(noIndent)
        if (olMatch != null) {
            blocks.add(MdBlock.OrderedListItem(olMatch.groupValues[1].toInt(), olMatch.groupValues[2]))
            continue
        }

        // 引用块
        val bqMatch = Regex("^>\\s?(.*)").find(noIndent)
        if (bqMatch != null) {
            blocks.add(MdBlock.BlockQuote(bqMatch.groupValues[1]))
            continue
        }

        // 普通段落
        blocks.add(MdBlock.Paragraph(noIndent))
    }

    // 收尾
    flushTable()

    if (inCodeBlock && codeBuf.isNotEmpty()) {
        blocks.add(MdBlock.CodeBlock(codeBuf.toString().trimEnd(), codeLang))
    }

    return blocks
}
