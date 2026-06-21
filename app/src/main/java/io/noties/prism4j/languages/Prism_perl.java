package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.Prism4j;
import static java.util.regex.Pattern.MULTILINE;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.grammar;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
public class Prism_perl {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("perl",
      token("comment",
        pattern(compile("(^\\s*)=\\w[\\s\\S]*?=cut.*", MULTILINE), true, true),
        pattern(compile("(^|[^\\\\$])#.*"), true, true)
      ),
      token("string",
        pattern(compile("\\b(?:q|qq|qw|qx)(?![a-zA-Z0-9])\\s*(?:([^a-zA-Z0-9\\s{(\\[<])(?:(?!\\1)[^\\\\]|\\\\[\\s\\S])*\\1|([a-zA-Z0-9])(?:(?!\\2)[^\\\\]|\\\\[\\s\\S])*\\2|\\((?:[^()\\\\]|\\\\[\\s\\S]|\\((?:[^()\\\\]|\\\\[\\s\\S])*\\))*\\)|\\{(?:[^{}\\\\]|\\\\[\\s\\S]|\\{(?:[^{}\\\\]|\\\\[\\s\\S])*\\})*\\}|\\[(?:[^\\[\\]\\\\]|\\\\[\\s\\S]|\\[(?:[^\\[\\]\\\\]|\\\\[\\s\\S])*\\])*\\]|<(?:[^<>\\\\]|\\\\[\\s\\S]|<(?:[^<>\\\\]|\\\\[\\s\\S])*>)*>)"), false, true),
        pattern(compile("(\"|`)(?:(?!\\1)[^\\\\]|\\\\[\\s\\S])*\\1"), false, true),
        pattern(compile("'(?:[^'\\\\\\r\\n]|\\\\.)*'"), false, true)
      ),
      token("regex",
        pattern(compile("\\b(?:m|qr)(?![a-zA-Z0-9])\\s*(?:([^a-zA-Z0-9\\s{(\\[<])(?:(?!\\1)[^\\\\]|\\\\[\\s\\S])*\\1|([a-zA-Z0-9])(?:(?!\\2)[^\\\\]|\\\\[\\s\\S])*\\2|\\((?:[^()\\\\]|\\\\[\\s\\S]|\\((?:[^()\\\\]|\\\\[\\s\\S])*\\))*\\)|\\{(?:[^{}\\\\]|\\\\[\\s\\S]|\\{(?:[^{}\\\\]|\\\\[\\s\\S])*\\})*\\}|\\[(?:[^\\[\\]\\\\]|\\\\[\\s\\S]|\\[(?:[^\\[\\]\\\\]|\\\\[\\s\\S])*\\])*\\]|<(?:[^<>\\\\]|\\\\[\\s\\S]|<(?:[^<>\\\\]|\\\\[\\s\\S])*>)*>)[msixpodualngc]*"), false, true),
        pattern(compile("(^|[^-])\\b(?:s|tr|y)(?![a-zA-Z0-9])\\s*(?:([^a-zA-Z0-9\\s{(\\[<])(?:(?!\\2)[^\\\\]|\\\\[\\s\\S])*\\2(?:(?!\\2)[^\\\\]|\\\\[\\s\\S])*\\2|([a-zA-Z0-9])(?:(?!\\3)[^\\\\]|\\\\[\\s\\S])*\\3(?:(?!\\3)[^\\\\]|\\\\[\\s\\S])*\\3|\\((?:[^()\\\\]|\\\\[\\s\\S]|\\((?:[^()\\\\]|\\\\[\\s\\S])*\\))*\\)\\s*\\((?:[^()\\\\]|\\\\[\\s\\S]|\\((?:[^()\\\\]|\\\\[\\s\\S])*\\))*\\)|\\{(?:[^{}\\\\]|\\\\[\\s\\S]|\\{(?:[^{}\\\\]|\\\\[\\s\\S])*\\})*\\}\\s*\\{(?:[^{}\\\\]|\\\\[\\s\\S]|\\{(?:[^{}\\\\]|\\\\[\\s\\S])*\\})*\\}|\\[(?:[^\\[\\]\\\\]|\\\\[\\s\\S]|\\[(?:[^\\[\\]\\\\]|\\\\[\\s\\S])*\\])*\\]\\s*\\[(?:[^\\[\\]\\\\]|\\\\[\\s\\S]|\\[(?:[^\\[\\]\\\\]|\\\\[\\s\\S])*\\])*\\])[msixpodualngcer]*"), true, true),
        pattern(compile("\\/(?:[^\\/\\\\\\r\\n]|\\\\.)*\\/[msixpodualngc]*(?=\\s*(?:$|[\\r\\n,.;})&|\\-+*~<>!?^]|(?:and|cmp|eq|ge|gt|le|lt|ne|not|or|x|xor)\\b))"), false, true)
      ),
      token("variable",
        pattern(compile("[&*$@%]\\{\\^[A-Z]+\\}")),
        pattern(compile("[&*$@%]\\^[A-Z_]")),
        pattern(compile("[&*$@%]#?(?=\\{)")),
        pattern(compile("[&*$@%]#?(?:(?:::)*'?(?!\\d)[\\w$]+(?![\\w$]))+(?:::)*")),
        pattern(compile("[&*$@%]\\d+")),
        pattern(compile("(?!%=)[$@%][!\"#$%&'()*+,\\-.\\/:;<=>?@\\[\\\\\\]^_`{|}~]"))
      ),
      token("filehandle", pattern(compile("<(?![<=])\\S*?>|\\b_\\b"), false, false, "symbol")),
      token("v-string", pattern(compile("v\\d+(?:\\.\\d+)*|\\d+(?:\\.\\d+){2,}"), false, false, "string")),
      token("function", pattern(compile("(\\bsub[ \\t]+)\\w+"), true)),
      token("keyword", pattern(compile("\\b(?:any|break|continue|default|delete|die|do|else|elsif|eval|for|foreach|given|goto|if|last|local|my|next|our|package|print|redo|require|return|say|state|sub|switch|undef|unless|until|use|when|while)\\b"))),
      token("number", pattern(compile("\\b(?:0x[\\dA-Fa-f](?:_?[\\dA-Fa-f])*|0b[01](?:_?[01])*|(?:(?:\\d(?:_?\\d)*)?\\.)?\\d(?:_?\\d)*(?:[Ee][+-]?\\d+)?)\\b"))),
      token("operator", pattern(compile("-[rwxoRWXOezsfdlpSbctugkTBMAC]\\b|\\+[+=]?|-[-=>]?|\\*\\*?=?|\\/\\/?=?|=[=~>]?|~[~=]?|\\|\\|?=?|&&?=?|<(?:=>?|<=?)?|>>?=?|![~=]?|[%^]=?|\\.(?:=|\\.\\.|)?|[\\\\?]|\\bx(?:=|\\b)|\\b(?:and|cmp|eq|ge|gt|le|lt|ne|not|or|xor)\\b"))),
      token("punctuation", pattern(compile("[{}\\[\\];(),:]]")))
    );
  }
}
