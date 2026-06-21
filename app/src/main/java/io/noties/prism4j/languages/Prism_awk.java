package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.Prism4j;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.grammar;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
public class Prism_awk {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("awk",
      token("hashbang", pattern(compile("^#!.*"), false, true, "comment")),
      token("comment", pattern(compile("#.*"), false, true)),
      token("string", pattern(compile("(^|[^\\\\])\"(?:[^\\\\\"\\r\\n]|\\\\.)*\""), true, true)),
      token("regex", pattern(compile("((?:^|[^\\w\\s)])\\s*)\\/(?:[^\\/\\\\\\r\\n]|\\\\.)*\\/"), true, true)),
      token("variable", pattern(compile("\\$\\w+"))),
      token("keyword", pattern(compile("\\b(?:BEGIN|BEGINFILE|END|ENDFILE|break|case|continue|default|delete|do|else|exit|for|function|getline|if|in|next|nextfile|printf?|return|switch|while)\\b|@(?:include|load)\\b"))),
      token("function", pattern(compile("\\b[a-z_]\\w*(?=\\s*\\()", CASE_INSENSITIVE))),
      token("number", pattern(compile("\\b(?:\\d+(?:\\.\\d+)?(?:e[+-]?\\d+)?|0x[a-fA-F0-9]+)\\b"))),
      token("operator", pattern(compile("--| \\+\\+|!?~|>&|>>|<<|(?:\\*\\*|[<>!=+\\-*\\/%^])=?|&&|\\|[|&]|[?:]"))),
      token("punctuation", pattern(compile("[()\\[\\]{},;]")))
    );
  }
}
