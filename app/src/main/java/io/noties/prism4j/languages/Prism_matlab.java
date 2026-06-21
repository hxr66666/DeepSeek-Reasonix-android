package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.Prism4j;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.grammar;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
public class Prism_matlab {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("matlab",
      token("comment",
        pattern(compile("%\\{[\\s\\S]*?\\}%")),
        pattern(compile("%.+"))
      ),
      token("string", pattern(compile("\\B'(?:''|[^'\\r\\n])*'"), false, true)),
      token("number", pattern(compile("(?:\\b\\d+(?:\\.\\d*)?|\\B\\.\\d+)(?:[eE][+-]?\\d+)?(?:[ij])?|\\b[ij]\\b"))),
      token("keyword", pattern(compile("\\b(?:NaN|break|case|catch|continue|else|elseif|end|for|function|if|inf|otherwise|parfor|pause|pi|return|switch|try|while)\\b"))),
      token("function", pattern(compile("\\b(?!\\d)\\w+(?=\\s*\\()"))),
      token("operator", pattern(compile("\\.?[*^\\/\\\\']|[+\\-:@]|[<>=~]=?|&&?|\\|\\|?"))),
      token("punctuation", pattern(compile("\\.{3}|[.,;\\[\\](){}!]")))
    );
  }
}
