package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.GrammarUtils;
import io.noties.prism4j.Prism4j;
import io.noties.prism4j.annotations.Extend;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.MULTILINE;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
@Extend("clike")
public class Prism_ruby {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    final Prism4j.Grammar ruby = GrammarUtils.extend(
      GrammarUtils.require(prism4j, "clike"),
      "ruby",
      token("comment", pattern(compile("#.*|^=begin\\s[\\s\\S]*?^=end", MULTILINE), false, true)),
      token("class-name", pattern(compile("(\\b(?:class|module)\\s+|\\bcatch\\s+\\()[\\w.\\\\]+|\\b[A-Z_]\\w*(?=\\s*\\.\\s*new\\b)"), true)),
      token("keyword", pattern(compile("\\b(?:BEGIN|END|alias|and|begin|break|case|class|def|define_method|defined|do|each|else|elsif|end|ensure|extend|for|if|in|include|module|new|next|nil|not|or|prepend|private|protected|public|raise|redo|require|rescue|retry|return|self|super|then|throw|undef|unless|until|when|while|yield)\\b"))),
      token("operator", pattern(compile("\\.{2,3}|&\\.|===|<?=>|[!=]?~|(?:&&|\\|\\||<<|>>|\\*\\*|[+\\-*/%<>!^&|=])=?|[?:]"))),
      token("punctuation", pattern(compile("[(){}\\[\\].,;]")))
    );
    GrammarUtils.insertBeforeToken(ruby, "keyword",
      token("variable", pattern(compile("[@$]+[a-zA-Z_]\\w*(?:[?!]|\\b)"))),
      token("symbol",
        pattern(compile("(^|[^:]):[a-zA-Z_]\\w*(?:[?!]|\\b)"), true, true),
        pattern(compile("(^|[^:]):\"(?:\\\\.|[^\\\\\"\\r\\n])*\""), true, true)
      ),
      token("method-definition", pattern(compile("(\\bdef\\s+)\\w+(?:\\s*\\.\\s*\\w+)?"), true))
    );
    GrammarUtils.insertBeforeToken(ruby, "number",
      token("builtin", pattern(compile("\\b(?:Array|Bignum|Binding|Class|Continuation|Dir|Exception|FalseClass|File|Fixnum|Float|Hash|IO|Integer|MatchData|Method|Module|NilClass|Numeric|Object|Proc|Range|Regexp|Stat|String|Struct|Symbol|TMS|Thread|ThreadGroup|Time|TrueClass)\\b"))),
      token("constant", pattern(compile("\\b[A-Z][A-Z0-9_]*(?:[?!]|\\b)")))
    );
    return ruby;
  }
}
