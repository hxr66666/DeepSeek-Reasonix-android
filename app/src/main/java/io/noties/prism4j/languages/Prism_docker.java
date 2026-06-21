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
public class Prism_docker {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("docker",
      token("instruction", pattern(
        compile("(^[ \\t]*)(?:ADD|ARG|CMD|COPY|ENTRYPOINT|ENV|EXPOSE|FROM|HEALTHCHECK|LABEL|MAINTAINER|ONBUILD|RUN|SHELL|STOPSIGNAL|USER|VOLUME|WORKDIR)(?=\\s)(?:\\\\.|[^\\r\\n\\\\])*(?:\\\\$(?:\\s|#.*$)*(?![\\s#])(?:\\\\.|[^\\r\\n\\\\])*)*", CASE_INSENSITIVE | MULTILINE),
        true, true
      )),
      token("comment", pattern(compile("(^[ \\t]*)#.*", MULTILINE), true, true))
    );
  }
}
