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
public class Prism_abnf {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("abnf",
      token("comment", pattern(compile(";.*"))),
      token("string", pattern(compile("(?:%[is])?\"[^\"\\n\\r]*\""), false, true)),
      token("range", pattern(compile("%(?:b[01]+-[01]+|d\\d+-\\d+|x[A-F\\d]+-[A-F\\d]+)", CASE_INSENSITIVE), false, false, "number")),
      token("terminal", pattern(compile("%(?:b[01]+(?:\\.[01]+)*|d\\d+(?:\\.\\d+)*|x[A-F\\d]+(?:\\.[A-F\\d]+)*)", CASE_INSENSITIVE), false, false, "number")),
      token("repetition", pattern(compile("(^|[^\\w-])(?:\\d*\\*\\d*|\\d+)"), true, false, "operator")),
      token("definition", pattern(compile("(^[ \\t]*)(?:[a-z][\\w-]*|<[^<>\\r\\n]*>)(?=\\s*=)", MULTILINE), true, false, "keyword")),
      token("core-rule", pattern(compile("(?:(^|[^<\\w-])(?:ALPHA|BIT|CHAR|CR|CRLF|CTL|DIGIT|DQUOTE|HEXDIG|HTAB|LF|LWSP|OCTET|SP|VCHAR|WSP)|<(?:ALPHA|BIT|CHAR|CR|CRLF|CTL|DIGIT|DQUOTE|HEXDIG|HTAB|LF|LWSP|OCTET|SP|VCHAR|WSP)>)(?![\\w-])", CASE_INSENSITIVE), true, false, "constant")),
      token("rule", pattern(compile("(^|[^<\\w-])[a-z][\\w-]*|<[^<>\\r\\n]*>", CASE_INSENSITIVE), true)),
      token("operator", pattern(compile("=\\/|\\/")))  ,
      token("punctuation", pattern(compile("[(\\)\\[\\]]")))
    );
  }
}
