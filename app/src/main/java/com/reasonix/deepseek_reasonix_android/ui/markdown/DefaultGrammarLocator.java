package com.reasonix.deepseek_reasonix_android.ui.markdown;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import io.noties.prism4j.GrammarLocator;
import io.noties.prism4j.Prism4j;
import io.noties.prism4j.Prism4j.Grammar;
import io.noties.prism4j.Prism4j.Token;
import io.noties.prism4j.languages.Prism_abap;
import io.noties.prism4j.languages.Prism_abnf;
import io.noties.prism4j.languages.Prism_actionscript;
import io.noties.prism4j.languages.Prism_ada;
import io.noties.prism4j.languages.Prism_antlr4;
import io.noties.prism4j.languages.Prism_apacheconf;
import io.noties.prism4j.languages.Prism_applescript;
import io.noties.prism4j.languages.Prism_armasm;
import io.noties.prism4j.languages.Prism_aspnet;
import io.noties.prism4j.languages.Prism_awk;
import io.noties.prism4j.languages.Prism_bash;
import io.noties.prism4j.languages.Prism_basic;
import io.noties.prism4j.languages.Prism_batch;
import io.noties.prism4j.languages.Prism_bnf;
import io.noties.prism4j.languages.Prism_c;
import io.noties.prism4j.languages.Prism_clike;
import io.noties.prism4j.languages.Prism_clojure;
import io.noties.prism4j.languages.Prism_cmake;
import io.noties.prism4j.languages.Prism_cobol;
import io.noties.prism4j.languages.Prism_cpp;
import io.noties.prism4j.languages.Prism_csharp;
import io.noties.prism4j.languages.Prism_css;
import io.noties.prism4j.languages.Prism_css_extras;
import io.noties.prism4j.languages.Prism_csv;
import io.noties.prism4j.languages.Prism_dart;
import io.noties.prism4j.languages.Prism_diff;
import io.noties.prism4j.languages.Prism_docker;
import io.noties.prism4j.languages.Prism_dot;
import io.noties.prism4j.languages.Prism_ebnf;
import io.noties.prism4j.languages.Prism_editorconfig;
import io.noties.prism4j.languages.Prism_elixir;
import io.noties.prism4j.languages.Prism_erlang;
import io.noties.prism4j.languages.Prism_fortran;
import io.noties.prism4j.languages.Prism_fsharp;
import io.noties.prism4j.languages.Prism_git;
import io.noties.prism4j.languages.Prism_go;
import io.noties.prism4j.languages.Prism_graphql;
import io.noties.prism4j.languages.Prism_groovy;
import io.noties.prism4j.languages.Prism_haskell;
import io.noties.prism4j.languages.Prism_haxe;
import io.noties.prism4j.languages.Prism_hcl;
import io.noties.prism4j.languages.Prism_http;
import io.noties.prism4j.languages.Prism_ignore;
import io.noties.prism4j.languages.Prism_ini;
import io.noties.prism4j.languages.Prism_java;
import io.noties.prism4j.languages.Prism_javascript;
import io.noties.prism4j.languages.Prism_javastacktrace;
import io.noties.prism4j.languages.Prism_json;
import io.noties.prism4j.languages.Prism_json5;
import io.noties.prism4j.languages.Prism_jsstacktrace;
import io.noties.prism4j.languages.Prism_jsx;
import io.noties.prism4j.languages.Prism_julia;
import io.noties.prism4j.languages.Prism_just;
import io.noties.prism4j.languages.Prism_kotlin;
import io.noties.prism4j.languages.Prism_latex;
import io.noties.prism4j.languages.Prism_lisp;
import io.noties.prism4j.languages.Prism_lua;
import io.noties.prism4j.languages.Prism_makefile;
import io.noties.prism4j.languages.Prism_markdown;
import io.noties.prism4j.languages.Prism_markup;
import io.noties.prism4j.languages.Prism_matlab;
import io.noties.prism4j.languages.Prism_mermaid;
import io.noties.prism4j.languages.Prism_nasm;
import io.noties.prism4j.languages.Prism_nginx;
import io.noties.prism4j.languages.Prism_nix;
import io.noties.prism4j.languages.Prism_nushell;
import io.noties.prism4j.languages.Prism_objectivec;
import io.noties.prism4j.languages.Prism_ocaml;
import io.noties.prism4j.languages.Prism_pascal;
import io.noties.prism4j.languages.Prism_perl;
import io.noties.prism4j.languages.Prism_php;
import io.noties.prism4j.languages.Prism_plant_uml;
import io.noties.prism4j.languages.Prism_plsql;
import io.noties.prism4j.languages.Prism_powershell;
import io.noties.prism4j.languages.Prism_prolog;
import io.noties.prism4j.languages.Prism_properties;
import io.noties.prism4j.languages.Prism_protobuf;
import io.noties.prism4j.languages.Prism_puppet;
import io.noties.prism4j.languages.Prism_python;
import io.noties.prism4j.languages.Prism_r;
import io.noties.prism4j.languages.Prism_regex;
import io.noties.prism4j.languages.Prism_ruby;
import io.noties.prism4j.languages.Prism_rust;
import io.noties.prism4j.languages.Prism_scala;
import io.noties.prism4j.languages.Prism_solidity;
import io.noties.prism4j.languages.Prism_sql;
import io.noties.prism4j.languages.Prism_swift;
import io.noties.prism4j.languages.Prism_systemd;
import io.noties.prism4j.languages.Prism_toml;
import io.noties.prism4j.languages.Prism_tsx;
import io.noties.prism4j.languages.Prism_typescript;
import io.noties.prism4j.languages.Prism_vbnet;
import io.noties.prism4j.languages.Prism_visual_basic;
import io.noties.prism4j.languages.Prism_yaml;
import io.noties.prism4j.languages.Prism_zig;

