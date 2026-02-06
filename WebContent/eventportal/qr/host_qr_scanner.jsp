<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>QRコードスキャン | イベントポータル</title>
    <script src="https://unpkg.com/html5-qrcode@2.3.8/html5-qrcode.min.js"></script>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background: #f5f5f5;
            margin: 0;
            padding: 0;
            min-height: 100vh;
        }

        .top-bar {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 15px 30px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }

        .top-bar-left {
            display: flex;
            align-items: center;
            gap: 20px;
        }

        .top-bar-right {
            display: flex;
            align-items: center;
            gap: 15px;
        }

        .user-name {
            font-weight: bold;
        }

        .top-btn {
            padding: 8px 20px;
            background: rgba(255,255,255,0.2);
            color: white;
            border: 2px solid white;
            border-radius: 5px;
            text-decoration: none;
            font-weight: bold;
            transition: all 0.3s;
            cursor: pointer;
        }

        .top-btn:hover {
            background: white;
            color: #667eea;
        }

        .container {
            max-width: 800px;
            margin: 30px auto;
            background: white;
            padding: 30px;
            border-radius: 20px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.1);
        }

        .header {
            text-align: center;
            margin-bottom: 30px;
        }

        .header h1 {
            color: #333;
            margin: 0 0 10px 0;
        }

        .event-info {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 20px;
            border-radius: 15px;
            margin-bottom: 30px;
        }

        .event-name {
            font-size: 24px;
            font-weight: bold;
            margin-bottom: 10px;
        }

        .stats {
            display: flex;
            justify-content: space-around;
            margin-top: 15px;
        }

        .stat-item {
            text-align: center;
        }

        .stat-value {
            font-size: 32px;
            font-weight: bold;
        }

        .stat-label {
            font-size: 14px;
            opacity: 0.9;
        }

        .scanner-container {
            position: relative;
            margin: 20px 0;
            border-radius: 15px;
            overflow: hidden;
        }

        #qr-reader {
            border-radius: 15px;
            overflow: hidden;
        }

        .scan-overlay {
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            width: 250px;
            height: 250px;
            pointer-events: none;
        }

        .scan-corner {
            position: absolute;
            width: 40px;
            height: 40px;
            border: 4px solid #4facfe;
        }

        .scan-corner.top-left {
            top: 0;
            left: 0;
            border-right: none;
            border-bottom: none;
        }

        .scan-corner.top-right {
            top: 0;
            right: 0;
            border-left: none;
            border-bottom: none;
        }

        .scan-corner.bottom-left {
            bottom: 0;
            left: 0;
            border-right: none;
            border-top: none;
        }

        .scan-corner.bottom-right {
            bottom: 0;
            right: 0;
            border-left: none;
            border-top: none;
        }

        .scan-line {
            position: absolute;
            width: 100%;
            height: 2px;
            background: linear-gradient(90deg, transparent, #4facfe, transparent);
            animation: scan 2s linear infinite;
        }

        @keyframes scan {
            0% { top: 0; }
            50% { top: 100%; }
            100% { top: 0; }
        }

        .manual-input {
            margin: 20px 0;
            padding: 20px;
            background: #f8f9fa;
            border-radius: 10px;
        }

        .manual-input h3 {
            margin-top: 0;
        }

        .input-group {
            display: flex;
            gap: 10px;
        }

        .input-group input {
            flex: 1;
            padding: 12px;
            border: 2px solid #dee2e6;
            border-radius: 8px;
            font-size: 16px;
        }

        .btn {
            padding: 12px 30px;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
            transition: all 0.3s;
        }

        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(102,126,234,0.4);
        }

        .btn-secondary {
            background: #6c757d;
            color: white;
        }

        .btn-secondary:hover {
            background: #545b62;
        }

        .info-message {
            background: #e7f3ff;
            color: #004085;
            padding: 15px;
            border-radius: 8px;
            margin: 20px 0;
            border-left: 4px solid #4facfe;
        }

        @media (max-width: 768px) {
            .top-bar {
                flex-direction: column;
                gap: 10px;
                padding: 15px;
            }

            .top-bar-right {
                flex-wrap: wrap;
                justify-content: center;
            }

            .container {
                margin: 20px;
                padding: 20px;
            }

            .stats {
                flex-direction: column;
                gap: 15px;
            }
        }
    </style>
