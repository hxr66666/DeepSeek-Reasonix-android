package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.Prism4j;
import static java.util.regex.Pattern.MULTILINE;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.grammar;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
public class Prism_plant_uml {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("plant-uml",
      token("comment", pattern(compile("(^[ \\t]*)'.*|\\/['\"][\\s\\S]*?'[\\/ ]", MULTILINE), true, true)),
      token("preprocessor", pattern(compile("(^[ \\t]*)!.*", MULTILINE), true, true, "property")),
      token("delimiter", pattern(compile("(^[ \\t]*)@(?:end|start)uml\\b", MULTILINE), true, true, "punctuation")),
      token("arrow", pattern(compile("[|<>*+#o{\\[\\]}x^?]?(?:-+(?:[drlu]|do|down|le|left|ri|right|up)?-+|[.]+(?:[drlu]|do|down|le|left|ri|right|up)?[.]+)(?:[>|<}\\[\\]{*+#ox^?])?|[<>]\\|"), false, true, "operator")),
      token("string", pattern(compile("\"[^\"]*\""), false, true)),
      token("keyword", pattern(compile("\\b(?:abstract|actor|agent|allowmixing|archimate|artifact|boundary|box|break|caption|card|circle|class|cloud|component|concise|control|create|database|destroy|diamond|entity|enum|file|folder|frame|group|hide|interface|label|legend|loop|namespace|network|newpage|node|note|object|opt|package|page|participant|partition|queue|rectangle|ref|remove|restore|return|robust|scale|sequence|show|skinparam|stack|start|state|storage|title|together|usecase)\\b"))),
      token("variable", pattern(compile("\\$\\w+|%[a-z]+%"))),
      token("number", pattern(compile("\\b\\d+(?:\\.\\d+)?\\b"))),
      token("operator", pattern(compile("[=:]|<\\|?|\\|?>|--|\\.\\.|\\.\\*|\\*\\.|<<|>>|--|~~"))),
      token("punctuation", pattern(compile("[{}\\[\\](),;]")))
    );
  }
}
