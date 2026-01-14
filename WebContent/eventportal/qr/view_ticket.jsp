<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>チケット詳細 - イベントポータル</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: Arial, sans-serif;
            background-color: #f5f5f5;
            padding: 20px;
        }

        .container {
            max-width: 600px;
            margin: 0 auto;
            background: white;
            border: 1px solid #ddd;
            border-radius: 5px;
        }

        .header {
            background-color: #4a5568;
            color: white;
            padding: 20px;
            border-radius: 5px 5px 0 0;
            text-align: center;
        }

        .header h1 {
            font-size: 22px;
            margin-bottom: 5px;
        }

        .header .ticket-id {
            font-size: 14px;
            opacity: 0.9;
        }

        .content {
            padding: 30px;
        }

        .qr-container {
            text-align: center;
            padding: 20px;
            background-color: #f8f9fa;
            border: 2px dashed #ddd;
            border-radius: 5px;
            margin-bottom: 30px;
        }

        .qr-container img {
            max-width: 300px;
            width: 100%;
            height: auto;
        }

        .qr-note {
            font-size: 12px;
            color: #666;
            margin-top: 10px;
        }

        .status-badge {
            display: inline-block;
            padding: 6px 15px;
            border-radius: 3px;
            font-size: 12px;
            font-weight: bold;
            margin-bottom: 20px;
        }

        .status-valid {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        .status-used {
            background-color: #e2e8f0;
            color: #4a5568;
            border: 1px solid #cbd5e0;
        }

        .status-invalid {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        .info-section {
            margin-bottom: 25px;
        }

        .info-label {
            font-weight: bold;
            color: #333;
            font-size: 13px;
            margin-bottom: 5px;
        }

        .info-value {
            color: #555;
            font-size: 14px;
            padding: 10px;
            background-color: #f8f9fa;
            border-radius: 3px;
        }

        .btn-back {
            display: inline-block;
            width: 100%;
            padding: 12px;
            background-color: #e2e8f0;
            color: #333;
            text-decoration: none;
            border-radius: 3px;
            text-align: center;
            font-size: 14px;
            margin-top: 20px;
        }

        .btn-back:hover {
            background-color: #cbd5e0;
        }

        .warning-message {
            background-color: #fff3cd;
            border: 1px solid #ffeaa7;
            color: #856404;
            padding: 12px;
            border-radius: 3px;
            margin-bottom: 20px;
            font-size: 13px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>チケット詳細</h1>
            <p class="ticket-id">チケットID: ${ticket.ticketId}</p>
        </div>

        <div class="content">
            <div style="text-align: center; margin-bottom: 20px;">
                <c:choose>
                    <c:when test="${ticket.status == 1}">
                        <span class="status-badge status-valid">有効</span>
                    </c:when>
                    <c:when test="${ticket.status == 2}">
                        <span class="status-badge status-used">使用済み</span>
                    </c:when>
                    <c:otherwise>
                        <span class="status-badge status-invalid">無効</span>
                    </c:otherwise>
                </c:choose>
            </div>

            <c:if test="${ticket.status == 2}">
                <div class="warning-message">
                    このチケットは既に使用済みです。再度入場することはできません。
                </div>
            </c:if>

            <c:if test="${ticket.status == 3}">
                <div class="warning-message">
                    このチケットは無効です。入場することはできません。
                </div>
            </c:if>

            <div class="qr-container">
                <c:choose>
                    <c:when test="${not empty ticket.qrImageData}">
                        <img src="data:image/png;base64,${ticket.qrImageData}" alt="QRコード">
                        <p class="qr-note">入場時にこのQRコードをご提示ください</p>
                    </c:when>
                    <c:otherwise>
                        <p style="color: #999;">QRコードが生成されていません</p>
                    </c:otherwise>
                </c:choose>
            </div>

            <div class="info-section">
                <div class="info-label">イベント名</div>
                <div class="info-value">${ticket.event.eventName}</div>
            </div>

            <div class="info-section">
                <div class="info-label">開催日時</div>
                <div class="info-value">${ticket.event.holdDatetime}</div>
            </div>

            <div class="info-section">
                <div class="info-label">開催場所</div>
                <div class="info-value">${ticket.event.place}</div>
            </div>

            <c:if test="${not empty ticket.ticketInfo}">
                <div class="info-section">
                    <div class="info-label">チケット情報</div>
                    <div class="info-value">${ticket.ticketInfo}</div>
                </div>
            </c:if>

            <div class="info-section">
                <div class="info-label">発行日時</div>
                <div class="info-value">${ticket.createdAt}</div>
            </div>

            <c:if test="${not empty ticket.usedAt}">
                <div class="info-section">
                    <div class="info-label">使用日時</div>
                    <div class="info-value">${ticket.usedAt}</div>
                </div>
            </c:if>

            <a href="${pageContext.request.contextPath}/eventportal/entrymenu/MyTickets.action" class="btn-back">
                チケット一覧に戻る
            </a>
        </div>
    </div>
</body>
</html>