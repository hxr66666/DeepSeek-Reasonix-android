package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.Prism4j;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.grammar;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
public class Prism_ada {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("ada",
      token("comment", pattern(compile("--.*/"))),
      token("string", pattern(compile("\"(?:\"\"|[^\"\\r\\f\\n])*\""), false, true)),
      token("number",
        pattern(compile("\\b\\d(?:_?\\d)*#[\\dA-F](?:_?[\\dA-F])*(?:\\.[\\dA-F](?:_?[\\dA-F])*)?#(?:E[+-]?\\d(?:_?\\d)*)?", CASE_INSENSITIVE)),
        pattern(compile("\\b\\d(?:_?\\d)*(?:\\.\\d(?:_?\\d)*)?(?:E[+-]?\\d(?:_?\\d)*)?\\b", CASE_INSENSITIVE))
      ),
      token("attribute", pattern(compile("\\b'\\w+"), false, false, "attr-name")),
      token("keyword", pattern(compile("\\b(?:abort|abs|abstract|accept|access|aliased|all|and|array|at|begin|body|case|constant|declare|delay|delta|digits|do|else|elsif|end|entry|exception|exit|for|function|generic|goto|if|in|interface|is|limited|loop|mod|new|not|null|of|or|others|out|overriding|package|pragma|private|procedure|protected|raise|range|record|rem|renames|requeue|return|reverse|select|separate|some|subtype|synchronized|tagged|task|terminate|then|type|until|use|when|while|with|xor)\\b", CASE_INSENSITIVE))),
      token("boolean", pattern(compile("\\b(?:false|true)\\b", CASE_INSENSITIVE))),
      token("operator", pattern(compile("<[=>]?|>=?|=>?|:=|\\/=?|\\*\\*?|[&+-]"))),
      token("punctuation", pattern(compile("\\.{1,2}|[,;():]"))),
      token("char", pattern(compile("'.'"))),
      token("variable", pattern(compile("\\b[a-z](?:\\w)*\\b", CASE_INSENSITIVE)))
    );
  }
}
