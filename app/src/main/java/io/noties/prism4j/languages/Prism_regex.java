package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.Prism4j;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.grammar;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
public class Prism_regex {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("regex",
      token("char-class", pattern(compile("((?:^|[^\\\\])(?:\\\\\\\\)*)\\[(?:[^\\\\\\]]|\\\\[\\s\\S])*\\]"), true)),
      token("special-escape", pattern(compile("\\\\[\\\\(){}\\[\\]^$+*?|.]"), false, false, "escape")),
      token("char-set", pattern(compile("\\.|\\\\[wsd]|\\\\p\\{[^{}]+\\}", CASE_INSENSITIVE), false, false, "class-name")),
      token("backreference",
        pattern(compile("\\\\(?![123][0-7]{2})[1-9]"), false, false, "keyword"),
        pattern(compile("\\\\k<[^<>']+>"), false, false, "keyword")
      ),
      token("anchor", pattern(compile("[$^]|\\\\[ABbGZz]"), false, false, "function")),
      token("escape", pattern(compile("\\\\(?:x[\\da-fA-F]{2}|u[\\da-fA-F]{4}|u\\{[\\da-fA-F]+\\}|0[0-7]{0,2}|[123][0-7]{2}|c[a-zA-Z]|.)"))),
      token("group",
        pattern(compile("\\((?:\\?(?:<[^<>']+>|'[^<>']+'|[>:]|<?[=!]|[idmnsuxU]+(?:-[idmnsuxU]+)?:?))?"), false, false, "punctuation"),
        pattern(compile("\\)"), false, false, "punctuation")
      ),
      token("quantifier", pattern(compile("(?:[+*?]|\\{\\d+(?:,\\d*)?\\})[?+]?"), false, false, "number")),
      token("alternation", pattern(compile("\\|"), false, false, "keyword"))
    );
  }
}
