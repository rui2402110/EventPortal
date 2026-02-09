<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>イベント一覧 | イベントポータル</title>
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

        .topbar {
            background: rgba(255, 255, 255, 0.95);
            padding: 15px 30px;
            border-radius: 15px;
            margin-bottom: 30px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
        }

        .topbar h1 {
            color: #667eea;
            font-size: 24px;
        }

        .topbar-buttons {
            display: flex;
            gap: 10px;
        }

        .topbar-btn {
            padding: 10px 20px;
            border: none;
            border-radius: 8px;
            font-size: 14px;
            font-weight: bold;
            cursor: pointer;
            transition: all 0.3s;
            text-decoration: none;
            display: inline-block;
        }

        .btn-home {
            background: #667eea;
            color: white;
        }

        .btn-home:hover {
            background: #5568d3;
            transform: translateY(-2px);
        }

        .btn-logout {
            background: #e74c3c;
            color: white;
        }

        .btn-logout:hover {
            background: #c0392b;
            transform: translateY(-2px);
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
        }

        .header {
            text-align: center;
            margin-bottom: 40px;
            color: white;
        }

        .header h2 {
            font-size: 36px;
            margin-bottom: 10px;
        }

        .header p {
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

        .error-message {
            background: #e74c3c;
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
            grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
            gap: 25px;
        }

        .event-card {
            background: white;
            border-radius: 15px;
            padding: 25px;
            box-shadow: 0 5px 20px rgba(0, 0, 0, 0.1);
            transition: all 0.3s;
            position: relative;
        }

        .event-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
        }

        .event-badge {
            position: absolute;
            top: 15px;
            right: 15px;
            padding: 5px 15px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: bold;
        }

        .badge-joined {
            background: #2ecc71;
            color: white;
        }

        .badge-available {
            background: #3498db;
            color: white;
        }

        .event-title {
            font-size: 22px;
            font-weight: bold;
            color: #333;
            margin-bottom: 15px;
            padding-right: 100px;
        }

        .event-info {
            margin-bottom: 15px;
        }

        .event-info-item {
            display: flex;
            align-items: center;
            margin-bottom: 10px;
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

        .event-buttons {
            display: flex;
            gap: 10px;
        }

        .btn {
            flex: 1;
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

        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }

        .btn-success {
            background: #2ecc71;
            color: white;
        }

        .btn-success:hover {
            background: #27ae60;
            transform: translateY(-2px);
        }

        .btn-secondary {
            background: #95a5a6;
            color: white;
        }

        .btn-secondary:hover {
            background: #7f8c8d;
        }

        .btn-joined {
            background: #ecf0f1;
            color: #7f8c8d;
            cursor: default;
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

        @media (max-width: 768px) {
            .events-grid {
                grid-template-columns: 1fr;
            }

            .topbar {
                flex-direction: column;
                gap: 15px;
            }

            .topbar-buttons {
                width: 100%;
                flex-direction: column;
            }

            .topbar-btn {
                width: 100%;
            }
        }
    </style>
</head>
<body>
    <!-- トップバー -->
    <div class="topbar">
        <h1>イベントポータル</h1>
        <div class="topbar-buttons">
            <a href="${pageContext.request.contextPath}/eventportal/entrymenu/EntryMenu.action"
               class="topbar-btn btn-home">
                トップページ
            </a>
            <a href="${pageContext.request.contextPath}/eventportal/auth/Logout.action"
               class="topbar-btn btn-logout">
                ログアウト
            </a>
        </div>
    </div>

    <div class="container">
        <div class="header">
            <h2> イベント一覧</h2>
            <p>参加したいイベントを選んでください</p>
        </div>

        <!-- 成功メッセージ -->
        <c:if test="${not empty successMessage}">
            <div class="success-message">
                ✓ ${successMessage}
            </div>
        </c:if>

        <!-- エラーメッセージ -->
        <c:if test="${not empty errorMessage}">
            <div class="error-message">
                ❌ ${errorMessage}
            </div>
        </c:if>

        <!-- イベント一覧 -->
        <c:choose>
            <c:when test="${not empty list}">
                <div class="events-grid">
                    <c:forEach var="event" items="${list}">
                        <div class="event-card">
                            <!-- 参加状態バッジ -->
                            <c:choose>
                                <c:when test="${event.hasTicket}">
                                    <span class="event-badge badge-joined">✓ 参加中</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="event-badge badge-available">参加可能</span>
                                </c:otherwise>
                            </c:choose>

                            <!-- イベントタイトル -->
                            <div class="event-title">${event.eventName}</div>

                            <!-- イベント情報 -->
                            <div class="event-info">
                                <div class="event-info-item">
                                    <i></i>
                                    <span>${event.holdingDate} ${event.holdingTime}</span>
                                </div>
                                <div class="event-info-item">
                                    <i></i>
                                    <span>${event.address}</span>
                                </div>
                                <div class="event-info-item">
                                    <i></i>
                                    <span>定員: ${event.maxCount}人</span>
                                </div>
                            </div>

                            <!-- イベント概要 -->
                            <div class="event-overview">
                                ${event.eventOverview}
                            </div>

                            <!-- ボタン -->
                            <div class="event-buttons">
                                <c:choose>
                                    <c:when test="${event.hasTicket}">
                                        <!-- 参加済みの場合 -->
                                        <a href="${pageContext.request.contextPath}/eventportal/entrymenu/ShowQRCode.action?eventId=${event.eventId}"
                                           class="btn btn-success">
                                            QRコード表示
                                        </a>
                                        <a href="${pageContext.request.contextPath}/eventportal/entrymenu/EntryEventDetail.action?eventId=${event.eventId}"
                                           class="btn btn-secondary">
                                            詳細
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <!-- 未参加の場合 -->
                                        <a href="${pageContext.request.contextPath}/eventportal/entrymenu/JoinEvent.action?eventId=${event.eventId}"
                                           class="btn btn-primary"
                                           onclick="return confirm('「${event.eventName}」に参加登録しますか？');">
                                            ✓ 参加する
                                        </a>
                                        <a href="${pageContext.request.contextPath}/eventportal/entrymenu/EntryEventDetail.action?eventId=${event.eventId}"
                                           class="btn btn-secondary">
                                           詳細
                                        </a>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="no-events">
                    <div></div>
                    <h3>イベントがありません</h3>
                    <p>現在、参加可能なイベントがありません。</p>
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
