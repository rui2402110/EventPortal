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

/* カレンダー用スタイル */
.calendar-container {
    position: relative;
    display: inline-block;
    width: 100%;
}

.calendar-input {
    cursor: pointer;
    background-color: #f8f9fa;
}

.calendar-popup {
    position: absolute;
    top: 100%;
    left: 0;
    z-index: 1000;
    background: white;
    border: 1px solid #ddd;
    border-radius: 8px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    padding: 16px;
    width: 320px;
    display: none;
}

.calendar-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;
}

.calendar-nav-btn {
    background: none;
    border: none;
    cursor: pointer;
    padding: 8px;
    border-radius: 50%;
    width: 32px;
    height: 32px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 18px;
}

.calendar-nav-btn:hover {
    background-color: #f0f0f0;
}

.calendar-title {
    font-size: 16px;
    font-weight: bold;
}

.calendar-weekdays {
    display: grid;
    grid-template-columns: repeat(7, 1fr);
    gap: 2px;
    margin-bottom: 8px;
}

.calendar-weekday {
    text-align: center;
    font-size: 12px;
    font-weight: bold;
    color: #666;
    padding: 8px 4px;
}

.calendar-days {
    display: grid;
    grid-template-columns: repeat(7, 1fr);
    gap: 2px;
    margin-bottom: 16px;
}

.calendar-day {
    width: 36px;
    height: 36px;
    border: none;
    background: none;
    cursor: pointer;
    border-radius: 4px;
    font-size: 14px;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.2s;
}

.calendar-day:hover {
    background-color: #e3f2fd;
}

.calendar-day.other-month {
    color: #ccc;
}

.calendar-day.selected {
    background-color: #2196f3;
    color: white;
}

.calendar-day.today {
    background-color: #e1f5fe;
    color: #0277bd;
    font-weight: bold;
}

.time-selectors {
    display: flex;
    gap: 16px;
    margin-bottom: 16px;
}

.time-selector {
    flex: 1;
}

.time-selector label {
    display: block;
    margin-bottom: 4px;
    font-size: 12px;
    font-weight: bold;
}

.time-selector select {
    width: 100%;
    padding: 4px;
    border: 1px solid #ddd;
    border-radius: 4px;
}

.selected-datetime {
    background-color: #f5f5f5;
    padding: 12px;
    border-radius: 4px;
    margin-bottom: 16px;
    font-size: 14px;
}

.calendar-buttons {
    display: flex;
    gap: 8px;
    justify-content: flex-end;
}

.calendar-btn {
    padding: 6px 12px;
    border: 1px solid #ddd;
    border-radius: 4px;
    cursor: pointer;
    font-size: 12px;
}

.calendar-btn.primary {
    background-color: #2196f3;
    color: white;
    border-color: #2196f3;
}

.calendar-btn:hover {
    background-color: #f0f0f0;
}

.calendar-btn.primary:hover {
    background-color: #1976d2;
}
  </style>
</head>
<div class="container">
    <h1>イベント入力フォーム</h1>

    <form id="addressForm">
        <div class="form-group">
            <label for="event_name">イベント名</label>
            <input type="text" id="event_name" name="event_name" placeholder="例: ドキドキマヤ文明鎮魂祭" required>
        </div>

        <div class="form-group">
            <label for="days">日時</label>
            <div class="calendar-container">
                <input type="text" id="days" name="days" class="calendar-input" placeholder="日時を選択してください" readonly required>
                <div id="calendar-popup" class="calendar-popup">
                    <div class="calendar-header">
                        <button type="button" class="calendar-nav-btn" id="prev-month">‹</button>
                        <div class="calendar-title" id="calendar-title"></div>
                        <button type="button" class="calendar-nav-btn" id="next-month">›</button>
                    </div>

                    <div class="calendar-weekdays">
                        <div class="calendar-weekday">日</div>
                        <div class="calendar-weekday">月</div>
                        <div class="calendar-weekday">火</div>
                        <div class="calendar-weekday">水</div>
                        <div class="calendar-weekday">木</div>
                        <div class="calendar-weekday">金</div>
                        <div class="calendar-weekday">土</div>
                    </div>

                    <div class="calendar-days" id="calendar-days"></div>

                    <div class="time-selectors">
                        <div class="time-selector">
                            <label>時</label>
                            <select id="hour-select"></select>
                        </div>
                        <div class="time-selector">
                            <label>分</label>
                            <select id="minute-select"></select>
                        </div>
                    </div>

                    <div class="selected-datetime">
                        <strong>選択中:</strong> <span id="selected-display">未選択</span>
                    </div>

                    <div class="calendar-buttons">
                        <button type="button" class="calendar-btn" id="calendar-cancel">キャンセル</button>
                        <button type="button" class="calendar-btn primary" id="calendar-confirm">決定</button>
                    </div>
                </div>
            </div>
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
            <input type="file" id="eventMapImage" accept="image/*" onchange="previewImage(event, 'mapPreview')">
            <div class="preview" id="mapPreview"></div>
        </div>

        <div class="form-group">
            <label>会場内マップ画像</label>
            <input type="file" id="eventInnerMapImage" accept="image/*" onchange="previewImage(event, 'innerMapPreview')">
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
<script>
//グローバル変数
let currentCalendarDate = new Date();
let selectedDateTime = new Date();
let isCalendarOpen = false;

