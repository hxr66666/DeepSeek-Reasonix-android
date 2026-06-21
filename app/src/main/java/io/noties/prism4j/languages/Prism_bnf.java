package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.Prism4j;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.grammar;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
public class Prism_bnf {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("bnf",
      token("string", pattern(compile("\"[^\\r\\n\"]*\"|'[^\\r\\n']*'"))),
      token("definition", pattern(compile("<[^<>\\r\\n\\t]+>(?=\\s*::=)"), false, false, "keyword")),
      token("rule", pattern(compile("<[^<>\\r\\n\\t]+>"))),
      token("operator", pattern(compile("::=|[|()\\[\\]{}*+?]|\\.{3}")))
    );
  }
}
