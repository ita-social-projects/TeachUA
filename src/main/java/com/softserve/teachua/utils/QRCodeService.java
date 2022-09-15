package com.softserve.teachua.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;

@Slf4j
@Component
public class QRCodeService {
    private static final Integer WIDTH = 200;
    private static final Integer HEIGHT = 200;
    private static final Integer ENCODELEVEL = 3;
    private static final String ENCODING = "utf-8";
    @Value("${baseURL}")
    private String BASE_URL;

    /**
     *
     * @param content
     *            content to be put into image
     * @param width
     *            width of QR code
     * @param height
     *            height of QR code
     * @param hints
     *            settings of the QR code
     *
     * @return returns a generated QR code image as {@code BufferedImage}
     */
    private BufferedImage getQrCodeImage(String content, int width, int height, Hashtable hints) {
        QRCodeWriter writer = new QRCodeWriter();
        if (hints == null) {
            hints = new Hashtable();
            hints.put(EncodeHintType.CHARACTER_SET, ENCODING);
        }
        BitMatrix matrix = null;

        try {
            matrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        } catch (WriterException e) {
            log.debug("Error occured while making image");
        }

        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(matrix);

        return bufferedImage;
    }

    private byte[] getQrCodeImageBytes(String content, int width, int height) {
        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.forBits(ENCODELEVEL));
        hints.put(EncodeHintType.CHARACTER_SET, ENCODING);
        BufferedImage zoomImage = getQrCodeImage(content, width, height, hints);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            ImageIO.write(zoomImage, "png", out);
        } catch (IOException e) {
            log.debug("Error while writing code to outputStream");
        }

        byte[] binaryData = out.toByteArray();

        return binaryData;
    }

    private String formContentUrl(Long serialNumber) {
        return BASE_URL + "/certificate/" + serialNumber;
    }

    public ByteArrayInputStream getQrCodeAsStream(Long serialNumber) {
        return new ByteArrayInputStream(getQrCodeImageBytes(formContentUrl(serialNumber), WIDTH, HEIGHT));
    }
}
