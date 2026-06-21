package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.Prism4j;
import static java.util.regex.Pattern.MULTILINE;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.grammar;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
public class Prism_diff {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("diff",
      token("coord",
        pattern(compile("^(?:\\*{3}|-{3}|\\+{3}).*$", MULTILINE)),
        pattern(compile("^@@.*@@$", MULTILINE)),
        pattern(compile("^\\d.*$", MULTILINE))
      ),
      token("deleted-sign", pattern(compile("^(?:[-].*(?:\\r\\n?|\\n|(?![\\s\\S])))+", MULTILINE), false, false, "deleted")),
      token("deleted-arrow", pattern(compile("^(?:[<].*(?:\\r\\n?|\\n|(?![\\s\\S])))+", MULTILINE), false, false, "deleted")),
      token("inserted-sign", pattern(compile("^(?:[+].*(?:\\r\\n?|\\n|(?![\\s\\S])))+", MULTILINE), false, false, "inserted")),
      token("inserted-arrow", pattern(compile("^(?:[>].*(?:\\r\\n?|\\n|(?![\\s\\S])))+", MULTILINE), false, false, "inserted")),
      token("unchanged", pattern(compile("^(?:[ ].*(?:\\r\\n?|\\n|(?![\\s\\S])))+", MULTILINE))),
      token("diff", pattern(compile("^(?:[!].*(?:\\r\\n?|\\n|(?![\\s\\S])))+", MULTILINE), false, false, "bold"))
    );
  }
}
