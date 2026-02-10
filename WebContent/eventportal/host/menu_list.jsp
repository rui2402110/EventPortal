<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>メニュー管理 | イベントポータル</title>
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

        .header-wrapper {
            max-width: 1200px;
            margin: 0 auto 20px auto;
        }

        .header-bar {
            background: white;
            padding: 15px 25px;
            border-radius: 15px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .header-bar h1 {
            color: #667eea;
            font-size: 20px;
            margin: 0;
        }

        .header-buttons {
            display: flex;
            gap: 10px;
        }

        .btn-back {
            background: #6c757d;
            color: white;
            padding: 10px 20px;
            border-radius: 8px;
            text-decoration: none;
            font-weight: bold;
            font-size: 14px;
            transition: all 0.3s;
        }

        .btn-back:hover {
            background: #5a6268;
        }

        .btn-logout {
            background: #e74c3c;
            color: white;
            padding: 10px 20px;
            border-radius: 8px;
            text-decoration: none;
            font-weight: bold;
            font-size: 14px;
            transition: all 0.3s;
        }

        .btn-logout:hover {
            background: #c0392b;
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

        .page-header h2 {
            color: #333;
            font-size: 28px;
            margin-bottom: 10px;
        }

        .event-info {
            color: #666;
            font-size: 16px;
            margin-bottom: 20px;
        }

        .btn-new-menu {
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

        .btn-new-menu:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
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

        .menu-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
            gap: 20px;
        }

        .menu-card {
            background: white;
            border-radius: 15px;
            padding: 20px;
            box-shadow: 0 5px 20px rgba(0, 0, 0, 0.1);
            transition: all 0.3s;
        }

        .menu-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
        }

        .menu-type-badge {
            display: inline-block;
            padding: 5px 15px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: bold;
            margin-bottom: 15px;
        }

        .badge-goods {
            background: #3498db;
            color: white;
        }

        .badge-food {
            background: #e67e22;
            color: white;
        }

        .menu-name {
            font-size: 20px;
            font-weight: bold;
            color: #333;
            margin-bottom: 10px;
        }

        .menu-price {
            font-size: 24px;
            color: #667eea;
            font-weight: bold;
            margin-bottom: 10px;
        }

        .menu-description {
            color: #666;
            font-size: 14px;
            line-height: 1.6;
            margin-bottom: 15px;
            min-height: 40px;
        }

        .menu-stock {
            color: #999;
            font-size: 13px;
            margin-bottom: 15px;
        }

        .menu-actions {
            display: flex;
            gap: 10px;
        }

        .btn-delete {
            flex: 1;
            background: #e74c3c;
            color: white;
            padding: 10px;
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

        .btn-delete:hover {
            background: #c0392b;
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
            .header-bar {
                flex-direction: column;
                gap: 10px;
            }

            .header-buttons {
                width: 100%;
                flex-direction: column;
            }

            .btn-back,
            .btn-logout {
                width: 100%;
                text-align: center;
            }

            .menu-grid {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>
    <!-- ヘッダーバー -->
    <div class="header-wrapper">
        <div class="header-bar">
            <h1> イベントポータル - 主催者</h1>
            <div class="header-buttons">
                <a href="${pageContext.request.contextPath}/eventportal/host/HostMenu.action"
                   class="btn-back">
                    ← 戻る
                </a>
                <a href="${pageContext.request.contextPath}/eventportal/auth/Logout.action"
                   class="btn-logout"
                   onclick="return confirm('ログアウトしますか？');">
                     ログアウト
                </a>
            </div>
        </div>
    </div>

    <div class="container">
        <div class="page-header">
            <h2>メニュー管理</h2>
            <div class="event-info">
                イベント: <strong>${event.eventName}</strong>
            </div>
            <a href="${pageContext.request.contextPath}/eventportal/host/MenuCreate.action?eventId=${event.eventId}"
               class="btn-new-menu">
                ➕ 新規メニュー追加
            </a>
        </div>

        <c:if test="${not empty successMessage}">
            <div class="success-message">
                ✓ ${successMessage}
            </div>
        </c:if>

        <c:choose>
            <c:when test="${not empty menuList}">
                <div class="menu-grid">
                    <c:forEach var="menu" items="${menuList}">
                        <div class="menu-card">
                            <!-- メニュー種別バッジ -->
                            <span class="menu-type-badge ${menu.menuType == 'グッズ' ? 'badge-goods' : 'badge-food'}">
                                ${menu.menuType}
                            </span>

                            <!-- メニュー名 -->
                            <div class="menu-name">${menu.menuName}</div>

                            <!-- 価格 -->
                            <div class="menu-price">¥${menu.price}</div>

                            <!-- 説明 -->
                            <div class="menu-description">
                                ${menu.description}
                            </div>

                            <!-- 在庫 -->
                            <div class="menu-stock">
                                在庫: ${menu.stockQuantity}個
                            </div>

                            <!-- アクション -->
                            <div class="menu-actions">
                                <a href="${pageContext.request.contextPath}/eventportal/host/MenuDelete.action?menuId=${menu.menuId}&eventId=${event.eventId}"
                                   class="btn-delete"
                                   onclick="return confirm('「${menu.menuName}」を削除してもよろしいですか？');">
                                    削除
                                </a>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="no-menu">
                    <div></div>
                    <h3>メニューがありません</h3>
                    <p>新しいメニューを追加してください。</p>
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
