package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.Prism4j;
import static java.util.regex.Pattern.MULTILINE;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.grammar;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
public class Prism_mermaid {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("mermaid",
      token("comment", pattern(compile("%%.+"), false, true)),
      token("keyword", pattern(compile("\\b(?:autonumber|classDiagram|classDiagram-v2|end|erDiagram|flowchart|gantt|gitGraph|graph|journey|link|pie|requirementDiagram|sequenceDiagram|stateDiagram|stateDiagram-v2|subgraph|title)\\b"))),
      token("entity", pattern(compile("\\b[A-Z][\\w-]*(?=\\s*[{(])"))),
      token("arrow", pattern(compile("[|<>ox]*(?:--+|==+|-\\.+)[|<>ox]*(?:[>ox]|[|<>ox]{2})?|[|<>ox]*(?:\\.+-)|\\.\\.|<\\|\\|?\\|\\|?>|\\|o--|o\\|\\|?--?|<?\\|\\||[=~]>|--"))),
      token("string", pattern(compile("\"[^\"\\r\\n]*\""), false, true)),
      token("number", pattern(compile("\\b\\d+(?:\\.\\d+)?\\b"))),
      token("operator", pattern(compile("[:&]|as\\b"))),
      token("punctuation", pattern(compile("[{}\\[\\]();,.]")))
    );
  }
}
