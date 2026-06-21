package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.Prism4j;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.grammar;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
public class Prism_nushell {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("nushell",

      // Comments: # to end of line
      token("comment", pattern(compile("#[^\\n\\r]*"))),

      // Date/datetime literals: d"1994-11-05T08:15:30Z", d'1994-11-05', d`...`
      token("date", pattern(compile("\\bd[\"'`][^\"'`]+[\"'`]"))),

      // Interpolated strings: $"..." and $'...'
      token("string",
        pattern(compile("\\$\"(?:[^\"\\\\]|\\\\.)*\""), false, true),
        pattern(compile("\\$'[^']*'"), false, true),
        pattern(compile("\"(?:[^\"\\\\]|\\\\.)*\""), false, true),
        pattern(compile("'[^']*'"), false, true),
        pattern(compile("`[^`]*`"), false, true)
      ),

      // Binary data literals: 0x[HEX], 0o[OCT], 0b[BIN]
      token("binary",
        pattern(compile("0x\\[(?:[0-9a-fA-F]|\\s)+\\]")),
        pattern(compile("0o\\[(?:[0-7]|\\s)+\\]")),
        pattern(compile("0b\\[(?:[01]|\\s)+\\]"))
      ),

      // Unit values (filesize and duration) — must precede bare numbers
      token("unit", pattern(compile(
        "-?(?:0|[1-9]\\d*)(?:\\.\\d+)?(?:[eE][+-]?\\d+)?" +
        "(?:b|kib|kb|mib|mb|gib|gb|tib|tb|pib|pb|eib|eb|zib|zb|ns|us|ms|sec|min|hr|day|wk|month|yr|dec)\\b",
        CASE_INSENSITIVE
      ))),

      // Numbers: hex, octal, binary, float, integer
      token("number",
        pattern(compile("0x[0-9a-fA-F]+", CASE_INSENSITIVE)),
        pattern(compile("0o[0-7]+")),
        pattern(compile("0b[01]+")),
        pattern(compile("-?(?:0|[1-9]\\d*)\\.\\d*(?:[eE][+-]?\\d+)?")),
        pattern(compile("-?(?:0|[1-9]\\d*)"))
      ),

      // Variables: $var_name
      token("variable", pattern(compile("\\$[^\\s\\t|${}()\\[\\]\\r\\n.:+\\-/*]+"))),

      // Keywords (control flow, definitions, and common builtins)
      token("keyword", pattern(compile(
        "\\b(?:def-env|let-env|def|if|else|for|in|while|let|mut|where|break|continue|return|" +
        "do|export|module|use|overlay|alias|source|hide|extern|const|and|or|not)\\b"
      ))),

      // Boolean and null
      token("boolean", pattern(compile("\\b(?:true|false)\\b"))),
      token("null", pattern(compile("\\bnull\\b"))),

      // Flags: --long-flag[=value] or -s
      token("flag",
        pattern(compile("--[a-zA-Z0-9][a-zA-Z0-9-]*(?:=[^\\s]*)?")),
        pattern(compile("-[a-zA-Z0-9]+"))
      ),

      // Operators (ordered longest-match first)
      token("operator", pattern(compile(
        "\\+\\=|-=|\\*=|\\/=|=~|!~|<=|>=|!=|==|\\*\\*|\\.\\.|//|\\|\\||&&|[+\\-*\\/|<>=!&%]"
      ))),

      // Punctuation
      token("punctuation", pattern(compile("[{}\\[\\]();,.]")))
    );
  }
}
