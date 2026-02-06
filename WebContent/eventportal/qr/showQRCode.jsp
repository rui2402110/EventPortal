<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>QRコード表示 | イベントポータル</title>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background: #f5f5f5;
            margin: 0;
            padding: 0;
            min-height: 100vh;
        }

        .top-bar {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 15px 30px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }

        .top-bar-left {
            display: flex;
            align-items: center;
            gap: 20px;
        }

        .top-bar-right {
            display: flex;
            align-items: center;
            gap: 15px;
        }

        .user-name {
            font-weight: bold;
        }

        .top-btn {
            padding: 8px 20px;
            background: rgba(255,255,255,0.2);
            color: white;
            border: 2px solid white;
            border-radius: 5px;
            text-decoration: none;
            font-weight: bold;
            transition: all 0.3s;
            cursor: pointer;
        }

        .top-btn:hover {
            background: white;
            color: #667eea;
        }

        .container {
            max-width: 500px;
            width: 100%;
            background: white;
            padding: 40px;
            border-radius: 20px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.1);
            text-align: center;
            margin: 40px auto;
        }

        .header {
            margin-bottom: 30px;
        }

        .header h1 {
            color: #333;
            font-size: 28px;
            margin: 0 0 10px 0;
        }

        .event-name {
            color: #667eea;
            font-size: 20px;
            font-weight: bold;
            margin: 10px 0;
        }

        .ticket-info {
            background: #f8f9fa;
            padding: 15px;
            border-radius: 10px;
            margin: 20px 0;
        }

        .info-row {
            display: flex;
            justify-content: space-between;
            padding: 8px 0;
            border-bottom: 1px solid #dee2e6;
        }

        .info-row:last-child {
            border-bottom: none;
        }

        .info-label {
            font-weight: bold;
            color: #495057;
        }

        .info-value {
            color: #212529;
        }

        .qr-container {
            background: white;
            padding: 30px;
            border-radius: 15px;
            margin: 20px 0;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
        }

        .qr-code {
            width: 100%;
            max-width: 300px;
            height: auto;
            border: 5px solid #667eea;
            border-radius: 10px;
            padding: 10px;
            background: white;
        }

        .status-badge {
            display: inline-block;
            padding: 8px 20px;
            border-radius: 20px;
            font-weight: bold;
            margin: 15px 0;
        }

        .status-valid {
            background: #d4edda;
            color: #155724;
        }

        .status-used {
            background: #f8d7da;
            color: #721c24;
        }

        .instructions {
            background: #e7f3ff;
            padding: 15px;
            border-radius: 10px;
            margin: 20px 0;
            text-align: left;
            color: #004085;
        }

        .instructions h3 {
            margin-top: 0;
            color: #004085;
        }

        .instructions ul {
            margin: 10px 0;
            padding-left: 20px;
        }

        .instructions li {
            margin: 5px 0;
        }

        .btn {
            padding: 15px 40px;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            transition: all 0.3s;
            margin: 10px 5px;
        }

        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(102,126,234,0.4);
        }

        .btn-secondary {
            background: #6c757d;
            color: white;
        }

        .btn-secondary:hover {
            background: #545b62;
        }

        .warning-message {
            background: #fff3cd;
            color: #856404;
            padding: 15px;
            border-radius: 8px;
            margin: 20px 0;
            border-left: 4px solid #ffc107;
        }

        @media (max-width: 600px) {
            .container {
                padding: 20px;
                margin: 20px;
            }

            .qr-code {
                max-width: 250px;
            }

            .top-bar {
                flex-direction: column;
                gap: 10px;
                padding: 15px;
            }

            .top-bar-right {
                flex-wrap: wrap;
                justify-content: center;
            }
        }
    </style>
</head>
<body>
    <!-- トップバー -->
    <div class="top-bar">
        <div class="top-bar-left">
            <span style="font-size: 24px; font-weight: bold;">イベントポータル</span>
        </div>
        <div class="top-bar-right">
            <span class="user-name">${user.user_name} 様</span>
            <a href="${pageContext.request.contextPath}/eventportal/entrymenu/EntryEventManage.action"
               class="top-btn">トップページ</a>
            <a href="${pageContext.request.contextPath}/eventportal/auth/Logout.action"
               class="top-btn">ログアウト</a>
        </div>
    </div>

    <div class="container">
        <div class="header">
            <h1>入場用QRコード</h1>
            <div class="event-name">${event.eventName}</div>
        </div>

        <div class="ticket-info">
            <div class="info-row">
                <span class="info-label">参加者名:</span>
                <span class="info-value">${user.user_name}</span>
            </div>
            <div class="info-row">
                <span class="info-label">チケットID:</span>
                <span class="info-value">${ticket.ticketId}</span>
            </div>
            <div class="info-row">
                <span class="info-label">開催日:</span>
                <span class="info-value">${event.holdingDate}</span>
            </div>
        </div>

        <c:choose>
            <c:when test="${ticket.status == 1}">
                <div class="status-badge status-valid">✓ 有効</div>
            </c:when>
            <c:when test="${ticket.status == 2}">
                <div class="status-badge status-used">✗ 使用済み</div>
                <div class="warning-message">
                    このチケットは既に使用済みです。<br>
                    使用日時: ${ticket.usedAt}
                </div>
            </c:when>
            <c:otherwise>
                <div class="status-badge status-used">✗ 無効</div>
            </c:otherwise>
        </c:choose>

        <div class="qr-container">
            <c:choose>
                <c:when test="${ticket.qrImageData != null}">
                    <img src="data:image/png;base64,${ticket.qrImageData}"
                         alt="QRコード"
                         class="qr-code">
                </c:when>
                <c:when test="${ticket.qrImagePath != null}">
                    <img src="${pageContext.request.contextPath}${ticket.qrImagePath}"
                         alt="QRコード"
                         class="qr-code">
                </c:when>
                <c:otherwise>
                    <div class="warning-message">
                        QRコードが生成されていません。
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="instructions">
            <h3>使い方</h3>
            <ul>
                <li>会場入口でこのQRコードをスタッフに提示してください</li>
                <li>スタッフがスキャンすると入場が記録されます</li>
                <li>QRコードは1回のみ有効です</li>
                <li>スクリーンショットを保存しておくと便利です</li>
            </ul>
        </div>

        <div>
            <a href="${pageContext.request.contextPath}/eventportal/entrymenu/EntryEventDetail.action?eventId=${event.eventId}"
               class="btn btn-primary">
                イベント詳細に戻る
            </a>
            <a href="${pageContext.request.contextPath}/eventportal/entrymenu/EntryEventManage.action"
               class="btn btn-secondary">
                参加イベント一覧
            </a>
        </div>
    </div>
</body>
</html>