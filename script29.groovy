/* Refer the link below to learn more about the use cases of script.
https://help.sap.com/viewer/368c481cd6954bdfa5d0435479fd4eaf/Cloud/es-419/148851bf8192412cba1f9d2c17f4bd25.html

If you want to know more about the SCRIPT APIs, refer the link below
https://help.sap.com/doc/a56f52e1a58e4e2bac7f7adbf45b2e26/Cloud/es-419/index.html */
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import java.io.ByteArrayOutputStream
import java.net.URL

def processData(message) {
    try {
        // Retrieve the URL from the message body
        def url = message.getBody(String)
        
        if (!url.startsWith("http")) {
            throw new Exception("Invalid URL format. The input must be a valid URL.")
        }

        // Fetch HTML content from the URL
        def htmlContent = new URL(url).text

        // Prepare output stream for the PDF
        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream()
        
        // Initialize PdfWriter and PdfDocument without any complex layout settings
        PdfWriter writer = new PdfWriter(pdfOutputStream)
        PdfDocument pdfDoc = new PdfDocument(writer)
        Document document = new Document(pdfDoc)

        // Add the HTML content as plain text in a Paragraph (not rendered as HTML)
        document.add(new Paragraph(htmlContent))

        // Close the document
        document.close()

        // Set the generated PDF as the message body
        message.setBody(pdfOutputStream.toByteArray())
        message.setHeader("Content-Type", "application/pdf")

        return message
    } catch (Exception e) {
        message.setBody("Error during PDF generation: ${e.message}")
        message.setHeader("Content-Type", "text/plain")
        return message
    }
}
