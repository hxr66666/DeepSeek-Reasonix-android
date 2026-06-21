package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.Prism4j;
import static java.util.regex.Pattern.MULTILINE;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.grammar;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
public class Prism_ini {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("ini",
      token("comment", pattern(compile("(^[ \\f\\t\\v]*)[#;][^\\n\\r]*", MULTILINE), true)),
      token("section", pattern(compile("(^[ \\f\\t\\v]*)\\[[^\\n\\r\\]]*\\]?", MULTILINE), true)),
      token("key", pattern(compile("(^[ \\f\\t\\v]*)[^ \\f\\n\\r\\t\\v=]+(?:[ \\f\\t\\v]+[^ \\f\\n\\r\\t\\v=]+)*(?=[ \\f\\t\\v]*=)", MULTILINE), true, false, "attr-name")),
      token("value", pattern(compile("(=[ \\f\\t\\v]*)[^ \\f\\n\\r\\t\\v]+(?:[ \\f\\t\\v]+[^ \\f\\n\\r\\t\\v]+)*"), true, false, "attr-value")),
      token("punctuation", pattern(compile("=")))
    );
  }
}
