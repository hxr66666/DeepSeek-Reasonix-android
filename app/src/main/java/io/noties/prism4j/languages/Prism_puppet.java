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
public class Prism_puppet {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("puppet",
      token("multiline-comment", pattern(compile("(^|[^\\\\])\\/\\*[\\s\\S]*?\\*\\/"), true, true, "comment")),
      token("comment", pattern(compile("(^|[^\\\\])#.*"), true, true)),
      token("string", pattern(compile("(?:\"(?:[^\"\\\\]|\\\\.)*\"|'[^']*')"), false, true)),
      token("variable", pattern(compile("\\$\\w+(?:::\\w+)*"))),
      token("keyword", pattern(compile("\\b(?:Exec|File|Package|Service|absent|alert|and|case|class|contain|create_resources|debug|default|define|defined|else|elsif|err|fail|false|file|function|if|import|in|include|info|inherits|lookup|node|not|notice|or|private|realize|require|resources|return|schedule|tag|true|undef|unless|warning)\\b"))),
      token("function", pattern(compile("\\b(?:abs|alert|all|any|assert_type|base64|basename|between|capitalize|ceiling|chomp|chop|compare|contain|convert_to|create_resources|debug|defined|delete|delete_at|delete_regex|delete_undef_values|delete_values|deprecation|digest|dirname|dos2unix|each|empty|err|fact|file|filter|find_file|flatten|floor|fqdn_rand|fqdn_rotate|get_module_path|getparam|getvar|grep|hash|has_interface_with|has_ip_address|has_ip_network|has_key|hdiff|include|info|intersection|is_a|is_absolute_path|is_array|is_bool|is_domain_name|is_email_address|is_float|is_function_available|is_hash|is_integer|is_ip_address|is_ipv4_address|is_ipv6_address|is_mac_address|is_numeric|is_string|join|join_keys_to_values|keys|length|loadyaml|lstrip|map|match|max|md5|member|merge|min|mysql_password|num2bool|parsejson|parseyaml|pick|pick_default|prefix|printf|realize|reduce|regsubst|require|reverse|rstrip|scanf|sha1|sha256|shuffle|size|sort|sprintf|squeeze|step|str2bool|str2saltedsha512|strftime|strip|suffix|swapcase|tag|tagged|time|to_json|to_json_pretty|to_yaml|type3x|type|unique|unix2dos|upcase|uriescape|validate_absolute_path|validate_array|validate_augeas|validate_bool|validate_cmd|validate_email_address|validate_hash|validate_integer|validate_ip_address|validate_ipv4_address|validate_ipv6_address|validate_numeric|validate_re|validate_slength|validate_string|values|values_at|warning|with)\\b"))),
      token("number", pattern(compile("(?:\\b0x[\\da-f]+|(?:\\b\\d+(?:\\.\\d*)?|\\B\\.\\d+)(?:e[+-]?\\d+)?)\\b", CASE_INSENSITIVE))),
      token("boolean", pattern(compile("\\b(?:false|true|undef)\\b"))),
      token("operator", pattern(compile("=>|\\+>|->|~>|[=!<>]=?|[|&+\\-*\\/%!]|\\b(?:and|in|not|or)\\b"))),
      token("punctuation", pattern(compile("[{}\\[\\]():,.]")))
    );
  }
}
