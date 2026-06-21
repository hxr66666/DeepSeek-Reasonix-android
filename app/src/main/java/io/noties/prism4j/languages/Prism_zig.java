package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.Prism4j;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.grammar;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
public class Prism_zig {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("zig",
      token("comment",
        pattern(compile("\\/\\/[/!].*"), false, false, "doc-comment"),
        pattern(compile("\\/{2}.*"))
      ),
      token("string",
        pattern(compile("(^|[^\\\\@])c?\"(?:[^\"\\\\\\r\\n]|\\\\.)*\""), true, true),
        pattern(compile("([\\r\\n])([ \\t]+c?\\\\{2}).*(?:(?:\\r\\n?|\\n)\\2.*)*"), true, true)
      ),
      token("char", pattern(compile("(^|[^\\\\])'(?:[^'\\\\\\r\\n]|[\\uD800-\\uDFFF]{2}|\\\\(?:.|x[a-fA-F\\d]{2}|u\\{[a-fA-F\\d]{1,6}\\}))'"), true, true)),
      token("builtin", pattern(compile("\\B@(?!\\d)\\w+(?=\\s*\\()"))),
      token("label", pattern(compile("(\\b(?:break|continue)\\s*:\\s*)\\w+\\b|\\b(?!\\d)\\w+\\b(?=\\s*:\\s*(?:\\{|while\\b))"), true)),
      token("keyword", pattern(compile("\\b(?:align|allowzero|and|anyframe|anytype|asm|async|await|break|cancel|catch|comptime|const|continue|defer|else|enum|errdefer|error|export|extern|fn|for|if|inline|linksection|nakedcc|noalias|nosuspend|null|or|orelse|packed|promise|pub|resume|return|stdcallcc|struct|suspend|switch|test|threadlocal|try|undefined|union|unreachable|usingnamespace|var|volatile|while)\\b"))),
      token("builtin-type", pattern(compile("\\b(?:anyerror|bool|c_u?(?:int|long|longlong|short)|c_longdouble|c_void|comptime_(?:float|int)|f(?:16|32|64|128)|[iu](?:8|16|32|64|128|size)|noreturn|type|void)\\b"), false, false, "keyword")),
      token("function", pattern(compile("\\b(?!\\d)\\w+(?=\\s*\\()"))),
      token("number", pattern(compile("\\b(?:0b[01]+|0o[0-7]+|0x[a-fA-F\\d]+(?:\\.[a-fA-F\\d]*)?(?:[pP][+-]?[a-fA-F\\d]+)?|\\d+(?:\\.\\d*)?(?:[eE][+-]?\\d+)?)\\b"))),
      token("boolean", pattern(compile("\\b(?:false|true)\\b"))),
      token("operator", pattern(compile("\\.[*?]|\\.{2,3}|[-=]>|\\*\\*|\\+\\+|\\|\\||(?:<<|>>|[-+*]%|[-+*\\/%^&|<>!=])=?|[?~]"))),
      token("punctuation", pattern(compile("[.:,;(){}\\[\\]]")))
    );
  }
}