public class DefaultGrammarLocator implements GrammarLocator {

  public DefaultGrammarLocator() {
    this(false, null);
  }

  /**
   * Constructor to allow initialization of all languages before requesting them. If you do not
   * want to initialize them, please use {@link #DefaultGrammarLocator()}.
   *
   * @param initLanguages set this to true or use {@link #DefaultGrammarLocator()}
   * @param prism4j       the {@link Prism4j} object
   */
  public DefaultGrammarLocator(boolean initLanguages, @Nullable Prism4j prism4j) {
    if (initLanguages && prism4j != null) {
      for (String i : languages()) {
        grammar(prism4j, i);
      }
    }
  }

  @SuppressWarnings("ConstantConditions")
  private static final Grammar NULL_GRAMMAR =
    new Grammar() {
      @Override
      public @NotNull String name() {
        return null;
      }

      @Override
      public @NotNull List<Token> tokens() {
        return null;
      }
    };

  private final HashMap<String, Grammar> grammarCache = new HashMap<>(100);

  @Nullable
  @Override
  public Grammar grammar(@NotNull Prism4j prism4j, @NotNull String language) {
    final String name = realLanguageName(language);
    Grammar grammar = grammarCache.get(name);
    if (grammar != null) {
      if (NULL_GRAMMAR == grammar) {
        grammar = null;
      }
      return grammar;
    }

    grammar = obtainGrammar(prism4j, name);
    grammarCache.put(name, Objects.requireNonNullElse(grammar, NULL_GRAMMAR));

    return grammar;
  }

