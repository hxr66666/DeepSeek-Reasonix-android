package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.GrammarUtils;
import io.noties.prism4j.Prism4j;
import io.noties.prism4j.annotations.Extend;

@SuppressWarnings("unused")
@Extend("javascript")
public class Prism_jsx {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    // JSX extends markup with JavaScript tokens
    // We use JavaScript as the base since markup + JS is complex to merge
    return GrammarUtils.extend(
      GrammarUtils.require(prism4j, "javascript"),
      "jsx"
    );
  }
}
