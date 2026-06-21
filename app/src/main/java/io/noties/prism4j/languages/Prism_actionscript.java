package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.GrammarUtils;
import io.noties.prism4j.Prism4j;
import io.noties.prism4j.annotations.Extend;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
@Extend("javascript")
public class Prism_actionscript {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return GrammarUtils.extend(
      GrammarUtils.require(prism4j, "javascript"),
      "actionscript",
      token("keyword", pattern(compile("\\b(?:as|break|case|catch|class|const|default|delete|do|dynamic|each|else|extends|final|finally|for|function|get|if|implements|import|in|include|instanceof|interface|internal|is|namespace|native|new|null|override|package|private|protected|public|return|set|static|super|switch|this|throw|try|typeof|use|var|void|while|with)\\b"))),
      token("operator", pattern(compile("\\+\\+|--|(?:[+\\-*\\/%^]|&&?|\\|\\|?|<<?|>>?>?|[!=]=?)=?|[~?@]")))
    );
  }
}
