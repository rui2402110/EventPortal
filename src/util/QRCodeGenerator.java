package util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QRCodeGenerator {

    private static final int DEFAULT_WIDTH = 350;
    private static final int DEFAULT_HEIGHT = 350;

    /**
     * チケット用QRコードをBase64とファイルの両方で生成
     * @param ticketId チケットID
     * @param outputDirectory 出力ディレクトリ（nullの場合はファイル保存なし）
     * @return Map with keys: "base64" and "filePath"
     */
    public static Map<String, String> generateTicketQRCodeComplete(String ticketId, String outputDirectory) {
        Map<String, String> result = new HashMap<>();

        try {
            // QRコード生成のヒント設定
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H); // 高い誤り訂正レベル
            hints.put(EncodeHintType.MARGIN, 1);

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(
                ticketId,
                BarcodeFormat.QR_CODE,
                DEFAULT_WIDTH,
                DEFAULT_HEIGHT,
                hints
            );

            BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);

            // Base64エンコード
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "PNG", baos);
            byte[] imageBytes = baos.toByteArray();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            result.put("base64", base64Image);

            // ファイル保存（ディレクトリが指定されている場合）
            if (outputDirectory != null && !outputDirectory.isEmpty()) {
                File dir = new File(outputDirectory);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                String fileName = ticketId + ".png";
                String filePath = outputDirectory + File.separator + fileName;
                Path path = FileSystems.getDefault().getPath(filePath);
                MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
                result.put("filePath", "/qr/" + fileName);

                System.out.println("QRコード生成成功: " + filePath);
            }

            return result;

        } catch (Exception e) {
            System.err.println("QRコード生成エラー: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * QRコードをBase64文字列として生成（メインメソッド）
     */
    public static String generateQRCodeBase64(String text) {
        return generateQRCodeBase64(text, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public static String generateQRCodeBase64(String text, int width, int height) {
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.MARGIN, 1);

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);
            BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "PNG", baos);
            byte[] imageBytes = baos.toByteArray();

            return Base64.getEncoder().encodeToString(imageBytes);

        } catch (Exception e) {
            System.err.println("QRコード生成エラー: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * ファイルとして保存
     */
    public static boolean generateQRCode(String text, String filePath) {
        return generateQRCode(text, filePath, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public static boolean generateQRCode(String text, String filePath, int width, int height) {
        try {
            File outputFile = new File(filePath);
            File parentDir = outputFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.MARGIN, 1);

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);
            Path path = FileSystems.getDefault().getPath(filePath);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

            System.out.println("QRコード生成成功: " + filePath);
            return true;

        } catch (Exception e) {
            System.err.println("QRコード生成エラー: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}