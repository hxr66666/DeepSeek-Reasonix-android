package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.GrammarUtils;
import io.noties.prism4j.Prism4j;
import io.noties.prism4j.annotations.Extend;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.MULTILINE;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
@Extend("clike")
public class Prism_fsharp {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    final Prism4j.Grammar fsharp = GrammarUtils.extend(
      GrammarUtils.require(prism4j, "clike"),
      "fsharp",
      token("comment",
        pattern(compile("(^|[^\\\\])\\(\\*(?!\\))[\\s\\S]*?\\*\\)"), true, true),
        pattern(compile("(^|[^\\\\:])\\/{2}.*"), true, true)
      ),
      token("string", pattern(compile("(?:\"\"\"[\\s\\S]*?\"\"\"|@\"(?:\"\"|[^\"])*\"|\"(?:\\\\[\\s\\S]|[^\\\\\"])*\")B?"), false, true)),
      token("class-name", pattern(compile("(\\b(?:exception|inherit|interface|new|of|type)\\s+|\\w\\s*:\\s*|\\s:\\???>\\s*)[.\\w]+\\b(?:\\s*(?:->|\\*)\\s*[.\\w]+\\b)*(?!\\s*[:.])", CASE_INSENSITIVE), true)),
      token("keyword", pattern(compile("\\b(?:let|return|use|yield)(?:!\\B|\\b)|\\b(?:abstract|and|as|asr|assert|atomic|base|begin|break|checked|class|component|const|constraint|constructor|continue|default|delegate|do|done|downcast|downto|eager|elif|else|end|event|exception|extern|external|false|finally|fixed|for|fun|function|functor|global|if|in|include|inherit|inline|interface|internal|land|lazy|lor|lsl|lsr|lxor|match|member|method|mixin|mod|module|mutable|namespace|new|not|null|object|of|open|or|override|parallel|private|process|protected|public|pure|rec|sealed|select|sig|static|struct|tailcall|then|to|trait|true|try|type|upcast|val|virtual|void|volatile|when|while|with)\\b"))),
      token("number",
        pattern(compile("\\b0x[\\da-fA-F]+(?:LF|lf|un)?\\b")),
        pattern(compile("\\b0b[01]+(?:uy|y)?\\b")),
        pattern(compile("(?:\\b\\d+(?:\\.\\d*)?|\\B\\.\\d+)(?:[fm]|e[+-]?\\d+)?\\b", CASE_INSENSITIVE)),
        pattern(compile("\\b\\d+(?:[IlLsy]|UL|u[lsy]?)?\\b"))
      ),
      token("operator", pattern(compile("([<>~&^])\\1\\1|([*.:<>&])\\2|<-|->|[!=:]=|<\\?|\\{1,3}>?|\\?\\?(?:<=|>=|<>|[-+*\\/%=<>])\\?\\?|[!?^&]|~[+~-]|:>|:\\?>?")))
    );
    GrammarUtils.insertBeforeToken(fsharp, "keyword",
      token("preprocessor", pattern(compile("(^[\\t ]*)#.*", MULTILINE), true, false, "property"))
    );
    GrammarUtils.insertBeforeToken(fsharp, "punctuation",
      token("computation-expression", pattern(compile("\\b[_a-z]\\w*(?=\\s*\\{)", CASE_INSENSITIVE), false, false, "keyword"))
    );
    GrammarUtils.insertBeforeToken(fsharp, "string",
      token("annotation", pattern(compile("\\[<.+?>\\]"), false, true)),
      token("char", pattern(compile("'(?:[^\\\\']|\\\\(?:.|\\d{3}|x[a-fA-F\\d]{2}|u[a-fA-F\\d]{4}|U[a-fA-F\\d]{8}))'B?"), false, true))
    );
    return fsharp;
  }
}