// DOM要素
const calendarInput = document.getElementById('days');
const calendarPopup = document.getElementById('calendar-popup');
const calendarTitle = document.getElementById('calendar-title');
const calendarDays = document.getElementById('calendar-days');
const hourSelect = document.getElementById('hour-select');
const minuteSelect = document.getElementById('minute-select');
const selectedDisplay = document.getElementById('selected-display');
const prevMonthBtn = document.getElementById('prev-month');
const nextMonthBtn = document.getElementById('next-month');
const calendarCancelBtn = document.getElementById('calendar-cancel');
const calendarConfirmBtn = document.getElementById('calendar-confirm');

// カレンダー初期化
function initializeCalendar() {
    // 時間の選択肢を生成
    hourSelect.innerHTML = '';
    for (let i = 0; i < 24; i++) {
        const option = document.createElement('option');
        option.value = i;
        option.textContent = i.toString().padStart(2, '0') + '時';
        hourSelect.appendChild(option);
    }

    // 分の選択肢を生成(5分刻み)
    minuteSelect.innerHTML = '';
    for (let i = 0; i < 60; i += 5) {
        const option = document.createElement('option');
        option.value = i;
        option.textContent = i.toString().padStart(2, '0') + '分';
        minuteSelect.appendChild(option);
    }

    // デフォルト時間を現在時刻に設定
    const now = new Date();
    selectedDateTime = new Date(now.getFullYear(), now.getMonth(), now.getDate(), now.getHours(), Math.floor(now.getMinutes() / 5) * 5);
    currentCalendarDate = new Date(selectedDateTime);

    hourSelect.value = selectedDateTime.getHours();
    minuteSelect.value = selectedDateTime.getMinutes();

    updateCalendarDisplay();
    updateSelectedDisplay();
}

// カレンダー表示更新
function updateCalendarDisplay() {
    const year = currentCalendarDate.getFullYear();
    const month = currentCalendarDate.getMonth();

    calendarTitle.textContent = `${year}年 ${month + 1}月`;

    const firstDay = new Date(year, month, 1).getDay();
    const daysInMonth = new Date(year, month + 1, 0).getDate();
    const daysInPrevMonth = new Date(year, month, 0).getDate();

    calendarDays.innerHTML = '';

    // 前月の日付
    for (let i = firstDay - 1; i >= 0; i--) {
        const day = daysInPrevMonth - i;
        const dayElement = createDayElement(day, true, false, false);
        calendarDays.appendChild(dayElement);
    }

    // 今月の日付
    const today = new Date();
    for (let day = 1; day <= daysInMonth; day++) {
        const isToday = (today.getFullYear() === year && today.getMonth() === month && today.getDate() === day);
        const isSelected = (selectedDateTime.getFullYear() === year && selectedDateTime.getMonth() === month && selectedDateTime.getDate() === day);
        const dayElement = createDayElement(day, false, isToday, isSelected);
        calendarDays.appendChild(dayElement);
    }

    // 次月の日付(42セル埋めるため)
    const totalCells = 42;
    const currentCells = firstDay + daysInMonth;
    for (let day = 1; currentCells + day - 1 < totalCells; day++) {
        const dayElement = createDayElement(day, true, false, false);
        calendarDays.appendChild(dayElement);
    }
}

