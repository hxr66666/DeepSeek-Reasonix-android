package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.Prism4j;
import static java.util.regex.Pattern.MULTILINE;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.grammar;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
public class Prism_systemd {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("systemd",
      token("comment", pattern(compile("^[;#].*", MULTILINE), false, true)),
      token("section", pattern(compile("^\\[[^\\n\\r\\[\\]]*\\](?=[ \\t]*$)", MULTILINE), false, true)),
      token("key", pattern(compile("^[^\\s=]+(?=[ \\t]*=)", MULTILINE), false, true, "attr-name")),
      token("value", pattern(
        compile("(=[ \\t]*(?!\\s))(?:\"(?:[^\\r\\n\"\\\\]|\\\\(?:[^\\r]|\\r\\n?))*\"(?!\\S)|(?=[^\"\\r\\n]))(?:[^\\s\\\\]|[ \\t]+(?:(?![ \\t\"])|\"(?:[^\\r\\n\"\\\\]|\\\\(?:[^\\r]|\\r\\n?))*\"(?!\\S))|\\\\[\\r\\n]+(?:[#;].*[\\r\\n]+)*(?![#;]))*"),
        true, true, "attr-value"
      )),
      token("punctuation", pattern(compile("=")))
    );
  }
}
