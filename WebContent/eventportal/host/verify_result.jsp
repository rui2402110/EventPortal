<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>入場処理結果 | イベントポータル</title>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background: #f5f5f5;
            margin: 0;
            padding: 0;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .top-bar {
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 15px 30px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            z-index: 1000;
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
            max-width: 500px;
            width: 100%;
            background: white;
            padding: 40px;
            border-radius: 20px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.1);
            text-align: center;
            margin-top: 80px;
            animation: fadeIn 0.5s;
        }

        @keyframes fadeIn {
            from {
                opacity: 0;
                transform: translateY(20px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .result-icon {
            font-size: 80px;
            margin-bottom: 20px;
        }

        .result-title {
            font-size: 32px;
            font-weight: bold;
            margin-bottom: 20px;
        }

        .result-success {
            color: #28a745;
        }

        .result-error {
            color: #dc3545;
        }

        .info-box {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 10px;
            margin: 20px 0;
            text-align: left;
        }

        .info-row {
            display: flex;
            justify-content: space-between;
            padding: 10px 0;
            border-bottom: 1px solid #dee2e6;
        }

        .info-row:last-child {
            border-bottom: none;
        }

        .info-label {
            font-weight: bold;
            color: #495057;
        }

        .info-value {
            color: #212529;
        }

        .countdown {
            font-size: 48px;
            font-weight: bold;
            color: #667eea;
            margin: 20px 0;
        }

        .message {
            font-size: 18px;
            color: #6c757d;
            margin: 15px 0;
        }

        .btn {
            padding: 15px 40px;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            transition: all 0.3s;
            margin: 10px 5px;
        }

        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(102,126,234,0.4);
        }

        @media (max-width: 768px) {
            .top-bar {
                flex-direction: column;
                gap: 10px;
                padding: 15px;
                position: relative;
            }

            .top-bar-right {
                flex-wrap: wrap;
                justify-content: center;
            }

            .container {
                margin: 20px;
                padding: 20px;
                margin-top: 20px;
            }

            .result-icon {
                font-size: 60px;
            }

            .result-title {
                font-size: 24px;
            }

            .countdown {
                font-size: 36px;
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
        <c:choose>
            <c:when test="${result == 'success'}">
                <!-- 入場成功 -->
                <div class="result-icon">✅</div>
                <div class="result-title result-success">入場承認</div>

                <div class="info-box">
                    <div class="info-row">
                        <span class="info-label">参加者名:</span>
                        <span class="info-value">${ticket.participantName}</span>
                    </div>
                    <div class="info-row">
                        <span class="info-label">チケットID:</span>
                        <span class="info-value">${ticket.ticketId}</span>
                    </div>
                    <div class="info-row">
                        <span class="info-label">イベント:</span>
                        <span class="info-value">${event.eventName}</span>
                    </div>
                    <div class="info-row">
                        <span class="info-label">入場時刻:</span>
                        <span class="info-value">${ticket.usedAt}</span>
                    </div>
                </div>

                <div class="countdown" id="countdown">5</div>
                <div class="message">秒後に自動的にスキャン画面に戻ります</div>

            </c:when>

            <c:when test="${result == 'used'}">
                <!-- 使用済み -->
                <div class="result-icon">⚠️</div>
                <div class="result-title result-error">使用済みチケット</div>

                <div class="info-box">
                    <div class="info-row">
                        <span class="info-label">参加者名:</span>
                        <span class="info-value">${ticket.participantName}</span>
                    </div>
                    <div class="info-row">
                        <span class="info-label">チケットID:</span>
                        <span class="info-value">${ticket.ticketId}</span>
                    </div>
                    <div class="info-row">
                        <span class="info-label">使用日時:</span>
                        <span class="info-value">${ticket.usedAt}</span>
                    </div>
                </div>

                <div class="countdown" id="countdown">5</div>
                <div class="message">秒後に自動的にスキャン画面に戻ります</div>

            </c:when>

            <c:otherwise>
                <!-- エラー -->
                <div class="result-icon">❌</div>
                <div class="result-title result-error">エラー</div>

                <div class="message">${errorMessage}</div>

                <div style="margin-top: 30px;">
                    <a href="${pageContext.request.contextPath}/eventportal/host/HostQRScanner.action?eventId=${eventId}"
                       class="btn btn-primary">
                        スキャン画面に戻る
                    </a>
                </div>

            </c:otherwise>
        </c:choose>
    </div>

    <c:if test="${result == 'success' || result == 'used'}">
        <script>
            // 音声フィードバック
            const audioContext = new (window.AudioContext || window.webkitAudioContext)();

            function playSound(frequency, duration) {
                const oscillator = audioContext.createOscillator();
                const gainNode = audioContext.createGain();

                oscillator.connect(gainNode);
                gainNode.connect(audioContext.destination);

                oscillator.frequency.value = frequency;
                oscillator.type = 'sine';

                gainNode.gain.setValueAtTime(0.3, audioContext.currentTime);
                gainNode.gain.exponentialRampToValueAtTime(0.01, audioContext.currentTime + duration);

                oscillator.start(audioContext.currentTime);
                oscillator.stop(audioContext.currentTime + duration);
            }

            // 成功音または警告音を再生
            const result = '${result}';
            if (result === 'success') {
                playSound(800, 0.2); // 高い音
            } else {
                playSound(200, 0.5); // 低い音
            }

            // カウントダウン
            let count = 5;
            const countdownElement = document.getElementById('countdown');

            const timer = setInterval(function() {
                count--;
                countdownElement.textContent = count;

                if (count <= 0) {
                    clearInterval(timer);
                    window.location.href = '${pageContext.request.contextPath}/eventportal/host/HostQRScanner.action?eventId=${eventId}';
                }
            }, 1000);
        </script>
    </c:if>
</body>
</html>