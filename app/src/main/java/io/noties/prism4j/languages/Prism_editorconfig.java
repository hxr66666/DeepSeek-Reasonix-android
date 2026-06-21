package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.Prism4j;
import static java.util.regex.Pattern.MULTILINE;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.grammar;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
public class Prism_editorconfig {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("editorconfig",
      token("comment", pattern(compile("[;#].*"))),
      token("section", pattern(compile("(^[ \\t]*)\\[.+\\]", MULTILINE), true, false, "selector")),
      token("key", pattern(compile("(^[ \\t]*)[^\\s=]+(?=[ \\t]*=)", MULTILINE), true, false, "attr-name")),
      token("value", pattern(compile("=.*"), false, false, "attr-value"))
    );
  }
}
