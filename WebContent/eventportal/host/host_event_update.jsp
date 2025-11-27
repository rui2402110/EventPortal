<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="bean.Event" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.LocalTime" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>イベントポータル - イベント編集</title>
  <style>
/* 基本スタイル */
.container {
    max-width: 800px;
    margin: 0 auto;
    padding: 20px;
    font-family: Arial, sans-serif;
}

.form-group {
    margin-bottom: 20px;
}

.form-group label {
    display: block;
    margin-bottom: 5px;
    font-weight: bold;
}

.form-group input,
.form-group select {
    width: 100%;
    padding: 8px;
    border: 1px solid #ddd;
    border-radius: 4px;
    box-sizing: border-box;
}

/* 日付と時刻の入力欄 */
.form-group input[type="date"],
.form-group input[type="time"] {
    font-size: 16px;
    padding: 10px;
}

.form-actions {
    text-align: center;
    margin-top: 30px;
}

.form-actions button {
    margin: 0 10px;
    padding: 10px 20px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
}

.form-actions button[type="submit"] {
    background-color: #007bff;
    color: white;
}

.form-actions button[type="button"] {
    background-color: #6c757d;
    color: white;
}

.loading {
    text-align: center;
    padding: 20px;
    font-size: 18px;
}

.result-section {
    background-color: #f8f9fa;
    padding: 20px;
    border-radius: 8px;
    margin-top: 20px;
}

.result-section table {
    width: 100%;
    border-collapse: collapse;
}

.result-section td {
    padding: 8px;
    border-bottom: 1px solid #ddd;
}

.error {
    color: red;
    background-color: #ffe6e6;
    padding: 10px;
    border-radius: 4px;
}

.form-group small {
    color: #666;
    font-size: 12px;
}

.preview {
    margin-top: 10px;
}

.preview img {
    max-width: 300px;
    max-height: 200px;
    border: 1px solid #ddd;
    border-radius: 4px;
}

.current-image {
    margin-top: 10px;
}

.current-image img {
    max-width: 300px;
    max-height: 200px;
    border: 1px solid #ddd;
    border-radius: 4px;
}

.current-image p {
    margin: 5px 0;
    font-size: 12px;
    color: #666;
}
  </style>
  <script>
    function previewImage(event, previewId) {
        const file = event.target.files[0];
        const preview = document.getElementById(previewId);

        if (file) {
            const reader = new FileReader();
            reader.onload = function(e) {
                preview.innerHTML = '<img src="' + e.target.result + '" alt="プレビュー">';
            }
            reader.readAsDataURL(file);
        } else {
            preview.innerHTML = '';
        }
    }
  </script>
</head>
<body>
<%
    // リクエストスコープからeventオブジェクトを取得
    Event event = (Event) request.getAttribute("evt");

    // 日付と時刻の初期値を取得
    int eventYear = 2024;
    int eventMonth = 1;
    int eventDay = 1;
    int eventHour = 0;
    int eventMinute = 0;

    if (event != null) {
        LocalDate holdingDate = event.getHoldingDate();
        LocalTime holdingTime = event.getHoldingTime();

        if (holdingDate != null) {
            eventYear = holdingDate.getYear();
            eventMonth = holdingDate.getMonthValue();
            eventDay = holdingDate.getDayOfMonth();
        }

        if (holdingTime != null) {
            eventHour = holdingTime.getHour();
            eventMinute = holdingTime.getMinute();
        }
    }

    // 住所を分解（可能な場合）
    String postalCode = "";
    String prefecture = "";
    String city = "";
    String street = "";
    String building = "";

    if (event != null && event.getAddress() != null) {
        String address = event.getAddress();
        // 住所全体を表示（分解は複雑なので、まとめて表示）
        // 必要に応じて分解ロジックを追加
    }
