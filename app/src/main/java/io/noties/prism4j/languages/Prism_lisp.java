package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.Prism4j;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.grammar;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
public class Prism_lisp {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("lisp",
      token("comment", pattern(compile(";.*"))),
      token("string", pattern(compile("\"(?:[^\"\\\\]|\\\\.)*\""), false, true)),
      token("keyword",
        pattern(compile("(\\()(?:and|(?:cl-)?letf|cl-loop|cond|cons|error|if|(?:lexical-)?let\\*?|message|not|null|or|provide|require|setq|unless|use-package|when|while)(?=[\\s\\)])"), true),
        pattern(compile("(\\()(?:append|by|collect|concat|do|finally|for|in|return)(?=[\\s\\)])"), true)
      ),
      token("boolean", pattern(compile("((?:[\\s([])(?:nil|t)(?=[\\s)])"), true)),
      token("number", pattern(compile("((?:[\\s([])[-+]?\\d+(?:\\.\\d*)?(?=[\\s)])"), true)),
      token("function", pattern(compile("(?!\\d)[-+*/~!@$%^=<>{}\\w]+(?=\\s*(?:[({]|[)\\[]]\\s*$))"))),
      token("car", pattern(compile("(\\()(?!\\d)[-+*/~!@$%^=<>{}\\w]+"), true)),
      token("punctuation",
        pattern(compile("(?:['\\'`,]?\\(|[)\\[\\]])")),
        pattern(compile("(\\s)\\.(?=\\s)"), true)
      )
    );
  }
}
