import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

const val dateOutputFormat = "dd MMM yyyy, hh:mm a"
object DateUtils {

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