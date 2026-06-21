package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.GrammarUtils;
import io.noties.prism4j.Prism4j;
import io.noties.prism4j.annotations.Extend;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
@Extend("clike")
public class Prism_solidity {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    final Prism4j.Grammar sol = GrammarUtils.extend(
      GrammarUtils.require(prism4j, "clike"),
      "solidity",
      token("class-name", pattern(compile("(\\b(?:contract|enum|interface|library|new|struct|using)\\s+)(?!\\d)[\\w$]+"), true)),
      token("keyword", pattern(compile("\\b(?:_|anonymous|as|assembly|assert|break|calldata|case|constant|constructor|continue|contract|default|delete|do|else|emit|enum|event|external|for|from|function|if|import|indexed|inherited|interface|internal|is|let|library|mapping|memory|modifier|new|payable|pragma|private|public|pure|require|returns?|revert|selfdestruct|solidity|storage|struct|suicide|switch|this|throw|using|var|view|while)\\b"))),
      token("operator", pattern(compile("=>|->|:=|=:|\\*\\*|\\+\\+|--|\\|\\||&&|<<=?|>>=?|[-+*\\/%^&|<>!=]=?|[~?]")))
    );
    GrammarUtils.insertBeforeToken(sol, "keyword",
      token("builtin", pattern(compile("\\b(?:address|bool|byte|u?int(?:8|16|24|32|40|48|56|64|72|80|88|96|104|112|120|128|136|144|152|160|168|176|184|192|200|208|216|224|232|240|248|256)?|string|bytes(?:[1-9]|[12]\\d|3[0-2])?)\\b")))
    );
    GrammarUtils.insertBeforeToken(sol, "number",
      token("version", pattern(compile("([<>]=?|\\^)\\d+\\.\\d+\\.\\d+\\b"), true, false, "number"))
    );
    return sol;
  }
}
