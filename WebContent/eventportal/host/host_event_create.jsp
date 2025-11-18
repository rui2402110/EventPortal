<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>イベントポータル - 認証</title>
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
  </style>
</head>
<div class="container">
    <h1>イベント入力フォーム</h1>

   <form method="post" enctype="multipart/form-data" action="${pageContext.request.contextPath}/eventportal/host/HostEventCreateExecute.action">
        <div class="form-group">
            <label for="event_name">イベント名</label>
            <input type="text" id="event_name" name="event_name" placeholder="例: ドキドキマヤ文明鎮魂祭" required>
        </div>

       <div class="form-group">
            <label for="event_year">年</label>
            <select id="event_year" name="event_year" required>
                <option value="">選択してください</option>
                <option value="2024">2024年</option>
                <option value="2025">2025年</option>
                <option value="2026">2026年</option>
                <option value="2027">2027年</option>
                <option value="2028">2028年</option>
            </select>
        </div>

        <div class="form-group">
            <label for="event_month">月</label>
            <select id="event_month" name="event_month" required>
                <option value="">選択してください</option>
                <option value="1">1月</option>
                <option value="2">2月</option>
                <option value="3">3月</option>
                <option value="4">4月</option>
                <option value="5">5月</option>
                <option value="6">6月</option>
                <option value="7">7月</option>
                <option value="8">8月</option>
                <option value="9">9月</option>
                <option value="10">10月</option>
                <option value="11">11月</option>
                <option value="12">12月</option>
            </select>
        </div>

        <div class="form-group">
            <label for="event_day">日</label>
            <select id="event_day" name="event_day" required>
                <option value="">選択してください</option>
                <option value="1">1日</option>
                <option value="2">2日</option>
                <option value="3">3日</option>
                <option value="4">4日</option>
                <option value="5">5日</option>
                <option value="6">6日</option>
                <option value="7">7日</option>
                <option value="8">8日</option>
                <option value="9">9日</option>
                <option value="10">10日</option>
                <option value="11">11日</option>
                <option value="12">12日</option>
                <option value="13">13日</option>
                <option value="14">14日</option>
                <option value="15">15日</option>
                <option value="16">16日</option>
                <option value="17">17日</option>
                <option value="18">18日</option>
                <option value="19">19日</option>
                <option value="20">20日</option>
                <option value="21">21日</option>
                <option value="22">22日</option>
                <option value="23">23日</option>
                <option value="24">24日</option>
                <option value="25">25日</option>
                <option value="26">26日</option>
                <option value="27">27日</option>
                <option value="28">28日</option>
                <option value="29">29日</option>
                <option value="30">30日</option>
                <option value="31">31日</option>
            </select>
        </div>

        <div class="form-group">
            <label for="event_hour">時</label>
            <select id="event_hour" name="event_hour" required>
                <option value="">選択してください</option>
                <option value="0">0時</option>
                <option value="1">1時</option>
                <option value="2">2時</option>
                <option value="3">3時</option>
                <option value="4">4時</option>
                <option value="5">5時</option>
                <option value="6">6時</option>
                <option value="7">7時</option>
                <option value="8">8時</option>
                <option value="9">9時</option>
                <option value="10">10時</option>
                <option value="11">11時</option>
                <option value="12">12時</option>
                <option value="13">13時</option>
                <option value="14">14時</option>
                <option value="15">15時</option>
                <option value="16">16時</option>
                <option value="17">17時</option>
                <option value="18">18時</option>
                <option value="19">19時</option>
                <option value="20">20時</option>
                <option value="21">21時</option>
                <option value="22">22時</option>
                <option value="23">23時</option>
            </select>
        </div>

        <div class="form-group">
            <label for="event_minute">分</label>
            <select id="event_minute" name="event_minute" required>
                <option value="">選択してください</option>
                <option value="0">0分</option>
                <option value="5">5分</option>
                <option value="10">10分</option>
                <option value="15">15分</option>
                <option value="20">20分</option>
                <option value="25">25分</option>
                <option value="30">30分</option>
                <option value="35">35分</option>
                <option value="40">40分</option>
                <option value="45">45分</option>
                <option value="50">50分</option>
                <option value="55">55分</option>
            </select>
        </div>

        <div class="form-group">
            <label for="content">概要</label>
            <input type="text" id="content" name="content" placeholder="例: マヤ文明の魂を鎮魂します" required>
        </div>

        <div class="form-group">
            <label for="postalCode">郵便番号</label>
            <input type="text" id="postalCode" name="postalCode" placeholder="例: 123-4567" maxlength="8" required>
            <small>ハイフンを含めて入力してください</small>
        </div>

        <div class="form-group">
            <label for="prefecture">都道府県</label>
            <select id="prefecture" name="prefecture" required>
                <option value="">選択してください</option>
                <option value="北海道">北海道</option>
                <option value="青森県">青森県</option>
                <option value="岩手県">岩手県</option>
                <option value="宮城県">宮城県</option>
                <option value="秋田県">秋田県</option>
                <option value="山形県">山形県</option>
                <option value="福島県">福島県</option>
                <option value="茨城県">茨城県</option>
                <option value="栃木県">栃木県</option>
                <option value="群馬県">群馬県</option>
                <option value="埼玉県">埼玉県</option>
                <option value="千葉県">千葉県</option>
                <option value="東京都">東京都</option>
                <option value="神奈川県">神奈川県</option>
                <option value="新潟県">新潟県</option>
                <option value="富山県">富山県</option>
                <option value="石川県">石川県</option>
                <option value="福井県">福井県</option>
                <option value="山梨県">山梨県</option>
                <option value="長野県">長野県</option>
                <option value="岐阜県">岐阜県</option>
                <option value="静岡県">静岡県</option>
                <option value="愛知県">愛知県</option>
                <option value="三重県">三重県</option>
                <option value="滋賀県">滋賀県</option>
                <option value="京都府">京都府</option>
                <option value="大阪府">大阪府</option>
                <option value="兵庫県">兵庫県</option>
                <option value="奈良県">奈良県</option>
                <option value="和歌山県">和歌山県</option>
                <option value="鳥取県">鳥取県</option>
                <option value="島根県">島根県</option>
                <option value="岡山県">岡山県</option>
                <option value="広島県">広島県</option>
                <option value="山口県">山口県</option>
                <option value="徳島県">徳島県</option>
                <option value="香川県">香川県</option>
                <option value="愛媛県">愛媛県</option>
                <option value="高知県">高知県</option>
                <option value="福岡県">福岡県</option>
                <option value="佐賀県">佐賀県</option>
                <option value="長崎県">長崎県</option>
                <option value="熊本県">熊本県</option>
                <option value="大分県">大分県</option>
                <option value="宮崎県">宮崎県</option>
                <option value="鹿児島県">鹿児島県</option>
                <option value="沖縄県">沖縄県</option>
            </select>
        </div>

        <div class="form-group">
            <label for="city">市区町村</label>
            <input type="text" id="city" name="city" placeholder="例: 渋谷区" required>
        </div>

        <div class="form-group">
            <label for="street">町名・番地</label>
            <input type="text" id="street" name="street" placeholder="例: 神南1-19-11" required>
        </div>

        <div class="form-group">
            <label for="building">建物名・部屋番号</label>
            <input type="text" id="building" name="building" placeholder="例: パークウェルビル5F(任意)">
            <small>マンション名や部屋番号がある場合は入力してください</small>
        </div>

        <div class="form-group">
    <label>会場マップ画像</label>
    <input type="file" id="eventMapImage" name="eventMapImage" accept="image/*" onchange="previewImage(event, 'mapPreview')">
    <div class="preview" id="mapPreview"></div>
