package util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Base64;

import javax.imageio.ImageIO;

/**
 * QRコード生成ユーティリティ（外部ライブラリ不要版）
 * 外部API（api.qrserver.com）を使用してQRコードを生成
 */
public class QRCodeGenerator {

    // QRコード生成API（無料・認証不要）
    private static final String QR_API_URL = "https://api.qrserver.com/v1/create-qr-code/";
    private static final int DEFAULT_SIZE = 300;

    /**
     * QRコードをファイルに保存
     *
     * @param data QRコードに埋め込むデータ
     * @param fileName ファイル名
     * @param outputPath 出力先パス
     * @return 生成されたファイル名
     * @throws Exception 生成失敗時
     */
    public static String generateQRCode(String data, String fileName, String outputPath) throws Exception {
        System.out.println("=== QRコード生成開始 ===");
        System.out.println("データ: " + data);
        System.out.println("ファイル名: " + fileName);
        System.out.println("出力パス: " + outputPath);

        try {
            // 出力ディレクトリが存在しない場合は作成
            File dir = new File(outputPath);
            if (!dir.exists()) {
                boolean created = dir.mkdirs();
                System.out.println("ディレクトリ作成: " + created);
                if (!created) {
                    throw new Exception("ディレクトリの作成に失敗: " + outputPath);
                }
            } else {
                System.out.println("ディレクトリ存在確認: OK");
            }

            // 書き込み権限チェック
            if (!dir.canWrite()) {
                throw new Exception("ディレクトリに書き込み権限がありません: " + outputPath);
            }

            // QRコード画像を取得
            BufferedImage qrImage = fetchQRCodeImage(data, DEFAULT_SIZE);

            // ファイルに保存
            File outputFile = new File(outputPath, fileName);
            ImageIO.write(qrImage, "PNG", outputFile);

            // 保存確認
            if (outputFile.exists()) {
                System.out.println("QRコード生成成功: " + outputFile.getAbsolutePath());
                System.out.println("ファイルサイズ: " + outputFile.length() + " bytes");
            } else {
                throw new Exception("ファイル保存に失敗しました");
            }

            System.out.println("=== QRコード生成完了 ===");
            return fileName;

        } catch (Exception e) {
            System.err.println("QRコード生成エラー: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * チケット用QRコードを生成（タイムスタンプ付きファイル名）
     *
     * @param ticketId チケットID
     * @param outputPath 出力先パス
     * @return 生成されたファイル名
     * @throws Exception 生成失敗時
     */
    public static String generateTicketQRCode(String ticketId, String outputPath) throws Exception {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String fileName = ticketId + "_" + timestamp + ".png";
        return generateQRCode(ticketId, fileName, outputPath);
    }

    /**
     * QRコードをBase64文字列で生成（データベース保存用）
     *
     * @param data QRコードに埋め込むデータ
     * @return Base64エンコードされた画像データ
     * @throws Exception 生成失敗時
     */
    public static String generateQRCodeBase64(String data) throws Exception {
        System.out.println("=== QRコード(Base64)生成開始 ===");
        System.out.println("データ: " + data);

        try {
            // QRコード画像を取得
            BufferedImage qrImage = fetchQRCodeImage(data, DEFAULT_SIZE);

            // Base64に変換
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(qrImage, "PNG", baos);
            byte[] imageBytes = baos.toByteArray();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            System.out.println("Base64データサイズ: " + base64Image.length() + " 文字");
            System.out.println("=== QRコード(Base64)生成完了 ===");

            return base64Image;

        } catch (Exception e) {
            System.err.println("QRコード(Base64)生成エラー: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * WebアプリケーションのコンテキストからQRコード保存パスを取得
     *
     * @param servletContext ServletContext
     * @return QRコード保存先の絶対パス
     */
    public static String getQRCodePath(javax.servlet.ServletContext servletContext) {
        String realPath = servletContext.getRealPath("/qr");
        System.out.println("QRコード保存パス: " + realPath);
        return realPath;
    }

    /**
     * 外部APIを使用してQRコード画像を取得
     *
     * @param data QRコードに埋め込むデータ
     * @param size QRコードのサイズ（ピクセル）
     * @return QRコード画像
     * @throws Exception 取得失敗時
     */
    private static BufferedImage fetchQRCodeImage(String data, int size) throws Exception {
        // URLエンコード
        String encodedData = URLEncoder.encode(data, "UTF-8");

        // API URL構築
        String apiUrl = QR_API_URL + "?size=" + size + "x" + size + "&data=" + encodedData;

        System.out.println("API呼び出し: " + apiUrl);

        // HTTP接続
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(10000); // 10秒
        connection.setReadTimeout(10000);

        try {
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 画像データを読み込み
                InputStream inputStream = connection.getInputStream();
                BufferedImage image = ImageIO.read(inputStream);
                inputStream.close();

                if (image == null) {
                    throw new Exception("画像データの読み込みに失敗しました");
                }

                System.out.println("API呼び出し成功: " + image.getWidth() + "x" + image.getHeight());
                return image;

            } else {
                throw new Exception("API呼び出し失敗: HTTP " + responseCode);
            }

        } finally {
            connection.disconnect();
        }
    }
}