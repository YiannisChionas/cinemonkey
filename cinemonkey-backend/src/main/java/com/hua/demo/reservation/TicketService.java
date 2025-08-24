package com.hua.demo.reservation;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class TicketService {

    public ByteArrayInputStream generateTicket(Reservation reservation) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            // Load circular logo with transparency
            InputStream logoInput = getClass().getClassLoader().getResourceAsStream("static/posters/LOGO.jpeg");
            PDImageXObject logo = PDImageXObject.createFromByteArray(document, logoInput.readAllBytes(), "logo");

            // Generate QR code
            BitMatrix bitMatrix = new MultiFormatWriter()
                    .encode("Reservation ID: " + reservation.getId(), BarcodeFormat.QR_CODE, 100, 100);
            Path tempQrPath = Files.createTempFile("qr_", ".png");
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", tempQrPath);
            PDImageXObject qrCodeImage = PDImageXObject.createFromFile(tempQrPath.toString(), document);

            try (PDPageContentStream cs = new PDPageContentStream(document, page)) {

                // Header bar
                cs.setNonStrokingColor(30, 30, 30);
                cs.addRect(0, 730, 650, 60);
                cs.fill();

                // Logo (top-right)
                cs.setNonStrokingColor(0, 0, 0);
                cs.fill();
                cs.drawImage(logo, 500, 735, 50, 50);

                // Header text
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
                cs.setNonStrokingColor(255, 255, 255);
                cs.newLineAtOffset(30, 755);
                cs.showText("You need to scan the ticket in order to enter. Have fun!");
                cs.endText();

                // Title
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 22);
                cs.setNonStrokingColor(0, 0, 0);
                cs.newLineAtOffset(220, 700);
                cs.showText("Cinema Ticket");
                cs.endText();

                // Ticket details
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
                cs.newLineAtOffset(50, 650);
                cs.showText("Cinema: Cine-Monkey");
                cs.newLineAtOffset(0, -20);
                cs.showText("Seats: NaN");
                cs.newLineAtOffset(0, -20);
                cs.showText("Movie: " + reservation.getReservedShowing().getShowingMovie().getTitle());
                cs.newLineAtOffset(0, -20);
                cs.showText("Date: " + reservation.getReservedShowing().getShowingDate());
                cs.newLineAtOffset(0, -20);
                cs.showText("User Email: " + reservation.getUserEmail());
                cs.newLineAtOffset(0, -20);
                cs.showText("User ID: " + reservation.getUserSub());
                cs.newLineAtOffset(0, -20);
                cs.showText("Booking ID: " + reservation.getId());
                cs.endText();

                // QR Code on the right
                cs.drawImage(qrCodeImage, 400, 580, 100, 100);

                // Footer notice (near bottom)
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_OBLIQUE, 10);
                cs.newLineAtOffset(50, 100);
                cs.showText("This ticket must be presented in print or electronic form before entering the hall.");
                cs.endText();
            }

            document.save(out);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
