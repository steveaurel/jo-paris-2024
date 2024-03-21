package com.infoevent.ticketservice.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
public class QRCodeUtils {

    /**
     * Generates a QR code image as a byte array from a given text.
     *
     * @param text The text to encode in the QR code.
     * @param width The width of the QR code image.
     * @param height The height of the QR code image.
     * @return A byte array representing the QR code image.
     * @throws WriterException If an error occurs during the writing process.
     * @throws IOException If an error occurs during output stream handling.
     */
    public static byte[] generateQRCodeImage(String text, int width, int height) throws WriterException, IOException {
        log.info("Generating QR Code for text: {}", text);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] qrCodeImage = pngOutputStream.toByteArray();
        log.info("QR Code generated successfully");
        return qrCodeImage;
    }

    /**
     * Decodes a QR code image back to a string.
     *
     * @param qrCodeImage The QR code image as a byte array.
     * @return The text encoded in the QR code image.
     * @throws IOException If an error occurs during input stream handling or image reading.
     * @throws NotFoundException If the QR code cannot be decoded.
     */
    public static String decodeQRCode(byte[] qrCodeImage) throws IOException, NotFoundException {
        log.info("Decoding QR Code image");
        ByteArrayInputStream inputStream = new ByteArrayInputStream(qrCodeImage);
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        try {
            Result result = new MultiFormatReader().decode(bitmap);
            String decodedText = result.getText();
            log.info("QR Code decoded successfully");
            return decodedText;
        } catch (NotFoundException e) {
            log.error("Error decoding QR Code: {}", e.getMessage());
            throw e;
        }
    }
}
