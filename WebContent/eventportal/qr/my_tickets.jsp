<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>マイチケット - イベントポータル</title>
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
            max-width: 900px;
            margin: 0 auto;
        }

        .header {
            background-color: #4a5568;
            color: white;
            padding: 20px;
            border-radius: 5px 5px 0 0;
        }

        .header h1 {
            font-size: 22px;
            margin-bottom: 5px;
        }

        .header p {
            font-size: 13px;
            opacity: 0.9;
        }

        .content {
            background: white;
            padding: 20px;
            border: 1px solid #ddd;
            border-top: none;
            border-radius: 0 0 5px 5px;
        }

        .btn-back {
            display: inline-block;
            padding: 8px 16px;
            background-color: #e2e8f0;
            color: #333;
            text-decoration: none;
            border-radius: 3px;
            margin-bottom: 20px;
            font-size: 13px;
        }

        .btn-back:hover {
            background-color: #cbd5e0;
        }

        .ticket-list {
            display: grid;
            gap: 15px;
        }

        .ticket-card {
            border: 1px solid #ddd;
            padding: 15px;
            border-radius: 5px;
            background-color: #fff;
            transition: box-shadow 0.2s;
        }

        .ticket-card:hover {
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }

        .ticket-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 10px;
            padding-bottom: 10px;
            border-bottom: 1px solid #eee;
        }

        .ticket-id {
            font-weight: bold;
            font-size: 16px;
            color: #333;
        }

        .status-badge {
            display: inline-block;
            padding: 4px 12px;
            border-radius: 3px;
            font-size: 11px;
            font-weight: bold;
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

        .ticket-info {
            margin-bottom: 10px;
        }

        .ticket-info .label {
            font-size: 12px;
            color: #666;
            display: inline-block;
            width: 100px;
        }

        .ticket-info .value {
            font-size: 13px;
            color: #333;
        }

        .btn-view {
            display: inline-block;
            padding: 8px 20px;
            background-color: #4a5568;
            color: white;
            text-decoration: none;
            border-radius: 3px;
            font-size: 13px;
            margin-top: 10px;
        }

        .btn-view:hover {
            background-color: #2d3748;
        }

        .no-tickets {
            text-align: center;
            padding: 60px 20px;
            color: #999;
        }

        .no-tickets-text {
            font-size: 16px;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>マイチケット</h1>
            <p>発行されたチケット一覧</p>
        </div>

        <div class="content">
            <a href="${pageContext.request.contextPath}/eventportal/entrymenu/EntryMenu.action" class="btn-back">
                ← メニューに戻る
            </a>

            <c:choose>
                <c:when test="${empty tickets}">
                    <div class="no-tickets">
                        <div class="no-tickets-text">発行されたチケットがありません</div>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="ticket-list">
                        <c:forEach var="ticket" items="${tickets}">
                            <div class="ticket-card">
                                <div class="ticket-header">
                                    <span class="ticket-id">${ticket.ticketId}</span>
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

                                <div class="ticket-info">
                                    <span class="label">イベント名:</span>
                                    <span class="value">${ticket.event.eventName}</span>
                                </div>

                                <div class="ticket-info">
                                    <span class="label">開催日時:</span>
                                    <span class="value">${ticket.event.holdDatetime}</span>
                                </div>

                                <c:if test="${not empty ticket.ticketInfo}">
                                    <div class="ticket-info">
                                        <span class="label">チケット情報:</span>
                                        <span class="value">${ticket.ticketInfo}</span>
                                    </div>
                                </c:if>

                                <div class="ticket-info">
                                    <span class="label">発行日時:</span>
                                    <span class="value">${ticket.createdAt}</span>
                                </div>

                                <a href="${pageContext.request.contextPath}/eventportal/entrymenu/ViewTicket.action?ticketId=${ticket.ticketId}"
                                   class="btn-view">
                                    QRコードを表示
                                </a>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</body>
</html>