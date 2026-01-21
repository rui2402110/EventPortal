package bean;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 参加者用QRコード情報を保持するBeanクラス
 */
public class EntryQRCode implements Serializable {
    private String qrCodeId;              // QRコードID
    private String userId;                // ユーザーID
    private String eventId;               // イベントID
    private String qrCodeData;            // QRコードデータ
    private String qrCodeImagePath;       // QRコード画像パス
    private LocalDateTime issuedDateTime; // 発行日時
    private LocalDateTime expirationDateTime; // 有効期限
    private int usageStatus;              // 使用状態（0:未使用, 1:使用済み）
    private LocalDateTime usedDateTime;   // 使用日時

    // コンストラクタ
    public EntryQRCode() {
    }

    // Getter & Setter
    public String getQrCodeId() {
        return qrCodeId;
    }

    public void setQrCodeId(String qrCodeId) {
        this.qrCodeId = qrCodeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getQrCodeData() {
        return qrCodeData;
    }

    public void setQrCodeData(String qrCodeData) {
        this.qrCodeData = qrCodeData;
    }

    public String getQrCodeImagePath() {
        return qrCodeImagePath;
    }

    public void setQrCodeImagePath(String qrCodeImagePath) {
        this.qrCodeImagePath = qrCodeImagePath;
    }

    public LocalDateTime getIssuedDateTime() {
        return issuedDateTime;
    }

    public void setIssuedDateTime(LocalDateTime issuedDateTime) {
        this.issuedDateTime = issuedDateTime;
    }

    public LocalDateTime getExpirationDateTime() {
        return expirationDateTime;
    }

    public void setExpirationDateTime(LocalDateTime expirationDateTime) {
        this.expirationDateTime = expirationDateTime;
    }

    public int getUsageStatus() {
        return usageStatus;
    }

    public void setUsageStatus(int usageStatus) {
        this.usageStatus = usageStatus;
    }

    public LocalDateTime getUsedDateTime() {
        return usedDateTime;
    }

    public void setUsedDateTime(LocalDateTime usedDateTime) {
        this.usedDateTime = usedDateTime;
    }

    // ユーティリティメソッド
    public boolean isUsed() {
        return usageStatus == 1;
    }

    public boolean isExpired() {
        if (expirationDateTime == null) {
            return false;
        }
        return LocalDateTime.now().isAfter(expirationDateTime);
    }

    public boolean isValid() {
        return !isUsed() && !isExpired();
    }
}