</div>

<div class="form-group">
    <label>会場内マップ画像</label>
    <input type="file" id="eventInnerMapImage" name="eventInnerMapImage" accept="image/*" onchange="previewImage(event, 'innerMapPreview')">
    <div class="preview" id="innerMapPreview"></div>
</div>

        <div class="form-group">
            <label for="maxcount">最大人数</label>
            <input type="text" id="maxcount" name="maxcount" placeholder="例:　500">
        </div>

        <div class="form-group">
            <label for="category">カテゴリ</label>
            <input type="text" id="category" name="category" placeholder="カンマは,ですよ">
            <small>カンマ区切りで入力してください</small>
        </div>

        <div class="form-group">
            <label for="phonenumber">電話番号</label>
            <input type="text" id="phonenumber" name="phonenumber" placeholder="例:　0120-500-500">
        </div>

        <div class="form-group">
            <label for="link">リンク</label>
            <input type="text" id="link" name="link" placeholder="例:　https://job-hunting.o-hara.ac.jp/Account?ReturnUrl=%2fActivityMember%2fSearch_Company%2f0%2f1">
        </div>

        <div class="form-group">
            <label for="credit">クレジット(任意)</label>
            <input type="text" id="credit" name="credit" placeholder="例:　引間実業">
        </div>

        <div class="form-actions">
            <button type="button" id="clearBtn">クリア</button>
            <button type="submit">確認</button>
        </div>
    </form>

    <div id="result" class="result-section" style="display: none;">
        <h2>入力内容確認</h2>
        <div id="resultContent"></div>
        <div class="form-actions">
            <button type="button" id="editBtn">編集</button>
            <button type="button" id="submitBtn">送信</button>
        </div>
    </div>
</div>