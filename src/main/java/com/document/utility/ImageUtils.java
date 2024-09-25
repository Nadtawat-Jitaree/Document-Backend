package com.document.utility;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.util.Base64;

@Slf4j
@Component
public class ImageUtils {

    public String convertBlobToBase(Blob input){
        String base64 = "";
        try {
            byte[] bytes = input.getBytes(1, (int) input.length());
            byte[] encoded = Base64.getEncoder().encode(bytes);
            base64 = new String(encoded);
        }catch (Exception e){
           e.printStackTrace();
        }

        return "data:image/jpg;base64, " + base64;
    }

    public static Blob convertBase64ToBlob(String base64Image){
        Blob b = null;
        try {
            if(base64Image.indexOf(",") > 0){
                base64Image = base64Image.split(",")[1];
            }
            byte[] decodedByte = Base64.getDecoder().decode(base64Image);
            b = new SerialBlob(decodedByte);
        }catch (Exception e){
            System.out.println(e);
        }
        return b;
    }
    
    public static String convertBlobToStringbase64(Blob input){
        String base64 = "";
        try {
            byte[] bytes = input.getBytes(1, (int) input.length());
            byte[] encoded = Base64.getEncoder().encode(bytes);
            base64 = new String(encoded);
        }catch (Exception e){
           e.printStackTrace();
        }
        return base64;
    }

    public static byte[] convertBlobToByteArray(Blob input){
        byte[] bytes = new byte[0];
        try {
            bytes = input.getBytes(1, (int) input.length());
            return bytes;
        }catch (Exception e){
            e.printStackTrace();
        }
        return bytes;
    }

    public String convertBlobToBaseResizeForReport(Blob input, int width, int height){
        String base64 = "";
        try {

            byte[] bytes = input.getBytes(1, (int) input.length());

            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            BufferedImage img = ImageIO.read(in);
            if(height == 0) {
                height = (width * img.getHeight())/ img.getWidth();
            }
            if(width == 0) {
                width = (height * img.getWidth())/ img.getHeight();
            }
            Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            imageBuff.getGraphics().drawImage(scaledImage, 0, 0, new Color(0,0,0), null);

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            ImageIO.write(imageBuff, "jpg", buffer);

            byte[] bytesAfterResize = buffer.toByteArray();
            byte[] encoded = Base64.getEncoder().encode(bytesAfterResize);
            base64 = new String(encoded);
        }catch (Exception e){
            e.printStackTrace();
        }

        return base64;
    }

    public static byte[] mergePDF(byte[] pdfMain, byte[] pdfSub) throws DocumentException, IOException {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        Document document = null;
        PdfCopy writer = null;
        PdfReader reader;
        PdfImportedPage page;
        try {
            reader = new PdfReader(pdfMain);
            int numberOfPages = reader.getNumberOfPages();

            if (document == null) {
                document = new Document(reader.getPageSizeWithRotation(1));
                writer = new PdfCopy(document, outStream); // new
                document.open();
            }
            for (int i = 0; i < numberOfPages;) {
                ++i;
                page = writer.getImportedPage(reader, i);
                writer.addPage(page);
            }

            reader = new PdfReader(pdfSub);
            numberOfPages = reader.getNumberOfPages();

            for (int i = 0; i < numberOfPages;) {
                ++i;
                page = writer.getImportedPage(reader, i);
                writer.addPage(page);
            }
        }

        catch (Exception e) {
            e.printStackTrace();
        }


        document.close();
        outStream.close();
        return outStream.toByteArray();

    }
    
    private static String encodeFileToBase64Binary(String imgFile) throws Exception{
	    File file = new File(imgFile);
        FileInputStream fileInputStreamReader = new FileInputStream(file);
	    log.info("Convert File:" +imgFile );
        String base64 = "";
//        FileInputStream fileInputStreamReader = new FileInputStream(file);
        ByteArrayOutputStream bOutStream = new ByteArrayOutputStream();
        byte[] imgBuf = new byte[1024];
	    try {  
	    	
	    	for (int readNum; (readNum = fileInputStreamReader.read(imgBuf)) != -1;) {
	    		bOutStream.write(imgBuf, 0, readNum); 
//                logger.info("read " + readNum + " bytes,");		    		
	    	}
	    	byte[] bytes = bOutStream.toByteArray();
	    	byte[]  encoded = Base64.getEncoder().encode(bytes);
	    	base64 = new String(encoded,"UTF8");
            fileInputStreamReader.close();
	    	
	    	
	   }catch (IOException ex){
         ex.printStackTrace();
         log.error("Read image Fail");	         
       }
	    		    
        return "data:image/jpeg;base64," + base64;
    }
}
