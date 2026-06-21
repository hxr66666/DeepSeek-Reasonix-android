package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.Prism4j;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.grammar;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
public class Prism_fortran {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("fortran",
      token("quoted-number", pattern(compile("[BOZ](['\"])[A-F0-9]+\\1", CASE_INSENSITIVE), false, false, "number")),
      token("string", pattern(compile("(?:\\b\\w+_)?(['\"])(?:\\1\\1|&(?:\\r\\n?|\\n)(?:[ \\t]*!.*(?:\\r\\n?|\\n)|(?![ \\t]*!))|(?!\\1).)*(?:\\1|&)"))),
      token("comment", pattern(compile("!.*"), false, true)),
      token("boolean", pattern(compile("\\.(?:FALSE|TRUE)\\.(?:_\\w+)?", CASE_INSENSITIVE))),
      token("number", pattern(compile("(?:\\b\\d+(?:\\.\\d*)?|\\B\\.\\d+)(?:[ED][+-]?\\d+)?(?:_\\w+)?", CASE_INSENSITIVE))),
      token("keyword",
        pattern(compile("\\b(?:CHARACTER|COMPLEX|DOUBLE ?PRECISION|INTEGER|LOGICAL|REAL)\\b", CASE_INSENSITIVE)),
        pattern(compile("\\b(?:END ?)?(?:BLOCK ?DATA|DO|FILE|FORALL|FUNCTION|IF|INTERFACE|MODULE(?! PROCEDURE)|PROGRAM|SELECT|SUBROUTINE|TYPE|WHERE)\\b", CASE_INSENSITIVE)),
        pattern(compile("\\b(?:ALLOCATABLE|ALLOCATE|BACKSPACE|CALL|CASE|CLOSE|COMMON|CONTAINS|CONTINUE|CYCLE|DATA|DEALLOCATE|DIMENSION|DO|END|EQUIVALENCE|EXIT|EXTERNAL|FORMAT|GO ?TO|IMPLICIT(?: NONE)?|INQUIRE|INTENT|INTRINSIC|MODULE PROCEDURE|NAMELIST|NULLIFY|OPEN|OPTIONAL|PARAMETER|POINTER|PRINT|PRIVATE|PUBLIC|READ|RETURN|REWIND|SAVE|SELECT|STOP|TARGET|WHILE|WRITE)\\b", CASE_INSENSITIVE)),
        pattern(compile("\\b(?:ASSIGNMENT|DEFAULT|ELEMENTAL|ELSE|ELSEIF|ELSEWHERE|ENTRY|IN|INCLUDE|INOUT|KIND|NULL|ONLY|OPERATOR|OUT|PURE|RECURSIVE|RESULT|SEQUENCE|STAT|THEN|USE)\\b", CASE_INSENSITIVE))
      ),
      token("operator",
        pattern(compile("\\*\\*|\\/\\/|=>|[=\\/]=|[<>]=?|::|[+\\-*=%]|\\.[A-Z]+\\.", CASE_INSENSITIVE)),
        pattern(compile("(^|(?!\\().)\\/(?!\\))"), true)
      ),
      token("punctuation", pattern(compile("\\(\\/|\\/\\)|[(),;:&]")))
    );
  }
}
