package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.Prism4j;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.grammar;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
public class Prism_rust {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("rust",
      token("comment",
        pattern(compile("(^|[^\\\\])\\/\\*(?:[^*/]|\\*(?!\\/)|/(?!\\*)|(?:\\/\\*(?:[^*/]|\\*(?!\\/)|/(?!\\*))*\\*\\/))*\\*\\/"), true, true),
        pattern(compile("(^|[^\\\\:])\\/{2}.*"), true, true)
      ),
      token("string", pattern(compile("b?\"(?:\\\\[\\s\\S]|[^\\\\\"])*\"|b?r(#*)\"(?:[^\"]|\"+(?!\\1))*\"\\1"), false, true)),
      token("char", pattern(compile("b?'(?:\\\\(?:x[0-7][\\da-fA-F]|u\\{(?:[\\da-fA-F]_*){1,6}\\}|.)|[^\\\\\\r\\n\\t'])'"), false, true)),
      token("attribute", pattern(compile("#!?\\[(?:[^\\[\\]\"]|\"(?:\\\\[\\s\\S]|[^\\\\\"])*\")*\\]"), false, true, "attr-name")),
      token("lifetime-annotation", pattern(compile("'\\w+"), false, false, "symbol")),
      token("fragment-specifier", pattern(compile("(\\$\\w+:)[a-z]+"), true, false, "punctuation")),
      token("variable", pattern(compile("\\$\\w+"))),
      token("function-definition", pattern(compile("(\\bfn\\s+)\\w+"), true, false, "function")),
      token("type-definition", pattern(compile("(\\b(?:enum|struct|trait|type|union)\\s+)\\w+"), true, false, "class-name")),
      token("module-declaration",
        pattern(compile("(\\b(?:crate|mod)\\s+)[a-z][a-z_\\d]*"), true, false, "namespace"),
        pattern(compile("(\\b(?:crate|self|super)\\s*)::(?:\\s*[a-z][a-z_\\d]*\\s*::)*"), true, false, "namespace")
      ),
      token("keyword",
        pattern(compile("\\b(?:Self|abstract|as|async|await|become|box|break|const|continue|crate|do|dyn|else|enum|extern|final|fn|for|if|impl|in|let|loop|macro|match|mod|move|mut|override|priv|pub|ref|return|self|static|struct|super|trait|try|type|typeof|union|unsafe|unsized|use|virtual|where|while|yield)\\b")),
        pattern(compile("\\b(?:bool|char|f(?:32|64)|[ui](?:8|16|32|64|128|size)|str)\\b"))
      ),
      token("function", pattern(compile("\\b[a-z_]\\w*(?=\\s*(?:::\\s*<|\\())"))),
      token("macro", pattern(compile("\\b\\w+!"), false, false, "property")),
      token("constant", pattern(compile("\\b[A-Z_][A-Z_\\d]+\\b"))),
      token("class-name", pattern(compile("\\b[A-Z]\\w*\\b"))),
      token("number", pattern(compile("\\b(?:0x[\\dA-Fa-f](?:_?[\\dA-Fa-f])*|0o[0-7](?:_?[0-7])*|0b[01](?:_?[01])*|(?:(?:\\d(?:_?\\d)*)?\\.)?\\d(?:_?\\d)*(?:[Ee][+-]?\\d+)?)(?:_?(?:f32|f64|[iu](?:8|16|32|64|size)?))?\\b"))),
      token("boolean", pattern(compile("\\b(?:false|true)\\b"))),
      token("punctuation", pattern(compile("->|\\.\\.|::|[{}\\[\\];(),:.]"))),
      token("operator", pattern(compile("[-+*\\/%!^]=?|=[=>]?|&[&=]?|\\|[|=]?|<<?=?|>>?=?|[@?]")))
    );
  }
}
