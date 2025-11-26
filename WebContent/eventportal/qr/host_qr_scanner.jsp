<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>QRコード読み取り | イベントポータル</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/common/style.css">
  <style>
    .scanner-container {
      max-width: 800px;
      margin: 20px auto;
      background: white;
      border-radius: 12px;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    }
    .scanner-header {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
      padding: 20px;
      text-align: center;
    }
    .event-info-bar {
      background: #f5f5f5;
      padding: 15px;
    }
    .event-stats {
      display: flex;
      justify-content: space-around;
      margin-top: 10px;
    }
    .stat-value {
      font-size: 24px;
      font-weight: bold;
      color: #667eea;
    }
    .scanner-body {
      padding: 30px;
    }
    #qr-reader {
      width: 100%;
      max-width: 500px;
      margin: 0 auto;
      border: 2px solid #ddd;
      border-radius: 8px;
    }
    .manual-input-section {
      margin-top: 30px;
      padding: 20px;
      background: #f9f9f9;
      border-radius: 8px;
    }
    .input-group {
      display: flex;
      gap: 10px;
      margin-top: 15px;
    }
    .input-group input {
      flex: 1;
      padding: 12px;
      border: 1px solid #ddd;
      border-radius: 6px;
    }
    .input-group button {
      padding: 12px 24px;
      background: #667eea;
      color: white;
      border: none;
      border-radius: 6px;
      cursor: pointer;
    }
    .result-section {
      margin-top: 30px;
      padding: 20px;
      border-radius: 8px;
      display: none;
    }
    .result-success { background: #e8f5e9; border-left: 4px solid #4caf50; }
    .result-error { background: #ffebee; border-left: 4px solid #f44336; }
    .result-warning { background: #fff3e0; border-left: 4px solid #ff9800; }
    .btn {
      padding: 10px 20px;
      border: none;
      border-radius: 6px;
      cursor: pointer;
      font-weight: bold;
    }
    .btn-admit { background: #4caf50; color: white; }
  </style>
  <script src="https://unpkg.com/html5-qrcode@2.3.8/html5-qrcode.min.js"></script>
</head>
<body>
  <div class="container">
    <div class="header">イベントポータル - 入場管理</div>
    <div class="scanner-container">
      <div class="scanner-header">
        <h2>QRコード読み取り</h2>
      </div>
      <div class="event-info-bar">
        <div style="font-weight: bold; font-size: 18px;">${event.eventName}</div>
        <div class="event-stats">
          <div style="text-align: center;">
            <div class="stat-value" id="admittedCount">${admittedCount}</div>
            <div style="font-size: 12px; color: #666;">入場済み</div>
          </div>
          <div style="text-align: center;">
            <div class="stat-value">${event.maxCount}</div>
            <div style="font-size: 12px; color: #666;">定員</div>
          </div>
        </div>
      </div>
      <div class="scanner-body">
        <div style="text-align: center; margin-bottom: 30px;">
          <h3>カメラでQRコードをスキャン</h3>
          <div id="qr-reader"></div>
          <div style="margin-top: 15px;">
            <button onclick="startScanning()" id="startBtn" style="padding: 8px 16px; background: #757575; color: white; border: none; border-radius: 4px; cursor: pointer;">スキャン開始</button>
            <button onclick="stopScanning()" id="stopBtn" style="padding: 8px 16px; background: #757575; color: white; border: none; border-radius: 4px; cursor: pointer; display: none;">スキャン停止</button>
          </div>
        </div>
        <div class="manual-input-section">
          <h3>チケットIDで検索</h3>
          <div class="input-group">
            <input type="text" id="ticketIdInput" placeholder="チケットIDを入力" onkeypress="if(event.key==='Enter') checkTicket()">
            <button onclick="checkTicket()">確認</button>
          </div>
        </div>
        <div class="result-section" id="resultSection">
          <div id="resultMessage"></div>
          <div id="attendeeInfo"></div>
          <div id="actionButtons"></div>
        </div>
      </div>
    </div>
    <div class="footer">@2025.................................................</div>
  </div>
  <script>
    let html5QrCode;
    let isScanning = false;

    function startScanning() {
      html5QrCode = new Html5Qrcode("qr-reader");
      html5QrCode.start(
        { facingMode: "environment" },
        { fps: 10, qrbox: { width: 250, height: 250 } },
        (decodedText) => {
          stopScanning();
          verifyTicket(decodedText);
        }
      ).then(() => {
        isScanning = true;
        document.getElementById('startBtn').style.display = 'none';
        document.getElementById('stopBtn').style.display = 'inline-block';
      }).catch(err => alert('カメラの起動に失敗しました'));
    }

    function stopScanning() {
      if (html5QrCode && isScanning) {
        html5QrCode.stop().then(() => {
          isScanning = false;
          document.getElementById('startBtn').style.display = 'inline-block';
          document.getElementById('stopBtn').style.display = 'none';
        });
      }
    }

    function checkTicket() {
      const ticketId = document.getElementById('ticketIdInput').value.trim();
      if (!ticketId) {
        alert('チケットIDを入力してください');
        return;
      }
      verifyTicket(ticketId);
    }

    function verifyTicket(ticketId) {
      showResult('loading', '<div style="text-align: center;"><p>確認中...</p></div>');

      fetch('${pageContext.request.contextPath}/eventportal/host/VerifyTicket.action', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: 'ticketId=' + encodeURIComponent(ticketId) + '&eventId=${event.eventId}'
      })
      .then(response => response.json())
      .then(data => {
        if (data.valid) {
          let info = '<div style="padding: 10px 0; border-bottom: 1px solid rgba(0,0,0,0.1);"><strong>チケットID:</strong> ' + data.ticketId + '</div>';
          info += '<div style="padding: 10px 0; border-bottom: 1px solid rgba(0,0,0,0.1);"><strong>参加者名:</strong> ' + data.userName + '</div>';
          let buttons = '<div style="margin-top: 20px; display: flex; gap: 10px; justify-content: center;">';
          buttons += '<button class="btn btn-admit" onclick="admitEntry(\'' + data.ticketId + '\')">入場を許可</button>';
          buttons += '<button class="btn" style="background: #757575; color: white;" onclick="resetScanner()">キャンセル</button>';
          buttons += '</div>';
          showResult('success', '<h3>✓ 有効なチケットです</h3>', info, buttons);
        } else if (data.alreadyUsed) {
          let info = '<p>このチケットは既に使用されています。</p>';
          info += '<div style="padding: 10px 0;"><strong>使用日時:</strong> ' + data.usedTime + '</div>';
          showResult('warning', '<h3>⚠ 使用済みチケットです</h3>', info, '<button class="btn" style="background: #2196f3; color: white;" onclick="resetScanner()">次へ</button>');
        } else {
          showResult('error', '<h3>× 無効なチケットです</h3>', '<p>' + (data.errorMessage || 'このチケットは使用できません') + '</p>', '<button class="btn" style="background: #2196f3; color: white;" onclick="resetScanner()">次へ</button>');
        }
      });
    }

    function admitEntry(ticketId) {
      fetch('${pageContext.request.contextPath}/eventportal/host/AdmitEntry.action', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: 'ticketId=' + encodeURIComponent(ticketId) + '&eventId=${event.eventId}'
      })
      .then(response => response.json())
      .then(data => {
        if (data.success) {
          showResult('success', '<h3>✓ 入場を記録しました</h3><p>入場を許可してください。</p>');
          document.getElementById('admittedCount').textContent = parseInt(document.getElementById('admittedCount').textContent) + 1;
          setTimeout(resetScanner, 3000);
        } else {
          showResult('error', '<h3>エラー</h3><p>入場記録に失敗しました。</p>');
        }
      });
    }

    function showResult(type, message, info = '', buttons = '') {
      const resultSection = document.getElementById('resultSection');
      resultSection.className = 'result-section result-' + type;
      resultSection.style.display = 'block';
      document.getElementById('resultMessage').innerHTML = message;
      document.getElementById('attendeeInfo').innerHTML = info;
      document.getElementById('actionButtons').innerHTML = buttons;
    }

    function resetScanner() {
      document.getElementById('resultSection').style.display = 'none';
      document.getElementById('ticketIdInput').value = '';
      if (!isScanning) startScanning();
    }
  </script>
</body>
</html>