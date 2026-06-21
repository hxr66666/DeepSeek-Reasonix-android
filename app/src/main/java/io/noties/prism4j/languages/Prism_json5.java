package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.GrammarUtils;
import io.noties.prism4j.Prism4j;
import io.noties.prism4j.annotations.Extend;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
@Extend("json")
public class Prism_json5 {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return GrammarUtils.extend(
      GrammarUtils.require(prism4j, "json"),
      "json5",
      token("property",
        pattern(compile("(\"|\')(?:\\\\(?:\\r\\n?|\\n|.)|(?!\\1)[^\\\\\\r\\n])*\\1(?=\\s*:)"), false, true),
        pattern(compile("(?!\\s)[_$a-zA-Z\\xA0-\\uFFFF](?:(?!\\s)[$\\w\\xA0-\\uFFFF])*(?=\\s*:)"), false, false, "unquoted")
      ),
      token("string", pattern(compile("(\"|\')(?:\\\\(?:\\r\\n?|\\n|.)|(?!\\1)[^\\\\\\r\\n])*\\1"), false, true)),
      token("number", pattern(compile("[+-]?\\b(?:NaN|Infinity|0x[a-fA-F\\d]+)\\b|[+-]?(?:\\b\\d+(?:\\.\\d*)?|\\B\\.\\d+)(?:[eE][+-]?\\d+\\b)?")))
    );
  }
}
