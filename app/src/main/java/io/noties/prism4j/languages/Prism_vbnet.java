package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.Prism4j;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.grammar;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
public class Prism_vbnet {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("vbnet",
      token("comment",
        pattern(compile("(?:!|REM\\b).+", CASE_INSENSITIVE)),
        pattern(compile("(^|[^\\\\:])'..*"), true, true)
      ),
      token("string", pattern(compile("(^|[^\"])\"(?:\"\"|[^\"])*\"(?!\")"), true, true)),
      token("keyword", pattern(compile("(?:\\b(?:ADDHANDLER|ADDRESSOF|ALIAS|AND|ANDALSO|AS|BEEP|BLOAD|BOOLEAN|BSAVE|BYREF|BYTE|BYVAL|CALL|CASE|CATCH|CBOOL|CBYTE|CCHAR|CDATE|CDBL|CDEC|CHAIN|CHAR|CHDIR|CINT|CLASS|CLEAR|CLNG|CLOSE|CLS|COBJ|COM|COMMON|CONST|CONTINUE|CSBYTE|CSHORT|CSNG|CSTR|CTYPE|CUINT|CULNG|CUSHORT|DATA|DATE|DECIMAL|DECLARE|DEFAULT|DELEGATE|DIM|DIRECTCAST|DO|DOUBLE|ELSE|ELSEIF|END|ENUM|ENVIRON|ERASE|ERROR|EVENT|EXIT|FALSE|FIELD|FILES|FINALLY|FOR|FRIEND|FUNCTION|GET|GETTYPE|GETXMLNAMESPACE|GLOBAL|GOSUB|GOTO|HANDLES|IF|IMPLEMENTS|IMPORTS|IN|INHERITS|INPUT|INTEGER|INTERFACE|IOCTL|IS|ISNOT|KEY|KILL|LET|LIB|LIKE|LOCATE|LOCK|LONG|LOOP|LSET|ME|MKDIR|MOD|MODULE|MUSTINHERIT|MUSTOVERRIDE|MYBASE|MYCLASS|NAME|NAMESPACE|NARROWING|NEW|NEXT|NOT|NOTHING|NOTINHERITABLE|NOTOVERRIDABLE|OBJECT|OF|OFF|ON|OPEN|OPERATOR|OPTION|OPTIONAL|OR|ORELSE|OUT|OVERLOADS|OVERRIDABLE|OVERRIDES|PARAMARRAY|PARTIAL|POKE|PRIVATE|PROPERTY|PROTECTED|PUBLIC|PUT|RAISEEVENT|READ|READONLY|REDIM|REM|REMOVEHANDLER|RESTORE|RESUME|RETURN|RMDIR|RSET|RUN|SBYTE|SELECT|SET|SHADOWS|SHARED|SHELL|SHORT|SINGLE|SLEEP|STATIC|STEP|STOP|STRING|STRUCTURE|SUB|SWAP|SYNCLOCK|SYSTEM|THEN|THROW|TIMER|TO|TROFF|TRON|TRUE|TRY|TRYCAST|TYPE|TYPEOF|UINTEGER|ULONG|UNLOCK|UNTIL|USHORT|USING|WAIT|WEND|WHEN|WHILE|WIDENING|WITH|WITHEVENTS|WRITE|WRITEONLY|XOR)|\\B(?:#CONST|#ELSE|#ELSEIF|#END|#IF))(?:\\$|\\b)", CASE_INSENSITIVE))),
      token("number", pattern(compile("(?:(?:\\b\\d+(?:\\.\\d+)?|\\.\\d+)(?:E[+-]?\\d+)?|&[HO][\\dA-F]+)(?:[FRD]|U?[ILS])?", CASE_INSENSITIVE))),
      token("boolean", pattern(compile("\\b(?:False|Nothing|True)\\b", CASE_INSENSITIVE))),
      token("operator", pattern(compile("[+\\-*/\\\\^<=>&#@$%!]|\\b_(?=[ \\t]*[\\r\\n])"))),
      token("punctuation", pattern(compile("[{}().,:?]")))
    );
  }
}
