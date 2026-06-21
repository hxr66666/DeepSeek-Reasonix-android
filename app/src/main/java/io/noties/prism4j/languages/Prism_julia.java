package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.Prism4j;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.grammar;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
public class Prism_julia {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("julia",
      token("comment", pattern(compile("(^|[^\\\\])(?:#=(?:[^#=]|=(?!#)|#(?!=)|#=(?:[^#=]|=(?!#)|#(?!=))*=#)*=#|#.*)"), true)),
      token("regex", pattern(compile("r\"(?:\\\\.|[^\"\\\\\\r\\n])*\"[imsx]{0,4}"), false, true)),
      token("string", pattern(compile("\"\"\"[\\s\\S]+?\"\"\"|(?:\\b\\w+)?\"(?:\\\\.|[^\"\\\\\\r\\n])*\"|`(?:[^\\\\`\\r\\n]|\\\\.)*`"), false, true)),
      token("char", pattern(compile("(^|[^\\w'])'(?:\\\\[^\\r\\n][^'\\r\\n]*|[^\\\\\\r\\n])'"), true, true)),
      token("keyword", pattern(compile("\\b(?:abstract|baremodule|begin|bitstype|break|catch|ccall|const|continue|do|else|elseif|end|export|finally|for|function|global|if|immutable|import|importall|in|let|local|macro|module|print|println|quote|return|struct|try|type|typealias|using|while)\\b"))),
      token("boolean", pattern(compile("\\b(?:false|true)\\b"))),
      token("number", pattern(compile("(?:\\b(?=\\d)|\\B(?=\\.))(?:0[box])?(?:[\\da-f]+(?:_[\\da-f]+)*(?:\\.(?:\\d+(?:_\\d+)*)?)?|\\.\\d+(?:_\\d+)*)(?:[efp][+-]?\\d+(?:_\\d+)*)?j?", CASE_INSENSITIVE))),
      token("operator", pattern(compile("&&|\\|\\||[-+*^%÷⊻&$\\\\]=?|\\/[\\/=]?|!=?=?|\\|[=>]?|<(?:<=?|[=:|])?|>(?:=|>>?=?)?|==?=?|[~≠≤≥'√∛]"))),
      token("punctuation", pattern(compile("::?|[{}\\[\\]();,.?]"))),
      token("constant", pattern(compile("\\b(?:(?:Inf|NaN)(?:16|32|64)?|im|pi)\\b|[πℯ]")))
    );
  }
}
