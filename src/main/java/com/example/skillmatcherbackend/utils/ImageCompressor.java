package com.example.skillmatcherbackend.utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Random;

import javax.imageio.ImageIO;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.multipart.MultipartFile;

public class ImageCompressor {

    private static final int IMG_WIDTH = 300;
    private static final int IMG_HEIGHT = 150;


    public static String compressImage(final MultipartFile multipartFile) {
        try {
            BufferedImage originalImage = ImageIO.read(multipartFile.getInputStream());
            Image newResizedImage = originalImage
                    .getScaledInstance(IMG_WIDTH, IMG_HEIGHT, Image.SCALE_SMOOTH);
            String imageFormat = "png";
            String compressedFileName = multipartFile.getOriginalFilename() + "_compressed_" + new Random().nextInt(100000) + "." + imageFormat;
            File compressedImageFile = new File(compressedFileName);
            ImageIO.write(convertToBufferedImage(newResizedImage),
                    imageFormat, compressedImageFile);
            String convertedImageFileToBase64 =  convertImageFileToBase64(imageFormat, compressedImageFile);
            compressedImageFile.delete();
            return convertedImageFileToBase64;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage convertToBufferedImage(Image img) {

        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bi = new BufferedImage(
                img.getWidth(null), img.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics2D = bi.createGraphics();
        graphics2D.drawImage(img, 0, 0, null);
        graphics2D.dispose();

        return bi;
    }


    private static String convertImageFileToBase64(final String imageFormat, final File compressedImageFile) throws IOException {
        return "data:image/" + imageFormat + ";base64," + new String(Base64.encodeBase64(Files.readAllBytes(compressedImageFile.toPath())));
    }

}
