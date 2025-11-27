<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>チケット一覧 - イベントポータル</title>
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
            max-width: 1100px;
            margin: 0 auto;
            background: white;
            border: 1px solid #ddd;
        }

        .header {
            background-color: #4a5568;
            color: white;
            padding: 20px;
            border-bottom: 3px solid #2d3748;
        }

        .header h1 {
            font-size: 22px;
            margin-bottom: 5px;
        }

        .header .event-name {
            font-size: 13px;
            opacity: 0.9;
        }

        .success-message {
            background-color: #d4edda;
            border: 1px solid #c3e6cb;
            color: #155724;
            padding: 12px 20px;
            margin: 20px;
        }

        .stats {
            display: flex;
            padding: 20px;
            gap: 20px;
            border-bottom: 1px solid #eee;
        }

        .stat-box {
            flex: 1;
            padding: 15px;
            background-color: #f8f9fa;
            border: 1px solid #dee2e6;
            text-align: center;
        }

        .stat-box .label {
            font-size: 12px;
            color: #666;
            margin-bottom: 5px;
        }

        .stat-box .value {
            font-size: 28px;
            font-weight: bold;
            color: #333;
        }

        .action-bar {
            padding: 15px 20px;
            background-color: #f8f9fa;
            border-bottom: 1px solid #eee;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .btn {
            padding: 8px 16px;
            border: none;
            border-radius: 3px;
            font-size: 13px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
        }

        .btn-primary {
            background-color: #4a5568;
            color: white;
        }

        .btn-primary:hover {
            background-color: #2d3748;
        }

        .btn-secondary {
            background-color: #e2e8f0;
            color: #333;
        }

        .btn-secondary:hover {
            background-color: #cbd5e0;
        }

        .table-container {
            padding: 20px;
            overflow-x: auto;
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        th {
            background-color: #f8f9fa;
            padding: 12px;
            text-align: left;
            font-size: 13px;
            font-weight: bold;
            color: #333;
            border-bottom: 2px solid #dee2e6;
        }

        td {
            padding: 12px;
            border-bottom: 1px solid #eee;
            font-size: 13px;
            color: #555;
        }

        tbody tr:hover {
            background-color: #f8f9fa;
        }

        .status-badge {
            display: inline-block;
            padding: 3px 10px;
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

        .no-tickets {
            text-align: center;
            padding: 60px 20px;
            color: #999;
        }

        .no-tickets-text {
            font-size: 16px;
            margin-bottom: 20px;
        }

        @media (max-width: 768px) {
            .stats {
                flex-direction: column;
            }

            .action-bar {
                flex-direction: column;
                gap: 10px;
            }

            .btn {
                width: 100%;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>発行済みチケット一覧</h1>
            <p class="event-name">イベント: ${event.eventName}</p>
        </div>

        <c:if test="${not empty successMessage}">
            <div class="success-message">
                ${successMessage}
            </div>
        </c:if>

        <div class="stats">
            <div class="stat-box">
                <div class="label">発行総数</div>
                <div class="value">${totalCount}</div>
            </div>
            <div class="stat-box">
                <div class="label">有効チケット</div>
                <div class="value">${validCount}</div>
            </div>
            <div class="stat-box">
                <div class="label">入場済み</div>
                <div class="value">${admittedCount}</div>
            </div>
        </div>

        <div class="action-bar">
            <a href="${pageContext.request.contextPath}/eventportal/host/CreateTicket.action?eventId=${event.eventId}"
               class="btn btn-primary">
                新規チケット発行
            </a>
            <a href="${pageContext.request.contextPath}/eventportal/host/HostMain.action"
               class="btn btn-secondary">
                イベント一覧に戻る
            </a>
        </div>

        <div class="table-container">
            <c:choose>
                <c:when test="${empty tickets}">
                    <div class="no-tickets">
                        <div class="no-tickets-text">発行済みチケットがありません</div>
                        <a href="${pageContext.request.contextPath}/eventportal/host/CreateTicket.action?eventId=${event.eventId}"
                           class="btn btn-primary">
                            最初のチケットを発行
                        </a>
                    </div>
                </c:when>
                <c:otherwise>
                    <table>
                        <thead>
                            <tr>
                                <th>チケットID</th>
                                <th>ユーザーID</th>
                                <th>チケット情報</th>
                                <th>ステータス</th>
                                <th>発行日時</th>
                                <th>使用日時</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="ticket" items="${tickets}">
                                <tr>
                                    <td><strong>${ticket.ticketId}</strong></td>
                                    <td>${ticket.userId}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty ticket.ticketInfo}">
                                                ${ticket.ticketInfo}
                                            </c:when>
                                            <c:otherwise>
                                                -
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
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
                                    </td>
                                    <td>${ticket.createdAt}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty ticket.usedAt}">
                                                ${ticket.usedAt}
                                            </c:when>
                                            <c:otherwise>
                                                -
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</body>
</html>