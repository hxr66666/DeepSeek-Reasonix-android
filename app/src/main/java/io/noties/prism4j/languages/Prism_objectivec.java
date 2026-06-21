package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.GrammarUtils;
import io.noties.prism4j.Prism4j;
import io.noties.prism4j.annotations.Extend;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
@Extend("c")
public class Prism_objectivec {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    // Extend c grammar, filtering out class-name token, then override string/keyword/operator
    return GrammarUtils.extend(
      GrammarUtils.require(prism4j, "c"),
      "objectivec",
      token -> !token.name().equals("class-name"),
      token("string", pattern(compile("@?\"(?:\\\\(?:\\r\\n|[\\s\\S])|[^\"\\\\\\r\\n])*\""), false, true)),
      token("keyword", pattern(compile("\\b(?:asm|auto|break|case|char|const|continue|default|do|double|else|enum|extern|float|for|goto|if|in|inline|int|long|register|return|self|short|signed|sizeof|static|struct|super|switch|typedef|typeof|union|unsigned|void|volatile|while)\\b|(?:@interface|@end|@implementation|@protocol|@class|@public|@protected|@private|@property|@try|@catch|@finally|@throw|@synthesize|@dynamic|@selector)\\b"))),
      token("operator", pattern(compile("-[->]?|\\+\\+?|!=?|<<?=?|>>?=?|==?|&&?|\\|\\|?|[~^%?*\\/@]")))
    );
  }
}
