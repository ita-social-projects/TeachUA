package com.softserve.edu.utils;

import org.monte.media.Format;
import org.monte.media.Registry;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.monte.media.FormatKeys.*;
import static org.monte.media.VideoFormatKeys.*;

public class VideoRecorderUtility extends ScreenRecorder {

    private static final String TIME_TEMPLATE = "yyyy-MM-dd_HH-mm-ss-S";

    private static ScreenRecorder screenRecorder;
    private final String name;
    private static File file;

    // Constructor
    public VideoRecorderUtility(GraphicsConfiguration cfg, Rectangle captureArea, Format fileFormat,
                        Format screenFormat, Format mouseFormat, Format audioFormat, File movieFolder, String name)
                            throws IOException, AWTException {
        super(cfg, captureArea, fileFormat, screenFormat, mouseFormat, audioFormat, movieFolder);
        this.name = name;
    }

    @Override
    protected File createMovieFile(Format fileFormat) throws IOException {
        // Check if movieFolder directory exists at all
        if(!movieFolder.exists()) {
            // Create new directory if it does not exist
            movieFolder.mkdirs();
            // Check if specified movieFolder path is a directory or not
        } else if (!movieFolder.isDirectory()) {
            throw new IOException("\"" + movieFolder + "\" is not a directory.");
        }
        // Define date, time format
        SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_TEMPLATE);
        // Return file with name and locate it in specified directory movieFolder
        return new File(movieFolder,
                    name + "-" + dateFormat.format(new Date()) + "." + Registry.getInstance().getExtension(fileFormat));
    }

    public static void startRecording(String methodName) {
        try {
            // Path where video recording will be stored inside project
            file = new File("./video/");
            // Get screen size
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            // Get screen width
            int width = screenSize.width;
            // Get screen height
            int height = screenSize.height - 50;

            Rectangle captureSize = new Rectangle(0, 0, width, height);

            GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice()
                    .getDefaultConfiguration();

            // Initialize screenRecorder instance
            screenRecorder = new VideoRecorderUtility(gc, captureSize,
                    new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI),
                    new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                            CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE, DepthKey, 24, FrameRateKey,
                            Rational.valueOf(15), QualityKey, 1.0f, KeyFrameIntervalKey, 15 * 60),
                    new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black", FrameRateKey, Rational.valueOf(30)),
                    null, file, methodName);

            // Start recording
            screenRecorder.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopRecording(boolean keepFile) {
        try {
            // Stop screen recording
            screenRecorder.stop();
            if(!keepFile) {
                // Delete last recording if keepFile parameter is false
                deleteRecording();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void deleteRecording() {
        try {
            if(file.exists()) {
                // Delete recording file is such exists
                file.delete();
                // Assign null to file variable
                file = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
