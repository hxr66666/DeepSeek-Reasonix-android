package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.Prism4j;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.grammar;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
public class Prism_r {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("r",
      token("comment", pattern(compile("#.*"))),
      token("string", pattern(compile("(['\"])(?:\\\\.|(?!\\1)[^\\\\\\r\\n])*\\1"), false, true)),
      token("percent-operator", pattern(compile("%[^%\\s]*%"), false, false, "operator")),
      token("boolean", pattern(compile("\\b(?:FALSE|TRUE)\\b"))),
      token("ellipsis", pattern(compile("\\.\\.\\.|\\.\\d+"))),
      token("number",
        pattern(compile("\\b(?:Inf|NaN)\\b")),
        pattern(compile("(?:\\b0x[\\dA-Fa-f]+(?:\\.\\d*)?|\\b\\d+(?:\\.\\d*)?|\\B\\.\\d+)(?:[EePp][+-]?\\d+)?[iL]?"))
      ),
      token("keyword", pattern(compile("\\b(?:NA|NA_character_|NA_complex_|NA_integer_|NA_real_|NULL|break|else|for|function|if|in|next|repeat|while)\\b"))),
      token("operator", pattern(compile("->?>?|<(?:=|<?-)?|[>=!]=?|::?|&&?|\\|\\|?|[+*\\/^$@~]"))),
      token("punctuation", pattern(compile("[(){}\\[\\],;]")))
    );
  }
}
