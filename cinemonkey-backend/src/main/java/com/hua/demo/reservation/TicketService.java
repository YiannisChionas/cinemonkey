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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class TicketService {

    private static final Logger log = LoggerFactory.getLogger(TicketService.class);

    // Παίρνει από ENV LOGO_URL, αλλιώς default στο νέο PNG
    @Value("${LOGO_URL:https://cinemonkey.com/media/LOGO_CLEAN.png}")
    private String logoUrl;

    private final HttpClient http = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(3))
            .build();

    public ByteArrayInputStream generateTicket(Reservation reservation) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            // 1) Φέρε logo από URL (χωρίς fallback στο classpath)
            byte[] logoBytes = fetchLogoBytesOrThrow();
            PDImageXObject logo = PDImageXObject.createFromByteArray(document, logoBytes, "logo");

            // 2) QR in-memory
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

                // Footer notice
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

    private byte[] fetchLogoBytesOrThrow() throws Exception {
        // μικρό cache-bust για να μη “μείνει” proxy/browser cache
        String busted = logoUrl.contains("?")
                ? logoUrl + "&v=" + System.currentTimeMillis()
                : logoUrl + "?v=" + System.currentTimeMillis();

        log.info("Loading logo from URL: {}", logoUrl);

        HttpRequest req = HttpRequest.newBuilder(URI.create(busted))
                .timeout(Duration.ofSeconds(5))
                .header("Cache-Control", "no-cache")
                .GET()
                .build();

        HttpResponse<byte[]> resp = http.send(req, HttpResponse.BodyHandlers.ofByteArray());
        if (resp.statusCode() != 200 || resp.body() == null || resp.body().length == 0) {
            throw new IllegalStateException("Failed to fetch logo from " + logoUrl + " (status " + resp.statusCode() + ")");
        }
        return resp.body();
    }
}
