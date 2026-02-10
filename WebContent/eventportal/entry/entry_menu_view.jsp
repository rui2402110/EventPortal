<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>グッズ・フード管理 | イベントポータル</title>
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

        .container {
            max-width: 1200px;
            margin: 0 auto;
        }

        .page-header {
            background: white;
            padding: 30px;
            border-radius: 15px;
            margin-bottom: 30px;
            box-shadow: 0 5px 20px rgba(0, 0, 0, 0.1);
        }

        .page-header h1 {
            color: #333;
            font-size: 32px;
            margin-bottom: 10px;
        }

        .event-info {
            color: #666;
            font-size: 16px;
            margin-bottom: 20px;
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

        .category-section {
            margin-bottom: 40px;
        }

        .category-title {
            background: white;
            padding: 15px 25px;
            border-radius: 10px;
            margin-bottom: 20px;
            box-shadow: 0 3px 10px rgba(0, 0, 0, 0.1);
        }

        .category-title h2 {
            color: #667eea;
            font-size: 24px;
        }

        .menu-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
            gap: 20px;
        }

        .menu-card {
            background: white;
            border-radius: 15px;
            padding: 20px;
            box-shadow: 0 5px 20px rgba(0, 0, 0, 0.1);
            transition: all 0.3s;
            display: flex;
            flex-direction: column;
        }

        .menu-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
        }

        .menu-name {
            font-size: 20px;
            font-weight: bold;
            color: #333;
            margin-bottom: 10px;
        }

        .menu-price {
            font-size: 28px;
            color: #667eea;
            font-weight: bold;
            margin-bottom: 10px;
        }

        .menu-description {
            color: #666;
            font-size: 14px;
            line-height: 1.6;
            margin-bottom: 15px;
            flex-grow: 1;
        }

        .menu-stock {
            color: #999;
            font-size: 13px;
            margin-bottom: 15px;
        }

        .stock-available {
            color: #2ecc71;
        }

        .stock-low {
            color: #e67e22;
        }

        .stock-out {
            color: #e74c3c;
        }

        .order-section {
            display: flex;
            gap: 10px;
            align-items: center;
            margin-top: auto;
        }

        .quantity-input {
            width: 80px;
            padding: 10px;
            border: 2px solid #e0e0e0;
            border-radius: 8px;
            font-size: 14px;
            text-align: center;
        }

        .btn-order {
            flex: 1;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 12px 20px;
            border: none;
            border-radius: 8px;
            font-size: 14px;
            font-weight: bold;
            cursor: pointer;
            transition: all 0.3s;
        }

        .btn-order:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }

        .btn-order:disabled {
            background: #ccc;
            cursor: not-allowed;
            transform: none;
        }

        .btn-back {
            background: #6c757d;
            color: white;
            padding: 15px 30px;
            border-radius: 10px;
            text-decoration: none;
            font-weight: bold;
            font-size: 16px;
            transition: all 0.3s;
            display: inline-block;
            margin-top: 30px;
        }

        .btn-back:hover {
            background: #5a6268;
        }

        .no-menu {
            text-align: center;
            padding: 60px 20px;
            background: white;
            border-radius: 15px;
            color: #999;
        }

        .no-menu i {
            font-size: 60px;
            margin-bottom: 20px;
        }

        @media (max-width: 768px) {
            .menu-grid {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="page-header">
            <h1>🛍️ グッズ・フード管理</h1>
            <div class="event-info">
                イベント: <strong>${event.eventName}</strong>
            </div>
        </div>

        <c:if test="${not empty successMessage}">
            <div class="success-message">
                ✓ ${successMessage}
            </div>
        </c:if>

        <c:choose>
            <c:when test="${not empty menuList}">
                <!-- グッズセクション -->
                <c:set var="hasGoods" value="false" />
                <c:forEach var="menu" items="${menuList}">
                    <c:if test="${menu.menuType == 'グッズ'}">
                        <c:set var="hasGoods" value="true" />
                    </c:if>
                </c:forEach>

                <c:if test="${hasGoods}">
                    <div class="category-section">
                        <div class="category-title">
                            <h2>🎁 グッズ</h2>
                        </div>
                        <div class="menu-grid">
                            <c:forEach var="menu" items="${menuList}">
                                <c:if test="${menu.menuType == 'グッズ'}">
                                    <div class="menu-card">
                                        <div class="menu-name">${menu.menuName}</div>
                                        <div class="menu-price">¥${menu.price}</div>

                                        <c:if test="${not empty menu.description}">
                                            <div class="menu-description">${menu.description}</div>
                                        </c:if>

                                        <div class="menu-stock ${menu.stockQuantity > 10 ? 'stock-available' : menu.stockQuantity > 0 ? 'stock-low' : 'stock-out'}">
                                            <c:choose>
                                                <c:when test="${menu.stockQuantity > 0}">
                                                    在庫: ${menu.stockQuantity}個
                                                </c:when>
                                                <c:otherwise>
                                                    ✗ 在庫切れ
                                                </c:otherwise>
                                            </c:choose>
                                        </div>

                                        <div class="order-section">
                                            <input type="number"
                                                   class="quantity-input"
                                                   id="quantity_${menu.menuId}"
                                                   min="1"
                                                   max="${menu.stockQuantity}"
                                                   value="1"
                                                   ${menu.stockQuantity == 0 ? 'disabled' : ''}>
                                            <button class="btn-order"
                                                    onclick="orderMenu('${menu.menuId}', '${menu.menuName}', ${menu.price}, ${menu.stockQuantity})"
                                                    ${menu.stockQuantity == 0 ? 'disabled' : ''}>
                                                🛒 注文する
                                            </button>
                                        </div>
                                    </div>
                                </c:if>
                            </c:forEach>
                        </div>
                    </div>
                </c:if>

                <!-- フードセクション -->
                <c:set var="hasFood" value="false" />
                <c:forEach var="menu" items="${menuList}">
                    <c:if test="${menu.menuType == 'フード'}">
                        <c:set var="hasFood" value="true" />
                    </c:if>
                </c:forEach>

                <c:if test="${hasFood}">
                    <div class="category-section">
                        <div class="category-title">
                            <h2>🍔 フード</h2>
                        </div>
                        <div class="menu-grid">
                            <c:forEach var="menu" items="${menuList}">
                                <c:if test="${menu.menuType == 'フード'}">
                                    <div class="menu-card">
                                        <div class="menu-name">${menu.menuName}</div>
                                        <div class="menu-price">¥${menu.price}</div>

                                        <c:if test="${not empty menu.description}">
                                            <div class="menu-description">${menu.description}</div>
                                        </c:if>

                                        <div class="menu-stock ${menu.stockQuantity > 10 ? 'stock-available' : menu.stockQuantity > 0 ? 'stock-low' : 'stock-out'}">
                                            <c:choose>
                                                <c:when test="${menu.stockQuantity > 0}">
                                                    在庫: ${menu.stockQuantity}個
                                                </c:when>
                                                <c:otherwise>
                                                    ✗ 在庫切れ
                                                </c:otherwise>
                                            </c:choose>
                                        </div>

                                        <div class="order-section">
                                            <input type="number"
                                                   class="quantity-input"
                                                   id="quantity_${menu.menuId}"
                                                   min="1"
                                                   max="${menu.stockQuantity}"
                                                   value="1"
                                                   ${menu.stockQuantity == 0 ? 'disabled' : ''}>
                                            <button class="btn-order"
                                                    onclick="orderMenu('${menu.menuId}', '${menu.menuName}', ${menu.price}, ${menu.stockQuantity})"
                                                    ${menu.stockQuantity == 0 ? 'disabled' : ''}>
                                                🛒 注文する
                                            </button>
                                        </div>
                                    </div>
                                </c:if>
                            </c:forEach>
                        </div>
                    </div>
                </c:if>
            </c:when>
            <c:otherwise>
                <div class="no-menu">
                    <div>📝</div>
                    <h3>メニューがありません</h3>
                    <p>現在、注文可能なメニューがありません。</p>
                </div>
            </c:otherwise>
        </c:choose>

        <a href="${pageContext.request.contextPath}/eventportal/entrymenu/EntryEventDetail.action?eventId=${event.eventId}"
           class="btn-back">
            ← イベント詳細に戻る
        </a>
    </div>

<script>
        function orderMenu(menuId, menuName, price, stockQuantity) {
            const quantityInput = document.getElementById('quantity_' + menuId);
            const quantity = parseInt(quantityInput.value);

            console.log('注文処理:', menuId, menuName, quantity);

            if (!quantity || quantity < 1) {
                alert('数量を正しく入力してください');
                return;
            }

            if (quantity > stockQuantity) {
                alert('在庫が不足しています\n在庫: ' + stockQuantity + '個');
                return;
            }

            const totalPrice = price * quantity;
            const message = '【注文確認】\n\n' +
                          'メニュー: ' + menuName + '\n' +
                          '単価: ¥' + price.toLocaleString() + '\n' +
                          '数量: ' + quantity + '個\n' +
                          '合計: ¥' + totalPrice.toLocaleString() + '\n\n' +
                          '注文しますか？';

            if (confirm(message)) {
                // ★★★ 実際の注文処理を実行 ★★★
                const eventId = '${event.eventId}';
                window.location.href = '${pageContext.request.contextPath}/eventportal/entrymenu/MenuOrder.action' +
                                       '?menuId=' + menuId +
                                       '&eventId=' + eventId +
                                       '&quantity=' + quantity;
            }
        }

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
