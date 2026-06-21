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
@Extend("markup")
public class Prism_aspnet {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    final Prism4j.Grammar aspnet = GrammarUtils.extend(
      GrammarUtils.require(prism4j, "markup"),
      "aspnet",
      token("page-directive", pattern(compile("<%\\s*@.*%>"), false, false, "tag")),
      token("directive", pattern(compile("<%.*%>"), false, false, "tag"))
    );
    GrammarUtils.insertBeforeToken(aspnet, "comment",
      token("asp-comment", pattern(compile("<%--[\\s\\S]*?--%>"), false, false, "comment"))
    );
    return aspnet;
  }
}
