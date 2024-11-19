/* Refer the link below to learn more about the use cases of script.
https://help.sap.com/viewer/368c481cd6954bdfa5d0435479fd4eaf/Cloud/es-419/148851bf8192412cba1f9d2c17f4bd25.html

If you want to know more about the SCRIPT APIs, refer the link below
https://help.sap.com/doc/a56f52e1a58e4e2bac7f7adbf45b2e26/Cloud/es-419/index.html */
import com.itextpdf.html2pdf.HtmlConverter
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.net.URL
import java.nio.charset.StandardCharsets

def processData(message) {
    try {
        // Retrieve the URL from the message body (replace with the correct method if necessary)
        def url = message.getBody(String)
        print message

        if (!url.startsWith("http")) {
            throw new Exception("Invalid URL format. The input must be a valid URL.")
        }

        // Fetch HTML content from the URL
        def htmlContent = new URL(url).text

        // Convert the HTML string to an InputStream
        ByteArrayInputStream htmlStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8))

        // Prepare output stream for the PDF
        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream()

        // Convert the HTML content to PDF
        HtmlConverter.convertToPdf(htmlStream, pdfOutputStream)

        // Set the generated PDF as the message body
        message.setBody(pdfOutputStream.toByteArray())
        message.setHeader("Content-Type", "application/pdf")

        return message
    } catch (Exception e) {
        // Handle exceptions and log error messages
        message.setBody("Error during PDF conversion: ${e.message}")
        message.setHeader("Content-Type", "text/plain")
        return message
    }
}
