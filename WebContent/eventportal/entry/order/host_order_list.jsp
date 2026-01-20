<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>注文管理 - イベントポータル</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/common/style.css">
    <style>
        .stats {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 20px;
            margin: 20px 0;
        }

        .stat-card {
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            text-align: center;
        }

        .stat-value {
            font-size: 32px;
            font-weight: bold;
            color: #2196f3;
            margin: 10px 0;
        }

        .stat-label {
            color: #666;
            font-size: 14px;
        }

        .order-table {
            width: 100%;
            background: white;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }

        .order-table table {
            width: 100%;
            border-collapse: collapse;
        }

        .order-table th {
            background-color: #f5f5f5;
            padding: 15px;
            text-align: left;
            font-weight: bold;
            border-bottom: 2px solid #ddd;
        }

        .order-table td {
            padding: 15px;
            border-bottom: 1px solid #eee;
        }

        .order-table tr:hover {
            background-color: #f9f9f9;
        }

        .status-badge {
            padding: 5px 10px;
            border-radius: 3px;
            font-size: 12px;
            font-weight: bold;
        }

        .status-1 {
            background-color: #e3f2fd;
            color: #1976d2;
        }

        .status-2 {
            background-color: #fff3e0;
            color: #f57c00;
        }

        .status-3 {
            background-color: #e8f5e9;
            color: #388e3c;
        }

        .status-9 {
            background-color: #ffebee;
            color: #d32f2f;
        }

        .btn-update {
            padding: 5px 10px;
            font-size: 12px;
            margin: 2px;
        }

        .no-orders {
            text-align: center;
            padding: 60px 20px;
            color: #999;
        }
    </style>
</head>
<body>
    <div class="header">イベントポータル - 注文管理</div>

    <div class="container">
        <h2>${event.eventName} - 注文一覧</h2>

        <div class="stats">
            <div class="stat-card">
                <div class="stat-label">総注文数</div>
                <div class="stat-value">${orderCount}</div>
            </div>
            <div class="stat-card">
                <div class="stat-label">総売上</div>
                <div class="stat-value">
                    <fmt:formatNumber value="${totalOrderAmount}" pattern="#,###" />円
                </div>
            </div>
        </div>

        <c:choose>
            <c:when test="${empty orders}">
                <div class="no-orders">
                    <p>まだ注文がありません</p>
                </div>
            </c:when>
            <c:otherwise>
                <div class="order-table">
                    <table>
                        <thead>
                            <tr>
                                <th>注文ID</th>
                                <th>ユーザーID</th>
                                <th>注文日時</th>
                                <th>商品数</th>
                                <th>合計金額</th>
                                <th>ステータス</th>
                                <th>操作</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="order" items="${orders}">
                                <tr>
                                    <td>${order.orderId}</td>
                                    <td>${order.userId}</td>
                                    <td>${order.orderDate}</td>
                                    <td>${order.items.size()}点</td>
                                    <td>
                                        <fmt:formatNumber value="${order.totalAmount}" pattern="#,###" />円
                                    </td>
                                    <td>
                                        <span class="status-badge status-${order.status}">
                                            ${order.statusText}
                                        </span>
                                    </td>
                                    <td>
                                        <c:if test="${order.status == 1}">
                                            <form action="${pageContext.request.contextPath}/eventportal/host/order/UpdateOrderStatus.action"
                                                  method="post" style="display: inline;">
                                                <input type="hidden" name="orderId" value="${order.orderId}">
                                                <input type="hidden" name="eventId" value="${event.eventId}">
                                                <input type="hidden" name="status" value="2">
                                                <button type="submit" class="btn btn-primary btn-update">準備中</button>
                                            </form>
                                        </c:if>
                                        <c:if test="${order.status == 2}">
                                            <form action="${pageContext.request.contextPath}/eventportal/host/order/UpdateOrderStatus.action"
                                                  method="post" style="display: inline;">
                                                <input type="hidden" name="orderId" value="${order.orderId}">
                                                <input type="hidden" name="eventId" value="${event.eventId}">
                                                <input type="hidden" name="status" value="3">
                                                <button type="submit" class="btn btn-primary btn-update">完了</button>
                                            </form>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>

        <div style="margin-top: 30px;">
            <a href="${pageContext.request.contextPath}/eventportal/host/HostEventDetail.action?eventId=${event.eventId}"
               class="btn btn-secondary">イベント詳細に戻る</a>
        </div>
    </div>

    <div class="footer">@2025.................................................</div>
</body>
</html>