  @NotNull
  protected String realLanguageName(@NotNull String name) {
    final String out;
    switch (name) {
      case "kt":
      case "kts":
        out = "kotlin";
        break;
      case "tex":
      case "context":
        out = "latex";
        break;
      case "ts":
        out = "typescript";
        break;
      case "js":
        out = "javascript";
        break;
      case "xml":
      case "html":
      case "mathml":
      case "svg":
        out = "markup";
        break;
      case "dotnet":
        out = "csharp";
        break;
      case "shell":
      case "sh":
        out = "bash";
        break;
      case "py":
        out = "python";
        break;
      case "yml":
        out = "yaml";
        break;
      case "webmanifest":
        out = "json";
        break;
      case "dockerfile":
        out = "docker";
        break;
      case "rb":
        out = "ruby";
        break;
      case "rs":
        out = "rust";
        break;
      case "objc":
        out = "objectivec";
        break;
      case "hs":
        out = "haskell";
        break;
      case "gv":
        out = "dot";
        break;
      case "vb":
      case "vba":
        out = "visual-basic";
        break;
      case "ps1":
      case "psd1":
      case "psm1":
        out = "powershell";
        break;
      case "proto":
        out = "protobuf";
        break;
      case "rbnf":
        out = "bnf";
        break;
      case "gitignore":
      case "hgignore":
      case "npmignore":
        out = "ignore";
        break;
      case "objectpascal":
        out = "pascal";
        break;
      case "sol":
        out = "solidity";
        break;
      case "g4":
        out = "antlr4";
        break;
      case "arm-asm":
        out = "armasm";
        break;
      case "aspx":
        out = "aspnet";
        break;
      case "m":
      case "matlab":
        out = "matlab";
        break;
      case "puml":
        out = "plant-uml";
        break;
      case "gawk":
        out = "awk";
        break;
      case "asppnet":
        out = "aspnet";
        break;
      case "matelab":
        out = "matlab";
        break;
      case "justfile":
        out = "just";
        break;
      default:
        out = name;
    }
    return out;
  }

