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
public class Prism_nginx {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("nginx",
      token("comment", pattern(compile("(^|[\\s{};])#.*"), true, true)),
      token("directive", pattern(
        compile("(^|\\s)\\w(?:[^;{}'\"\\\\\\s]|\\\\.|\"(?:[^\"\\\\]|\\\\.)*\"|'(?:[^'\\\\]|\\\\.)*'|\\s+(?:#.*(?!.)|(?![#\\s])))*?(?=\\s*[;{])"),
        true, true
      )),
      token("punctuation", pattern(compile("[{};]")))
    );
  }
}
