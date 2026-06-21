package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.Prism4j;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.grammar;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
public class Prism_erlang {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("erlang",
      token("comment", pattern(compile("%.+"))),
      token("string", pattern(compile("\"(?:\\\\.|[^\\\\\"\\r\\n])*\""), false, true)),
      token("quoted-function", pattern(compile("'(?:\\\\.|[^\\\\\'\\r\\n])+'(?=\\()"), false, false, "function")),
      token("quoted-atom", pattern(compile("'(?:\\\\.|[^\\\\\'\\r\\n])+'"), false, false, "atom")),
      token("boolean", pattern(compile("\\b(?:false|true)\\b"))),
      token("keyword", pattern(compile("\\b(?:after|begin|case|catch|end|fun|if|of|receive|try|when)\\b"))),
      token("number",
        pattern(compile("\\$\\\\?.")),
        pattern(compile("\\b\\d+#[a-z0-9]+", CASE_INSENSITIVE)),
        pattern(compile("(?:\\b\\d+(?:\\.\\d*)?|\\B\\.\\d+)(?:e[+-]?\\d+)?", CASE_INSENSITIVE))
      ),
      token("function", pattern(compile("\\b[a-z][\\w@]*(?=\\()"))),
      token("variable", pattern(compile("(^|[^@])(?:\\b|\\?)[A-Z_][\\w@]*"), true)),
      token("operator",
        pattern(compile("[=/<>:]=|=[:/]=|\\+\\+?|--?|[=*/!]|\\b(?:and|andalso|band|bnot|bor|bsl|bsr|bxor|div|not|or|orelse|rem|xor)\\b")),
        pattern(compile("(^|[^<])<(?!<)"), true),
        pattern(compile("(^|[^>])>(?!>)"), true)
      ),
      token("atom", pattern(compile("\\b[a-z][\\w@]*"))),
      token("punctuation", pattern(compile("[()\\[\\]{}:;,.#|]|<<|>>")))
    );
  }
}
