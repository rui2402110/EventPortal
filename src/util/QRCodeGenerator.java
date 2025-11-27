package util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.cert.X509Certificate;
import java.util.Base64;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * QRコード生成ユーティリティ（SSL検証無効版・Java 8互換）
 */
public class QRCodeGenerator {
    private static final String QR_API_URL = "https://api.qrserver.com/v1/create-qr-code/";
    private static final int DEFAULT_SIZE = 300;

    // SSL検証を無効化（開発環境のみ使用）
    static {
        disableSSLVerification();
    }

    /**
     * SSL証明書検証を無効化（開発環境専用）
     */
    private static void disableSSLVerification() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() { return null; }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) { }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) { }
                }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

            System.out.println("⚠️ SSL検証が無効化されました（開発環境のみ使用）");
        } catch (Exception e) {
            System.err.println("SSL検証無効化エラー: " + e.getMessage());
        }
    }

    /**
     * QRコードをBase64形式で生成
     */
    public static String generateQRCodeBase64(String data) throws Exception {
        return generateQRCodeBase64(data, DEFAULT_SIZE);
    }

    /**
     * QRコードをBase64形式で生成（サイズ指定可能）
     */
    public static String generateQRCodeBase64(String data, int size) throws Exception {
        String urlString = QR_API_URL + "?size=" + size + "x" + size + "&data=" +
                          java.net.URLEncoder.encode(data, "UTF-8");

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);

        try (InputStream in = conn.getInputStream();
             ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {

            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, dataBuffer.length)) != -1) {
                buffer.write(dataBuffer, 0, bytesRead);
            }

            byte[] imageBytes = buffer.toByteArray();
            String base64 = Base64.getEncoder().encodeToString(imageBytes);
            System.out.println("QRコード生成成功（Base64）: " + data);
            return base64;
        } finally {
            conn.disconnect();
        }
    }

    /**
     * QRコードを画像ファイルとして保存
     */
    public static String generateTicketQRCode(String data, String outputPath) throws Exception {
        return generateTicketQRCode(data, outputPath, DEFAULT_SIZE);
    }

    /**
     * QRコードを画像ファイルとして保存（サイズ指定可能）
     */
    public static String generateTicketQRCode(String data, String outputPath, int size) throws Exception {
        File directory = new File(outputPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String urlString = QR_API_URL + "?size=" + size + "x" + size + "&data=" +
                          java.net.URLEncoder.encode(data, "UTF-8");

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);

        String filename = data + ".png";
        String filepath = outputPath + File.separator + filename;

        try (BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
             FileOutputStream fileOutputStream = new FileOutputStream(filepath)) {

            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }

            System.out.println("QRコード画像保存成功: " + filepath);
        } finally {
            conn.disconnect();
        }

        return filepath;
    }

    /**
     * 保存されたQRコード画像をBase64形式で読み込み
     */
    public static String imageToBase64(String imagePath) throws Exception {
        byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));
        return Base64.getEncoder().encodeToString(imageBytes);
    }
}