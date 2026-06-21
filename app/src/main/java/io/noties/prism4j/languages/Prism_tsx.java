package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.GrammarUtils;
import io.noties.prism4j.Prism4j;
import io.noties.prism4j.annotations.Extend;

@SuppressWarnings("unused")
@Extend("typescript")
public class Prism_tsx {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    // TSX is essentially TypeScript with JSX-like syntax support
    // For the purposes of this implementation, we delegate to TypeScript
    return GrammarUtils.extend(
      GrammarUtils.require(prism4j, "typescript"),
      "tsx"
    );
  }
}
