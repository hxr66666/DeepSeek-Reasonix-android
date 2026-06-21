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
public class Prism_bash {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("bash",
      token("shebang", pattern(compile("^#!\\s*\\/.*"), false, false, "important")),
      token("comment", pattern(compile("(^|[^\"{\\\\$])#.*"), true)),
      token("string",
        pattern(compile("(\"|')(?:\\\\[\\s\\S]|(?!\\1)[^\\\\])*\\1"), false, true),
        pattern(compile("\\$'(?:[^'\\\\]|\\\\[\\s\\S])*'"), false, true)
      ),
      token("environment", pattern(compile("\\$?\\b(?:BASH|BASHOPTS|BASH_ALIASES|BASH_ARGC|BASH_ARGV|BASH_CMDS|BASH_COMPLETION_COMPAT_DIR|BASH_LINENO|BASH_REMATCH|BASH_SOURCE|BASH_VERSINFO|BASH_VERSION|COLORTERM|COLUMNS|COMP_WORDBREAKS|DBUS_SESSION_BUS_ADDRESS|DEFAULTS_PATH|DESKTOP_SESSION|DIRSTACK|DISPLAY|EUID|GDMSESSION|GDM_LANG|GNOME_KEYRING_CONTROL|GNOME_KEYRING_PID|GPG_AGENT_INFO|GROUPS|HISTCONTROL|HISTFILE|HISTFILESIZE|HISTSIZE|HOME|HOSTNAME|HOSTTYPE|IFS|INSTANCE|JOB|LANG|LANGUAGE|LC_ADDRESS|LC_ALL|LC_IDENTIFICATION|LC_MEASUREMENT|LC_MONETARY|LC_NAME|LC_NUMERIC|LC_PAPER|LC_TELEPHONE|LC_TIME|LESSCLOSE|LESSOPEN|LINES|LOGNAME|LS_COLORS|MACHTYPE|MAILCHECK|MANDATORY_PATH|NO_AT_BRIDGE|OLDPWD|OPTERR|OPTIND|ORBIT_SOCKETDIR|OSTYPE|PAPERSIZE|PATH|PIPESTATUS|PPID|PS1|PS2|PS3|PS4|PWD|RANDOM|REPLY|SECONDS|SELINUX_INIT|SESSION|SESSIONTYPE|SESSION_MANAGER|SHELL|SHELLOPTS|SHLVL|SSH_AUTH_SOCK|TERM|UID|UPSTART_EVENTS|UPSTART_INSTANCE|UPSTART_JOB|UPSTART_SESSION|USER|WINDOWID|XAUTHORITY|XDG_CONFIG_DIRS|XDG_CURRENT_DESKTOP|XDG_DATA_DIRS|XDG_GREETER_DATA_DIR|XDG_MENU_PREFIX|XDG_RUNTIME_DIR|XDG_SEAT|XDG_SEAT_PATH|XDG_SESSION_DESKTOP|XDG_SESSION_ID|XDG_SESSION_PATH|XDG_SESSION_TYPE|XDG_VTNR|XMODIFIERS)\\b"), false, false, "constant")),
      token("variable",
        pattern(compile("\\$\\?\\(\\([\\s\\S]+?\\)\\)"), false, true),
        pattern(compile("\\$\\((?:\\([^)]+\\)|[^()])+\\)|`[^`]+`"), false, true),
        pattern(compile("\\$\\{[^}]+\\}"), false, true),
        pattern(compile("\\$(?:\\w+|[#?*!@$])"))
      ),
      token("function", pattern(compile("(^|[\\s;|&]|[<>]\\()(?:add|apt|apt-cache|apt-get|awk|basename|bash|bc|cat|cd|chmod|chown|cp|curl|cut|date|diff|dig|docker|du|echo|find|git|grep|head|history|hostname|id|ifconfig|ip|java|kill|less|ln|ls|make|man|mkdir|mv|nc|node|npm|ping|ps|pwd|rm|rsync|scp|sed|sh|sleep|sort|ssh|sudo|tail|tar|tee|top|touch|tr|uniq|uname|unzip|vi|vim|wc|wget|which|whoami|xargs|zip)(?=\\s|$|[)\\s;|&])", MULTILINE), true, false, null)),
      token("keyword", pattern(compile("(^|[\\s;|&]|[<>]\\()(?:case|do|done|elif|else|esac|fi|for|function|if|in|select|then|until|while)(?=$|[)\\s;|&])", MULTILINE), true)),
      token("builtin", pattern(compile("(^|[\\s;|&]|[<>]\\()(?:\\.|:|alias|bind|break|builtin|caller|cd|command|continue|declare|echo|enable|eval|exec|exit|export|getopts|hash|help|let|local|logout|mapfile|printf|pwd|read|readarray|readonly|return|set|shift|shopt|source|test|times|trap|type|typeset|ulimit|umask|unalias|unset)(?=$|[)\\s;|&])", MULTILINE), true, false, "class-name")),
      token("boolean", pattern(compile("(^|[\\s;|&]|[<>]\\()(?:false|true)(?=$|[)\\s;|&])", MULTILINE), true)),
      token("operator", pattern(compile("\\d?<>|>\\||\\+=|=[=~]?|!=?|<<[<-]?|[&\\d]?>>|\\d[<>]&?|[<>][&=]?|&[>&]?|\\|[&|]?"))),
      token("punctuation", pattern(compile("\\$?\\(\\(?|\\)\\)?|\\.\\.|[{}\\[\\];\\\\]"))),
      token("number", pattern(compile("(^|\\s)(?:[1-9]\\d*|0)(?:[.,]\\d+)?\\b"), true))
    );
  }
}
