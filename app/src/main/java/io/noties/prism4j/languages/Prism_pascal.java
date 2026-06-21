package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.Prism4j;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.grammar;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
public class Prism_pascal {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("pascal",
      token("directive", pattern(compile("\\{\\$[\\s\\S]*?\\}"), false, true, "property")),
      token("comment", pattern(compile("\\(\\*[\\s\\S]*?\\*\\)|\\{[\\s\\S]*?\\}|\\/\\/.*"), false, true)),
      token("string", pattern(compile("(?:'(?:''|[^'\\r\\n])*'(?!')|#[&$%]?[a-f\\d]+)+|\\^[a-z]", CASE_INSENSITIVE), false, true)),
      token("keyword",
        pattern(compile("(^|[^&])\\b(?:absolute|array|asm|begin|case|const|constructor|destructor|do|downto|else|end|file|for|function|goto|if|implementation|inherited|inline|interface|label|nil|object|of|operator|packed|procedure|program|record|reintroduce|repeat|self|set|string|then|to|type|unit|until|uses|var|while|with)\\b", CASE_INSENSITIVE), true),
        pattern(compile("(^|[^&])\\b(?:dispose|exit|false|new|true)\\b", CASE_INSENSITIVE), true),
        pattern(compile("(^|[^&])\\b(?:class|dispinterface|except|exports|finalization|finally|initialization|inline|library|on|out|packed|property|raise|resourcestring|threadvar|try)\\b", CASE_INSENSITIVE), true),
        pattern(compile("(^|[^&])\\b(?:absolute|abstract|alias|assembler|bitpacked|break|cdecl|continue|cppdecl|cvar|default|deprecated|dynamic|enumerator|experimental|export|external|far|far16|forward|generic|helper|implements|index|interrupt|iochecks|local|message|name|near|nodefault|noreturn|nostackframe|oldfpccall|otherwise|overload|override|pascal|platform|private|protected|public|published|read|register|reintroduce|result|safecall|saveregisters|softfloat|specialize|static|stdcall|stored|strict|unaligned|unimplemented|varargs|virtual|write)\\b", CASE_INSENSITIVE), true)
      ),
      token("number",
        pattern(compile("(?:[&%]\\d+|\\$[a-f\\d]+)", CASE_INSENSITIVE)),
        pattern(compile("\\b\\d+(?:\\.\\d+)?(?:e[+-]?\\d+)?", CASE_INSENSITIVE))
      ),
      token("operator",
        pattern(compile("\\.\\.|\\*\\*|:=|<[<=>]?|>[>=]?|[+\\-*\\/]=?|[@^=]")),
        pattern(compile("(^|[^&])\\b(?:and|as|div|exclude|in|include|is|mod|not|or|shl|shr|xor)\\b"), true)
      ),
      token("punctuation", pattern(compile("\\(\\.|\\.\\)|[()\\[\\]:;,.]")))
    );
  }
}