  @Nullable
  protected Grammar obtainGrammar(@NotNull Prism4j prism4j, @NotNull String name) {
    final Grammar grammar;
    switch (name) {
      case "abap":
        grammar = Prism_abap.create(prism4j);
        break;
      case "abnf":
        grammar = Prism_abnf.create(prism4j);
        break;
      case "actionscript":
        grammar = Prism_actionscript.create(prism4j);
        break;
      case "ada":
        grammar = Prism_ada.create(prism4j);
        break;
      case "antlr4":
        grammar = Prism_antlr4.create(prism4j);
        break;
      case "apacheconf":
        grammar = Prism_apacheconf.create(prism4j);
        break;
      case "applescript":
        grammar = Prism_applescript.create(prism4j);
        break;
      case "armasm":
        grammar = Prism_armasm.create(prism4j);
        break;
      case "aspnet":
        grammar = Prism_aspnet.create(prism4j);
        break;
      case "awk":
        grammar = Prism_awk.create(prism4j);
        break;
      case "bash":
        grammar = Prism_bash.create(prism4j);
        break;
      case "basic":
        grammar = Prism_basic.create(prism4j);
        break;
      case "batch":
        grammar = Prism_batch.create(prism4j);
        break;
      case "bnf":
        grammar = Prism_bnf.create(prism4j);
        break;
      case "c":
        grammar = Prism_c.create(prism4j);
        break;
      case "clike":
        grammar = Prism_clike.create(prism4j);
        break;
      case "clojure":
        grammar = Prism_clojure.create(prism4j);
        break;
      case "cmake":
        grammar = Prism_cmake.create(prism4j);
        break;
      case "cobol":
        grammar = Prism_cobol.create(prism4j);
        break;
      case "cpp":
        grammar = Prism_cpp.create(prism4j);
        break;
      case "csharp":
        grammar = Prism_csharp.create(prism4j);
        break;
      case "csv":
        grammar = Prism_csv.create(prism4j);
        break;
      case "css":
        grammar = Prism_css.create(prism4j);
        break;
      case "css-extras":
        grammar = Prism_css_extras.create(prism4j);
        break;
      case "dart":
        grammar = Prism_dart.create(prism4j);
        break;
      case "diff":
        grammar = Prism_diff.create(prism4j);
        break;
      case "docker":
        grammar = Prism_docker.create(prism4j);
        break;
      case "dot":
        grammar = Prism_dot.create(prism4j);
        break;
      case "ebnf":
        grammar = Prism_ebnf.create(prism4j);
        break;
      case "editorconfig":
        grammar = Prism_editorconfig.create(prism4j);
        break;
      case "elixir":
        grammar = Prism_elixir.create(prism4j);
        break;
      case "erlang":
        grammar = Prism_erlang.create(prism4j);
        break;
      case "fortran":
        grammar = Prism_fortran.create(prism4j);
        break;
      case "fsharp":
        grammar = Prism_fsharp.create(prism4j);
        break;
      case "git":
        grammar = Prism_git.create(prism4j);
        break;
      case "go":
        grammar = Prism_go.create(prism4j);
        break;
      case "graphql":
        grammar = Prism_graphql.create(prism4j);
        break;
      case "groovy":
        grammar = Prism_groovy.create(prism4j);
        break;
      case "haskell":
        grammar = Prism_haskell.create(prism4j);
        break;
      case "haxe":
        grammar = Prism_haxe.create(prism4j);
        break;
      case "hcl":
        grammar = Prism_hcl.create(prism4j);
        break;
      case "http":
        grammar = Prism_http.create(prism4j);
        break;
      case "ignore":
        grammar = Prism_ignore.create(prism4j);
        break;
      case "ini":
        grammar = Prism_ini.create(prism4j);
        break;
      case "java":
        grammar = Prism_java.create(prism4j);
        break;
      case "javastacktrace":
        grammar = Prism_javastacktrace.create(prism4j);
        break;
      case "javascript":
        grammar = Prism_javascript.create(prism4j);
        break;
      case "json":
        grammar = Prism_json.create(prism4j);
        break;
      case "just":
        grammar = Prism_just.create(prism4j);
        break;
      case "json5":
        grammar = Prism_json5.create(prism4j);
        break;
      case "jsstacktrace":
        grammar = Prism_jsstacktrace.create(prism4j);
        break;
      case "jsx":
        grammar = Prism_jsx.create(prism4j);
        break;
      case "julia":
        grammar = Prism_julia.create(prism4j);
        break;
      case "kotlin":
        grammar = Prism_kotlin.create(prism4j);
        break;
      case "latex":
        grammar = Prism_latex.create(prism4j);
        break;
      case "lisp":
        grammar = Prism_lisp.create(prism4j);
        break;
      case "lua":
        grammar = Prism_lua.create(prism4j);
        break;
      case "makefile":
        grammar = Prism_makefile.create(prism4j);
        break;
      case "markdown":
        grammar = Prism_markdown.create(prism4j);
        break;
      case "markup":
        grammar = Prism_markup.create(prism4j);
        break;
      case "matlab":
        grammar = Prism_matlab.create(prism4j);
        break;
      case "mermaid":
        grammar = Prism_mermaid.create(prism4j);
        break;
      case "nasm":
        grammar = Prism_nasm.create(prism4j);
        break;
      case "nginx":
        grammar = Prism_nginx.create(prism4j);
        break;
      case "nix":
        grammar = Prism_nix.create(prism4j);
        break;
      case "nu":
      case "nushell":
        grammar = Prism_nushell.create(prism4j);
        break;
      case "objectivec":
        grammar = Prism_objectivec.create(prism4j);
        break;
      case "ocaml":
        grammar = Prism_ocaml.create(prism4j);
        break;
      case "pascal":
        grammar = Prism_pascal.create(prism4j);
        break;
      case "perl":
        grammar = Prism_perl.create(prism4j);
        break;
      case "php":
        grammar = Prism_php.create(prism4j);
        break;
      case "plant-uml":
        grammar = Prism_plant_uml.create(prism4j);
        break;
      case "plsql":
        grammar = Prism_plsql.create(prism4j);
        break;
      case "powershell":
        grammar = Prism_powershell.create(prism4j);
        break;
      case "properties":
        grammar = Prism_properties.create(prism4j);
        break;
      case "prolog":
        grammar = Prism_prolog.create(prism4j);
        break;
      case "protobuf":
        grammar = Prism_protobuf.create(prism4j);
        break;
      case "puppet":
        grammar = Prism_puppet.create(prism4j);
        break;
      case "python":
        grammar = Prism_python.create(prism4j);
        break;
      case "r":
        grammar = Prism_r.create(prism4j);
        break;
      case "regex":
        grammar = Prism_regex.create(prism4j);
        break;
      case "ruby":
        grammar = Prism_ruby.create(prism4j);
        break;
      case "rust":
        grammar = Prism_rust.create(prism4j);
        break;
      case "scala":
        grammar = Prism_scala.create(prism4j);
        break;
      case "solidity":
        grammar = Prism_solidity.create(prism4j);
        break;
      case "sql":
        grammar = Prism_sql.create(prism4j);
        break;
      case "swift":
        grammar = Prism_swift.create(prism4j);
        break;
      case "systemd":
        grammar = Prism_systemd.create(prism4j);
        break;
      case "toml":
        grammar = Prism_toml.create(prism4j);
        break;
      case "tsx":
        grammar = Prism_tsx.create(prism4j);
        break;
      case "typescript":
        grammar = Prism_typescript.create(prism4j);
        break;
      case "vbnet":
        grammar = Prism_vbnet.create(prism4j);
        break;
      case "visual-basic":
        grammar = Prism_visual_basic.create(prism4j);
        break;
      case "yaml":
        grammar = Prism_yaml.create(prism4j);
        break;
      case "zig":
        grammar = Prism_zig.create(prism4j);
        break;
      default:
        grammar = null;
    }
    return grammar;
  }



