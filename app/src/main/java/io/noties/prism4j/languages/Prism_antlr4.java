package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.Prism4j;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.grammar;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
public class Prism_antlr4 {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("antlr4",
      token("comment", pattern(compile("\\/\\/.*|\\/\\*[\\s\\S]*?(?:\\*\\/|$)"))),
      token("string", pattern(compile("'(?:\\\\.|[^\\\\\'\\r\\n])*'"), false, true)),
      token("character-class", pattern(compile("\\[(?:\\\\.|[^\\\\\\]\\r\\n])*\\]"), false, true, "regex")),
      token("action", pattern(compile("\\{(?:[^{}]|\\{(?:[^{}]|\\{(?:[^{}]|\\{[^{}]*\\})*\\})*\\})*\\}"), false, true)),
      token("annotation", pattern(compile("@\\w+(?:::\\w+)*"), false, false, "keyword")),
      token("label", pattern(compile("#[ \\t]*\\w+"), false, false, "punctuation")),
      token("keyword", pattern(compile("\\b(?:catch|channels|finally|fragment|grammar|import|lexer|locals|mode|options|parser|returns|throws|tokens)\\b"))),
      token("constant", pattern(compile("\\b[A-Z][A-Z_]*\\b"))),
      token("operator", pattern(compile("\\.\\.|->|[|~]|[*+?]\\??"))),
      token("punctuation", pattern(compile("[;:()=]")))
    );
  }
}
