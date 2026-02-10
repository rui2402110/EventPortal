<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>メニュー追加 | イベントポータル</title>
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
            max-width: 800px;
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
            max-width: 800px;
            margin: 0 auto;
            background: white;
            border-radius: 20px;
            padding: 40px;
            box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
        }

        .header {
            text-align: center;
            margin-bottom: 40px;
        }

        .header h2 {
            color: #333;
            font-size: 32px;
            margin-bottom: 10px;
        }

        .header p {
            color: #666;
            font-size: 16px;
        }

        .form-group {
            margin-bottom: 25px;
        }

        .form-group label {
            display: block;
            font-weight: bold;
            color: #333;
            margin-bottom: 8px;
            font-size: 14px;
        }

        .form-group label .required {
            color: #e74c3c;
            margin-left: 4px;
        }

        .form-group input[type="text"],
        .form-group input[type="number"],
        .form-group textarea,
        .form-group select {
            width: 100%;
            padding: 12px 15px;
            border: 2px solid #e0e0e0;
            border-radius: 8px;
            font-size: 14px;
            transition: all 0.3s;
        }

        .form-group input:focus,
        .form-group textarea:focus,
        .form-group select:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }

        .form-group textarea {
            resize: vertical;
            min-height: 100px;
        }

        .form-row {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
        }

        .error-message {
            background: #fee;
            color: #c33;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            border-left: 4px solid #c33;
        }

        .button-group {
            display: flex;
            gap: 15px;
            margin-top: 30px;
        }

        .btn {
            flex: 1;
            padding: 15px 30px;
            border: none;
            border-radius: 10px;
            font-size: 16px;
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
            box-shadow: 0 8px 20px rgba(102, 126, 234, 0.4);
        }

        .btn-secondary {
            background: #6c757d;
            color: white;
        }

        .btn-secondary:hover {
            background: #5a6268;
        }

        .help-text {
            font-size: 12px;
            color: #999;
            margin-top: 5px;
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

            .container {
                padding: 20px;
            }

            .form-row {
                grid-template-columns: 1fr;
            }

            .button-group {
                flex-direction: column;
            }
        }
    </style>
</head>
<body>
    <!-- ヘッダーバー -->
    <div class="header-wrapper">
        <div class="header-bar">
            <h1>🎪 イベントポータル - 主催者</h1>
            <div class="header-buttons">
                <a href="${pageContext.request.contextPath}/eventportal/host/MenuList.action?eventId=${event.eventId}"
                   class="btn-back">
                    ← 戻る
                </a>
                <a href="${pageContext.request.contextPath}/eventportal/auth/Logout.action"
                   class="btn-logout"
                   onclick="return confirm('ログアウトしますか？');">
                    🚪 ログアウト
                </a>
            </div>
        </div>
    </div>

    <div class="container">
        <div class="header">
            <h2>🍔 メニュー追加</h2>
            <p>イベント: <strong>${event.eventName}</strong></p>
        </div>

        <c:if test="${not empty errorMessage}">
            <div class="error-message">
                ❌ ${errorMessage}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/eventportal/host/MenuCreateExecute.action"
              method="post"
              onsubmit="return validateForm()">

            <!-- イベントID（hidden） -->
            <input type="hidden" name="eventId" value="${event.eventId}">

            <!-- メニュー名 -->
            <div class="form-group">
                <label for="menuName">
                    メニュー名<span class="required">*</span>
                </label>
                <input type="text"
                       id="menuName"
                       name="menuName"
                       required
                       maxlength="100"
                       placeholder="例: オリジナルTシャツ">
            </div>

            <!-- メニュー種別と価格 -->
            <div class="form-row">
                <div class="form-group">
                    <label for="menuType">
                        種別<span class="required">*</span>
                    </label>
                    <select id="menuType" name="menuType" required>
                        <option value="">選択してください</option>
                        <option value="グッズ">グッズ</option>
                        <option value="フード">フード</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="price">
                        価格<span class="required">*</span>
                    </label>
                    <input type="number"
                           id="price"
                           name="price"
                           required
                           min="0"
                           max="1000000"
                           placeholder="例: 3000">
                    <div class="help-text">円</div>
                </div>
            </div>

            <!-- 在庫数 -->
            <div class="form-group">
                <label for="stockQuantity">
                    在庫数<span class="required">*</span>
                </label>
                <input type="number"
                       id="stockQuantity"
                       name="stockQuantity"
                       required
                       min="0"
                       max="10000"
                       placeholder="例: 50">
                <div class="help-text">個</div>
            </div>

            <!-- 説明 -->
            <div class="form-group">
                <label for="description">
                    説明
                </label>
                <textarea id="description"
                          name="description"
                          maxlength="500"
                          placeholder="メニューの説明を入力してください"></textarea>
                <div class="help-text">最大500文字</div>
            </div>

            <!-- ボタングループ -->
            <div class="button-group">
                <button type="submit" class="btn btn-primary">
                    ✨ 追加する
                </button>
                <a href="${pageContext.request.contextPath}/eventportal/host/MenuList.action?eventId=${event.eventId}"
                   class="btn btn-secondary">
                    ← 戻る
                </a>
            </div>
        </form>
    </div>

    <script>
        console.log("=== menu_create.jsp 読み込み完了 ===");

        function validateForm() {
            console.log('=== フォームバリデーション開始 ===');

            const menuName = document.getElementById('menuName').value.trim();
            const menuType = document.getElementById('menuType').value;
            const price = document.getElementById('price').value;
            const stockQuantity = document.getElementById('stockQuantity').value;

            console.log('メニュー名:', menuName);
            console.log('種別:', menuType);
            console.log('価格:', price);
            console.log('在庫数:', stockQuantity);

            if (!menuName) {
                alert('メニュー名を入力してください');
                return false;
            }

            if (!menuType) {
                alert('種別を選択してください');
                return false;
            }

            if (!price || price < 0) {
                alert('価格を正しく入力してください');
                return false;
            }

            if (!stockQuantity || stockQuantity < 0) {
                alert('在庫数を正しく入力してください');
                return false;
            }

            console.log('✓ バリデーションOK');
            return confirm('この内容でメニューを追加しますか？');
        }
    </script>
</body>
</html>
