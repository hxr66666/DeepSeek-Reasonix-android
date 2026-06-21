package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.Prism4j;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.grammar;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
public class Prism_ocaml {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("ocaml",
      token("comment", pattern(compile("\\(\\*[\\s\\S]*?\\*\\)"), false, true)),
      token("char", pattern(compile("'(?:[^\\\\\\r\\n']|\\\\(?:.|[ox]?[0-9a-f]{1,3}))'", CASE_INSENSITIVE), false, true)),
      token("string",
        pattern(compile("\"(?:\\\\(?:[\\s\\S]|\\r\\n)|[^\\\\\\r\\n\"])*\""), false, true),
        pattern(compile("\\{([a-z_]*)\\|[\\s\\S]*?\\|\\1\\}"), false, true)
      ),
      token("number",
        pattern(compile("\\b(?:0b[01][01_]*|0o[0-7][0-7_]*)\\b", CASE_INSENSITIVE)),
        pattern(compile("\\b0x[a-f0-9][a-f0-9_]*(?:\\.[a-f0-9_]*)?(?:p[+-]?\\d[\\d_]*)?(?!\\w)", CASE_INSENSITIVE)),
        pattern(compile("\\b\\d[\\d_]*(?:\\.[\\d_]*)?(?:e[+-]?\\d[\\d_]*)?(?!\\w)", CASE_INSENSITIVE))
      ),
      token("directive", pattern(compile("\\B#\\w+"), false, false, "property")),
      token("label", pattern(compile("\\B~\\w+"), false, false, "property")),
      token("type-variable", pattern(compile("\\B'\\w+"), false, false, "function")),
      token("variant", pattern(compile("`\\w+"), false, false, "symbol")),
      token("keyword", pattern(compile("\\b(?:as|assert|begin|class|constraint|do|done|downto|else|end|exception|external|for|fun|function|functor|if|in|include|inherit|initializer|lazy|let|match|method|module|mutable|new|nonrec|object|of|open|private|rec|sig|struct|then|to|try|type|val|value|virtual|when|where|while|with)\\b"))),
      token("boolean", pattern(compile("\\b(?:false|true)\\b"))),
      token("operator-like-punctuation", pattern(compile("\\[[<>|]|[>|]\\]|\\{<|>\\}"), false, false, "punctuation")),
      token("operator", pattern(compile("\\.[.~]|:[=>]|[=<>@^|&+\\-*\\/$%!?~][!$%&*+\\-.\\/<=>?@^|~]*|\\b(?:and|asr|land|lor|lsl|lsr|lxor|mod|or)\\b"))),
      token("punctuation", pattern(compile(";;|::|[(){}\\[\\].,:;#]|\\b_\\b")))
    );
  }
}
