<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>イベント作成 | イベントポータル</title>
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

        .header h1 {
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
        .form-group input[type="date"],
        .form-group input[type="time"],
        .form-group input[type="number"],
        .form-group input[type="tel"],
        .form-group input[type="url"],
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
            min-height: 120px;
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
    <div class="container">
        <div class="header">
            <h1>✨ イベント作成</h1>
            <p>新しいイベントを作成してください</p>
        </div>

        <c:if test="${not empty errorMessage}">
            <div class="error-message">
                ❌ ${errorMessage}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/eventportal/host/HostEventCreateExecute.action"
              method="post"
              onsubmit="return validateForm()">

            <!-- イベント名 -->
            <div class="form-group">
                <label for="eventName">
                    イベント名<span class="required">*</span>
                </label>
                <input type="text"
                       id="eventName"
                       name="eventName"
                       required
                       maxlength="100"
                       placeholder="例: 春の音楽フェスティバル">
            </div>

            <!-- 開催日時 -->
            <div class="form-row">
                <div class="form-group">
                    <label for="holdingDate">
                        開催日<span class="required">*</span>
                    </label>
                    <input type="date"
                           id="holdingDate"
                           name="holdingDate"
                           required>
                </div>
                <div class="form-group">
                    <label for="holdingTime">
                        開催時刻<span class="required">*</span>
                    </label>
                    <input type="time"
                           id="holdingTime"
                           name="holdingTime"
                           required>
                </div>
            </div>

            <!-- 住所 -->
            <div class="form-group">
                <label for="address">
                    開催場所<span class="required">*</span>
                </label>
                <input type="text"
                       id="address"
                       name="address"
                       required
                       maxlength="200"
                       placeholder="例: 東京都渋谷区○○ホール">
            </div>

            <!-- 定員と開催状態 -->
            <div class="form-row">
                <div class="form-group">
                    <label for="maxCount">
                        定員<span class="required">*</span>
                    </label>
                    <input type="number"
                           id="maxCount"
                           name="maxCount"
                           required
                           min="1"
                           max="10000"
                           placeholder="例: 100">
                    <div class="help-text">1〜10000人の範囲で指定</div>
                </div>
                <div class="form-group">
                    <label for="eventHoldState">
                        開催状態<span class="required">*</span>
                    </label>
                    <select id="eventHoldState" name="eventHoldState" required>
                        <option value="1" selected>開催前</option>
                        <option value="2">開催中</option>
                        <option value="3">開催後</option>
                    </select>
                </div>
            </div>

            <!-- 電話番号 -->
            <div class="form-group">
                <label for="phoneNumber">
                    お問い合わせ電話番号
                </label>
                <input type="tel"
                       id="phoneNumber"
                       name="phoneNumber"
                       maxlength="15"
                       placeholder="例: 03-1234-5678">
            </div>

            <!-- リンク -->
            <div class="form-group">
                <label for="link">
                    イベント公式サイト
                </label>
                <input type="url"
                       id="link"
                       name="link"
                       maxlength="500"
                       placeholder="例: https://example.com">
            </div>

            <!-- イベント概要 -->
            <div class="form-group">
                <label for="eventOverview">
                    イベント概要<span class="required">*</span>
                </label>
                <textarea id="eventOverview"
                          name="eventOverview"
                          required
                          maxlength="1000"
                          placeholder="イベントの詳細を入力してください"></textarea>
                <div class="help-text">最大1000文字</div>
            </div>

            <!-- カテゴリID（オプション） -->
            <div class="form-group">
                <label for="categoryId">
                    カテゴリID
                </label>
                <input type="text"
                       id="categoryId"
                       name="categoryId"
                       maxlength="50"
                       placeholder="例: music, sports">
            </div>

            <!-- ボタングループ -->
            <div class="button-group">
                <button type="submit" class="btn btn-primary">
                    ✨ 作成する
                </button>
                <a href="${pageContext.request.contextPath}/eventportal/host/HostMenu.action"
                   class="btn btn-secondary">
                    ← 戻る
                </a>
            </div>
        </form>
    </div>

    <script>
        console.log("=== host_event_create.jsp 読み込み完了 ===");

        function validateForm() {
            console.log("=== フォームバリデーション開始 ===");

            const eventName = document.getElementById('eventName').value.trim();
            const holdingDate = document.getElementById('holdingDate').value;
            const holdingTime = document.getElementById('holdingTime').value;
            const address = document.getElementById('address').value.trim();
            const maxCount = document.getElementById('maxCount').value;
            const eventOverview = document.getElementById('eventOverview').value.trim();

            console.log('イベント名:', eventName);
            console.log('開催日:', holdingDate);
            console.log('開催時刻:', holdingTime);
            console.log('住所:', address);
            console.log('定員:', maxCount);
            console.log('概要:', eventOverview);

            if (!eventName) {
                alert('イベント名を入力してください');
                return false;
            }

            if (!holdingDate) {
                alert('開催日を選択してください');
                return false;
            }

            if (!holdingTime) {
                alert('開催時刻を選択してください');
                return false;
            }

            if (!address) {
                alert('開催場所を入力してください');
                return false;
            }

            if (!maxCount || maxCount < 1 || maxCount > 10000) {
                alert('定員は1〜10000人の範囲で入力してください');
                return false;
            }

            if (!eventOverview) {
                alert('イベント概要を入力してください');
                return false;
            }

            console.log('✓ バリデーションOK');
            return confirm('この内容でイベントを作成しますか？');
        }

        // 開催日の制限（今日以降のみ選択可能）
        document.addEventListener('DOMContentLoaded', function() {
            const dateInput = document.getElementById('holdingDate');
            const today = new Date().toISOString().split('T')[0];
            dateInput.setAttribute('min', today);
            console.log('最小日付設定:', today);
        });
    </script>
</body>
</html>
