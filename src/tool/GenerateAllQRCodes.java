package tool;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Base64;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * 既存チケットのQRコード一括生成ツール
 */
public class GenerateAllQRCodes {

    public static void main(String[] args) {
        String url = "jdbc:h2:tcp://localhost/~/eventportal";
        String user = "sa";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {

            System.out.println("データベースに接続しました");

            // QRコードが未生成のチケットを取得
            String selectSQL = "SELECT ticket_id FROM TICKET WHERE qr_image_data IS NULL OR qr_image_data = ''";
            PreparedStatement selectStmt = conn.prepareStatement(selectSQL);
            ResultSet rs = selectStmt.executeQuery();

            int count = 0;
            QRCodeWriter qrCodeWriter = new QRCodeWriter();

            while (rs.next()) {
                String ticketId = rs.getString("ticket_id");
                System.out.println("処理中: " + ticketId);

                try {
                    // QRコード生成
                    BitMatrix bitMatrix = qrCodeWriter.encode(ticketId, BarcodeFormat.QR_CODE, 300, 300);
                    BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

                    // Base64エンコード
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(bufferedImage, "PNG", baos);
                    byte[] imageBytes = baos.toByteArray();
                    String base64Image = Base64.getEncoder().encodeToString(imageBytes);

                    // データベース更新
                    String updateSQL = "UPDATE TICKET SET qr_image_data = ? WHERE ticket_id = ?";
                    PreparedStatement updateStmt = conn.prepareStatement(updateSQL);
                    updateStmt.setString(1, base64Image);
                    updateStmt.setString(2, ticketId);
                    updateStmt.executeUpdate();
                    updateStmt.close();

                    count++;
                    System.out.println("  ✓ QRコード生成完了");

                } catch (Exception e) {
                    System.err.println("  ✗ エラー: " + e.getMessage());
                }
            }

            rs.close();
            selectStmt.close();

            System.out.println("\n=== 完了 ===");
            System.out.println("生成したQRコード数: " + count);

        } catch (Exception e) {
            System.err.println("エラー: " + e.getMessage());
            e.printStackTrace();
        }
    }
}