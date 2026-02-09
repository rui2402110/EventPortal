<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>主催イベント一覧 | イベントポータル</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 20px;
        }

        .header-bar {
            background: white;
            padding: 20px 30px;
            border-radius: 15px;
            margin-bottom: 30px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .header-bar h1 {
            color: #667eea;
            font-size: 24px;
        }

        .header-buttons {
            display: flex;
            gap: 10px;
        }

        .btn-new-event {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 12px 25px;
            border-radius: 10px;
            text-decoration: none;
            font-weight: bold;
            font-size: 14px;
            transition: all 0.3s;
            display: inline-block;
        }

        .btn-new-event:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }

        .btn-logout {
            background: #e74c3c;
            color: white;
            padding: 12px 25px;
            border-radius: 10px;
            text-decoration: none;
            font-weight: bold;
            font-size: 14px;
            transition: all 0.3s;
            display: inline-block;
            border: none;
            cursor: pointer;
        }

        .btn-logout:hover {
            background: #c0392b;
            transform: translateY(-2px);
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
        }

        .page-header {
            text-align: center;
            color: white;
            margin-bottom: 40px;
        }

        .page-header h2 {
            font-size: 36px;
            margin-bottom: 10px;
        }

        .page-header p {
            font-size: 18px;
            opacity: 0.9;
        }

        .success-message {
            background: #2ecc71;
            color: white;
            padding: 15px 20px;
            border-radius: 10px;
            margin-bottom: 20px;
            text-align: center;
            font-weight: bold;
            animation: slideDown 0.5s ease;
        }

        @keyframes slideDown {
            from {
                opacity: 0;
                transform: translateY(-20px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .events-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
            gap: 25px;
        }

        .event-card {
            background: white;
            border-radius: 15px;
            padding: 25px;
            box-shadow: 0 5px 20px rgba(0, 0, 0, 0.1);
            transition: all 0.3s;
        }

        .event-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
        }

        .event-status {
            display: inline-block;
            padding: 5px 15px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: bold;
            margin-bottom: 15px;
        }

        .status-1 {
            background: #3498db;
            color: white;
        }

        .status-2 {
            background: #2ecc71;
            color: white;
        }

        .status-3 {
            background: #95a5a6;
            color: white;
        }

        .event-title {
            font-size: 22px;
            font-weight: bold;
            color: #333;
            margin-bottom: 15px;
        }

        .event-info {
            margin-bottom: 15px;
        }

        .event-info-item {
            display: flex;
            align-items: center;
            margin-bottom: 8px;
            color: #666;
            font-size: 14px;
        }

        .event-info-item i {
            margin-right: 10px;
            width: 20px;
        }

        .event-overview {
            color: #777;
            font-size: 14px;
            line-height: 1.6;
            margin-bottom: 20px;
            max-height: 60px;
            overflow: hidden;
            text-overflow: ellipsis;
        }

        .event-actions {
            display: grid;
            grid-template-columns: 1fr;
            gap: 10px;
        }

        .action-row {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 10px;
        }

        .btn {
            padding: 12px 20px;
            border: none;
            border-radius: 8px;
            font-size: 14px;
            font-weight: bold;
            cursor: pointer;
            transition: all 0.3s;
            text-decoration: none;
            text-align: center;
            display: inline-block;
        }

        .btn-qr {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            grid-column: 1 / -1;
        }

        .btn-qr:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }

        .btn-detail {
            background: #3498db;
            color: white;
        }

        .btn-detail:hover {
            background: #2980b9;
        }

        .btn-edit {
            background: #f39c12;
            color: white;
        }

        .btn-edit:hover {
            background: #e67e22;
        }

        .btn-delete {
            background: #e74c3c;
            color: white;
            grid-column: 1 / -1;
        }

        .btn-delete:hover {
            background: #c0392b;
        }

        .no-events {
            text-align: center;
            padding: 60px 20px;
            background: white;
            border-radius: 15px;
            color: #999;
        }

        .no-events i {
            font-size: 60px;
            margin-bottom: 20px;
        }

        .no-events .btn-create {
            margin-top: 20px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 15px 30px;
            border-radius: 10px;
            text-decoration: none;
            font-weight: bold;
            display: inline-block;
            transition: all 0.3s;
        }

        .no-events .btn-create:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }

        @media (max-width: 768px) {
            .header-bar {
                flex-direction: column;
                gap: 15px;
            }

            .header-buttons {
                width: 100%;
                flex-direction: column;
            }

            .btn-new-event,
            .btn-logout {
                width: 100%;
                text-align: center;
            }

            .events-grid {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header-bar">
            <h1> イベントポータル - 主催者</h1>
            <div class="header-buttons">
                <a href="${pageContext.request.contextPath}/eventportal/host/HostEventCreate.action"
                   class="btn-new-event">
                    新規イベント作成
                </a>
                <a href="${pageContext.request.contextPath}/eventportal/auth/Logout.action"
                   class="btn-logout"
                   onclick="return confirm('ログアウトしますか？');">
                     ログアウト
                </a>
            </div>
        </div>

        <div class="page-header">
            <h2>主催イベント一覧</h2>
            <p>あなたが主催するイベントを管理できます</p>
        </div>

        <c:if test="${not empty successMessage}">
            <div class="success-message">
                ✓ ${successMessage}
            </div>
        </c:if>

        <c:choose>
            <c:when test="${not empty event}">
                <div class="events-grid">
                    <c:forEach var="evt" items="${event}">
                        <div class="event-card">
                            <!-- 開催状態バッジ -->
                            <span class="event-status status-${evt.eventHoldState}">
                                <c:choose>
                                    <c:when test="${evt.eventHoldState == '1'}">開催前</c:when>
                                    <c:when test="${evt.eventHoldState == '2'}">開催中</c:when>
                                    <c:when test="${evt.eventHoldState == '3'}">開催後</c:when>
                                </c:choose>
                            </span>

                            <!-- イベントタイトル -->
                            <div class="event-title">${evt.eventName}</div>

                            <!-- イベント情報 -->
                            <div class="event-info">
                                <div class="event-info-item">
                                    <i></i>
                                    <span>${evt.holdingDate} ${evt.holdingTime}</span>
                                </div>
                                <div class="event-info-item">
                                    <i></i>
                                    <span>${evt.address}</span>
                                </div>
                                <div class="event-info-item">
                                    <i></i>
                                    <span>定員: ${evt.maxCount}人</span>
                                </div>
                            </div>

                            <!-- イベント概要 -->
                            <div class="event-overview">
                                ${evt.eventOverview}
                            </div>

                            <!-- アクションボタン -->
                            <div class="event-actions">
                                <!-- QRスキャン画面 -->
                                <a href="${pageContext.request.contextPath}/eventportal/host/HostQRScanner.action?eventId=${evt.eventId}"
                                   class="btn btn-qr">
                                    QRスキャン画面
                                </a>

                                <!-- 詳細と編集 -->
                                <div class="action-row">
                                    <a href="${pageContext.request.contextPath}/eventportal/host/TicketList.action?eventId=${evt.eventId}"
                                       class="btn btn-detail">
                                         詳細
                                    </a>
                                    <a href="${pageContext.request.contextPath}/eventportal/host/HostEventUpdate.action?eventId=${evt.eventId}"
                                       class="btn btn-edit">
                                        編集
                                    </a>
                                </div>

                                <!-- 削除 -->
                                <a href="${pageContext.request.contextPath}/eventportal/host/HostEventDelete.action?eventId=${evt.eventId}"
                                   class="btn btn-delete"
                                   onclick="return confirm('「${evt.eventName}」を削除してもよろしいですか？\nこの操作は取り消せません。');">
                                    削除
                                </a>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="no-events">
                    <div></div>
                    <h3>イベントがありません</h3>
                    <p>新しいイベントを作成してください。</p>
                    <a href="${pageContext.request.contextPath}/eventportal/host/HostEventCreate.action"
                       class="btn-create">
                         最初のイベントを作成
                    </a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <script>
        // 成功メッセージを3秒後に自動で消す
        window.addEventListener('DOMContentLoaded', function() {
            const successMessage = document.querySelector('.success-message');
            if (successMessage) {
                setTimeout(function() {
                    successMessage.style.opacity = '0';
                    successMessage.style.transform = 'translateY(-20px)';
                    setTimeout(function() {
                        successMessage.remove();
                    }, 500);
                }, 3000);
            }
        });
    </script>
</body>
</html>
