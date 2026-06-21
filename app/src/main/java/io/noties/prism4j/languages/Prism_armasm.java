package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.Prism4j;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.grammar;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
public class Prism_armasm {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("armasm",
      token("comment", pattern(compile(";.*"), false, true)),
      token("string", pattern(compile("\"(?:[^\"\\r\\n]|\"\")*\""), false, true)),
      token("char", pattern(compile("'(?:[^'\\r\\n]{0,4}|'')' "), false, true)),
      token("version-symbol", pattern(compile("\\|[\\w@]+\\|"), false, true, "property")),
      token("boolean", pattern(compile("\\b(?:FALSE|TRUE)\\b"))),
      token("directive", pattern(compile("\\b(?:ALIAS|ALIGN|AREA|ARM|ASSERT|ATTR|CN|CODE|CODE16|CODE32|COMMON|CP|DATA|DCB|DCD|DCDO|DCDU|DCFD|DCFDU|DCI|DCQ|DCQU|DCW|DCWU|DN|ELIF|ELSE|END|ENDFUNC|ENDIF|ENDP|ENTRY|EQU|EXPORT|EXPORTAS|EXTERN|FIELD|FILL|FN|FUNCTION|GBLA|GBLL|GBLS|GET|GLOBAL|IF|IMPORT|INCBIN|INCLUDE|INFO|KEEP|LCLA|LCLL|LCLS|LTORG|MACRO|MAP|MEND|MEXIT|NOFP|OPT|PRESERVE8|PROC|QN|READONLY|RELOC|REQUIRE|REQUIRE8|RLIST|ROUT|SETA|SETL|SETS|SN|SPACE|SUBT|THUMB|THUMBX|TTL|WEND|WHILE)\\b"), false, false, "property")),
      token("instruction", pattern(compile("(?:(?:^|(?:^|[^\\\\])(?:\\r\\n?|\\n))[ \\t]*(?:(?:[A-Z][A-Z0-9_]*[a-z]\\w*|[a-z]\\w*|\\d+)[ \\t]+)?)\\b[A-Z.]+\\b"), true, false, "keyword")),
      token("variable", pattern(compile("\\$\\w+"))),
      token("number", pattern(compile("(?:\\b[2-9]_\\d+|(?:\\b\\d+(?:\\.\\d+)?|\\B\\.\\d+)(?:e-?\\d+)?|\\b0(?:[fd]_|x)[0-9a-f]+|&[0-9a-f]+)\\b", CASE_INSENSITIVE))),
      token("register", pattern(compile("\\b(?:r\\d|lr)\\b"), false, false, "symbol")),
      token("operator", pattern(compile("<>|<<|>>|&&|\\|\\||[=!<>/]=?|[+\\-*%#?&|^]|:[A-Z]+:"))),
      token("punctuation", pattern(compile("[()\\[\\],]")))
    );
  }
}
