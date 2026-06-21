package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.Prism4j;
import static java.util.regex.Pattern.MULTILINE;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.grammar;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
public class Prism_jsstacktrace {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("jsstacktrace",
      token("error-message", pattern(compile("^\\S.*", MULTILINE), false, false, "string")),
      token("not-my-code", pattern(compile("^(?:[ \\t]+)at[ \\t]+(?!\\s)(?:node\\.js|<unknown>|.*(?:node_modules|\\(<anonymous>\\)|\\(<unknown>|<anonymous>$|\\(internal\\/|\\(node\\.js)).*", MULTILINE), false, false, "comment")),
      token("filename", pattern(compile("(?<=\\bat\\s|\\()(?:[a-zA-Z]:)?[^():]+(?=:)"), true, false, "url")),
      token("function", pattern(compile("(?<=\\bat\\s(?:new\\s)?)(?!\\s)[_$a-zA-Z\\xA0-\\uFFFF<][.$\\w\\xA0-\\uFFFF<>]*"), true)),
      token("keyword", pattern(compile("\\b(?:at|new)\\b"))),
      token("alias", pattern(compile("\\[(?:as\\s+)?(?!\\s)[_$a-zA-Z\\xA0-\\uFFFF][$\\w\\xA0-\\uFFFF]*\\]"), false, false, "variable")),
      token("line-number", pattern(compile(":\\d+(?::\\d+)?\\b"), false, false, "number")),
      token("punctuation", pattern(compile("[()]")))
    );
  }
}
