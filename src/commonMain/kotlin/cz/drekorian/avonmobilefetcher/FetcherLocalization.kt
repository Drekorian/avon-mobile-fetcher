package cz.drekorian.avonmobilefetcher

import com.github.ajalt.clikt.output.Localization
import cz.drekorian.avonmobilefetcher.resources.i18n

internal class FetcherLocalization : Localization {
    override fun usageError(): String =
        i18n("usage_error")

    override fun badParameter(): String =
        i18n("bad_parameter")

    override fun badParameterWithMessage(message: String): String =
        i18n("bad_parameter_with_message")
            .nFormat(message)

    override fun badParameterWithParam(paramName: String): String =
        i18n("bad_parameter_with_param")
            .nFormat(paramName)

    override fun badParameterWithMessageAndParam(paramName: String, message: String): String =
        i18n("bad_parameter_with_message_and_param")
            .nFormat(paramName, message)

    override fun missingOption(paramName: String): String =
        i18n("missing_option")
            .nFormat(paramName)

    override fun missingArgument(paramName: String): String =
        i18n("missing_argument")
            .nFormat(paramName)

    override fun noSuchSubcommand(name: String, possibilities: List<String>): String = buildString {
        append(
            i18n("no_such_subcommand")
                .nFormat(name)
        )
        when {
            possibilities.size == 1 -> append(
                i18n("no_such_subcommand_single_possibility")
                    .nFormat(possibilities.first())
            )

            possibilities.size > 1 -> append(
                i18n("no_such_subcommand_multiple_possibilities")
                    .nFormat(possibilities.joinToString())
            )
        }
    }

    override fun noSuchOption(name: String, possibilities: List<String>): String = buildString {
        append(i18n("no_such_option").nFormat(name))

        when {
            possibilities.size == 1 -> append(
                i18n("no_such_option_single_possibility")
                    .nFormat(possibilities.first())
            )

            possibilities.size > 1 -> append(
                i18n("no_such_option_multiple_possibilities")
                    .nFormat(possibilities.joinToString())
            )
        }
    }

    override fun noSuchOptionWithSubCommandPossibility(name: String, subcommand: String): String =
        i18n("no_such_option_with_subcommand_possibility")
            .nFormat(name, subcommand, name)

    override fun incorrectOptionValueCount(name: String, count: Int): String = when (count) {
        0 -> i18n("incorrect_option_value_count_zero")
            .nFormat(name)

        1 -> i18n("incorrect_option_value_count_one")
            .nFormat(name)

        else -> i18n("incorrect_option_value_count_multiple")
            .nFormat(name, count.toString())
    }

    override fun incorrectArgumentValueCount(name: String, count: Int): String = when (count) {
        0 -> i18n("incorrect_argument_value_count_zero")
            .nFormat(name)

        1 -> i18n("incorrect_argument_value_count_one")
            .nFormat(name)

        else -> i18n("incorrect_argument_value_count_multiple")
            .nFormat(name, count.toString())
    }

    override fun mutexGroupException(name: String, others: List<String>): String =
        i18n("mutex_group_exception")
            .nFormat(name, others.joinToString(separator = i18n("mutex_group_exception_or")))

    override fun fileNotFound(filename: String): String =
        i18n("file_not_found")
            .nFormat(filename)

    override fun invalidFileFormat(filename: String, message: String): String =
        i18n("invalid_file_format")
            .nFormat(filename, message)

    override fun invalidFileFormat(filename: String, lineNumber: Int, message: String): String =
        i18n("invalid_file_format_line_number")
            .nFormat(filename, lineNumber.toString(), message)

    override fun fileEndsWithSlash(): String =
        i18n("file_ends_with_slash")

    override fun unclosedQuote(): String =
        i18n("unclosed_quote")

    override fun extraArgumentOne(name: String): String =
        i18n("extra_argument_one")
            .nFormat(name)

    override fun extraArgumentMany(name: String, count: Int): String =
        i18n("extra_argument_many")
            .nFormat(name)

    override fun invalidFlagValueInFile(name: String): String =
        i18n("invalid_flag_value_in_file")
            .nFormat(name)

    override fun requiredMutexOption(options: String): String =
        i18n("required_mutex_option")
            .nFormat(options)

    override fun invalidGroupChoice(value: String, choices: List<String>): String =
        i18n("invalid_group_choice")
            .nFormat(value, choices.joinToString())

    override fun floatConversionError(value: String): String =
        i18n("float_conversion_error")
            .nFormat(value)

    override fun intConversionError(value: String): String =
        i18n("int_conversion_error")
            .nFormat(value)

    override fun boolConversionError(value: String): String =
        i18n("bool_conversion_error")
            .nFormat(value)

    override fun rangeExceededMax(value: String, limit: String): String =
        i18n("range_exceeded_max")
            .nFormat(value, limit)

    override fun rangeExceededMin(value: String, limit: String): String =
        i18n("range_exceeded_min")
            .nFormat(value, limit)

    override fun rangeExceededBoth(value: String, min: String, max: String): String =
        i18n("range_exceeded_both")
            .nFormat(value, min, max)

    override fun countedOptionExceededLimit(count: Int, limit: Int): String =
        i18n("counted_option_exceeded_limit")
            .nFormat(count.toString(), limit.toString())

    override fun invalidChoice(choice: String, choices: List<String>): String =
        i18n("invalid_choice")
            .nFormat(choice, choices.joinToString())

    override fun pathTypeFile(): String =
        i18n("path_type_file")

    override fun pathTypeDirectory(): String =
        i18n("path_type_directory")

    override fun pathTypeOther(): String =
        i18n("path_type_other")

    override fun pathDoesNotExist(pathType: String, path: String): String =
        i18n("path_does_not_exist")
            .nFormat(pathType, path)

    override fun pathIsFile(pathType: String, path: String): String =
        i18n("path_is_file")
            .nFormat(pathType, path)

    override fun pathIsDirectory(pathType: String, path: String): String =
        i18n("path_is_directory")
            .nFormat(pathType, path)

    override fun pathIsNotWritable(pathType: String, path: String): String =
        i18n("path_is_not_writable")
            .nFormat(pathType, path)

    override fun pathIsNotReadable(pathType: String, path: String): String =
        i18n("path_is_not_readable")
            .nFormat(pathType, path)

    override fun pathIsSymlink(pathType: String, path: String): String =
        i18n("path_is_symlink")
            .nFormat(pathType, path)

    override fun defaultMetavar(): String =
        i18n("default_meta_var")

    override fun stringMetavar(): String =
        i18n("string_meta_var")

    override fun usageTitle(): String =
        i18n("usage_title")

    override fun optionsTitle(): String =
        i18n("options_title")

    override fun argumentsTitle(): String =
        i18n("arguments_title")

    override fun commandsTitle(): String =
        i18n("commands_title")

    override fun optionsMetavar(): String =
        i18n("options_meta_var")

    override fun commandMetavar(): String =
        i18n("command_meta_var")

    override fun argumentsMetavar(): String =
        i18n("arguments_meta_var")

    override fun helpTagDefault(): String =
        i18n("help_tag_default")

    override fun helpTagRequired(): String =
        i18n("help_tag_required")

    override fun helpOptionMessage(): String =
        i18n("help_option_message")
}
