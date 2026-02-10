<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>イベント詳細 | イベントポータル</title>
    <style>
        /* 既存のスタイルをそのまま維持 */
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
            background: #f8f9fa;
            color: #333;
            border-radius: 10px;
            margin-bottom: 30px;
        }

        .event-table {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
        }

        .event-table th,
        .event-table td {
            padding: 12px;
            text-align: left;
            border: 1px solid #dee2e6;
        }

        .event-table th {
            background: #f8f9fa;
            font-weight: bold;
            width: 20%;
        }

        .button-group {
            display: flex;
            flex-direction: column;
            gap: 15px;
            margin: 30px 0;
            max-width: 400px;
            margin-left: auto;
            margin-right: auto;
        }

        .btn {
            padding: 15px 30px;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
            text-decoration: none;
            text-align: center;
            transition: all 0.3s;
            color: white;
            display: block;
        }

        .btn-map {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }

        .btn-map:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 15px rgba(102,126,234,0.4);
        }

        .btn-goods {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
        }

        .btn-goods:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 15px rgba(240,147,251,0.4);
        }

        .btn-qr {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        }

        .btn-qr:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 15px rgba(79,172,254,0.4);
        }

        .btn-back {
            background: #6c757d;
        }

        .btn-back:hover {
            background: #545b62;
        }

        .status-badge {
            display: inline-block;
            padding: 5px 15px;
            border-radius: 20px;
            font-weight: bold;
            font-size: 14px;
        }

        .status-before {
            background: #d1ecf1;
            color: #0c5460;
        }

        .status-ongoing {
            background: #d4edda;
            color: #155724;
        }

        .status-finished {
            background: #f8d7da;
            color: #721c24;
        }

        @media (max-width: 768px) {
            .top-bar {
                flex-direction: column;
                gap: 10px;
                padding: 15px;
            }

            .top-bar-right {
                flex-wrap: wrap;
                justify-content: center;
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
            <h1>イベント詳細</h1>
        </div>

        <table class="event-table">
            <tr>
                <th>イベントID</th>
                <td>${evt.eventId}</td>
                <th>イベント名</th>
                <td>${evt.eventName}</td>
            </tr>
            <tr>
                <th>開催日時</th>
                <td>${evt.holdingDate} ${evt.holdingTime}</td>
                <th>場所</th>
                <td>${evt.address}</td>
            </tr>
            <tr>
                <th>定員</th>
                <td>${evt.maxCount}名</td>
                <th>状態</th>
                <td>
                    <c:choose>
                        <c:when test="${evt.eventHoldState == '1'}">
                            <span class="status-badge status-before">開催前</span>
                        </c:when>
                        <c:when test="${evt.eventHoldState == '2'}">
                            <span class="status-badge status-ongoing">開催中</span>
                        </c:when>
                        <c:when test="${evt.eventHoldState == '3'}">
                            <span class="status-badge status-finished">開催後</span>
                        </c:when>
                        <c:otherwise>
                            <span class="status-badge">不明</span>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
            <tr>
                <th>電話番号</th>
                <td>${evt.phoneNumber}</td>
                <th>リンク</th>
                <td>
                    <c:if test="${evt.link != null && evt.link != ''}">
                        <a href="${evt.link}" target="_blank">${evt.link}</a>
                    </c:if>
                </td>
            </tr>
            <tr>
                <th>概要</th>
                <td colspan="3">${evt.eventOverview}</td>
            </tr>
        </table>

        <div class="button-group">
            <!-- 会場外マップ表示 -->
            <a href="${pageContext.request.contextPath}/eventportal/entrymenu/entrydetail/EntryMapoutHall.action?eventId=${evt.eventId}"
               class="btn btn-map">
                📍 会場外マップ表示
            </a>

            <!-- 会場内マップ表示 -->
            <a href="${pageContext.request.contextPath}/eventportal/entrymenu/entrydetail/EntryMapinHall.action?eventId=${evt.eventId}"
               class="btn btn-map">
                🏢 会場内マップ表示
            </a>

<!-- グッズ・フード管理 -->
<a href="${pageContext.request.contextPath}/eventportal/entrymenu/EntryMenuView.action?eventId=${evt.eventId}"
   class="btn btn-goods">
    🛍️ グッズ・フード管理
</a>
            <!-- ★★★ QRコード表示（重要：必ずActionを経由）★★★ -->
            <a href="${pageContext.request.contextPath}/eventportal/entrymenu/ShowQRCode.action?eventId=${evt.eventId}"
               class="btn btn-qr">
                📱 QRコード表示
            </a>

            <!-- 戻るボタン -->
            <a href="${pageContext.request.contextPath}/eventportal/entrymenu/EntryEventManage.action"
               class="btn btn-back">
                ← 参加イベント一覧に戻る
            </a>
        </div>
    </div>
</body>
</html>