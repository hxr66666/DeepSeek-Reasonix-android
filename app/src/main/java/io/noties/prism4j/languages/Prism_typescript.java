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
public class Prism_typescript {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    final Prism4j.Grammar ts = GrammarUtils.extend(
      GrammarUtils.require(prism4j, "javascript"),
      "typescript",
      token("builtin", pattern(compile("\\b(?:Array|Function|Promise|any|boolean|console|never|number|string|symbol|unknown)\\b"))),
      token("keyword", pattern(compile("\\b(?:abstract|as|asserts|async|await|break|case|catch|class|const|constructor|continue|debugger|declare|default|delete|do|else|enum|export|extends|finally|for|from|function|get|if|implements|import|in|instanceof|interface|infer|is|keyof|let|module|namespace|new|of|package|private|protected|public|readonly|require|return|set|static|super|switch|this|throw|try|type|typeof|var|void|while|with|yield)\\b")))
    );
    GrammarUtils.insertBeforeToken(ts, "function",
      token("decorator", pattern(compile("@[\\$\\w\\xA0-\\uFFFF]+")))
    );
    return ts;
  }
}
