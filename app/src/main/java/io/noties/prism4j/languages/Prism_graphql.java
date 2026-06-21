package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.Prism4j;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.grammar;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
public class Prism_graphql {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("graphql",
      token("comment", pattern(compile("#.*"))),
      token("description", pattern(compile("(?:\"\"\"(?:[^\"]|(?!\"\"\")\")*/\"\"\"|\"|(?:\\\\.|[^\\\\\"\\r\\n])*\")(?=\\s*[a-z_])", CASE_INSENSITIVE), false, true, "string")),
      token("string", pattern(compile("\"\"\"(?:[^\"]|(?!\"\"\")\")*/\"\"\"|\"(?:\\\\.|[^\\\\\"\\r\\n])*\""), false, true)),
      token("number", pattern(compile("(?:\\B-|\\b)\\d+(?:\\.\\d+)?(?:e[+-]?\\d+)?\\b", CASE_INSENSITIVE))),
      token("boolean", pattern(compile("\\b(?:false|true)\\b"))),
      token("variable", pattern(compile("\\$[a-z_]\\w*", CASE_INSENSITIVE))),
      token("directive", pattern(compile("@[a-z_]\\w*", CASE_INSENSITIVE), false, false, "function")),
      token("attr-name", pattern(compile("\\b[a-z_]\\w*(?=\\s*(?:\\((?:[^()\"]|\"(?:\\\\.|[^\\\\\"\\r\\n])*\")*\\))?:)", CASE_INSENSITIVE), false, true)),
      token("atom-input", pattern(compile("\\b[A-Z]\\w*Input\\b"), false, false, "class-name")),
      token("scalar", pattern(compile("\\b(?:Boolean|Float|ID|Int|String)\\b"))),
      token("constant", pattern(compile("\\b[A-Z][A-Z_\\d]*\\b"))),
      token("class-name", pattern(compile("(\\b(?:enum|implements|interface|on|scalar|type|union)\\s+|&\\s*|:\\s*|\\[)[A-Z_]\\w*"), true)),
      token("fragment", pattern(compile("(\\bfragment\\s+|\\.{3}\\s*(?!on\\b))[a-zA-Z_]\\w*"), true, false, "function")),
      token("definition-mutation", pattern(compile("(\\bmutation\\s+)[a-zA-Z_]\\w*"), true, false, "function")),
      token("definition-query", pattern(compile("(\\bquery\\s+)[a-zA-Z_]\\w*"), true, false, "function")),
      token("keyword", pattern(compile("\\b(?:directive|enum|extend|fragment|implements|input|interface|mutation|on|query|repeatable|scalar|schema|subscription|type|union)\\b"))),
      token("operator", pattern(compile("[!=|&]|\\.{3}"))),
      token("property-query", pattern(compile("\\w+(?=\\s*\\()"))),
      token("object", pattern(compile("\\w+(?=\\s*\\{)"))),
      token("punctuation", pattern(compile("[!(){}\\[\\]:=,]"))),
      token("property", pattern(compile("\\w+")))
    );
  }
}
