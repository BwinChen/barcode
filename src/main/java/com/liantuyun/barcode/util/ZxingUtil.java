package com.liantuyun.barcode.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Hashtable;
import java.util.Map;

/**
 * @see <a href="https://www.cnblogs.com/manusas/p/6801436.html">ZXing二维码和条形码</a>
 */
public class ZxingUtil {

    /**
     * 生成二维码
     *
     * @param contents
     * @param width
     * @param height
     * @param imgPath
     */
    public void encodeQRCode(String contents, int width, int height, String imgPath) {
        Map<EncodeHintType, Object> hints = new Hashtable<>();
        // 指定纠错等级
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        // 指定编码格式
        hints.put(EncodeHintType.CHARACTER_SET, "GBK");
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(contents,
                    BarcodeFormat.QR_CODE, width, height, hints);

            MatrixToImageWriter.writeToStream(bitMatrix, "png",
                    new FileOutputStream(imgPath));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析二维码
     *
     * @param imgPath
     * @return
     */
    public String decodeQRCode(String imgPath) {
        BufferedImage image = null;
        Result result = null;
        try {
            image = ImageIO.read(new File(imgPath));
            if (image == null) {
                System.out.println("the decode image may be not exit.");
            }
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            Map<DecodeHintType, Object> hints = new Hashtable<>();
            hints.put(DecodeHintType.CHARACTER_SET, "GBK");

            result = new MultiFormatReader().decode(bitmap, hints);
            return result.getText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成条形码
     *
     * @param contents
     * @param width
     * @param height
     * @param imgPath
     */
    // int width = 105, height = 50; 长度很容易报错:NotFoundException
    public void encodeBarCode(String contents, int width, int height, String imgPath) {
        int codeWidth = 3 + // start guard
                (7 * 6) + // left bars
                5 + // middle guard
                (7 * 6) + // right bars
                3; // end guard
        codeWidth = Math.max(codeWidth, width);
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, BarcodeFormat.EAN_13, codeWidth, height, null);
            MatrixToImageWriter.writeToStream(bitMatrix, "png", new FileOutputStream(imgPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析条形码
     *
     * @param imgPath
     * @return
     */
    public String decodeBarCode(String imgPath) {
        BufferedImage image = null;
        Result result = null;
        try {
            image = ImageIO.read(new File(imgPath));
            if (image == null) {
                System.out.println("the decode image may be not exit.");
            }
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
//            Map<DecodeHintType, Object> hints = new Hashtable<>();
//            hints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
//            hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
//            result = new MultiFormatReader().decode(bitmap, hints);
            result = new MultiFormatReader().decode(bitmap, null);
            return result.getText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        String imgPath = "barcode.png";
        // 益达无糖口香糖的条形码
        String contents = "6923450657713";
        int width = 105, height = 50;
        ZxingUtil handler = new ZxingUtil();

        handler.encodeBarCode(contents, width, height, imgPath);
        String barcode = handler.decodeBarCode(imgPath);
        System.out.println(barcode);

        handler.encodeQRCode("abc123中文@#\\", 200, 200, imgPath);
        String qrcode = handler.decodeQRCode(imgPath);
        System.out.println(qrcode);
    }

}