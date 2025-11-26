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
      width: 250px;
      height: 250px;
      margin: 0 auto;
      background: white;
      padding: 15px;
      border-radius: 8px;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    }
    .qr-code img {
      width: 100%;
      height: 100%;
      display: block;
    }
    .btn-group {
      display: flex;
      gap: 10px;
      justify-content: center;
      margin-top: 20px;
    }
    .action-btn {
      padding: 12px 24px;
      border: none;
      border-radius: 6px;
      cursor: pointer;
      font-size: 14px;
      font-weight: bold;
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
        <div class="event-info">
          <h3 style="margin-top: 0; color: #333;">イベント情報</h3>
          <div class="info-row">
            <div class="info-label">イベント名</div>
            <div class="info-value">${event.eventName}</div>
          </div>
          <div class="info-row">
            <div class="info-label">開催日時</div>
            <div class="info-value">${event.holdingDate} ${event.holdingTime}</div>
          </div>
          <div class="info-row">
            <div class="info-label">会場</div>
            <div class="info-value">${event.address}</div>
          </div>
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
                  <span class="status-badge status-used">使用済み</span>
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
            <img src="${pageContext.request.contextPath}/qr/${ticket.qrImagePath}"
                 alt="入場用QRコード"
                 id="qrCodeImage">
          </div>
          <p style="margin-top: 15px; color: #666; font-size: 14px;">
            ※ 入場時にこのQRコードを提示してください<br>
            ※ スクリーンショットでの提示も可能です
          </p>
        </div>
      </div>
    </div>
    <div class="btn-group">
      <button class="action-btn btn-download" onclick="downloadQRCode()">QRコードをダウンロード</button>
      <button class="action-btn btn-print" onclick="window.print()">印刷</button>
      <button class="action-btn btn-back" onclick="location.href='${pageContext.request.contextPath}/eventportal/entrymenu/MyTickets.action'">戻る</button>
    </div>
    <div class="footer">@2025.................................................</div>
  </div>
  <script>
    function downloadQRCode() {
      const img = document.getElementById('qrCodeImage');
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