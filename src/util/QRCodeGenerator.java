package util;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * QRコード生成ユーティリティクラス
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

        if (generateQRCode(testData, outputPath)) {
            System.out.println("テストQRコード生成成功");
            System.out.println("データ: " + testData);
            System.out.println("ファイル: " + outputPath);
        } else {
            System.out.println("テストQRコード生成失敗");
        }
    }
}