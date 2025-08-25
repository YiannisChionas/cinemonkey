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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.util.Objects;

@Service
public class TicketService {

    // Θα διαβάσει από ENV LOGO_URL. Αν δεν υπάρχει, χρησιμοποιεί το default.
    @Value("${LOGO_URL:https://cinemonkey.com/media/LOGO.jpeg}")
    private String logoUrl;

    public ByteArrayInputStream generateTicket(Reservation reservation) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            // 1) Φόρτωσε logo (με retry + fallback σε classpath)
            byte[] logoBytes = fetchLogoBytesWithFallback();
            PDImageXObject logo = PDImageXObject.createFromByteArray(document, logoBytes, "logo");

            // 2) Δημιούργησε QR in-memory (PNG)
            BitMatrix bitMatrix = new MultiFormatWriter()
                    .encode("Reservation ID: " + reservation.getId(), BarcodeFormat.QR_CODE, 100, 100);
            ByteArrayOutputStream qrOut = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", qrOut);
            PDImageXObject qrCodeImage = PDImageXObject.createFromByteArray(document, qrOut.toByteArray(), "qrcode");

            // 3) Ζωγράφισε PDF
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

    private byte[] fetchLogoBytesWithFallback() throws Exception {
        // Προσπάθησε από URL με λίγα retries (σε περίπτωση που nginx/minio αργήσουν)
        byte[] bytes = fetchWithRetry(logoUrl, 5, Duration.ofSeconds(1));
        if (bytes != null && bytes.length > 0) return bytes;

        // Fallback: classpath
        try (InputStream is = getClass().getResourceAsStream("/static/posters/LOGO.jpeg")) {
            if (is != null) {
                return is.readAllBytes();
            }
        }

        throw new IllegalStateException(
                "Logo not found at " + logoUrl + " and no classpath fallback at /static/posters/LOGO.jpeg");
    }

    private byte[] fetchWithRetry(String url, int attempts, Duration backoff) {
        Objects.requireNonNull(url, "logo url is null");
        for (int i = 0; i < attempts; i++) {
            try (InputStream is = URI.create(url).toURL().openStream()) {
                return is.readAllBytes();
            } catch (Exception e) {
                try {
                    Thread.sleep(backoff.toMillis() * (i + 1));
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return null;
                }
            }
        }
        return null;
    }
}
