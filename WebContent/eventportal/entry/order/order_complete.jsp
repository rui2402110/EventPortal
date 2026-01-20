<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>注文完了 - イベントポータル</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f5f5f5;
            margin: 0;
            padding: 20px;
        }

        .container {
            max-width: 800px;
            margin: 0 auto;
            background: white;
            border-radius: 10px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
            overflow: hidden;
        }

        .header {
            background-color: #28a745;
            color: white;
            padding: 30px;
            text-align: center;
        }

        .header .icon {
            font-size: 60px;
            margin-bottom: 10px;
        }

        .header h1 {
            margin: 0;
            font-size: 28px;
        }

        .content {
            padding: 30px;
        }

        .success-message {
            background-color: #d4edda;
            color: #155724;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 30px;
        }

        .order-info {
            background-color: #f8f9fa;
            padding: 20px;
            border-radius: 5px;
            margin-bottom: 30px;
        }

        .info-row {
            display: flex;
            justify-content: space-between;
            padding: 10px 0;
            border-bottom: 1px solid #dee2e6;
        }

        .info-row:last-child {
            border-bottom: none;
        }

        .info-label {
            font-weight: bold;
            color: #666;
        }

        .order-items {
            margin-bottom: 30px;
        }

        .order-items h2 {
            margin-bottom: 20px;
        }

        .item {
            display: flex;
            justify-content: space-between;
            padding: 15px;
            border-bottom: 1px solid #e0e0e0;
        }

        .item:last-child {
            border-bottom: none;
        }

        .item-info {
            flex: 1;
        }

        .item-name {
            font-weight: bold;
            margin-bottom: 5px;
        }

        .item-detail {
            color: #666;
            font-size: 14px;
        }

        .item-price {
            text-align: right;
            font-weight: bold;
        }

        .total-amount {
            background-color: #f8f9fa;
            padding: 20px;
            text-align: right;
            font-size: 20px;
            font-weight: bold;
            border-top: 2px solid #dee2e6;
        }

        .buttons {
            display: flex;
            gap: 10px;
            justify-content: center;
            margin-top: 30px;
        }

        .btn {
            padding: 12px 30px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            text-align: center;
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

        .note {
            background-color: #fff3cd;
            color: #856404;
            padding: 15px;
            border-radius: 5px;
            margin-top: 20px;
            font-size: 14px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <div class="icon">✓</div>
            <h1>注文が完了しました</h1>
        </div>

        <div class="content">
            <c:if test="${not empty successMessage}">
                <div class="success-message">
                    ${successMessage}
                </div>
            </c:if>

            <div class="order-info">
                <div class="info-row">
                    <span class="info-label">注文ID:</span>
                    <span>${order.orderId}</span>
                </div>
                <div class="info-row">
                    <span class="info-label">注文日時:</span>
                    <span>${order.orderDate}</span>
                </div>
                <div class="info-row">
                    <span class="info-label">ステータス:</span>
                    <span>${order.statusText}</span>
                </div>
            </div>

            <div class="order-items">
                <h2>注文内容</h2>
                <c:forEach var="item" items="${order.items}">
                    <div class="item">
                        <div class="item-info">
                            <div class="item-name">${item.product.productName}</div>
                            <div class="item-detail">
                                単価: <fmt:formatNumber value="${item.unitPrice}" pattern="#,###" />円
                                × ${item.quantity}個
                            </div>
                        </div>
                        <div class="item-price">
                            <fmt:formatNumber value="${item.subtotal}" pattern="#,###" />円
                        </div>
                    </div>
                </c:forEach>
            </div>

            <div class="total-amount">
                合計金額: <fmt:formatNumber value="${order.totalAmount}" pattern="#,###" />円
            </div>

            <div class="note">
                <strong>ご注文ありがとうございます</strong><br>
                商品の受け取りは、イベント会場内の指定された場所で行ってください。<br>
                準備ができましたら、スタッフがお呼びいたします。
            </div>

            <div class="buttons">
                <a href="${pageContext.request.contextPath}/eventportal/entrymenu/MyTickets.action"
                   class="btn btn-primary">
                    マイチケットに戻る
                </a>
                <a href="${pageContext.request.contextPath}/eventportal/entry/order/EntryProductList.action?eventId=${order.eventId}&ticketId=${order.ticketId}"
                   class="btn btn-secondary">
                    商品一覧に戻る
                </a>
            </div>
        </div>
    </div>
</body>
</html>