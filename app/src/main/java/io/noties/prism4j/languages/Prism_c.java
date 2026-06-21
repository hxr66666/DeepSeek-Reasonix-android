package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;

import io.noties.prism4j.GrammarUtils;
import io.noties.prism4j.Prism4j;
import io.noties.prism4j.annotations.Extend;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.MULTILINE;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.grammar;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
@Extend("clike")
public class Prism_c {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {

    // Reusable patterns shared between top-level grammar and macro inside grammar
    final Prism4j.Pattern commentPattern = pattern(
      compile("//(?:[^\\r\\n\\\\]|\\\\(?:\\r\\n?|\\n|(?![\\r\\n])))*|/\\*[\\s\\S]*?(?:\\*/|$)"),
      false, true
    );
    final Prism4j.Pattern stringPattern = pattern(
      compile("\"(?:\\\\(?:\\r\\n|[\\s\\S])|[^\"\\\\\\r\\n])*\""),
      false, true
    );
    final Prism4j.Pattern charPattern = pattern(
      compile("'(?:\\\\(?:\\r\\n|[\\s\\S])|[^'\\\\\\r\\n]){0,32}'"),
      false, true
    );

    final Prism4j.Grammar c = GrammarUtils.extend(
      GrammarUtils.require(prism4j, "clike"),
      "c",
      token -> !"boolean".equals(token.name()),
      token("comment", commentPattern),
      token("string", stringPattern),
      token("class-name", pattern(
        compile("(\\b(?:enum|struct)\\s+(?:__attribute__\\s*\\(\\([\\s\\S]*?\\)\\)\\s*)?)\\w+|\\b[a-z]\\w*_t\\b"),
        true
      )),
      token("keyword", pattern(compile("\\b(?:_Alignas|_Alignof|_Atomic|_Bool|_Complex|_Generic|_Imaginary|_Noreturn|_Static_assert|_Thread_local|__attribute__|asm|auto|break|case|char|const|continue|default|do|double|else|enum|extern|float|for|goto|if|inline|int|long|register|return|short|signed|sizeof|static|struct|switch|typedef|typeof|union|unsigned|void|volatile|while)\\b"))),
      token("function", pattern(compile("\\b[a-z_]\\w*(?=\\s*\\()", CASE_INSENSITIVE))),
      token("number", pattern(compile("(?:\\b0x(?:[\\da-f]+(?:\\.[\\da-f]*)?|\\.[\\da-f]+)(?:p[+-]?\\d+)?|(?:\\b\\d+(?:\\.\\d*)?|\\B\\.\\d+)(?:e[+-]?\\d+)?)[ful]{0,4}", CASE_INSENSITIVE))),
      token("operator", pattern(compile(">>=?|<<=?|->|([-+&|:])\\1|[?:~]|[-+*\\/%&|^!=<>]=?")))
    );

    GrammarUtils.insertBeforeToken(c, "string",
      token("char", charPattern),
      token("macro", pattern(
        compile("(^[\\t ]*)#\\s*[a-z](?:[^\\r\\n\\\\/]|\\/(?!\\*)|\\/\\*(?:[^*]|\\*(?!\\/))*\\*\\/|\\\\(?:\\r\\n|[\\s\\S]))*", CASE_INSENSITIVE | MULTILINE),
        true,
        true,
        "property",
        grammar("inside",
          token("string",
            pattern(compile("^(#\\s*include\\s*)<[^>]+>"), true),
            stringPattern
          ),
          token("char", charPattern),
          token("comment", commentPattern),
          token("macro-name",
            pattern(compile("(^#\\s*define\\s+)\\w+\\b(?!\\()", CASE_INSENSITIVE), true),
            pattern(compile("(^#\\s*define\\s+)\\w+\\b(?=\\()", CASE_INSENSITIVE), true, false, "function")
          ),
          token("directive", pattern(
            compile("(#\\s*)[a-z]+"),
            true,
            false,
            "keyword"
          )),
          token("directive-hash", pattern(compile("^#"))),
          token("punctuation", pattern(compile("##|\\\\(?=[\\r\\n])")))
        )
      ))
    );

    GrammarUtils.insertBeforeToken(c, "function",
      token("constant", pattern(compile("\\b(?:EOF|NULL|SEEK_CUR|SEEK_END|SEEK_SET|__DATE__|__FILE__|__LINE__|__TIMESTAMP__|__TIME__|__func__|stderr|stdin|stdout)\\b")))
    );

    return c;
  }
}
