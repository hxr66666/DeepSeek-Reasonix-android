package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.Prism4j;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.grammar;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
public class Prism_visual_basic {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("visual-basic",
      token("comment", pattern(compile("(?:[''']|REM\\b)(?:[^\\r\\n_]|_(?:\\r\\n?|\\n)?)*", CASE_INSENSITIVE))),
      token("directive", pattern(compile("#(?:Const|Else|ElseIf|End|ExternalChecksum|ExternalSource|If|Region)(?:\\b_[ \\t]*(?:\\r\\n?|\\n)|.)+", CASE_INSENSITIVE), false, true, "property")),
      token("string", pattern(compile("\\$?\"\"\"(?:\"\"[^\"]|[^\"])*\"\"\"C?|\\$?\"[^\"]*\"C?", CASE_INSENSITIVE), false, true)),
      token("date", pattern(compile("#[ \\t]*(?:\\d+([/-])\\d+\\1\\d+(?:[ \\t]+(?:\\d+[ \\t]*(?:AM|PM)|\\d+:\\d+(?::\\d+)?(?:[ \\t]*(?:AM|PM))?))?|\\d+[ \\t]*(?:AM|PM)|\\d+:\\d+(?::\\d+)?(?:[ \\t]*(?:AM|PM))?)[ \\t]*#", CASE_INSENSITIVE), false, false, "number")),
      token("number", pattern(compile("(?:(?:\\b\\d+(?:\\.\\d+)?|\\.\\d+)(?:E[+-]?\\d+)?|&[HO][\\dA-F]+)(?:[FRD]|U?[ILS])?", CASE_INSENSITIVE))),
      token("boolean", pattern(compile("\\b(?:False|Nothing|True)\\b", CASE_INSENSITIVE))),
      token("keyword", pattern(compile("\\b(?:AddHandler|AddressOf|Alias|And(?:Also)?|As|Boolean|ByRef|Byte|ByVal|Call|Case|Catch|C(?:Bool|Byte|Char|Date|Dbl|Dec|Int|Lng|Obj|SByte|Short|Sng|Str|Type|UInt|ULng|UShort)|Char|Class|Const|Continue|Currency|Date|Decimal|Declare|Default|Delegate|Dim|DirectCast|Do|Double|Each|Else(?:If)?|End(?:If)?|Enum|Erase|Error|Event|Exit|Finally|For|Friend|Function|Get(?:Type|XMLNamespace)?|Global|GoSub|GoTo|Handles|If|Implements|Imports|In|Inherits|Integer|Interface|Is|IsNot|Let|Lib|Like|Long|Loop|Me|Mod|Module|Must(?:Inherit|Override)|My(?:Base|Class)|Namespace|Narrowing|New|Next|Not(?:Inheritable|Overridable)?|Object|Of|On|Operator|Option(?:al)?|Or(?:Else)?|Out|Overloads|Overridable|Overrides|ParamArray|Partial|Private|Property|Protected|Public|RaiseEvent|ReadOnly|ReDim|RemoveHandler|Resume|Return|SByte|Select|Set|Shadows|Shared|short|Single|Static|Step|Stop|String|Structure|Sub|SyncLock|Then|Throw|To|Try|TryCast|Type|TypeOf|U(?:Integer|Long|Short)|Until|Using|Variant|Wend|When|While|Widening|With(?:Events)?|WriteOnly|Xor)\\b", CASE_INSENSITIVE))),
      token("operator", pattern(compile("[+\\-*/\\\\^<=>&#@$%!]|\\b_(?=[ \\t]*[\\r\\n])"))),
      token("punctuation", pattern(compile("[{}().,:?]")))
    );
  }
}
