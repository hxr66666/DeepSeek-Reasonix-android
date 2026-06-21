package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.Prism4j;
import static java.util.regex.Pattern.MULTILINE;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.grammar;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
public class Prism_javastacktrace {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("javastacktrace",
      token("summary", pattern(
        compile("^([ \\t]*)(?:(?:Caused by:|Suppressed:|Exception in thread \"[^\"]*\")[ \\t]+)?[\\w$.]+(?::.*)?$", MULTILINE),
        true
      )),
      token("stack-frame", pattern(
        compile("^([ \\t]*)at (?:[\\w$.\\/]|@[\\w$.+-]*\\/)+(?:<init>)?\\([^()]*\\)", MULTILINE),
        true
      )),
      token("more", pattern(
        compile("^([ \\t]*)\\.{3} \\d+ [a-z]+(?: [a-z]+)*", MULTILINE),
        true
      ))
    );
  }
}
