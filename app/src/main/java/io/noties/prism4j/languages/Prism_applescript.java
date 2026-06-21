package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.Prism4j;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.grammar;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
public class Prism_applescript {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("applescript",
      token("comment",
        pattern(compile("\\(\\*(?:\\(\\*(?:[^*]|\\*(?!\\)))*\\*\\)|(?!\\(\\*)[\\s\\S])*?\\*\\)")),
        pattern(compile("--.+")),
        pattern(compile("#.+"))
      ),
      token("string", pattern(compile("\"(?:\\\\.|[^\"\\\\\\r\\n])*\""), false, true)),
      token("number", pattern(compile("(?:\\b\\d+(?:\\.\\d*)?|\\B\\.\\d+)(?:e-?\\d+)?\\b", CASE_INSENSITIVE))),
      token("keyword", pattern(compile("\\b(?:about|above|after|against|apart from|around|aside from|at|back|before|beginning|behind|below|beneath|beside|between|but|by|considering|continue|copy|does|eighth|else|end|equal|error|every|exit|false|fifth|first|for|fourth|from|front|get|given|global|if|ignoring|in|instead of|into|is|it|its|last|local|me|middle|my|ninth|of|on|onto|out of|over|prop|property|put|repeat|return|returning|second|set|seventh|since|sixth|some|tell|tenth|that|the|then|third|through|thru|timeout|times|to|transaction|true|try|until|where|while|whose|with|without)\\b"))),
      token("class-name", pattern(compile("\\b(?:POSIX file|RGB color|alias|application|boolean|centimeters|centimetres|class|constant|cubic centimeters|cubic centimetres|cubic feet|cubic inches|cubic meters|cubic metres|cubic yards|date|degrees Celsius|degrees Fahrenheit|degrees Kelvin|feet|file|gallons|grams|inches|integer|kilograms|kilometers|kilometres|list|liters|litres|meters|metres|miles|number|ounces|pounds|quarts|real|record|reference|script|square feet|square kilometers|square kilometres|square meters|square metres|square miles|square yards|text|yards)\\b"))),
      token("operator",
        pattern(compile("[&=\u2260\u2264\u2265*+\\-\\/\u00f7^]|[<>]=?")),
        pattern(compile("\\b(?:(?:begin|end|start)s? with|(?:contains?|(?:does not|doesn't) contain)|(?:is|isn't|is not) (?:contained by|in)|(?:(?:is|isn't|is not) )?(?:greater|less) than(?: or equal)?(?: to)?|(?:comes|(?:does not|doesn't) come) (?:after|before)|(?:is|isn't|is not) equal(?: to)?|(?:(?:does not|doesn't) equal|equal to|equals|is not|isn't)|(?:a )?(?:ref(?: to)?|reference to)|(?:and|as|div|mod|not|or))\\b"))
      ),
      token("punctuation", pattern(compile("[{}():,\u00ac\u00ab\u00bb\u300a\u300b]")))
    );
  }
}
