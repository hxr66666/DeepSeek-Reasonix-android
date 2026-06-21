package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.Prism4j;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.grammar;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
public class Prism_nix {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("nix",
      token("comment", pattern(compile("\\/\\*[\\s\\S]*?\\*\\/|#.*"), false, true)),
      token("string", pattern(compile("\"(?:[^\"\\\\]|\\\\[\\s\\S])*\"|''(?:(?!'')[\\s\\S]|''(?:'|\\\\|\\$\\{))*''"), false, true)),
      token("url",
        pattern(compile("\\b(?:[a-z]{3,7}:\\/\\/)[\\w\\-+%~\\/.:#=?&]+")),
        pattern(compile("([^\\/])(?:[\\w\\-+%~.:#=?&]*(?!\\/\\/)\\??[\\w\\-+%~\\/.:#=?&])?(?!\\/\\/)\\/?[\\w\\-+%~\\/.:#=?&]*"), true)
      ),
      token("antiquotation", pattern(compile("\\$(?=\\{)"), false, false, "important")),
      token("number", pattern(compile("\\b\\d+\\b"))),
      token("keyword", pattern(compile("\\b(?:assert|builtins|else|if|in|inherit|let|null|or|then|with)\\b"))),
      token("function", pattern(compile("\\b(?:abort|add|all|any|attrNames|attrValues|baseNameOf|compareVersions|concatLists|currentSystem|deepSeq|derivation|dirOf|div|elem(?:At)?|fetch(?:Tarball|url)|filter(?:Source)?|fromJSON|genList|getAttr|getEnv|hasAttr|hashString|head|import|intersectAttrs|is(?:Attrs|Bool|Function|Int|List|Null|String)|length|lessThan|listToAttrs|map|mul|parseDrvName|pathExists|read(?:Dir|File)|removeAttrs|replaceStrings|seq|sort|stringLength|sub(?:string)?|tail|throw|to(?:File|JSON|Path|String|XML)|trace|typeOf)\\b|\\bfoldl'\\B"))),
      token("boolean", pattern(compile("\\b(?:false|true)\\b"))),
      token("operator", pattern(compile("[=!<>]=?|\\+\\+?|\\|\\||&&|\\/\\/|->?|[?@]"))),
      token("punctuation", pattern(compile("[{}()\\[\\].,:;]")))
    );
  }
}
