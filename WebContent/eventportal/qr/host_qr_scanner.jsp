<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>QRコードスキャン | イベントポータル</title>
    <!-- jsQRライブラリを追加 -->
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
            <p>イベントID: ${eventId}</p>
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
                <p>カメラが使用できない場合は、QRコードデータを手動で入力してください。</p>

                <form action="${pageContext.request.contextPath}/eventportal/host/HostQRCodeScan.action"
                      method="post">
                    <input type="hidden" name="eventId" value="${eventId}">

                    <div class="input-group">
                        <input type="text"
                               name="qrCodeData"
                               id="manualInput"
                               class="input-field"
                               placeholder="QRコードデータを入力"
                               required>
                        <button type="submit" class="btn btn-success">確認</button>
                    </div>
                </form>
            </div>

            <div class="button-group">
                <a href="${pageContext.request.contextPath}/eventportal/host/HostQRCodeManage.action?eventId=${eventId}"
                   class="btn btn-danger">戻る</a>
            </div>
        </div>
    </div>

    <script>
        // グローバル変数
        let video = document.getElementById('video');
        let canvas = document.getElementById('canvas');
        let context = canvas.getContext('2d');
        let scanning = false;
        let lastScannedCode = null;
        let scanCooldown = false;

        // カメラの初期化
        function initCamera() {
            if (navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
                // カメラの設定オプション
                const constraints = {
                    video: {
                        facingMode: 'environment', // 背面カメラを優先
                        width: { ideal: 1280 },
                        height: { ideal: 720 }
                    }
                };

                navigator.mediaDevices.getUserMedia(constraints)
                    .then(function(stream) {
                        video.srcObject = stream;
                        video.setAttribute('playsinline', true); // iOS Safari対応

                        // ビデオのメタデータが読み込まれたら開始
                        video.addEventListener('loadedmetadata', function() {
                            video.play();
                            scanning = true;
                            console.log('カメラ起動成功');
                            requestAnimationFrame(scan);
                        });
                    })
                    .catch(function(error) {
                        console.error('カメラへのアクセスエラー:', error);
                        let errorMessage = 'カメラへのアクセスが拒否されました。';

                        if (error.name === 'NotFoundError') {
                            errorMessage = 'カメラが見つかりません。';
                        } else if (error.name === 'NotAllowedError') {
                            errorMessage = 'カメラへのアクセスが拒否されました。ブラウザの設定を確認してください。';
                        } else if (error.name === 'NotReadableError') {
                            errorMessage = 'カメラが他のアプリケーションで使用されています。';
                        }

                        document.getElementById('scanStatus').textContent = errorMessage + ' 手動入力をご利用ください。';
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
                // キャンバスサイズを調整
                canvas.height = video.videoHeight;
                canvas.width = video.videoWidth;
                context.drawImage(video, 0, 0, canvas.width, canvas.height);

                try {
                    // 画像データを取得
                    const imageData = context.getImageData(0, 0, canvas.width, canvas.height);

                    // jsQRライブラリを使用してQRコードを検出
                    const code = jsQR(imageData.data, imageData.width, imageData.height, {
                        inversionAttempts: 'dontInvert'
                    });

                    if (code && code.data) {
                        console.log('QRコード検出:', code.data);

                        // 同じコードを連続で読み取らないようにする
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

            // 次のフレームでスキャン継続
            if (scanning) {
                requestAnimationFrame(scan);
            }
        }

        // QRコード検出時の処理
        function handleQRCode(data) {
            console.log('QRコード処理開始:', data);

            // スキャン停止
            scanning = false;
            scanCooldown = true;

            // UIフィードバック
            document.getElementById('scanStatus').textContent = 'QRコードを検出しました。処理中...';
            document.querySelector('.status-message').className = 'status-message status-success';

            // ビープ音を鳴らす
            playBeep();

            // フォームを作成して自動送信
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = '${pageContext.request.contextPath}/eventportal/host/HostQRCodeScan.action';

            const eventIdInput = document.createElement('input');
            eventIdInput.type = 'hidden';
            eventIdInput.name = 'eventId';
            eventIdInput.value = '${eventId}';
            form.appendChild(eventIdInput);

            const qrCodeDataInput = document.createElement('input');
            qrCodeDataInput.type = 'hidden';
            qrCodeDataInput.name = 'qrCodeData';
            qrCodeDataInput.value = data;
            form.appendChild(qrCodeDataInput);

            document.body.appendChild(form);

            // 少し待ってから送信（ユーザーにフィードバックを見せるため）
            setTimeout(function() {
                form.submit();
            }, 500);
        }

        // ビープ音を鳴らす
        function playBeep() {
            try {
                const audioContext = new (window.AudioContext || window.webkitAudioContext)();
                const oscillator = audioContext.createOscillator();
                const gainNode = audioContext.createGain();

                oscillator.connect(gainNode);
                gainNode.connect(audioContext.destination);

                oscillator.frequency.value = 1000; // 1000Hz
                oscillator.type = 'sine';
                gainNode.gain.value = 0.3;
                gainNode.gain.exponentialRampToValueAtTime(0.01, audioContext.currentTime + 0.1);

                oscillator.start(audioContext.currentTime);
                oscillator.stop(audioContext.currentTime + 0.1);
            } catch (e) {
                console.error('ビープ音の再生に失敗しました:', e);
            }
        }

        // ページロード時にカメラを初期化
        window.addEventListener('load', function() {
            console.log('ページロード完了、カメラ初期化開始');
            initCamera();
        });

        // ページを離れる時にカメラを停止
        window.addEventListener('beforeunload', function() {
            scanning = false;
            if (video.srcObject) {
                video.srcObject.getTracks().forEach(track => track.stop());
            }
        });
    </script>
</body>
</html>