package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.Prism4j;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.grammar;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
public class Prism_elixir {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("elixir",
      token("doc", pattern(compile("@(?:doc|moduledoc)\\s+(?:(\"\"\"|''')[\\s\\S]*?\\1|(\"|\')(?:\\\\(?:\\r\\n|[\\s\\S])|(?!\\2)[^\\\\\\r\\n])*\\2)"))),
      token("comment", pattern(compile("#.*"), false, true)),
      token("regex", pattern(compile("~[rR](?:(\"\"\"|''')(?:\\\\[\\s\\S]|(?!\\1)[^\\\\])+\\1|([/|\"'])(?:\\\\.|(?!\\2)[^\\\\\\r\\n])+\\2|\\((?:\\\\.|[^\\\\)\\r\\n])+\\)|\\[(?:\\\\.|[^\\\\\\]\\r\\n])+\\]|\\{(?:\\\\.|[^\\\\}\\r\\n])+\\}|<(?:\\\\.|[^\\\\>\\r\\n])+>)[uismxfr]*"), false, true)),
      token("string",
        pattern(compile("~[cCsSwW](?:(\"\"\"|''')(?:\\\\[\\s\\S]|(?!\\1)[^\\\\])+\\1|([/|\"'])(?:\\\\.|(?!\\2)[^\\\\\\r\\n])+\\2|\\((?:\\\\.|[^\\\\)\\r\\n])+\\)|\\[(?:\\\\.|[^\\\\\\]\\r\\n])+\\]|\\{(?:\\\\.|#\\{[^}]+\\}|#(?!\\{)|[^#\\\\}\\r\\n])+\\}|<(?:\\\\.|[^\\\\>\\r\\n])+>)[csa]?"), false, true),
        pattern(compile("(\"\"\"|''')[\\s\\S]*?\\1"), false, true),
        pattern(compile("(\"|')(?:\\\\(?:\\r\\n|[\\s\\S])|(?!\\1)[^\\\\\\r\\n])*\\1"), false, true)
      ),
      token("atom", pattern(compile("(^|[^:]):\\w+"), true, false, "symbol")),
      token("module", pattern(compile("\\b[A-Z]\\w*\\b"), false, false, "class-name")),
      token("attr-name", pattern(compile("\\b\\w+\\??:(?!:)"))),
      token("argument", pattern(compile("(^|[^&])&\\d+"), true, false, "variable")),
      token("attribute", pattern(compile("@\\w+"), false, false, "variable")),
      token("function", pattern(compile("\\b[_a-zA-Z]\\w*[?!]?(?:(?=\\s*(?:\\.\\s*)?\\()|(?=\\/\\d))"))),
      token("number", pattern(compile("\\b(?:0[box][a-f\\d_]+|\\d[\\d_]*)(?:\\.[\\d_]+)?(?:e[+-]?[\\d_]+)?\\b", CASE_INSENSITIVE))),
      token("keyword", pattern(compile("\\b(?:after|alias|and|case|catch|cond|def(?:callback|delegate|exception|impl|macro|module|n|np|p|protocol|struct)?|do|else|end|fn|for|if|import|not|or|quote|raise|require|rescue|try|unless|unquote|use|when)\\b"))),
      token("boolean", pattern(compile("\\b(?:false|nil|true)\\b"))),
      token("operator",
        pattern(compile("\\bin\\b|&&?|\\|[|>]?|\\\\\\\\|::|\\.\\.\\.|\\+\\+?|-[->]?|<[-=>]|>=|!==?|\\B!|=(?:==?|[>~])?|[*\\/^]")),
        pattern(compile("([^<])<(?!<)"), true),
        pattern(compile("([^>])>(?!>)"), true)
      ),
      token("punctuation", pattern(compile("<<|>>|[.,%\\[\\]{}()]")))
    );
  }
}
