package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.Prism4j;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.MULTILINE;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.grammar;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
public class Prism_ebnf {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("ebnf",
      token("comment", pattern(compile("\\(\\*[\\s\\S]*?\\*\\)"))),
      token("string", pattern(compile("\"[^\"\\r\\n]*\"|'[^'\\r\\n]*'"), false, true)),
      token("special", pattern(compile("\\?[^?\\r\\n]*\\?"), false, true, "class-name")),
      token("definition", pattern(compile("(^[\\t ]*)[a-z]\\w*(?:[ \\t]+[a-z]\\w*)*(?=\\s*=)", CASE_INSENSITIVE | MULTILINE), true, false, "keyword")),
      token("rule", pattern(compile("\\b[a-z]\\w*(?:[ \\t]+[a-z]\\w*)*\\b", CASE_INSENSITIVE))),
      token("punctuation", pattern(compile("\\([:/]|[:/]\\)|[.,;()\\[\\]{}]"))),
      token("operator", pattern(compile("[-=|*/!]")))
    );
  }
}
