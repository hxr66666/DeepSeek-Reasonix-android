package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.Prism4j;
import static java.util.regex.Pattern.MULTILINE;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.grammar;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
public class Prism_http {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("http",
      token("request-line", pattern(
        compile("^(?:CONNECT|DELETE|GET|HEAD|OPTIONS|PATCH|POST|PRI|PUT|SEARCH|TRACE)\\s(?:https?:\\/\\/|\\/)\\S*\\sHTTP\\/[\\d.]+", MULTILINE)
      )),
      token("response-status", pattern(
        compile("^HTTP\\/[\\d.]+ \\d+ .+", MULTILINE)
      )),
      token("header", pattern(
        compile("^[\\w-]:.+(?:(?:\\r\\n?|\\n)[ \\t].+)*", MULTILINE)
      ))
    );
  }
}
