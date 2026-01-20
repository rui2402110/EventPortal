<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>商品一覧 - イベントポータル</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: Arial, sans-serif;
            background-color: #f5f5f5;
        }

        .header {
            background-color: #4a5568;
            color: white;
            padding: 20px;
            text-align: center;
        }

        .container {
            max-width: 1200px;
            margin: 20px auto;
            padding: 0 20px;
        }

        .message {
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 5px;
        }

        .message.success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        .message.error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        .product-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }

        .product-card {
            background: white;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            transition: transform 0.2s;
        }

        .product-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 4px 12px rgba(0,0,0,0.15);
        }

        .product-image {
            width: 100%;
            height: 200px;
            object-fit: cover;
            background-color: #e0e0e0;
        }

        .product-info {
            padding: 15px;
        }

        .product-name {
            font-size: 18px;
            font-weight: bold;
            margin-bottom: 10px;
        }

        .product-overview {
            font-size: 14px;
            color: #666;
            margin-bottom: 10px;
            height: 60px;
            overflow: hidden;
        }

        .product-price {
            font-size: 20px;
            font-weight: bold;
            color: #2196f3;
            margin-bottom: 10px;
        }

        .product-stock {
            font-size: 14px;
            margin-bottom: 15px;
        }

        .stock-ok {
            color: #28a745;
        }

        .stock-low {
            color: #ffc107;
        }

        .stock-out {
            color: #dc3545;
        }

        .add-to-cart-form {
            display: flex;
            gap: 10px;
            align-items: center;
        }

        .quantity-input {
            width: 60px;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }

        .btn {
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 14px;
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

        .btn-primary:disabled {
            background-color: #cbd5e0;
            cursor: not-allowed;
        }

        .btn-secondary {
            background-color: #e2e8f0;
            color: #333;
        }

        .btn-secondary:hover {
            background-color: #cbd5e0;
        }

        .cart-summary {
            position: fixed;
            bottom: 0;
            left: 0;
            right: 0;
            background: white;
            box-shadow: 0 -2px 10px rgba(0,0,0,0.1);
            padding: 15px;
            text-align: center;
        }

        .cart-info {
            font-size: 16px;
            margin-bottom: 10px;
        }

        .no-products {
            text-align: center;
            padding: 60px 20px;
            background: white;
            border-radius: 8px;
        }
    </style>
</head>
<body>
    <div class="header">
        <h1>商品一覧</h1>
        <p>イベントID: ${eventId}</p>
    </div>

    <div class="container">
        <c:if test="${not empty successMessage}">
            <div class="message success">${successMessage}</div>
        </c:if>

        <c:if test="${not empty error}">
            <div class="message error">${error}</div>
        </c:if>

        <c:choose>
            <c:when test="${empty products}">
                <div class="no-products">
                    <p>現在、販売中の商品はありません</p>
                </div>
            </c:when>
            <c:otherwise>
                <div class="product-grid">
                    <c:forEach var="product" items="${products}">
                        <div class="product-card">
                            <c:choose>
                                <c:when test="${not empty product.image}">
                                    <img src="${pageContext.request.contextPath}/${product.image}"
                                         alt="${product.productName}"
                                         class="product-image">
                                </c:when>
                                <c:otherwise>
                                    <div class="product-image"></div>
                                </c:otherwise>
                            </c:choose>

                            <div class="product-info">
                                <div class="product-name">${product.productName}</div>
                                <div class="product-overview">${product.overview}</div>
                                <div class="product-price">
                                    <fmt:formatNumber value="${product.price}" pattern="#,###" />円
                                </div>
                                <div class="product-stock">
                                    <c:choose>
                                        <c:when test="${product.stock > 10}">
                                            <span class="stock-ok">在庫: ${product.stock}</span>
                                        </c:when>
                                        <c:when test="${product.stock > 0}">
                                            <span class="stock-low">残りわずか (${product.stock})</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="stock-out">在庫切れ</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>

                                <form action="${pageContext.request.contextPath}/eventportal/entry/order/AddToCart.action"
                                      method="post" class="add-to-cart-form">
                                    <input type="hidden" name="eventId" value="${eventId}">
                                    <input type="hidden" name="ticketId" value="${ticket.ticketId}">
                                    <input type="hidden" name="itemId" value="${product.itemId}">
                                    <input type="number" name="quantity" value="1" min="1"
                                           max="${product.stock}" class="quantity-input"
                                           ${product.stock == 0 ? 'disabled' : ''}>
                                    <button type="submit" class="btn btn-primary"
                                            ${product.stock == 0 ? 'disabled' : ''}>
                                        カートに追加
                                    </button>
                                </form>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>

        <div style="padding-bottom: 100px;">
            <a href="${pageContext.request.contextPath}/eventportal/entrymenu/ViewTicket.action?ticketId=${ticket.ticketId}"
               class="btn btn-secondary">チケット詳細に戻る</a>
        </div>
    </div>

    <c:if test="${not empty sessionScope.cart}">
        <div class="cart-summary">
            <div class="cart-info">
                カート内商品: ${sessionScope.cart.size()}件
            </div>
            <form action="${pageContext.request.contextPath}/eventportal/entry/order/OrderExecute.action"
                  method="post" style="display: inline;">
                <input type="hidden" name="eventId" value="${eventId}">
                <input type="hidden" name="ticketId" value="${ticket.ticketId}">
                <button type="submit" class="btn btn-primary">注文を確定する</button>
            </form>
        </div>
    </c:if>
</body>
</html>