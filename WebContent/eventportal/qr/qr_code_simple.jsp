<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
  <title>入場QRコード</title>
  <style>
    * { margin: 0; padding: 0; box-sizing: border-box; }
    body {
      font-family: -apple-system, BlinkMacSystemFont, sans-serif;
      background: #000;
      color: #fff;
      display: flex;
      flex-direction: column;
      min-height: 100vh;
    }
    .qr-fullscreen {
      flex: 1;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      padding: 20px;
    }
    .qr-container {
      background: #fff;
      padding: 20px;
      border-radius: 16px;
      max-width: 90vw;
      display: flex;
      flex-direction: column;
      align-items: center;
    }
    .qr-image {
      width: 100%;
      max-width: 400px;
      height: auto;
    }
    .qr-info {
      margin-top: 20px;
      text-align: center;
      color: #333;
    }
    .event-name {
      font-size: 18px;
      font-weight: bold;
      margin-bottom: 8px;
    }
    .back-button {
      position: fixed;
      top: 20px;
      left: 20px;
      background: rgba(255, 255, 255, 0.2);
      color: #fff;
      padding: 8px 16px;
      border-radius: 20px;
      text-decoration: none;
    }
  </style>
</head>
<body>
  <a href="${pageContext.request.contextPath}/eventportal/entrymenu/MyTickets.action" class="back-button">← 戻る</a>
  <div class="qr-fullscreen">
    <div class="qr-container">
      <img src="${pageContext.request.contextPath}/qr/${ticket.qrImagePath}" alt="入場QRコード" class="qr-image">
      <div class="qr-info">
        <div class="event-name">${event.eventName}</div>
        <div style="font-family: monospace; font-size: 14px; color: #666;">ID: ${ticket.ticketId}</div>
        <div style="font-size: 16px; color: #444; margin-top: 8px;">${user.user_name} 様</div>
      </div>
    </div>
  </div>
</body>
</html>