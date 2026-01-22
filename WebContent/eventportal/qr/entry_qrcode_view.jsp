<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>入場QRコード | イベントポータル</title>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background: #f5f5f5;
            margin: 0;
            padding: 0;
        }

        .container {
            max-width: 600px;
            margin: 0 auto;
            padding: 20px;
        }

        .header {
            background: #007bff;
            color: white;
            padding: 20px;
            text-align: center;
            border-radius: 10px 10px 0 0;
        }

        .qrcode-container {
            background: white;
            padding: 40px;
            text-align: center;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            border-radius: 0 0 10px 10px;
        }

        .qrcode-image {
            width: 300px;
            height: 300px;
            margin: 20px auto;
            border: 10px solid #f0f0f0;
            border-radius: 10px;
            background: #fff;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .qrcode-image img {
            width: 100%;
            height: 100%;
            object-fit: contain;
        }

        .qrcode-info {
            margin: 20px 0;
            padding: 20px;
            background: #f9f9f9;
            border-radius: 10px;
        }

        .info-row {
            display: flex;
            justify-content: space-between;
            margin: 10px 0;
            padding: 10px;
            border-bottom: 1px solid #e0e0e0;
        }

        .info-row:last-child {
            border-bottom: none;
        }

        .info-label {
            font-weight: bold;
            color: #666;
        }

        .info-value {
            color: #333;
        }

        .status-badge {
            display: inline-block;
            padding: 5px 15px;
            border-radius: 20px;
            font-size: 14px;
            font-weight: bold;
        }

        .status-unused {
            background: #28a745;
            color: white;
        }

        .status-used {
            background: #dc3545;
            color: white;
        }

        .btn-container {
            margin-top: 30px;
            text-align: center;
        }

        .btn {
            display: inline-block;
            padding: 12px 30px;
            margin: 0 10px;
            background: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            font-size: 16px;
            border: none;
            cursor: pointer;
            transition: background 0.3s;
        }

        .btn:hover {
            background: #0056b3;
        }

        .btn-secondary {
            background: #6c757d;
        }

        .btn-secondary:hover {
            background: #5a6268;
        }

        .success-message {
            background: #d4edda;
            border: 1px solid #c3e6cb;
            color: #155724;
            padding: 15px;
            border-radius: 5px;
            margin: 20px 0;
        }

        .error-message {
            background: #f8d7da;
            border: 1px solid #f5c6cb;
            color: #721c24;
            padding: 15px;
            border-radius: 5px;
            margin: 20px 0;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>入場QRコード</h1>
            <p>このQRコードをイベント会場で提示してください</p>
        </div>

        <div class="qrcode-container">
            <c:if test="${not empty successMessage}">
                <div class="success-message">${successMessage}</div>
            </c:if>

            <c:choose>
                <c:when test="${not empty ticket}">
                    <div class="qrcode-image">
                        <c:choose>
                            <c:when test="${not empty ticket.qrImageData}">
                                <img src="data:image/png;base64,${ticket.qrImageData}" alt="QRコード">
                            </c:when>
                            <c:when test="${not empty ticket.qrImagePath}">
                                <img src="${pageContext.request.contextPath}${ticket.qrImagePath}" alt="QRコード">
                            </c:when>
                            <c:otherwise>
                                <p style="color: #999;">QRコードが生成されていません</p>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div class="qrcode-info">
                        <div class="info-row">
                            <span class="info-label">チケットID:</span>
                            <span class="info-value">${ticket.ticketId}</span>
                        </div>

                        <c:if test="${not empty event}">
                            <div class="info-row">
                                <span class="info-label">イベント名:</span>
                                <span class="info-value">${event.eventName}</span>
                            </div>

                            <div class="info-row">
                                <span class="info-label">開催日時:</span>
                                <span class="info-value">${event.holdingDate} ${event.holdingTime}</span>
                            </div>

                            <div class="info-row">
                                <span class="info-label">会場:</span>
                                <span class="info-value">${event.address}</span>
                            </div>
                        </c:if>

                        <div class="info-row">
                            <span class="info-label">発行日時:</span>
                            <span class="info-value">${ticket.createdAt}</span>
                        </div>

                        <div class="info-row">
                            <span class="info-label">ステータス:</span>
                            <span class="info-value">
                                <c:choose>
                                    <c:when test="${ticket.status == 1}">
                                        <span class="status-badge status-unused">未使用</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="status-badge status-used">使用済み</span>
                                    </c:otherwise>
                                </c:choose>
                            </span>
                        </div>
                    </div>

                    <div class="btn-container">
                        <button class="btn" onclick="window.print()">印刷</button>
                        <a href="${pageContext.request.contextPath}/eventportal/entrymenu/EntryEventManage.action"
                           class="btn btn-secondary">イベント管理に戻る</a>
                    </div>

                </c:when>
                <c:otherwise>
                    <div class="error-message">
                        <strong>エラー:</strong> QRコードが見つかりません。
                    </div>
                    <div class="btn-container">
                        <a href="${pageContext.request.contextPath}/eventportal/entrymenu/EntryEventList.action"
                           class="btn">イベント一覧へ戻る</a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</body>
</html>