package test;

import java.io.File;

import util.QRCodeGenerator;

/**
 * QRCodeGenerator の動作確認用テストクラス
 * このクラスのみで完結するテスト
 */
public class QRCodeGeneratorTest {
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("QRCodeGenerator 単独動作テスト");
        System.out.println("==========================================\n");

        // テスト1: ファイル保存テスト
        testFileSave();

        System.out.println("\n==========================================");

        // テスト2: Base64生成テスト
        testBase64Generation();

        System.out.println("\n==========================================");
        System.out.println("すべてのテスト完了");
        System.out.println("==========================================");
    }

    /**
     * テスト1: ファイル保存テスト
     */
    private static void testFileSave() {
        System.out.println("【テスト1】QRコード画像ファイル生成");

        try {
            // デスクトップに保存
            String testData = "TEST_TICKET_001";
            String fileName = "qr_test_001.png";
            String outputPath = System.getProperty("user.home") + File.separator + "Desktop";

            System.out.println("  データ: " + testData);
            System.out.println("  保存先: " + outputPath);
            System.out.println("  ファイル名: " + fileName);
            System.out.println("\n  生成中...");

            String result = QRCodeGenerator.generateQRCode(testData, fileName, outputPath);

            // 生成確認
            File savedFile = new File(outputPath, fileName);
            if (savedFile.exists()) {
                System.out.println("\n✓ テスト1: 成功");
                System.out.println("  → デスクトップに " + result + " が保存されました");
                System.out.println("  → ファイルサイズ: " + savedFile.length() + " bytes");
                System.out.println("  → スマホのカメラでスキャンして確認してください");
            } else {
                System.err.println("\n✗ テスト1: 失敗（ファイルが見つかりません）");
            }

        } catch (Exception e) {
            System.err.println("\n✗ テスト1: エラー発生");
            e.printStackTrace();
        }
    }

    /**
     * テスト2: Base64生成テスト
     */
    private static void testBase64Generation() {
        System.out.println("【テスト2】Base64形式のQRコード生成");

        try {
            String testData = "TEST_TICKET_002";

            System.out.println("  データ: " + testData);
            System.out.println("\n  生成中...");

            String base64Result = QRCodeGenerator.generateQRCodeBase64(testData);

            if (base64Result != null && base64Result.length() > 0) {
                System.out.println("\n✓ テスト2: 成功");
                System.out.println("  → Base64データサイズ: " + base64Result.length() + " 文字");
                System.out.println("  → 最初の50文字: " + base64Result.substring(0, Math.min(50, base64Result.length())) + "...");

                // HTMLファイルとして保存してブラウザで確認できるようにする
                saveAsHtml(base64Result, testData);

            } else {
                System.err.println("\n✗ テスト2: 失敗（Base64データが空です）");
            }

        } catch (Exception e) {
            System.err.println("\n✗ テスト2: エラー発生");
            e.printStackTrace();
        }
    }

    /**
     * Base64データをHTMLファイルとして保存
     */
    private static void saveAsHtml(String base64Data, String ticketId) {
        try {
            String outputPath = System.getProperty("user.home") + File.separator + "Desktop";
            String htmlFileName = "qr_test_002.html";

            String htmlContent = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "  <title>QRコードテスト</title>\n" +
                "  <style>\n" +
                "    body { text-align: center; font-family: Arial; padding: 50px; }\n" +
                "    img { border: 2px solid #ddd; padding: 10px; }\n" +
                "  </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <h1>Base64形式のQRコード</h1>\n" +
                "  <p>チケットID: " + ticketId + "</p>\n" +
                "  <img src=\"data:image/png;base64," + base64Data + "\" alt=\"QRコード\" width=\"300\" height=\"300\">\n" +
                "  <p>このQRコードをスマホのカメラでスキャンしてください</p>\n" +
                "</body>\n" +
                "</html>";

            java.io.FileWriter writer = new java.io.FileWriter(outputPath + File.separator + htmlFileName);
            writer.write(htmlContent);
            writer.close();

            System.out.println("  → HTMLファイルも保存しました: " + htmlFileName);
            System.out.println("  → デスクトップのHTMLファイルをブラウザで開いて確認できます");

        } catch (Exception e) {
            System.err.println("  → HTMLファイルの保存に失敗しました: " + e.getMessage());
        }
    }
}
