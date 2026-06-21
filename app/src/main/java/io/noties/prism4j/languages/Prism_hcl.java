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
public class Prism_hcl {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("hcl",
      token("comment", pattern(compile("(?:\\/\\/|#).*|\\/\\*[\\s\\S]*?(?:\\*\\/|$)"))),
      token("heredoc", pattern(compile("<<-?(\\w+\\b)[\\s\\S]*?^[ \\t]*\\1", MULTILINE), false, true, "string")),
      token("keyword",
        pattern(compile("(?:data|resource)\\s+(?:\"(?:\\\\[\\s\\S]|[^\\\\\"])*\")(?=\\s+\"[\\w-]+\"\\s+\\{)", CASE_INSENSITIVE)),
        pattern(compile("(?:backend|module|output|provider|provisioner|variable)\\s+(?:[\\w-]+|\"(?:\\\\[\\s\\S]|[^\\\\\"])*\")\\s+(?=\\{)", CASE_INSENSITIVE)),
        pattern(compile("[\\w-]+(?=\\s+\\{)"))
      ),
      token("property",
        pattern(compile("[-\\w\\.]+(?=\\s*=(?!=))")),
        pattern(compile("\"(?:\\\\[\\s\\S]|[^\\\\\"])+ \"(?=\\s*[:=])"))
      ),
      token("string", pattern(compile("\"(?:[^\\\\$\"]|\\\\[\\s\\S]|\\$(?:(?=\")|\\$+(?!\\$)|[^\"${])|\\$\\{(?:[^{}\"']|\"(?:[^\\\\\"]|\\\\[\\s\\S])*\")*\\})*\""), false, true)),
      token("number", pattern(compile("\\b0x[\\da-f]+\\b|\\b\\d+(?:\\.\\d*)?(?:e[+-]?\\d+)?", CASE_INSENSITIVE))),
      token("boolean", pattern(compile("\\b(?:false|true)\\b", CASE_INSENSITIVE))),
      token("punctuation", pattern(compile("[=\\[\\]{}]")))
    );
  }
}
