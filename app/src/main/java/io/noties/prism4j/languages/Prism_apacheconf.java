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
public class Prism_apacheconf {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("apacheconf",
      token("comment", pattern(compile("#.*"))),
      token("directive-block", pattern(compile("<\\/??\\b(?:Auth[nz]ProviderAlias|Directory|DirectoryMatch|Else|ElseIf|Files|FilesMatch|If|IfDefine|IfModule|IfVersion|Limit|LimitExcept|Location|LocationMatch|Macro|Proxy|Require(?:All|Any|None)|VirtualHost)\\b.*>", CASE_INSENSITIVE), false, false, "tag")),
      token("directive-flags", pattern(compile("\\[(?:[\\w=],?)+\\]"), false, false, "keyword")),
      token("string", pattern(compile("(\"|\').*\\1"))),
      token("variable", pattern(compile("[$%]\\{?(?:\\w\\.?[-+:]?)+\\}?"))),
      token("regex", pattern(compile("\\^?.*\\$|\\^.*\\$?")))
    );
  }
}
