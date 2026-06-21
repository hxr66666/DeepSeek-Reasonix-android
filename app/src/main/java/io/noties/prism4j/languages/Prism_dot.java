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
public class Prism_dot {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("dot",
      token("comment", pattern(compile("\\/\\/.*|\\/\\*[\\s\\S]*?\\*\\/|^#.*", MULTILINE), false, true)),
      token("graph-name", pattern(
        compile("(\\b(?:digraph|graph|subgraph)[ \\t\\r\\n]+)(?:[a-zA-Z_\\x80-\\uFFFF][\\w\\x80-\\uFFFF]*|-?(?:\\.\\d+|\\d+(?:\\.\\d*)?)|-?(?:\\.\\.\\d+|\\d+(?:\\.\\d*)?)|\".+\")"),
        true, true, "class-name"
      )),
      token("keyword", pattern(compile("\\b(?:digraph|edge|graph|node|strict|subgraph)\\b", CASE_INSENSITIVE))),
      token("compass-point", pattern(compile("(:[ \\t\\r\\n]*)(?:[ewc_]|[ns][ew]?)(?![\\w\\x80-\\uFFFF])"), true, false, "builtin")),
      token("attr-name", pattern(compile("(^|[\\[;, \\t\\r\\n])[a-zA-Z_\\x80-\\uFFFF][\\w\\x80-\\uFFFF]*(?=[ \\t\\r\\n]*=)"), true, true)),
      token("attr-value", pattern(compile("(=[ \\t\\r\\n]*)(?:[a-zA-Z_\\x80-\\uFFFF][\\w\\x80-\\uFFFF]*|-?(?:\\.\\d+|\\d+(?:\\.\\d*)?)|\".+\")"), true, true)),
      token("node", pattern(compile("(^|[^-.\\w\\x80-\\uFFFF\\\\])[a-zA-Z_\\x80-\\uFFFF][\\w\\x80-\\uFFFF]*"), true, true)),
      token("operator", pattern(compile("[=:]|-[->]"))),
      token("punctuation", pattern(compile("[\\[\\]{};,]")))
    );
  }
}