</head>
<body>
    <!-- トップバー -->
    <div class="top-bar">
        <div class="top-bar-left">
            <span style="font-size: 24px; font-weight: bold;">🎫 イベント管理</span>
        </div>
        <div class="top-bar-right">
            <span class="user-name">${user.user_name} 様</span>
            <a href="${pageContext.request.contextPath}/eventportal/host/HostMenu.action"
               class="top-btn">🏠 トップページ</a>
            <a href="${pageContext.request.contextPath}/eventportal/auth/Logout.action"
               class="top-btn">🚪 ログアウト</a>
        </div>
    </div>

    <div class="container">
        <div class="header">
            <h1>📱 QRコード入場受付</h1>
        </div>

        <div class="event-info">
            <div class="event-name">${event.eventName}</div>
            <div>開催日: ${event.holdingDate}</div>
            <div class="stats">
                <div class="stat-item">
                    <div class="stat-value">${admittedCount}</div>
                    <div class="stat-label">入場者数</div>
                </div>
                <div class="stat-item">
                    <div class="stat-value">${event.maxCount}</div>
                    <div class="stat-label">定員</div>
                </div>
            </div>
        </div>

        <div class="info-message">
            📷 カメラでQRコードをスキャンするか、下の入力欄にチケットIDを入力してください
        </div>

        <div class="scanner-container">
            <div id="qr-reader"></div>
            <div class="scan-overlay">
                <div class="scan-corner top-left"></div>
                <div class="scan-corner top-right"></div>
                <div class="scan-corner bottom-left"></div>
                <div class="scan-corner bottom-right"></div>
                <div class="scan-line"></div>
            </div>
        </div>

        <div class="manual-input">
            <h3>🔢 手動入力</h3>
            <form action="${pageContext.request.contextPath}/eventportal/host/AdmitEntry.action" method="POST" id="manualForm">
                <input type="hidden" name="eventId" value="${event.eventId}">
                <div class="input-group">
                    <input type="text"
                           name="ticketId"
                           id="ticketIdInput"
                           placeholder="チケットID (例: TKT001)"
                           pattern="TKT[0-9]{3,}"
                           required>
                    <button type="submit" class="btn btn-primary">入場承認</button>
                </div>
            </form>
        </div>

        <div style="text-align: center; margin-top: 30px;">
            <a href="${pageContext.request.contextPath}/eventportal/host/HostEventDetail.action?eventId=${event.eventId}"
               class="btn btn-secondary">
                イベント詳細に戻る
            </a>
        </div>
    </div>

    <script>
        let lastScannedCode = null;
        let lastScannedTime = 0;
        const SCAN_COOLDOWN = 3000; // 3秒間は同じコードを無視

        function onScanSuccess(decodedText, decodedResult) {
            const now = Date.now();

            // 連続スキャン防止
            if (decodedText === lastScannedCode && (now - lastScannedTime) < SCAN_COOLDOWN) {
                console.log('同じQRコードを短時間で再スキャンしました。無視します。');
                return;
            }

            lastScannedCode = decodedText;
            lastScannedTime = now;

            console.log('QRコード検出:', decodedText);

            // フォームを作成して送信
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = '${pageContext.request.contextPath}/eventportal/host/AdmitEntry.action';

            const eventIdInput = document.createElement('input');
            eventIdInput.type = 'hidden';
            eventIdInput.name = 'eventId';
            eventIdInput.value = '${event.eventId}';

            const ticketIdInput = document.createElement('input');
            ticketIdInput.type = 'hidden';
            ticketIdInput.name = 'ticketId';
            ticketIdInput.value = decodedText;

            form.appendChild(eventIdInput);
            form.appendChild(ticketIdInput);
            document.body.appendChild(form);
            form.submit();
        }

        function onScanFailure(error) {
            // スキャン失敗は頻繁に発生するので、コンソールログのみ
            // console.warn('QRコードスキャン失敗:', error);
        }

        // QRコードリーダーを初期化
        const html5QrcodeScanner = new Html5QrcodeScanner(
            "qr-reader",
            {
                fps: 10,
                qrbox: { width: 250, height: 250 },
                aspectRatio: 1.0,
                rememberLastUsedCamera: true
            },
            false
        );

        html5QrcodeScanner.render(onScanSuccess, onScanFailure);

        // 手動入力フォームのバリデーション
        document.getElementById('manualForm').addEventListener('submit', function(e) {
            const ticketId = document.getElementById('ticketIdInput').value.trim();
            if (!ticketId.match(/^TKT[0-9]{3,}$/)) {
                e.preventDefault();
                alert('チケットIDは「TKT」に続けて3桁以上の数字を入力してください（例: TKT001）');
                return false;
            }
        });
    </script>
</body>
</html>