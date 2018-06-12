package com.smartshelf.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Hashtable;
import java.util.Map;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

public class QRCodeManager {

	private final static Logger log = LoggerFactory.getLogger(QRCodeManager.class);
	
	public static String decodeQRCode(File qrCodeImage) throws IOException {
		
		log.info("Image size: " + qrCodeImage.length());
		/*
		BufferedImage bufferedImage = ImageIO.read(qrCodeImage);
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        File outputfile = new File("saved.png");
        ImageIO.write(bufferedImage, "png", outputfile);
        
        
        try {
            Result result = new MultiFormatReader().decode(bitmap);
            log.info("Successfully decoded QRCode image. Resulttext: " + result.getText());
            return result.getText();
        } catch (NotFoundException e) {
            log.error("No QRCode recognized in image");    
        }*/
		
	    BufferedImage image = null;
	    BinaryBitmap bitmap = null;
	    Result result = null;

	    try {
	        image = ImageIO.read(qrCodeImage);
	        int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
	        RGBLuminanceSource source = new RGBLuminanceSource(image.getWidth(), image.getHeight(), pixels);
	        bitmap = new BinaryBitmap(new HybridBinarizer(source));
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    if (bitmap == null) {
	        return null;
	    }

	    QRCodeReader reader = new QRCodeReader();

        Map<DecodeHintType, Object> hintMap = new Hashtable<DecodeHintType, Object>();
        hintMap.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        hintMap.put(DecodeHintType.POSSIBLE_FORMATS, EnumSet.allOf(BarcodeFormat.class));
        try {
			result = reader.decode(bitmap, hintMap);
		} catch (NotFoundException | ChecksumException | FormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		    
        return null;
	}
	
	public static File convert(MultipartFile file) throws IOException {
		
	    File convFile = new File(file.getOriginalFilename());
	    convFile.createNewFile();
	    FileOutputStream fos = new FileOutputStream(convFile);
	    fos.write(file.getBytes());
	    fos.close();
	    return convFile;
	}
}
