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
public class Prism_protobuf {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    final Prism4j.Grammar proto = GrammarUtils.extend(
      GrammarUtils.require(prism4j, "clike"),
      "protobuf",
      token("class-name",
        pattern(compile("(\\b(?:enum|extend|message|service)\\s+)[A-Za-z_]\\w*(?=\\s*\\{)"), true),
        pattern(compile("(\\b(?:rpc\\s+\\w+|returns)\\s*\\(\\s*(?:stream\\s+)?)\\.[A-Za-z_]\\w*(?:\\.[A-Za-z_]\\w*)*(?=\\s*\\))"), true)
      ),
      token("keyword", pattern(compile("\\b(?:enum|extend|extensions|import|message|oneof|option|optional|package|public|repeated|required|reserved|returns|rpc(?=\\s+\\w)|service|stream|syntax|to)\\b(?!\\s*=\\s*\\d)"))),
      token("function", pattern(compile("\\b[a-z_]\\w*(?=\\s*\\()", CASE_INSENSITIVE)))
    );
    GrammarUtils.insertBeforeToken(proto, "operator",
      token("builtin", pattern(compile("\\b(?:bool|bytes|double|s?fixed(?:32|64)|float|[su]?int(?:32|64)|string)\\b")))
    );
    return proto;
  }
}
