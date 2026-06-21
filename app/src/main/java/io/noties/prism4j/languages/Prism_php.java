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
public class Prism_php {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("php",
      token("delimiter", pattern(compile("\\?>$|^<\\?(?:php(?=\\s)|=)?", CASE_INSENSITIVE | MULTILINE), false, false, "important")),
      token("comment", pattern(compile("\\/\\*[\\s\\S]*?\\*\\/|\\/\\/.*|#(?!\\[).*"))),
      token("variable", pattern(compile("\\$+(?:\\w+\\b|(?=\\{))"))),
      token("package", pattern(compile("(namespace\\s+|use\\s+(?:function\\s+)?)(?:\\\\?\\b[a-z_]\\w*)+\\b(?!\\\\)", CASE_INSENSITIVE), true)),
      token("class-name-definition", pattern(compile("(\\b(?:class|enum|interface|trait)\\s+)\\b[a-z_]\\w*(?!\\\\)\\b", CASE_INSENSITIVE), true, false, "class-name")),
      token("function-definition", pattern(compile("(\\bfunction\\s+)[a-z_]\\w*(?=\\s*\\()", CASE_INSENSITIVE), true, false, "function")),
      token("keyword",
        pattern(compile("(\\(\\s*)\\b(?:array|bool|boolean|float|int|integer|object|string)\\b(?=\\s*\\))", CASE_INSENSITIVE), true, true, "type-casting"),
        pattern(compile("([(,?]\\s*)\\b(?:array(?!\\s*\\()|bool|callable|(?:false|null)(?=\\s*\\|)|float|int|iterable|mixed|object|self|static|string)\\b(?=\\s*\\$)", CASE_INSENSITIVE), true, true, "type-hint"),
        pattern(compile("(\\)\\s*:\\s*(?:\\?\\s*)?)\\b(?:array(?!\\s*\\()|bool|callable|(?:false|null)(?=\\s*\\|)|float|int|iterable|mixed|never|object|self|static|string|void)\\b", CASE_INSENSITIVE), true, true, "return-type"),
        pattern(compile("\\b(?:array(?!\\s*\\()|bool|float|int|iterable|mixed|object|string|void)\\b", CASE_INSENSITIVE), false, true, "type-declaration"),
        pattern(compile("(\\|\\s*)(?:false|null)\\b|\\b(?:false|null)(?=\\s*\\|)", CASE_INSENSITIVE), true, true, "type-declaration"),
        pattern(compile("\\b(?:parent|self|static)(?=\\s*::)", CASE_INSENSITIVE), false, true, "static-context"),
        pattern(compile("(\\byield\\s+)from\\b", CASE_INSENSITIVE), true),
        pattern(compile("\\bclass\\b", CASE_INSENSITIVE)),
        pattern(compile("((?:^|[^\\s>:]|(?:^|[^-])>|(?:^|[^:]):)\\s*)\\b(?:abstract|and|array|as|break|callable|case|catch|clone|const|continue|declare|default|die|do|echo|else|elseif|empty|enddeclare|endfor|endforeach|endif|endswitch|endwhile|enum|eval|exit|extends|final|finally|fn|for|foreach|function|global|goto|if|implements|include|include_once|instanceof|insteadof|interface|isset|list|match|namespace|never|new|or|parent|print|private|protected|public|readonly|require|require_once|return|self|static|switch|throw|trait|try|unset|use|var|while|xor|yield|__halt_compiler)\\b", CASE_INSENSITIVE), true)
      ),
      token("class-name",
        pattern(compile("(\\b(?:extends|implements|instanceof|new(?!\\s+self|\\s+static))\\s+|\\bcatch\\s*\\()\\b[a-z_]\\w*(?!\\\\)\\b", CASE_INSENSITIVE), true, true),
        pattern(compile("(\\|\\s*)\\b[a-z_]\\w*(?!\\\\)\\b", CASE_INSENSITIVE), true, true),
        pattern(compile("\\b[a-z_]\\w*(?!\\\\)\\b(?=\\s*\\|)", CASE_INSENSITIVE), false, true)
      ),
      token("constant",
        pattern(compile("\\b(?:false|true)\\b", CASE_INSENSITIVE), false, false, "boolean"),
        pattern(compile("(::[ \\t]*)\\b[a-z_]\\w*\\b(?!\\s*\\()", CASE_INSENSITIVE), true, true),
        pattern(compile("(\\b(?:case|const)\\s+)\\b[a-z_]\\w*(?=\\s*[;=])", CASE_INSENSITIVE), true, true),
        pattern(compile("\\b(?:null)\\b", CASE_INSENSITIVE)),
        pattern(compile("\\b[A-Z_][A-Z0-9_]*\\b(?!\\s*\\()"))
      ),
      token("function", pattern(compile("(^|[^\\\\\\w])\\\\?[a-z_](?:[\\w\\\\]*\\w)?(?=\\s*\\()", CASE_INSENSITIVE), true)),
      token("property", pattern(compile("(->\\s*)\\w+"), true)),
      token("number", pattern(compile("\\b0b[01]+(?:_[01]+)*\\b|\\b0o[0-7]+(?:_[0-7]+)*\\b|\\b0x[\\da-f]+(?:_[\\da-f]+)*\\b|(?:\\b\\d+(?:_\\d+)*\\.?(?:\\d+(?:_\\d+)*)?|\\B\\.\\d+)(?:e[+-]?\\d+)?", CASE_INSENSITIVE))),
      token("operator", pattern(compile("<?=>|\\?\\?=?|\\.{3}|\\??->|[!=]=?=?|::|\\*\\*=?|--|\\+\\+|&&|\\|\\|||<<|>>|[?~]|[\\/^|%*&<>.+-]=?"))),
      token("string",
        pattern(compile("<<<'([^']+)'[\\r\\n](?:.*[\\r\\n])*?\\1;"), false, true, "nowdoc-string"),
        pattern(compile("<<<(?:\"([^\"]+)\"[\\r\\n](?:.*[\\r\\n])*?\\1;|([a-z_]\\w*)[\\r\\n](?:.*[\\r\\n])*?\\2;)", CASE_INSENSITIVE), false, true, "heredoc-string"),
        pattern(compile("`(?:\\\\[\\s\\S]|[^\\\\`])*`"), false, true, "backtick-quoted-string"),
        pattern(compile("'(?:\\\\[\\s\\S]|[^\\\\'])*'"), false, true, "single-quoted-string"),
        pattern(compile("\"(?:\\\\[\\s\\S]|[^\\\\\"])*\""), false, true, "double-quoted-string")
      ),
      token("punctuation", pattern(compile("[{}\\[\\]();,]")))
    );
  }
}
