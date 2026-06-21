package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.Prism4j;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.grammar;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
public class Prism_prolog {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("prolog",
      token("comment", pattern(compile("\\/\\*[\\s\\S]*?\\*\\/|%.*"), false, true)),
      token("string", pattern(compile("([\"'])(?:\\1\\1|\\\\(?:\\r\\n|[\\s\\S])|(?!\\1)[^\\\\\\r\\n])*\\1(?!\\1)"), false, true)),
      token("builtin", pattern(compile("\\b(?:fx|fy|xf[xy]?|yfx?)\\b"))),
      token("function", pattern(compile("\\b[a-z]\\w*(?:(?=\\()|\\/?\\d+)"))),
      token("number", pattern(compile("\\b\\d+(?:\\.\\d*)?"))),
      token("operator", pattern(compile("[:\\\\=><\\-?*@\\/;+^|!$.]+|\\b(?:is|mod|not|xor)\\b"))),
      token("punctuation", pattern(compile("[(){}\\[\\],]")))
    );
  }
}
