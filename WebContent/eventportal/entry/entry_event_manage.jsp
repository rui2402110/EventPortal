<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>参加イベント一覧 | イベントポータル</title>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background: #f5f5f5;
            margin: 0;
            padding: 0;
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
            max-width: 1200px;
            margin: 30px auto;
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }

        .header {
            text-align: center;
            padding: 20px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 10px;
            margin-bottom: 30px;
        }

        .success-message {
            background: #d4edda;
            color: #155724;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            border-left: 4px solid #28a745;
        }

        .event-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
            gap: 25px;
            margin: 30px 0;
        }

        .event-card {
            background: white;
            border-radius: 15px;
            padding: 25px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            transition: all 0.3s;
            border: 2px solid #f0f0f0;
        }

        .event-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 25px rgba(102,126,234,0.2);
            border-color: #667eea;
        }

        .event-header {
            display: flex;
            justify-content: space-between;
            align-items: start;
            margin-bottom: 15px;
        }

        .event-id {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 5px 15px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: bold;
        }

        .event-title {
            font-size: 20px;
            font-weight: bold;
            color: #333;
            margin: 10px 0;
        }

        .event-info {
            margin: 15px 0;
            color: #666;
            font-size: 14px;
        }

        .event-info-row {
            display: flex;
            align-items: center;
            margin: 8px 0;
        }

        .event-info-icon {
            margin-right: 8px;
        }

        .event-actions {
            display: flex;
            gap: 10px;
            margin-top: 20px;
        }

        .btn {
            flex: 1;
            padding: 12px 20px;
            border: none;
            border-radius: 8px;
            font-size: 14px;
            font-weight: bold;
            cursor: pointer;
            text-decoration: none;
            text-align: center;
            transition: all 0.3s;
            display: inline-block;
        }

        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 15px rgba(102,126,234,0.4);
        }

        .btn-success {
            background: linear-gradient(135deg, #28a745 0%, #20c997 100%);
            color: white;
        }

        .btn-success:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 15px rgba(40,167,69,0.4);
        }

        .btn-secondary {
            background: #6c757d;
            color: white;
        }

        .btn-secondary:hover {
            background: #545b62;
        }

        .status-badge {
            display: inline-block;
            padding: 5px 15px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: bold;
        }

        .status-joined {
            background: #d4edda;
            color: #155724;
        }

        .status-available {
            background: #d1ecf1;
            color: #0c5460;
        }

        @media (max-width: 768px) {
            .top-bar {
                flex-direction: column;
                gap: 10px;
            }

            .event-grid {
                grid-template-columns: 1fr;
            }

            .container {
                margin: 20px;
                padding: 20px;
            }
        }
    </style>
</head>
<body>
    <!-- トップバー -->
    <div class="top-bar">
        <div class="top-bar-left">
            <span style="font-size: 24px; font-weight: bold;">📱 イベントポータル</span>
        </div>
        <div class="top-bar-right">
            <span class="user-name">${user.user_name} 様</span>
            <a href="${pageContext.request.contextPath}/eventportal/entrymenu/EntryEventManage.action"
               class="top-btn">🏠 トップページ</a>
            <a href="${pageContext.request.contextPath}/eventportal/auth/Logout.action"
               class="top-btn">🚪 ログアウト</a>
        </div>
    </div>

    <div class="container">
        <div class="header">
            <h1>イベント一覧</h1>
        </div>

        <!-- 参加成功メッセージ -->
        <c:if test="${param.joined == 'true'}">
            <div class="success-message">
                ✓ イベントへの参加登録が完了しました！
            </div>
        </c:if>

        <div class="event-grid">
            <c:forEach var="event" items="${list}">
                <div class="event-card">
                    <div class="event-header">
                        <div class="event-id">${event.eventId}</div>
                        <c:choose>
                            <c:when test="${event.hasTicket}">
                                <span class="status-badge status-joined">✓ 参加中</span>
                            </c:when>
                            <c:otherwise>
                                <span class="status-badge status-available">参加可能</span>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div class="event-title">${event.eventName}</div>

                    <div class="event-info">
                        <div class="event-info-row">
                            <span class="event-info-icon">📅</span>
                            <span>${event.holdingDate} ${event.holdingTime}</span>
                        </div>
                        <div class="event-info-row">
                            <span class="event-info-icon">📍</span>
                            <span>${event.address}</span>
                        </div>
                        <div class="event-info-row">
                            <span class="event-info-icon">👥</span>
                            <span>定員: ${event.maxCount}名</span>
                        </div>
                    </div>

                    <div class="event-actions">
                        <c:choose>
                            <c:when test="${event.hasTicket}">
                                <!-- 参加済みの場合 -->
                                <a href="${pageContext.request.contextPath}/eventportal/entrymenu/EntryEventDetail.action?eventId=${event.eventId}"
                                   class="btn btn-primary">
                                    📋 詳細を見る
                                </a>
                            </c:when>
                            <c:otherwise>
                                <!-- 未参加の場合 -->
                                <a href="${pageContext.request.contextPath}/eventportal/entrymenu/JoinEvent.action?eventId=${event.eventId}"
                                   class="btn btn-success"
                                   onclick="return confirm('このイベントに参加登録しますか？');">
                                    ✓ 参加する
                                </a>
                                <a href="${pageContext.request.contextPath}/eventportal/entrymenu/EntryEventDetail.action?eventId=${event.eventId}"
                                   class="btn btn-secondary">
                                    👁️ 詳細
                                </a>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </c:forEach>
        </div>

        <c:if test="${empty list}">
            <div style="text-align: center; padding: 60px; color: #999;">
                <div style="font-size: 60px; margin-bottom: 20px;">📭</div>
                <div style="font-size: 18px;">イベントがありません</div>
            </div>
        </c:if>
    </div>
</body>
</html>