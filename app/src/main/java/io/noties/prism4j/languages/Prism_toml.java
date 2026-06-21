package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.Prism4j;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.MULTILINE;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.grammar;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
public class Prism_toml {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("toml",
      token("comment", pattern(compile("#.*"), false, true)),
      token("table", pattern(
        compile("(^[\\t ]*\\[\\s*(?:\\[\\s*)?)(?:[\\w-]+|'[^'\\n\\r]*'|\"(?:\\\\.|[^\\\\\"\r\n])*\")(?:\\s*\\.\\s*(?:[\\w-]+|'[^'\\n\\r]*'|\"(?:\\\\.|[^\\\\\"\r\n])*\"))*(?=\\s*\\])", MULTILINE),
        true, true, "class-name"
      )),
      token("key", pattern(
        compile("(^[\\t ]*|[{,]\\s*)(?:[\\w-]+|'[^'\\n\\r]*'|\"(?:\\\\.|[^\\\\\"\r\n])*\")(?:\\s*\\.\\s*(?:[\\w-]+|'[^'\\n\\r]*'|\"(?:\\\\.|[^\\\\\"\r\n])*\"))*(?=\\s*=)", MULTILINE),
        true, true, "property"
      )),
      token("string", pattern(compile("\"\"\"(?:\\\\[\\s\\S]|[^\\\\])*?\"\"\"|'''[\\s\\S]*?'''|'[^'\\n\\r]*'|\"(?:\\\\.|[^\\\\\"\r\n])*\""), false, true)),
      token("date",
        pattern(compile("\\b\\d{4}-\\d{2}-\\d{2}(?:[T\\s]\\d{2}:\\d{2}:\\d{2}(?:\\.\\d+)?(?:Z|[+-]\\d{2}:\\d{2})?)?\\b", CASE_INSENSITIVE), false, false, "number"),
        pattern(compile("\\b\\d{2}:\\d{2}:\\d{2}(?:\\.\\d+)?\\b"), false, false, "number")
      ),
      token("number", pattern(compile("(?:\\b0(?:x[\\da-zA-Z]+(?:_[\\da-zA-Z]+)*|o[0-7]+(?:_[0-7]+)*|b[10]+(?:_[10]+)*)\\b)|[-+]?\\b\\d+(?:_\\d+)*(?:\\.\\d+(?:_\\d+)*)?(?:[eE][+-]?\\d+(?:_\\d+)*)?\\b|[-+]?\\b(?:inf|nan)\\b"))),
      token("boolean", pattern(compile("\\b(?:false|true)\\b"))),
      token("punctuation", pattern(compile("[.,=\\[\\]{}]")))
    );
  }
}
