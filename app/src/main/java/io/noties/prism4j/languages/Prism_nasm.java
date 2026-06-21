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
public class Prism_nasm {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("nasm",
      token("comment", pattern(compile(";.*$", MULTILINE))),
      token("string", pattern(compile("([\"'`])(?:\\\\.|(?!\\1)[^\\\\\\r\\n])*\\1"))),
      token("label", pattern(compile("(^\\s*)[A-Za-z._?$][\\w.?$@~#]*:", MULTILINE), true, false, "function")),
      token("keyword",
        pattern(compile("\\[?BITS (?:16|32|64)\\]?")),
        pattern(compile("(^\\s*)section\\s*[a-z.]+:?", CASE_INSENSITIVE | MULTILINE), true),
        pattern(compile("(?:extern|global)[^;\\r\\n]*", CASE_INSENSITIVE)),
        pattern(compile("(?:CPU|DEFAULT|FLOAT).*$", MULTILINE))
      ),
      token("register", pattern(compile("\\b(?:st\\d|[xyz]mm\\d\\d?|[cdt]r\\d|r\\d\\d?[bwd]?|[er]?[abcd]x|[abcd][hl]|[er]?(?:bp|di|si|sp)|[cdefgs]s)\\b", CASE_INSENSITIVE), false, false, "variable")),
      token("number", pattern(compile("(?:\\b|(?=\\$))(?:0[hx](?:\\.[\\da-f]+|[\\da-f]+(?:\\.[\\da-f]+)?)(?:p[+-]?\\d+)?|\\d[\\da-f]+[hx]|\\$\\d[\\da-f]*|0[oq][0-7]+|[0-7]+[oq]|0[by][01]+|[01]+[by]|0[dt]\\d+|(?:\\d+(?:\\.\\d+)?|\\.\\d+)(?:\\.?e[+-]?\\d+)?[dt]?)\\b", CASE_INSENSITIVE))),
      token("operator", pattern(compile("[\\[\\]*+\\-\\/%<>=&|$!]")))
    );
  }
}
