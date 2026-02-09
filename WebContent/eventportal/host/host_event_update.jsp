<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>イベント編集 | イベントポータル</title>
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
        .form-group input[type="file"],
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

        .form-group input[type="file"] {
            padding: 10px;
            cursor: pointer;
        }

        .current-image {
            margin-top: 10px;
            padding: 10px;
            background: #f5f5f5;
            border-radius: 8px;
        }

        .current-image img {
            max-width: 100%;
            max-height: 200px;
            border-radius: 8px;
            margin-top: 10px;
        }

        .current-image p {
            font-size: 12px;
            color: #666;
            margin-bottom: 5px;
        }

        .image-preview {
            margin-top: 10px;
            display: none;
        }

        .image-preview img {
            max-width: 100%;
            max-height: 300px;
            border-radius: 8px;
            border: 2px solid #e0e0e0;
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
            <h1>✏️ イベント編集</h1>
            <p>イベント情報を編集してください</p>
        </div>

        <c:if test="${not empty errorMessage}">
            <div class="error-message">
                ❌ ${errorMessage}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/eventportal/host/EventUpdateExecute.action"
              method="post"
              enctype="multipart/form-data"
              onsubmit="return validateForm()">

            <!-- イベントID（hidden） -->
            <input type="hidden" name="eventId" value="${event.eventId}">

            <!-- イベント名 -->
            <div class="form-group">
                <label for="eventName">
                    イベント名<span class="required">*</span>
                </label>
                <input type="text"
                       id="eventName"
                       name="eventName"
                       value="${event.eventName}"
                       required
                       maxlength="100">
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
                           value="${event.holdingDate}"
                           required>
                </div>
                <div class="form-group">
                    <label for="holdingTime">
                        開催時刻<span class="required">*</span>
                    </label>
                    <input type="time"
                           id="holdingTime"
                           name="holdingTime"
                           value="${event.holdingTime}"
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
                       value="${event.address}"
                       required
                       maxlength="200">
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
                           value="${event.maxCount}"
                           required
                           min="1"
                           max="10000">
                    <div class="help-text">1〜10000人の範囲で指定</div>
                </div>
                <div class="form-group">
                    <label for="eventHoldState">
                        開催状態<span class="required">*</span>
                    </label>
                    <select id="eventHoldState" name="eventHoldState" required>
                        <option value="1" ${event.eventHoldState == '1' ? 'selected' : ''}>開催前</option>
                        <option value="2" ${event.eventHoldState == '2' ? 'selected' : ''}>開催中</option>
                        <option value="3" ${event.eventHoldState == '3' ? 'selected' : ''}>開催後</option>
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
                       value="${event.phoneNumber}"
                       maxlength="15">
            </div>

            <!-- リンク -->
            <div class="form-group">
                <label for="link">
                    イベント公式サイト
                </label>
                <input type="url"
                       id="link"
                       name="link"
                       value="${event.link}"
                       maxlength="500">
            </div>

            <!-- イベント概要 -->
            <div class="form-group">
                <label for="eventOverview">
                    イベント概要<span class="required">*</span>
                </label>
                <textarea id="eventOverview"
                          name="eventOverview"
                          required
                          maxlength="1000">${event.eventOverview}</textarea>
                <div class="help-text">最大1000文字</div>
            </div>

            <!-- 会場マップ（画像アップロード） -->
            <div class="form-row">
                <div class="form-group">
                    <label for="mapInHall">
                        🗺️ 会場内マップ
                    </label>

                    <c:if test="${not empty event.mapInHall}">
                        <div class="current-image">
                            <p>📎 現在の画像:</p>
                            <img src="${pageContext.request.contextPath}/${event.mapInHall}" alt="会場内マップ">
                        </div>
                    </c:if>

                    <input type="file"
                           id="mapInHall"
                           name="mapInHall"
                           accept="image/*"
                           onchange="previewImage(this, 'previewInHall')">
                    <div class="help-text">新しい画像を選択すると置き換えられます（PNG, JPG, 最大5MB）</div>
                    <div id="previewInHall" class="image-preview"></div>
                </div>

                <div class="form-group">
                    <label for="mapOutOfHall">
                        🗺️ 会場外マップ
                    </label>

                    <c:if test="${not empty event.mapOutOfHall}">
                        <div class="current-image">
                            <p>📎 現在の画像:</p>
                            <img src="${pageContext.request.contextPath}/${event.mapOutOfHall}" alt="会場外マップ">
                        </div>
                    </c:if>

                    <input type="file"
                           id="mapOutOfHall"
                           name="mapOutOfHall"
                           accept="image/*"
                           onchange="previewImage(this, 'previewOutOfHall')">
                    <div class="help-text">新しい画像を選択すると置き換えられます（PNG, JPG, 最大5MB）</div>
                    <div id="previewOutOfHall" class="image-preview"></div>
                </div>
            </div>

            <!-- カテゴリID（オプション） -->
            <div class="form-group">
                <label for="categoryId">
                    カテゴリID
                </label>
                <input type="text"
                       id="categoryId"
                       name="categoryId"
                       value="${event.categoryId}"
                       maxlength="50">
            </div>

            <!-- ボタングループ -->
            <div class="button-group">
                <button type="submit" class="btn btn-primary">
                    💾 更新する
                </button>
                <a href="${pageContext.request.contextPath}/eventportal/host/HostMenu.action"
                   class="btn btn-secondary">
                    ← 戻る
                </a>
            </div>
        </form>
    </div>

    <script>
        console.log("=== host_event_update.jsp 読み込み完了 ===");

        // 画像プレビュー機能
        function previewImage(input, previewId) {
            const preview = document.getElementById(previewId);

            if (input.files && input.files[0]) {
                const file = input.files[0];

                // ファイルサイズチェック（5MB）
                if (file.size > 5 * 1024 * 1024) {
                    alert('ファイルサイズが大きすぎます。5MB以下のファイルを選択してください。');
                    input.value = '';
                    preview.style.display = 'none';
                    return;
                }

                // 画像形式チェック
                if (!file.type.match('image.*')) {
                    alert('画像ファイルを選択してください。');
                    input.value = '';
                    preview.style.display = 'none';
                    return;
                }

                const reader = new FileReader();

                reader.onload = function(e) {
                    preview.innerHTML = '<p style="font-size:12px;color:#2ecc71;margin-bottom:5px;">✓ 新しい画像プレビュー:</p><img src="' + e.target.result + '" alt="プレビュー">';
                    preview.style.display = 'block';
                };

                reader.readAsDataURL(file);

                console.log('画像選択:', file.name, 'サイズ:', (file.size / 1024).toFixed(2) + 'KB');
            } else {
                preview.style.display = 'none';
            }
        }

        function validateForm() {
            console.log('=== フォームバリデーション開始 ===');

            const eventName = document.getElementById('eventName').value.trim();
            const holdingDate = document.getElementById('holdingDate').value;
            const holdingTime = document.getElementById('holdingTime').value;
            const address = document.getElementById('address').value.trim();
            const maxCount = document.getElementById('maxCount').value;
            const eventOverview = document.getElementById('eventOverview').value.trim();

            if (!eventName || !holdingDate || !holdingTime || !address || !maxCount || !eventOverview) {
                alert('必須項目を入力してください');
                return false;
            }

            if (maxCount < 1 || maxCount > 10000) {
                alert('定員は1〜10000人の範囲で入力してください');
                return false;
            }

            console.log('✓ バリデーションOK');
            return confirm('この内容でイベントを更新しますか？');
        }
    </script>
</body>
</html>
