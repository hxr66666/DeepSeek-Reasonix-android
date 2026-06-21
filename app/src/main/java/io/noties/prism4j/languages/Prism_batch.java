package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.Prism4j;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.MULTILINE;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.grammar;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
public class Prism_batch {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("batch",
      token("comment",
        pattern(compile("^::.*", MULTILINE)),
        pattern(compile("((?:^|[&(])[ \\t]*)rem\\b(?:[^^&)\\r\\n]|\\^(?:\\r\\n|[\\s\\S]))*", CASE_INSENSITIVE | MULTILINE), true)
      ),
      token("label", pattern(compile("^:.*", MULTILINE), false, false, "property")),
      token("variable", pattern(compile("%%?[~:\\w]+%?|!\\S+!"))),
      token("keyword", pattern(compile("\\b(?:call|cd|chdir|cls|color|copy|date|del|dir|echo|endlocal|erase|exit|for|ftype|goto|if|md|mkdir|move|path|pause|popd|prompt|pushd|rd|ren|rename|rmdir|set|setlocal|shift|start|time|title|type|ver|verify|vol|xcopy)\\b", CASE_INSENSITIVE))),
      token("operator", pattern(compile("[&@]"))),
      token("number", pattern(compile("(?:\\b|-)-?\\d+\\b"))),
      token("string", pattern(compile("\"(?:[\\\\\"]\"|[^\"])*\"(?!\")"), false, true)),
      token("punctuation", pattern(compile("[()']")))
    );
  }
}
