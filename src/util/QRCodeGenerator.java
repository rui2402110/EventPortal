package util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * QRコード生成ユーティリティクラス（完全版）
 * ZXingライブラリを使用した実装
 */
public class QRCodeGenerator {

    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 300;

    /**
     * QRコード画像を生成してファイルに保存
     * @param text QRコードに埋め込むテキスト
     * @param filePath 保存先のファイルパス
     * @return 生成成功時はtrue
     */
    public static boolean generateQRCode(String text, String filePath) {
        return generateQRCode(text, filePath, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * QRコード画像を生成してファイルに保存（サイズ指定可能）
     * @param text QRコードに埋め込むテキスト
     * @param filePath 保存先のファイルパス
     * @param width 画像の幅
     * @param height 画像の高さ
     * @return 生成成功時はtrue
     */
    public static boolean generateQRCode(String text, String filePath, int width, int height) {
        try {
            // ファイルの親ディレクトリが存在しない場合は作成
            File outputFile = new File(filePath);
            File parentDir = outputFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            // QRコード生成のヒント設定
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            hints.put(EncodeHintType.MARGIN, 2);

            // QRコードライターを作成
            QRCodeWriter qrCodeWriter = new QRCodeWriter();

            // テキストをQRコードに変換
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);

            // 画像ファイルとして保存
            Path path = FileSystems.getDefault().getPath(filePath);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

            System.out.println("QRコード生成成功: " + filePath);
            return true;

        } catch (WriterException e) {
            System.err.println("QRコード生成エラー: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            System.err.println("ファイル書き込みエラー: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("予期しないエラー: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * QRコードをBase64文字列として生成
     * @param text QRコードに埋め込むテキスト
     * @return Base64エンコードされたQRコード画像
     */
    public static String generateQRCodeBase64(String text) {
        return generateQRCodeBase64(text, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * QRコードをBase64文字列として生成（サイズ指定可能）
     * @param text QRコードに埋め込むテキスト
     * @param width 画像の幅
     * @param height 画像の高さ
     * @return Base64エンコードされたQRコード画像
     */
    public static String generateQRCodeBase64(String text, int width, int height) {
        try {
            // QRコード生成のヒント設定
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            hints.put(EncodeHintType.MARGIN, 2);

            // QRコードライターを作成
            QRCodeWriter qrCodeWriter = new QRCodeWriter();

            // テキストをQRコードに変換
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);

            // BufferedImageに変換
            BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);

            // ByteArrayOutputStreamに書き込み
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "PNG", baos);
            byte[] imageBytes = baos.toByteArray();

            // Base64エンコード
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            System.out.println("QRコード(Base64)生成成功");
            return base64Image;

        } catch (WriterException e) {
            System.err.println("QRコード生成エラー: " + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            System.err.println("画像変換エラー: " + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            System.err.println("予期しないエラー: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * チケット用QRコードを生成
     * @param ticketId チケットID
     * @param outputDirectory 出力ディレクトリ
     * @return 生成された画像の相対パス
     */
    public static String generateTicketQRCode(String ticketId, String outputDirectory) {
        try {
            // ディレクトリが存在しない場合は作成
            File dir = new File(outputDirectory);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // ファイル名を生成
            String fileName = ticketId + ".png";
            String filePath = outputDirectory + File.separator + fileName;

            // QRコードを生成
            boolean success = generateQRCode(ticketId, filePath);

            if (success) {
                // 相対パスを返す（Webアプリケーションで使用するため）
                return fileName;
            } else {
                return null;
            }

        } catch (Exception e) {
            System.err.println("チケットQRコード生成エラー: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * QRコードデータの検証
     * @param data QRコードデータ
     * @return 有効な場合はtrue
     */
    public static boolean validateQRCodeData(String data) {
        if (data == null || data.trim().isEmpty()) {
            return false;
        }

        // データサイズの上限チェック（QRコードの容量制限）
        if (data.length() > 2953) { // QRコード Version 40 の最大容量
            return false;
        }

        return true;
    }

    /**
     * テスト用メインメソッド
     */
    public static void main(String[] args) {
        // テストデータ
        String testData = "TEST-" + System.currentTimeMillis();
        String outputPath = "./test_qrcode.png";

        // ファイル生成テスト
        if (generateQRCode(testData, outputPath)) {
            System.out.println("テストQRコード生成成功");
            System.out.println("データ: " + testData);
            System.out.println("ファイル: " + outputPath);
        } else {
            System.out.println("テストQRコード生成失敗");
        }

        // Base64生成テスト
        String base64 = generateQRCodeBase64(testData);
        if (base64 != null) {
            System.out.println("Base64生成成功 (長さ: " + base64.length() + ")");
        } else {
            System.out.println("Base64生成失敗");
        }

        // チケットQRコード生成テスト
        String ticketPath = generateTicketQRCode("TKT001", "./qrcodes");
        if (ticketPath != null) {
            System.out.println("チケットQRコード生成成功: " + ticketPath);
        } else {
            System.out.println("チケットQRコード生成失敗");
        }
    }
}