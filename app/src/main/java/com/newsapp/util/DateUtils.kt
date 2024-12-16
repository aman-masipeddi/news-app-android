import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

const val dateOutputFormat = "dd MMM yyyy, hh:mm a"
object DateUtils {

    /**
     * Formats a date string from one format to another.
     *
     * @param dateStr The input date string.
     * @param inputFormat The format of the input date string.
     * @param outputFormat The desired format of the output date string.
     * @return The formatted date string, or an error message if parsing fails.
     */
    fun formatDateISO(
        dateStr: String,
        outputFormat: String
    ): String {
        return try {
            val parsedDate = ZonedDateTime.parse(dateStr, DateTimeFormatter.ISO_DATE_TIME)

            // Format the date using the output format
            val outputFormatter = DateTimeFormatter.ofPattern(outputFormat)
            parsedDate.format(outputFormatter)
        } catch (e: Exception) {
            "Error: Unable to format date. ${e.message}"
        }
    }
}