%>
<div class="container">
    <h1>イベント編集フォーム</h1>

   <form method="post" enctype="multipart/form-data" action="${pageContext.request.contextPath}/eventportal/host/HostEventUpdateExecute.action">
        <!-- イベントIDを隠しフィールドで送信 -->
        <input type="hidden" name="event_id" value="<%= event != null ? event.getEventId() : "" %>">

        <div class="form-group">
            <label for="event_name">イベント名</label>
            <input type="text" id="event_name" name="event_name"
                   value="<%= event != null && event.getEventName() != null ? event.getEventName() : "" %>"
                   placeholder="例: ドキドキマヤ文明鎮魂祭" required>
        </div>

       <div class="form-group">
            <label for="event_year">年</label>
            <select id="event_year" name="event_year" required>
                <option value="">選択してください</option>
                <% for (int year = 2024; year <= 2028; year++) { %>
                    <option value="<%= year %>" <%= (year == eventYear) ? "selected" : "" %>><%= year %>年</option>
                <% } %>
            </select>
        </div>

        <div class="form-group">
            <label for="event_month">月</label>
            <select id="event_month" name="event_month" required>
                <option value="">選択してください</option>
                <% for (int month = 1; month <= 12; month++) { %>
                    <option value="<%= month %>" <%= (month == eventMonth) ? "selected" : "" %>><%= month %>月</option>
                <% } %>
            </select>
        </div>

        <div class="form-group">
            <label for="event_day">日</label>
            <select id="event_day" name="event_day" required>
                <option value="">選択してください</option>
                <% for (int day = 1; day <= 31; day++) { %>
                    <option value="<%= day %>" <%= (day == eventDay) ? "selected" : "" %>><%= day %>日</option>
                <% } %>
            </select>
        </div>

        <div class="form-group">
            <label for="event_hour">時</label>
            <select id="event_hour" name="event_hour" required>
                <option value="">選択してください</option>
                <% for (int hour = 0; hour <= 23; hour++) { %>
                    <option value="<%= hour %>" <%= (hour == eventHour) ? "selected" : "" %>><%= hour %>時</option>
                <% } %>
            </select>
        </div>

        <div class="form-group">
            <label for="event_minute">分</label>
            <select id="event_minute" name="event_minute" required>
                <option value="">選択してください</option>
                <% for (int minute = 0; minute <= 55; minute += 5) { %>
                    <option value="<%= minute %>" <%= (minute == eventMinute) ? "selected" : "" %>><%= minute %>分</option>
                <% } %>
            </select>
        </div>

        <div class="form-group">
            <label for="content">概要</label>
            <input type="text" id="content" name="content"
                   value="<%= event != null && event.getEventOverview() != null ? event.getEventOverview() : "" %>"
                   placeholder="例: マヤ文明の魂を鎮魂します" required>
        </div>

        <% if (event != null && event.getAddress() != null && !event.getAddress().isEmpty()) { %>
        <div class="form-group">
            <label>現在の住所</label>
            <div style="background-color: #f0f0f0; padding: 10px; border-radius: 4px; margin-bottom: 10px;">
                <%= event.getAddress() %>
            </div>
            <small style="color: #666;">※以下のフォームで新しい住所を入力してください</small>
        </div>
        <% } %>

        <div class="form-group">
            <label for="postalCode">郵便番号</label>
            <input type="text" id="postalCode" name="postalCode"
                   value="<%= postalCode %>"
                   placeholder="例: 123-4567" maxlength="8" required>
            <small>ハイフンを含めて入力してください</small>
        </div>

        <div class="form-group">
            <label for="prefecture">都道府県</label>
            <select id="prefecture" name="prefecture" required>
                <option value="">選択してください</option>
                <option value="北海道" <%= "北海道".equals(prefecture) ? "selected" : "" %>>北海道</option>
                <option value="青森県" <%= "青森県".equals(prefecture) ? "selected" : "" %>>青森県</option>
                <option value="岩手県" <%= "岩手県".equals(prefecture) ? "selected" : "" %>>岩手県</option>
                <option value="宮城県" <%= "宮城県".equals(prefecture) ? "selected" : "" %>>宮城県</option>
                <option value="秋田県" <%= "秋田県".equals(prefecture) ? "selected" : "" %>>秋田県</option>
                <option value="山形県" <%= "山形県".equals(prefecture) ? "selected" : "" %>>山形県</option>
                <option value="福島県" <%= "福島県".equals(prefecture) ? "selected" : "" %>>福島県</option>
                <option value="茨城県" <%= "茨城県".equals(prefecture) ? "selected" : "" %>>茨城県</option>
                <option value="栃木県" <%= "栃木県".equals(prefecture) ? "selected" : "" %>>栃木県</option>
                <option value="群馬県" <%= "群馬県".equals(prefecture) ? "selected" : "" %>>群馬県</option>
                <option value="埼玉県" <%= "埼玉県".equals(prefecture) ? "selected" : "" %>>埼玉県</option>
                <option value="千葉県" <%= "千葉県".equals(prefecture) ? "selected" : "" %>>千葉県</option>
                <option value="東京都" <%= "東京都".equals(prefecture) ? "selected" : "" %>>東京都</option>
                <option value="神奈川県" <%= "神奈川県".equals(prefecture) ? "selected" : "" %>>神奈川県</option>
                <option value="新潟県" <%= "新潟県".equals(prefecture) ? "selected" : "" %>>新潟県</option>
                <option value="富山県" <%= "富山県".equals(prefecture) ? "selected" : "" %>>富山県</option>
                <option value="石川県" <%= "石川県".equals(prefecture) ? "selected" : "" %>>石川県</option>
                <option value="福井県" <%= "福井県".equals(prefecture) ? "selected" : "" %>>福井県</option>
                <option value="山梨県" <%= "山梨県".equals(prefecture) ? "selected" : "" %>>山梨県</option>
                <option value="長野県" <%= "長野県".equals(prefecture) ? "selected" : "" %>>長野県</option>
                <option value="岐阜県" <%= "岐阜県".equals(prefecture) ? "selected" : "" %>>岐阜県</option>
                <option value="静岡県" <%= "静岡県".equals(prefecture) ? "selected" : "" %>>静岡県</option>
                <option value="愛知県" <%= "愛知県".equals(prefecture) ? "selected" : "" %>>愛知県</option>
                <option value="三重県" <%= "三重県".equals(prefecture) ? "selected" : "" %>>三重県</option>
                <option value="滋賀県" <%= "滋賀県".equals(prefecture) ? "selected" : "" %>>滋賀県</option>
                <option value="京都府" <%= "京都府".equals(prefecture) ? "selected" : "" %>>京都府</option>
                <option value="大阪府" <%= "大阪府".equals(prefecture) ? "selected" : "" %>>大阪府</option>
                <option value="兵庫県" <%= "兵庫県".equals(prefecture) ? "selected" : "" %>>兵庫県</option>
                <option value="奈良県" <%= "奈良県".equals(prefecture) ? "selected" : "" %>>奈良県</option>
                <option value="和歌山県" <%= "和歌山県".equals(prefecture) ? "selected" : "" %>>和歌山県</option>
                <option value="鳥取県" <%= "鳥取県".equals(prefecture) ? "selected" : "" %>>鳥取県</option>
                <option value="島根県" <%= "島根県".equals(prefecture) ? "selected" : "" %>>島根県</option>
                <option value="岡山県" <%= "岡山県".equals(prefecture) ? "selected" : "" %>>岡山県</option>
                <option value="広島県" <%= "広島県".equals(prefecture) ? "selected" : "" %>>広島県</option>
                <option value="山口県" <%= "山口県".equals(prefecture) ? "selected" : "" %>>山口県</option>
                <option value="徳島県" <%= "徳島県".equals(prefecture) ? "selected" : "" %>>徳島県</option>
                <option value="香川県" <%= "香川県".equals(prefecture) ? "selected" : "" %>>香川県</option>
                <option value="愛媛県" <%= "愛媛県".equals(prefecture) ? "selected" : "" %>>愛媛県</option>
                <option value="高知県" <%= "高知県".equals(prefecture) ? "selected" : "" %>>高知県</option>
                <option value="福岡県" <%= "福岡県".equals(prefecture) ? "selected" : "" %>>福岡県</option>
                <option value="佐賀県" <%= "佐賀県".equals(prefecture) ? "selected" : "" %>>佐賀県</option>
                <option value="長崎県" <%= "長崎県".equals(prefecture) ? "selected" : "" %>>長崎県</option>
                <option value="熊本県" <%= "熊本県".equals(prefecture) ? "selected" : "" %>>熊本県</option>
                <option value="大分県" <%= "大分県".equals(prefecture) ? "selected" : "" %>>大分県</option>
                <option value="宮崎県" <%= "宮崎県".equals(prefecture) ? "selected" : "" %>>宮崎県</option>
                <option value="鹿児島県" <%= "鹿児島県".equals(prefecture) ? "selected" : "" %>>鹿児島県</option>
                <option value="沖縄県" <%= "沖縄県".equals(prefecture) ? "selected" : "" %>>沖縄県</option>
            </select>
        </div>

        <div class="form-group">
            <label for="city">市区町村</label>
            <input type="text" id="city" name="city"
                   value="<%= city %>"
                   placeholder="例: 渋谷区" required>
        </div>

        <div class="form-group">
            <label for="street">町名・番地</label>
            <input type="text" id="street" name="street"
                   value="<%= street %>"
                   placeholder="例: 神南1-19-11" required>
        </div>

        <div class="form-group">
            <label for="building">建物名・部屋番号</label>
            <input type="text" id="building" name="building"
                   value="<%= building %>"
                   placeholder="例: パークウェルビル5F(任意)">
            <small>マンション名や部屋番号がある場合は入力してください</small>
        </div>

        <div class="form-group">
            <label>会場マップ画像</label>
            <% if (event != null && event.getMapOutOfHall() != null && !event.getMapOutOfHall().isEmpty()) { %>
                <div class="current-image">
                    <p>現在の画像:</p>
                    <img src="<%= event.getMapOutOfHall() %>" alt="会場マップ" onerror="this.style.display='none'; this.nextElementSibling.style.display='block';">
                    <p style="display:none; color: #999;">画像を読み込めませんでした: <%= event.getMapOutOfHall() %></p>
                    <p>新しい画像を選択すると上書きされます</p>
                </div>
            <% } else { %>
                <p style="color: #999;">現在、画像は登録されていません</p>
            <% } %>
            <input type="file" id="eventMapImage" name="eventMapImage" accept="image/*" onchange="previewImage(event, 'mapPreview')">
            <div class="preview" id="mapPreview"></div>
        </div>

        <div class="form-group">
            <label>会場内マップ画像</label>
            <% if (event != null && event.getMapInHall() != null && !event.getMapInHall().isEmpty()) { %>
                <div class="current-image">
                    <p>現在の画像:</p>
                    <img src="<%= event.getMapInHall() %>" alt="会場内マップ" onerror="this.style.display='none'; this.nextElementSibling.style.display='block';">
                    <p style="display:none; color: #999;">画像を読み込めませんでした: <%= event.getMapInHall() %></p>
                    <p>新しい画像を選択すると上書きされます</p>
                </div>
            <% } else { %>
                <p style="color: #999;">現在、画像は登録されていません</p>
            <% } %>
            <input type="file" id="eventInnerMapImage" name="eventInnerMapImage" accept="image/*" onchange="previewImage(event, 'innerMapPreview')">
            <div class="preview" id="innerMapPreview"></div>
        </div>

        <div class="form-group">
            <label for="maxcount">最大人数</label>
            <input type="text" id="maxcount" name="maxcount"
                   value="<%= event != null && event.getMaxCount() != null ? event.getMaxCount() : "" %>"
                   placeholder="例: 500">
        </div>

        <div class="form-group">
            <label for="category">カテゴリ</label>
            <input type="text" id="category" name="category"
                   value="<%= event != null && event.getCategoryId() != null ? event.getCategoryId() : "" %>"
                   placeholder="カンマは,ですよ">
            <small>カンマ区切りで入力してください</small>
        </div>

        <div class="form-group">
            <label for="phonenumber">電話番号</label>
            <input type="text" id="phonenumber" name="phonenumber"
                   value="<%= event != null && event.getPhoneNumber() != null ? event.getPhoneNumber() : "" %>"
                   placeholder="例: 0120-500-500">
        </div>

        <div class="form-group">
            <label for="link">リンク</label>
            <input type="text" id="link" name="link"
                   value="<%= event != null && event.getLink() != null ? event.getLink() : "" %>"
                   placeholder="例: https://job-hunting.o-hara.ac.jp/Account?ReturnUrl=%2fActivityMember%2fSearch_Company%2f0%2f1">
        </div>

        <div class="form-group">
            <label for="credit">クレジット(任意)</label>
            <input type="text" id="credit" name="credit"
                   value="<%= event != null && event.getCredit() != null ? event.getCredit() : "" %>"
                   placeholder="例: 引間実業">
        </div>

        <div class="form-actions">
            <button type="button" onclick="history.back()">キャンセル</button>
            <button type="submit">更新</button>
        </div>
    </form>
</div>
</body>
</html>