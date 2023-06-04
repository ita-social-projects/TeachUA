package com.softserve.certificate.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.Hashtable;
import java.util.Map;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class QRCodeService {
    private static final Integer WIDTH = 200;
    private static final Integer HEIGHT = 200;
    private static final Integer ENCODELEVEL = 3;
    private static final String ENCODING = "utf-8";
    @Value("${application.baseURL}")
    private String baseUrl;

    /**
     * Return a Qr Code Image than can be used on the certificate.
     *
     * @param content        content to be put into image
     * @param width          width of QR code
     * @param height         height of QR code
     * @param hints          settings of the QR code
     * @param qrColorsConfig colors configuration for QR code image
     *                       if null, the method will use a black-and-white style
     * @return returns a generated QR code image as {@code BufferedImage}
     */
    private BufferedImage getQrCodeImage(String content, int width, int height, Map<EncodeHintType, Serializable> hints,
                                         MatrixToImageConfig qrColorsConfig) {
        QRCodeWriter writer = new QRCodeWriter();
        if (hints == null) {
            hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, ENCODING);
        }
        BitMatrix matrix = null;

        try {
            matrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        } catch (WriterException e) {
            log.debug("Error occured while making image");
        }

        if (qrColorsConfig != null) {
            return MatrixToImageWriter.toBufferedImage(matrix, qrColorsConfig);
        } else {
            return MatrixToImageWriter.toBufferedImage(matrix);
        }
    }

    private byte[] getQrCodeImageBytes(String content, int width, int height, MatrixToImageConfig qrColorsConfig) {
        Map<EncodeHintType, Serializable> hints = new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.forBits(ENCODELEVEL));
        hints.put(EncodeHintType.CHARACTER_SET, ENCODING);
        BufferedImage qrCodeImage = getQrCodeImage(content, width, height, hints, qrColorsConfig);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            ImageIO.write(qrCodeImage, "png", out);
        } catch (IOException e) {
            log.debug("Error while writing code to outputStream");
        }

        return out.toByteArray();
    }

    private String formContentUrl(Long serialNumber) {
        return baseUrl + "/certificate/" + serialNumber;
    }

    public ByteArrayInputStream getCertificateQrCodeAsStream(Long serialNumber) {
        return new ByteArrayInputStream(
                getQrCodeImageBytes(formContentUrl(serialNumber), WIDTH, HEIGHT, getColorConfig(serialNumber)));
    }

    public byte[] getCertificateQrCodeAsStream(Long serialNumber, float width, float height,
                                               MatrixToImageConfig colorConfig) {
        return getQrCodeImageBytes(formContentUrl(serialNumber), (int) width, (int) height, colorConfig);
    }

    // @formatter:off
    private MatrixToImageConfig getColorConfig(Long serialNumber) {
        return switch (Long.toString(serialNumber).charAt(0)) {
          case '1', '2', '4' -> new MatrixToImageConfig(
                  new Color(255, 255, 255).getRGB(),
                  new Color(0, 0, 0, 0).getRGB());
          default -> new MatrixToImageConfig(
                  new Color(0, 0, 0).getRGB(),
                  new Color(0, 0, 0, 0).getRGB());
        };
    }
    // @formatter:on
}
