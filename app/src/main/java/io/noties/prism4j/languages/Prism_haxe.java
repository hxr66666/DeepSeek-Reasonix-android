package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.GrammarUtils;
import io.noties.prism4j.Prism4j;
import io.noties.prism4j.annotations.Extend;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
@Extend("clike")
public class Prism_haxe {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    final Prism4j.Grammar haxe = GrammarUtils.extend(
      GrammarUtils.require(prism4j, "clike"),
      "haxe",
      token("string", pattern(compile("\"(?:[^\"\\\\]|\\\\[\\s\\S])*\""), false, true)),
      token("class-name",
        pattern(compile("(\\b(?:abstract|class|enum|extends|implements|interface|new|typedef)\\s+)[A-Z_]\\w*"), true),
        pattern(compile("\\b[A-Z]\\w*\\b"))
      ),
      token("keyword", pattern(compile("\\bthis\\b|\\b(?:abstract|as|break|case|cast|catch|class|continue|default|do|dynamic|else|enum|extends|extern|final|for|from|function|if|implements|import|in|inline|interface|macro|new|null|operator|overload|override|package|private|public|return|static|super|switch|throw|to|try|typedef|untyped|using|var|while)(?!\\.)\\b"))),
      token("function", pattern(compile("\\b[a-z_]\\w*(?=\\s*(?:<[^<>]*>\\s*)?\\()", CASE_INSENSITIVE), false, true)),
      token("operator", pattern(compile("\\.{3}|\\+\\+|--|&&|\\|\\||->|=>|(?:<<?|>{1,3}|[-+*\\/%!=&|^])=?|[?:~]")))
    );
    GrammarUtils.insertBeforeToken(haxe, "class-name",
      token("regex", pattern(compile("~\\/(?:[^\\/\\\\\\r\\n]|\\\\.)+\\/[a-z]*"), false, true))
    );
    GrammarUtils.insertBeforeToken(haxe, "keyword",
      token("preprocessor", pattern(compile("#(?:else|elseif|end|if)\\b.*"), false, false, "property")),
      token("metadata", pattern(compile("@:?[\\w.]+"), false, false, "symbol")),
      token("reification", pattern(compile("\\$(?:\\w+|(?=\\{))"), false, false, "important"))
    );
    return haxe;
  }
}