  @Override
  @NotNull
  public HashSet<String> languages() {
    final HashSet<String> languages = new HashSet<>(150);
    languages.add("abap");
    languages.add("abnf");
    languages.add("actionscript");
    languages.add("ada");
    languages.add("antlr4");
    languages.add("apacheconf");
    languages.add("applescript");
    languages.add("armasm");
    languages.add("aspnet");
    languages.add("awk");
    languages.add("bash");
    languages.add("basic");
    languages.add("batch");
    languages.add("bnf");
    languages.add("c");
    languages.add("clike");
    languages.add("clojure");
    languages.add("cmake");
    languages.add("cobol");
    languages.add("cpp");
    languages.add("csharp");
    languages.add("csv");
    languages.add("css");
    languages.add("css-extras");
    languages.add("dart");
    languages.add("diff");
    languages.add("docker");
    languages.add("dot");
    languages.add("ebnf");
    languages.add("editorconfig");
    languages.add("elixir");
    languages.add("erlang");
    languages.add("fortran");
    languages.add("fsharp");
    languages.add("git");
    languages.add("go");
    languages.add("graphql");
    languages.add("groovy");
    languages.add("haskell");
    languages.add("haxe");
    languages.add("hcl");
    languages.add("http");
    languages.add("ignore");
    languages.add("ini");
    languages.add("java");
    languages.add("javastacktrace");
    languages.add("javascript");
    languages.add("json");
    languages.add("json5");
    languages.add("just");
    languages.add("jsstacktrace");
    languages.add("jsx");
    languages.add("julia");
    languages.add("kotlin");
    languages.add("latex");
    languages.add("lisp");
    languages.add("lua");
    languages.add("makefile");
    languages.add("markdown");
    languages.add("markup");
    languages.add("matlab");
    languages.add("mermaid");
    languages.add("nasm");
    languages.add("nginx");
    languages.add("nix");
    languages.add("nushell");
    languages.add("objectivec");
    languages.add("ocaml");
    languages.add("pascal");
    languages.add("perl");
    languages.add("php");
    languages.add("plant-uml");
    languages.add("plsql");
    languages.add("powershell");
    languages.add("properties");
    languages.add("prolog");
    languages.add("protobuf");
    languages.add("puppet");
    languages.add("python");
    languages.add("r");
    languages.add("regex");
    languages.add("ruby");
    languages.add("rust");
    languages.add("scala");
    languages.add("solidity");
    languages.add("sql");
    languages.add("swift");
    languages.add("systemd");
    languages.add("toml");
    languages.add("tsx");
    languages.add("typescript");
    languages.add("vbnet");
    languages.add("visual-basic");
    languages.add("yaml");
    languages.add("zig");
    return languages;
  }
}