// 日付要素作成
function createDayElement(day, isOtherMonth, isToday, isSelected) {
    const dayElement = document.createElement('button');
    dayElement.type = 'button';
    dayElement.className = 'calendar-day';
    dayElement.textContent = day;

    if (isOtherMonth) {
        dayElement.classList.add('other-month');
    }
    if (isToday) {
        dayElement.classList.add('today');
    }
    if (isSelected) {
        dayElement.classList.add('selected');
    }

    dayElement.addEventListener('click', () => {
        if (isOtherMonth) {
            // 前月・次月の日付をクリックした場合は月を移動
            if (day > 15) {
                // 前月
                currentCalendarDate.setMonth(currentCalendarDate.getMonth() - 1);
            } else {
                // 次月
                currentCalendarDate.setMonth(currentCalendarDate.getMonth() + 1);
            }
        }

        // 選択日を更新
        selectedDateTime.setFullYear(currentCalendarDate.getFullYear());
        selectedDateTime.setMonth(currentCalendarDate.getMonth());
        selectedDateTime.setDate(day);

        updateCalendarDisplay();
        updateSelectedDisplay();
    });

    return dayElement;
}

// 選択中の日時表示更新
function updateSelectedDisplay() {
    const year = selectedDateTime.getFullYear();
    const month = selectedDateTime.getMonth() + 1;
    const date = selectedDateTime.getDate();
    const hour = selectedDateTime.getHours();
    const minute = selectedDateTime.getMinutes();
    const weekday = ['日', '月', '火', '水', '木', '金', '土'][selectedDateTime.getDay()];

    selectedDisplay.textContent = `${year}年${month}月${date}日(${weekday}) ${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}`;
}

// カレンダーポップアップの表示/非表示
function toggleCalendar() {
    if (isCalendarOpen) {
        calendarPopup.style.display = 'none';
        isCalendarOpen = false;
    } else {
        // カレンダーを開く前に現在の入力値を確認
        const currentValue = calendarInput.value;
        if (currentValue) {
            // 既に入力されている値がある場合は、その値をカレンダーに反映
            const dateTimeMatch = currentValue.match(/(\d{4})年(\d{1,2})月(\d{1,2})日.*?(\d{1,2}):(\d{2})/);
            if (dateTimeMatch) {
                const [, year, month, date, hour, minute] = dateTimeMatch;
                selectedDateTime = new Date(
                    parseInt(year),
                    parseInt(month) - 1,
                    parseInt(date),
                    parseInt(hour),
                    parseInt(minute)
                );
                currentCalendarDate = new Date(selectedDateTime);

                // セレクトボックスの値も更新
                hourSelect.value = selectedDateTime.getHours();
                minuteSelect.value = selectedDateTime.getMinutes();

                updateCalendarDisplay();
                updateSelectedDisplay();
            }
        }

        calendarPopup.style.display = 'block';
        isCalendarOpen = true;
    }
}

// カレンダーイベントリスナー設定
function setupCalendarEventListeners() {
    // カレンダー関連
    calendarInput.addEventListener('click', toggleCalendar);

    prevMonthBtn.addEventListener('click', () => {
        currentCalendarDate.setMonth(currentCalendarDate.getMonth() - 1);
        updateCalendarDisplay();
    });

    nextMonthBtn.addEventListener('click', () => {
        currentCalendarDate.setMonth(currentCalendarDate.getMonth() + 1);
        updateCalendarDisplay();
    });

    hourSelect.addEventListener('change', (e) => {
        selectedDateTime.setHours(parseInt(e.target.value));
        updateSelectedDisplay();
    });

    minuteSelect.addEventListener('change', (e) => {
        selectedDateTime.setMinutes(parseInt(e.target.value));
        updateSelectedDisplay();
    });

    calendarCancelBtn.addEventListener('click', () => {
        toggleCalendar();
    });

    calendarConfirmBtn.addEventListener('click', () => {
        const year = selectedDateTime.getFullYear();
        const month = selectedDateTime.getMonth() + 1;
        const date = selectedDateTime.getDate();
        const hour = selectedDateTime.getHours();
        const minute = selectedDateTime.getMinutes();
        const weekday = ['日', '月', '火', '水', '木', '金', '土'][selectedDateTime.getDay()];

        calendarInput.value = `${year}年${month}月${date}日(${weekday}) ${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}`;
        toggleCalendar();
    });

    // カレンダー外をクリックで閉じる
    document.addEventListener('click', (e) => {
        if (!calendarInput.contains(e.target) && !calendarPopup.contains(e.target)) {
            if (isCalendarOpen) {
                calendarPopup.style.display = 'none';
                isCalendarOpen = false;
            }
        }
    });
}
</script>