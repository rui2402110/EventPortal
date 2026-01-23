<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>QRコードスキャン | イベントポータル</title>
    <script src="https://cdn.jsdelivr.net/npm/jsqr@1.4.0/dist/jsQR.js"></script>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background: #f5f5f5;
            margin: 0;
            padding: 0;
        }
        .container {
            max-width: 800px;
            margin: 0 auto;
            padding: 20px;
        }
        .header {
            background: #17a2b8;
            color: white;
            padding: 20px;
            text-align: center;
            border-radius: 10px;
            margin-bottom: 20px;
        }
        .scan-container {
            background: white;
            padding: 40px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        .scan-area {
            position: relative;
            width: 100%;
            max-width: 400px;
            margin: 0 auto 30px;
            background: #000;
            border-radius: 10px;
            overflow: hidden;
        }
        #video {
            width: 100%;
            height: auto;
            display: block;
        }
        #canvas {
            display: none;
        }
        .scan-overlay {
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            width: 250px;
            height: 250px;
            border: 3px solid #28a745;
            border-radius: 10px;
            pointer-events: none;
        }
        .scan-line {
            position: absolute;
            width: 100%;
            height: 2px;
            background: linear-gradient(90deg, transparent, #28a745, transparent);
            animation: scan 2s linear infinite;
        }
        @keyframes scan {
            0% { top: 0; }
            100% { top: 100%; }
        }
        .manual-input {
            margin-top: 30px;
            padding-top: 30px;
            border-top: 2px solid #e9ecef;
        }
        .input-group {
            display: flex;
            gap: 10px;
            margin-top: 15px;
        }
        .input-field {
            flex: 1;
            padding: 12px;
            border: 1px solid #ced4da;
            border-radius: 5px;
            font-size: 16px;
        }
        .btn {
            padding: 12px 30px;
            background: #007bff;
            color: white;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            cursor: pointer;
            transition: background 0.3s;
            text-decoration: none;
            display: inline-block;
        }
        .btn:hover {
            background: #0056b3;
        }
        .btn:disabled {
            background: #6c757d;
            cursor: not-allowed;
        }
        .btn-success {
            background: #28a745;
        }
        .btn-success:hover {
            background: #218838;
        }
        .btn-danger {
            background: #dc3545;
        }
        .btn-danger:hover {
            background: #c82333;
        }
        .status-message {
            margin-top: 20px;
            padding: 15px;
            border-radius: 5px;
            text-align: center;
            font-weight: bold;
        }
        .status-scanning {
            background: #cfe2ff;
            color: #004085;
        }
        .status-success {
            background: #d4edda;
            color: #155724;
        }
        .status-error {
            background: #f8d7da;
            color: #721c24;
        }
        .button-group {
            display: flex;
            gap: 10px;
            justify-content: center;
            margin-top: 30px;
        }
        .info-text {
            text-align: center;
            color: #6c757d;
            margin: 20px 0;
        }
        .entry-log {
            margin-top: 30px;
            background: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
        }
        .entry-log h3 {
            margin-top: 0;
        }
        .entry-item {
            padding: 10px;
            background: white;
            margin: 10px 0;
            border-radius: 5px;
            border-left: 4px solid #28a745;
        }
        @media (max-width: 600px) {
            .scan-container {
                padding: 20px;
            }
            .input-group {
                flex-direction: column;
            }
            .button-group {
                flex-direction: column;
            }
            .btn {
                width: 100%;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>QRコードスキャン</h1>
            <p>イベント: ${event.eventName}</p>
            <p>入場済み人数: <span id="admittedCount">${admittedCount}</span>人</p>
        </div>

        <div class="scan-container">
            <div class="scan-area">
                <video id="video" autoplay playsinline></video>
                <canvas id="canvas"></canvas>
                <div class="scan-overlay">
                    <div class="scan-line"></div>
                </div>
            </div>

            <div class="status-message status-scanning">
                <span id="scanStatus">カメラでQRコードをスキャンしてください</span>
            </div>

            <div class="info-text">
                <p>参加者のQRコードをカメラに向けてください。</p>
                <p>自動的に読み取りを行います。</p>
            </div>

            <div class="manual-input">
                <h3>手動入力</h3>
                <p>カメラが使用できない場合は、チケットIDを手動で入力してください。</p>
                <form id="manualForm">
                    <div class="input-group">
                        <input type="text"
                               id="manualInput"
                               class="input-field"
                               placeholder="チケットIDを入力"
                               required>
                        <button type="submit" class="btn btn-success">確認</button>
                    </div>
                </form>
            </div>

            <div class="button-group">
                <a href="${pageContext.request.contextPath}/eventportal/host/HostEventDetail.action?eventId=${event.eventId}"
                   class="btn btn-danger">戻る</a>
            </div>

            <div class="entry-log">
                <h3>入場履歴</h3>
                <div id="entryHistory">
                    <p style="color: #999;">まだ入場者がいません</p>
                </div>
            </div>
        </div>
    </div>

    <script>
        const eventId = "${event.eventId}";
        let video = document.getElementById('video');
        let canvas = document.getElementById('canvas');
        let context = canvas.getContext('2d');
        let scanning = false;
        let lastScannedCode = null;
        let scanCooldown = false;

        // カメラの初期化
        function initCamera() {
            if (navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
                const constraints = {
                    video: {
                        facingMode: 'environment',
                        width: { ideal: 1280 },
                        height: { ideal: 720 }
                    }
                };

                navigator.mediaDevices.getUserMedia(constraints)
                    .then(function(stream) {
                        video.srcObject = stream;
                        video.setAttribute('playsinline', true);
                        video.addEventListener('loadedmetadata', function() {
                            video.play();
                            scanning = true;
                            console.log('カメラ起動成功');
                            requestAnimationFrame(scan);
                        });
                    })
                    .catch(function(error) {
                        console.error('カメラへのアクセスエラー:', error);
                        let errorMessage = 'カメラへのアクセスが拒否されました。手動入力をご利用ください。';
                        document.getElementById('scanStatus').textContent = errorMessage;
                        document.querySelector('.status-message').className = 'status-message status-error';
                    });
            } else {
                document.getElementById('scanStatus').textContent =
                    'このブラウザはカメラをサポートしていません。手動入力をご利用ください。';
                document.querySelector('.status-message').className = 'status-message status-error';
            }
        }

        // QRコードスキャン処理
        function scan() {
            if (!scanning) return;

            if (video.readyState === video.HAVE_ENOUGH_DATA) {
                canvas.height = video.videoHeight;
                canvas.width = video.videoWidth;
                context.drawImage(video, 0, 0, canvas.width, canvas.height);

                try {
                    const imageData = context.getImageData(0, 0, canvas.width, canvas.height);
                    const code = jsQR(imageData.data, imageData.width, imageData.height, {
                        inversionAttempts: 'dontInvert'
                    });

                    if (code && code.data) {
                        console.log('QRコード検出:', code.data);
                        if (code.data !== lastScannedCode && !scanCooldown) {
                            scanning = false;
                            lastScannedCode = code.data;
                            handleQRCode(code.data);
                            return;
                        }
                    }
                } catch (error) {
                    console.error('スキャンエラー:', error);
                }
            }

            if (scanning) {
                requestAnimationFrame(scan);
            }
        }

        // QRコード検出時の処理
        function handleQRCode(ticketId) {
            console.log('チケット検証開始:', ticketId);
            scanning = false;
            scanCooldown = true;

            document.getElementById('scanStatus').textContent = 'QRコードを検出しました。処理中...';
            document.querySelector('.status-message').className = 'status-message status-success';
            playBeep();

            verifyAndAdmitTicket(ticketId);
        }

        // チケット検証と入場処理
        function verifyAndAdmitTicket(ticketId) {
            fetch('${pageContext.request.contextPath}/eventportal/host/VerifyTicket.action', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'ticketId=' + encodeURIComponent(ticketId) + '&eventId=' + encodeURIComponent(eventId)
            })
            .then(response => response.json())
            .then(data => {
                if (data.valid) {
                    // 有効なチケット - 入場処理
                    admitEntry(ticketId, data.userName, data.eventName);
                } else if (data.alreadyUsed) {
                    // 使用済み
                    showError('このチケットは既に使用済みです\\n使用者: ' + data.userName + '\\n使用時刻: ' + data.usedTime);
                    setTimeout(resetScanner, 3000);
                } else {
                    // 無効なチケット
                    showError(data.errorMessage || 'チケットが無効です');
                    setTimeout(resetScanner, 3000);
                }
            })
            .catch(error => {
                console.error('検証エラー:', error);
                showError('エラーが発生しました');
                setTimeout(resetScanner, 3000);
            });
        }

        // 入場処理
        function admitEntry(ticketId, userName, eventName) {
            fetch('${pageContext.request.contextPath}/eventportal/host/AdmitEntry.action', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'ticketId=' + encodeURIComponent(ticketId) + '&eventId=' + encodeURIComponent(eventId)
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    showSuccess('入場を記録しました\\n' + userName + ' 様');
                    addEntryToHistory(data.entry);
                    updateAdmittedCount();
                    setTimeout(resetScanner, 2000);
                } else {
                    showError(data.message || '入場記録に失敗しました');
                    setTimeout(resetScanner, 3000);
                }
            })
            .catch(error => {
                console.error('入場記録エラー:', error);
                showError('エラーが発生しました');
                setTimeout(resetScanner, 3000);
            });
        }

        // 入場人数を更新
        function updateAdmittedCount() {
            const countElement = document.getElementById('admittedCount');
            const currentCount = parseInt(countElement.textContent);
            countElement.textContent = currentCount + 1;
        }

        // 入場履歴に追加
        function addEntryToHistory(entry) {
            const historyDiv = document.getElementById('entryHistory');
            if (historyDiv.querySelector('p[style*="color: #999"]')) {
                historyDiv.innerHTML = '';
            }

            const entryItem = document.createElement('div');
            entryItem.className = 'entry-item';
            entryItem.innerHTML = `
                <strong>${entry.userName}</strong><br>
                <small>チケットID: ${entry.ticketId} | ${new Date(entry.time).toLocaleTimeString('ja-JP')}</small>
            `;
            historyDiv.insertBefore(entryItem, historyDiv.firstChild);
        }

        function showSuccess(message) {
            const statusElement = document.getElementById('scanStatus');
            statusElement.textContent = message;
            document.querySelector('.status-message').className = 'status-message status-success';
        }

        function showError(message) {
            const statusElement = document.getElementById('scanStatus');
            statusElement.textContent = message;
            document.querySelector('.status-message').className = 'status-message status-error';
        }

        function resetScanner() {
            scanning = true;
            scanCooldown = false;
            lastScannedCode = null;
            document.getElementById('scanStatus').textContent = 'カメラでQRコードをスキャンしてください';
            document.querySelector('.status-message').className = 'status-message status-scanning';
            requestAnimationFrame(scan);
        }

        function playBeep() {
            try {
                const audioContext = new (window.AudioContext || window.webkitAudioContext)();
                const oscillator = audioContext.createOscillator();
                const gainNode = audioContext.createGain();
                oscillator.connect(gainNode);
                gainNode.connect(audioContext.destination);
                oscillator.frequency.value = 1000;
                oscillator.type = 'sine';
                gainNode.gain.value = 0.3;
                gainNode.gain.exponentialRampToValueAtTime(0.01, audioContext.currentTime + 0.1);
                oscillator.start(audioContext.currentTime);
                oscillator.stop(audioContext.currentTime + 0.1);
            } catch (e) {
                console.error('ビープ音の再生に失敗しました:', e);
            }
        }

        // 手動入力フォーム
        document.getElementById('manualForm').addEventListener('submit', function(e) {
            e.preventDefault();
            const ticketId = document.getElementById('manualInput').value.trim();
            if (ticketId) {
                scanning = false;
                handleQRCode(ticketId);
                document.getElementById('manualInput').value = '';
            }
        });

        window.addEventListener('load', function() {
            console.log('ページロード完了、カメラ初期化開始');
            initCamera();
        });

        window.addEventListener('beforeunload', function() {
            scanning = false;
            if (video.srcObject) {
                video.srcObject.getTracks().forEach(track => track.stop());
            }
        }

    </script>
</body>
</html>