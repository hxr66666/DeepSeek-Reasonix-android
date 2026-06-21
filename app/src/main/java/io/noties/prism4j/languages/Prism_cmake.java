package io.noties.prism4j.languages;

import org.jetbrains.annotations.NotNull;
import io.noties.prism4j.Prism4j;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import static io.noties.prism4j.Prism4j.grammar;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;

@SuppressWarnings("unused")
public class Prism_cmake {

  @NotNull
  public static Prism4j.Grammar create(@NotNull Prism4j prism4j) {
    return grammar("cmake",
      token("comment", pattern(compile("#.*"))),
      token("string", pattern(compile("\"(?:[^\\\\\"']|\\\\.)*\""), false, true)),
      token("variable", pattern(compile("\\b(?:CMAKE_\\w+|\\w+_(?:(?:BINARY|SOURCE)_DIR|DESCRIPTION|HOMEPAGE_URL|ROOT|VERSION(?:_MAJOR|_MINOR|_PATCH|_TWEAK)?)|(?:ANDROID|APPLE|BORLAND|BUILD_SHARED_LIBS|CPACK_(?:ABSOLUTE_DESTINATION_FILES|COMPONENT_INCLUDE_TOPLEVEL_DIRECTORY|ERROR_ON_ABSOLUTE_INSTALL_DESTINATION|INCLUDE_TOPLEVEL_DIRECTORY|INSTALL_DEFAULT_DIRECTORY_PERMISSIONS|INSTALL_SCRIPT|PACKAGING_INSTALL_PREFIX|SET_DESTDIR|WARN_ON_ABSOLUTE_INSTALL_DESTINATION)|UNIX|WIN32|XCODE))\\b"))),
      token("keyword", pattern(compile("\\b(?:add_compile_definitions|add_compile_options|add_custom_command|add_custom_target|add_definitions|add_dependencies|add_executable|add_library|add_link_options|add_subdirectory|add_test|aux_source_directory|break|build_command|cmake_host_system_information|cmake_minimum_required|cmake_parse_arguments|cmake_policy|configure_file|continue|create_test_sourcelist|else|elseif|enable_language|enable_testing|endforeach|endfunction|endif|endmacro|endwhile|execute_process|export|file|find_file|find_library|find_package|find_path|find_program|foreach|function|get_cmake_property|get_directory_property|get_filename_component|get_property|get_source_file_property|get_target_property|get_test_property|if|include|include_directories|include_guard|install|link_directories|link_libraries|list|load_cache|macro|make_directory|mark_as_advanced|math|message|option|project|remove_definitions|return|separate_arguments|set|set_directory_properties|set_property|set_source_files_properties|set_target_properties|set_tests_properties|site_name|source_group|string|subdir_depends|subdirs|target_compile_definitions|target_compile_features|target_compile_options|target_include_directories|target_link_directories|target_link_libraries|target_link_options|target_sources|try_compile|try_run|unset|variable_watch|while|write_file)(?=\\s*\\()\\b", CASE_INSENSITIVE))),
      token("boolean", pattern(compile("\\b(?:FALSE|OFF|ON|TRUE)\\b"))),
      token("namespace", pattern(compile("\\b(?:INTERFACE|PRIVATE|PROPERTIES|PUBLIC|SHARED|STATIC|TARGET_OBJECTS)\\b"))),
      token("operator", pattern(compile("\\b(?:AND|DEFINED|EQUAL|GREATER|LESS|MATCHES|NOT|OR|STREQUAL|STRGREATER|STRLESS|VERSION_EQUAL|VERSION_GREATER|VERSION_LESS)\\b"))),
      token("inserted", pattern(compile("\\b\\w+::\\w+\\b"), false, false, "class-name")),
      token("number", pattern(compile("\\b\\d+(?:\\.\\d+)*\\b"))),
      token("function", pattern(compile("\\b[a-z_]\\w*(?=\\s*\\()\\b", CASE_INSENSITIVE))),
      token("punctuation", pattern(compile("[()>}]|\\$[<{]")))
    );
  }
}
