package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;

import io.noties.prism4j.Prism4j;

import static java.util.regex.Pattern.MULTILINE;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.grammar;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

/**
 * Justfile syntax highlighting.
 * Grammar reference: https://just.systems/man/en/
 *
 * Tokens based on the Justfile grammar spec (justfile_grammar.md):
 *   COMMENT, BACKTICK, INDENTED_BACKTICK, STRING, INDENTED_STRING,
 *   RAW_STRING, INDENTED_RAW_STRING, NAME, LINE_PREFIX, etc.
 */
@SuppressWarnings("unused")
public class Prism_just {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("just",
      // COMMENT: #([^!].*)?$  — # not followed by !
      token("comment", pattern(
        compile("#(?:[^!\\r\\n][^\\r\\n]*)?$", MULTILINE)
      )),

      // INDENTED_BACKTICK: ```...```  — must come before single backtick
      token("indented-backtick", pattern(
        compile("```[\\s\\S]*?```"),
        false, true, "string"
      )),

      // BACKTICK: `[^`]*`  — command substitution
      token("backtick", pattern(
        compile("`[^`\\r\\n]*`"),
        false, true, "string"
      )),

      // INDENTED_RAW_STRING: '''...'''  — must come before single-quote string
      token("indented-raw-string", pattern(
        compile("'''[\\s\\S]*?'''"),
        false, true, "string"
      )),

      // INDENTED_STRING: """..."""  — must come before double-quote string
      token("indented-string", pattern(
        compile("\"\"\"[\\s\\S]*?\"\"\""),
        false, true, "string"
      )),

      // RAW_STRING: '[^']*'
      token("raw-string", pattern(
        compile("'[^'\\r\\n]*'"),
        false, true, "string"
      )),

      // STRING: "..." with escape sequences
      token("string", pattern(
        compile("\"(?:[^\"\\\\\\r\\n]|\\\\.)*\""),
        false, true
      )),

      // Attributes: [confirm], [doc(...)], [group(...)], [linux], [macos], etc.
      token("attribute", pattern(
        compile("^\\[[^\\]\\r\\n]*\\]", MULTILINE),
        false, false, "keyword"
      )),

      // Interpolation in recipe body: {{ expression }}
      token("interpolation", pattern(
        compile("\\{\\{[^{}\\r\\n]*\\}\\}"),
        false, false, "variable"
      )),

      // Keywords: alias, export, import, mod, set, if, else, assert
      token("keyword", pattern(
        compile("\\b(?:alias|assert|else|export|if|import|mod|set)\\b")
      )),

      // Boolean literals
      token("boolean", pattern(
        compile("\\b(?:false|true)\\b")
      )),

      // Built-in functions
      token("builtin", pattern(
        compile("\\b(?:arch|blake3|blake3_file|capitalize|env|env_var|env_var_or_default|error|extension|file_name|file_stem|home_directory|invocation_directory|invocation_directory_native|is_dependency|join|just_executable|just_pid|justfile|justfile_directory|kebabcase|lowercamelcase|lowercase|num_cpus|os|os_family|parent_directory|path_exists|quote|read|replace|replace_regex|semver_matches|sha256|sha256_file|shell|shoutykebabcase|shoutysnakecase|snakecase|source_directory|source_file|titlecase|trim|trim_end|trim_end_match|trim_end_matches|trim_start|trim_start_match|trim_start_matches|uppercamelcase|uppercase|uuid|without_extension)(?=\\s*\\()"),
        false, false, "function"
      )),

      // Setting names (used after `set`)
      token("setting", pattern(
        compile("(?<=\\bset\\s{1,32})(?:allow-duplicate-recipes|allow-duplicate-variables|dotenv-filename|dotenv-load|dotenv-path|dotenv-required|export|fallback|ignore-comments|positional-arguments|quiet|script-interpreter|shell|tempdir|unstable|windows-powershell|windows-shell|working-directory)\\b"),
        false, false, "attr-name"
      )),

      // Recipe names: NAME at start of line (possibly prefixed with @), followed by params or colon
      token("recipe", pattern(
        compile("^@?[a-zA-Z_][a-zA-Z0-9_-]*(?=\\s*(?:[($\\w]|:(?!=)))", MULTILINE),
        false, false, "function"
      )),

      // Variable reference: $NAME in parameters
      token("variable", pattern(
        compile("\\$[a-zA-Z_][a-zA-Z0-9_-]*")
      )),

      // LINE_PREFIX: @-, -@, @, - at start of recipe body lines (after indent)
      token("line-prefix", pattern(
        compile("(?<=^[ \\t]+)(?:@-|-@|@|-)", MULTILINE),
        false, false, "operator"
      )),

      // Operators: :=, ::, ||, &&, ==, !=, =~, +, /, ?, @
      token("operator", pattern(
        compile(":=|::|\\|\\||&&|==|!=|=~|[+/?@]")
      )),

      // Punctuation
      token("punctuation", pattern(
        compile("[()\\[\\]{},:]")
      ))
    );
  }
}
