<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>入場チケット | イベントポータル</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/common/style.css">
  <style>
    .ticket-container {
      max-width: 600px;
      margin: 30px auto;
      background: white;
      border-radius: 12px;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
      overflow: hidden;
    }
    .ticket-header {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
      padding: 20px;
      text-align: center;
    }
    .ticket-header h2 {
      margin: 0;
      font-size: 24px;
    }
    .ticket-id {
      font-family: 'Courier New', monospace;
      font-size: 18px;
      font-weight: bold;
      color: #667eea;
      margin-top: 15px;
    }
    .ticket-body {
      padding: 30px;
    }
    .event-name-link {
      display: block;
      padding: 15px;
      margin: 20px 0;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
      text-decoration: none;
      border-radius: 8px;
      text-align: center;
      font-size: 18px;
      font-weight: bold;
      transition: all 0.3s;
      box-shadow: 0 2px 8px rgba(102, 126, 234, 0.3);
    }
    .event-name-link:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgba(102, 126, 234, 0.5);
    }
    .info-row {
      display: flex;
      padding: 12px 0;
      border-bottom: 1px solid #eee;
    }
    .info-label {
      flex: 0 0 120px;
      font-weight: bold;
      color: #555;
    }
    .info-value {
      flex: 1;
      color: #333;
    }
    .qr-section {
      text-align: center;
      padding: 30px;
      background: #f9f9f9;
      border-radius: 8px;
      margin: 20px 0;
    }
    .qr-code {
      width: 300px;
      height: 300px;
      margin: 0 auto;
      background: white;
      padding: 15px;
      border-radius: 8px;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
      display: flex;
      align-items: center;
      justify-content: center;
    }
    .qr-code img {
      width: 100%;
      height: 100%;
      object-fit: contain;
    }
    .status-badge {
      display: inline-block;
      padding: 4px 12px;
      border-radius: 12px;
      font-size: 12px;
      font-weight: bold;
    }
    .status-valid {
      background: #e8f5e9;
      color: #2e7d32;
    }
    .status-used {
      background: #ffebee;
      color: #c62828;
    }
    .btn-group {
      display: flex;
      gap: 10px;
      justify-content: center;
      margin-top: 20px;
      flex-wrap: wrap;
    }
    .action-btn {
      padding: 12px 24px;
      border: none;
      border-radius: 6px;
      cursor: pointer;
      font-size: 14px;
      font-weight: bold;
      transition: all 0.3s;
      text-decoration: none;
      display: inline-block;
    }
    .btn-download {
      background: #4CAF50;
      color: white;
    }
    .btn-print {
      background: #2196F3;
      color: white;
    }
    .btn-back {
      background: #757575;
      color: white;
    }
    .btn-shop {
      background: #FF9800;
      color: white;
      font-size: 16px;
    }
  </style>
</head>
<body>
  <div class="container">
    <div class="header">イベントポータル - 入場チケット</div>
    <div class="ticket-container">
      <div class="ticket-header">
        <h2>入場チケット</h2>
        <div class="ticket-id">Ticket ID: ${ticket.ticketId}</div>
      </div>
      <div class="ticket-body">
        <c:if test="${not empty event}">
          <a href="${pageContext.request.contextPath}/eventportal/entrymenu/entrydetail/EntryQrDisp.action?eventId=${event.eventId}"
             class="event-name-link">
            ${event.eventName}
          </a>
        </c:if>

        <div class="event-info">
          <h3 style="margin-top: 0; color: #333;">イベント情報</h3>
          <c:if test="${not empty event}">
            <div class="info-row">
              <div class="info-label">開催日時</div>
              <div class="info-value">${event.holdingDate} ${event.holdingTime}</div>
            </div>
            <div class="info-row">
              <div class="info-label">会場</div>
              <div class="info-value">${event.address}</div>
            </div>
          </c:if>
          <div class="info-row">
            <div class="info-label">参加者名</div>
            <div class="info-value">${user.user_name}</div>
          </div>
          <div class="info-row">
            <div class="info-label">ステータス</div>
            <div class="info-value">
              <c:choose>
                <c:when test="${ticket.status == 1}">
                  <span class="status-badge status-valid">有効</span>
                </c:when>
                <c:when test="${ticket.status == 2}">
                  <span class="status-badge status-used">入場済み</span>
                </c:when>
                <c:otherwise>
                  <span class="status-badge">無効</span>
                </c:otherwise>
              </c:choose>
            </div>
          </div>
        </div>

        <div class="qr-section">
          <h3 style="margin-top: 0; color: #333;">入場用QRコード</h3>
          <div class="qr-code">
            <c:choose>
              <c:when test="${not empty ticket.qrImageData}">
                <img src="data:image/png;base64,${ticket.qrImageData}"
                     alt="入場用QRコード"
                     id="qrCodeImage">
              </c:when>
              <c:when test="${not empty ticket.qrImagePath}">
                <img src="${pageContext.request.contextPath}${ticket.qrImagePath}"
                     alt="入場用QRコード"
                     id="qrCodeImage">
              </c:when>
              <c:otherwise>
                <p style="color: #999;">QRコードが生成されていません</p>
              </c:otherwise>
            </c:choose>
          </div>
          <p style="margin-top: 15px; color: #666; font-size: 14px;">
            ※ 入場時にこのQRコードを提示してください<br>
            ※ 入場後は会場内でグッズ・フードを注文できます
          </p>
        </div>

        <c:if test="${ticket.status == 2}">
          <div style="background: #e8f5e9; padding: 15px; border-radius: 8px; margin: 20px 0;">
            <h4 style="margin: 0 0 10px 0; color: #2e7d32;">✓ 入場済み</h4>
            <p style="margin: 0; color: #555;">会場内でグッズ・フードをご注文いただけます</p>
          </div>
        </c:if>
      </div>
    </div>

    <div class="btn-group">
      <c:if test="${ticket.status == 2}">
        <a href="${pageContext.request.contextPath}/eventportal/entry/order/EntryProductList.action?eventId=${event.eventId}&ticketId=${ticket.ticketId}"
           class="action-btn btn-shop">
          グッズ・フードを注文
        </a>
      </c:if>
      <button class="action-btn btn-download" onclick="downloadQRCode()">QRコードをダウンロード</button>
      <button class="action-btn btn-print" onclick="window.print()">印刷</button>
      <button class="action-btn btn-back" onclick="location.href='${pageContext.request.contextPath}/eventportal/entrymenu/MyTickets.action'">戻る</button>
    </div>

    <div class="footer">@2025.................................................</div>
  </div>
  <script>
    function downloadQRCode() {
      const img = document.getElementById('qrCodeImage');
      if (!img) {
        alert('QRコードが見つかりません');
        return;
      }
      const link = document.createElement('a');
      link.href = img.src;
      link.download = 'ticket_qr_${ticket.ticketId}.png';
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    }
  </script>
</body>
</html>