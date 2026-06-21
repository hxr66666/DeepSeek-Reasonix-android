package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.Prism4j;
import static java.util.regex.Pattern.MULTILINE;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.grammar;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
public class Prism_properties {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("properties",
      token("comment", pattern(compile("^[ \\t]*[#!].*$", MULTILINE))),
      token("value", pattern(compile("(^[ \\t]*(?:\\\\(?:\\r\\n|[\\s\\S])|[^\\\\\\s:=])+(?:[ ]*[=:][ ]*(?! )|[ ])+)(?:\\\\(?:\\r\\n|[\\s\\S])|[^\\\\\\r\\n])+", MULTILINE), true, false, "attr-value")),
      token("key", pattern(compile("^[ \\t]*(?:\\\\(?:\\r\\n|[\\s\\S])|[^\\\\\\s:=])+(?=[ ]*[=:]|[ ])", MULTILINE), false, false, "attr-name")),
      token("punctuation", pattern(compile("[=:]")))
    );
  }
}
