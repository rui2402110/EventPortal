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
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>QRコードスキャン</h1>
            <p>イベント: ${event.eventName}</p>
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

                <form action="${pageContext.request.contextPath}/eventportal/host/VerifyTicket.action"
                      method="post">
                    <input type="hidden" name="eventId" value="${event.eventId}">

                    <div class="input-group">
                        <input type="text"
                               name="ticketId"
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
        </div>
    </div>

    <script>
        let video = document.getElementById('video');
        let canvas = document.getElementById('canvas');
        let context = canvas.getContext('2d');
        let scanning = false;

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
                            requestAnimationFrame(scan);
                        });
                    })
                    .catch(function(error) {
                        console.error('カメラエラー:', error);
                        document.getElementById('scanStatus').textContent =
                            'カメラにアクセスできません。手動入力をご利用ください。';
                        document.querySelector('.status-message').className = 'status-message status-error';
                    });
            }
        }

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
                        scanning = false;
                        handleQRCode(code.data);
                        return;
                    }
                } catch (error) {
                    console.error('スキャンエラー:', error);
                }
            }

            if (scanning) {
                requestAnimationFrame(scan);
            }
        }

        function handleQRCode(ticketId) {
            document.getElementById('scanStatus').textContent = 'QRコード検出。確認中...';
            document.querySelector('.status-message').className = 'status-message status-success';

            // フォーム送信
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = '${pageContext.request.contextPath}/eventportal/host/VerifyTicket.action';

            const eventIdInput = document.createElement('input');
            eventIdInput.type = 'hidden';
            eventIdInput.name = 'eventId';
            eventIdInput.value = '${event.eventId}';
            form.appendChild(eventIdInput);

            const ticketIdInput = document.createElement('input');
            ticketIdInput.type = 'hidden';
            ticketIdInput.name = 'ticketId';
            ticketIdInput.value = ticketId;
            form.appendChild(ticketIdInput);

            document.body.appendChild(form);
            form.submit();
        }

        window.addEventListener('load', initCamera);

        window.addEventListener('beforeunload', function() {
            scanning = false;
            if (video.srcObject) {
                video.srcObject.getTracks().forEach(track => track.stop());
            }
        });
    </script>
</body>
</html>