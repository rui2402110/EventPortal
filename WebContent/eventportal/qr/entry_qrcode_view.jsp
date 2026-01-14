<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>入場QRコード | イベントポータル</title>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background: #f5f5f5;
            margin: 0;
            padding: 0;
        }

        .container {
            max-width: 600px;
            margin: 0 auto;
            padding: 20px;
        }

        .header {
            background: #007bff;
            color: white;
            padding: 20px;
            text-align: center;
            border-radius: 10px 10px 0 0;
        }

        .qrcode-container {
            background: white;
            padding: 40px;
            text-align: center;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            border-radius: 0 0 10px 10px;
        }

        .qrcode-image {
            width: 300px;
            height: 300px;
            margin: 20px auto;
            border: 10px solid #f0f0f0;
            border-radius: 10px;
            background: #fff;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .qrcode-image img {
            width: 100%;
            height: 100%;
            object-fit: contain;
        }

        .qrcode-info {
            margin: 20px 0;
            padding: 20px;
            background: #f9f9f9;
            border-radius: 10px;
        }

        .info-row {
            display: flex;
            justify-content: space-between;
            margin: 10px 0;
            padding: 10px;
            border-bottom: 1px solid #e0e0e0;
        }

        .info-row:last-child {
            border-bottom: none;
        }

        .info-label {
            font-weight: bold;
            color: #666;
        }

        .info-value {
            color: #333;
        }

        .status-badge {
            display: inline-block;
            padding: 5px 15px;
            border-radius: 20px;
            font-size: 14px;
            font-weight: bold;
        }

        .status-unused {
            background: #28a745;
            color: white;
        }

        .status-used {
            background: #dc3545;
            color: white;
        }

        .btn-container {
            margin-top: 30px;
            text-align: center;
        }

        .btn {
            display: inline-block;
            padding: 12px 30px;
            margin: 0 10px;
            background: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            font-size: 16px;
            border: none;
            cursor: pointer;
            transition: background 0.3s;
        }

        .btn:hover {
            background: #0056b3;
        }

        .btn-secondary {
            background: #6c757d;
        }

        .btn-secondary:hover {
            background: #5a6268;
        }

        .warning-message {
            background: #fff3cd;
            border: 1px solid #ffc107;
            color: #856404;
            padding: 15px;
            border-radius: 5px;
            margin: 20px 0;
        }

        .error-message {
            background: #f8d7da;
            border: 1px solid #f5c6cb;
            color: #721c24;
            padding: 15px;
            border-radius: 5px;
            margin: 20px 0;
        }

        .loading-spinner {
            border: 4px solid #f3f3f3;
            border-top: 4px solid #007bff;
            border-radius: 50%;
            width: 40px;
            height: 40px;
            animation: spin 1s linear infinite;
            margin: auto;
        }

        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }

        @media print {
            .btn-container {
                display: none;
            }

            .header {
                background: none;
                color: black;
                border: 2px solid #000;
            }
        }

        @media (max-width: 600px) {
            .qrcode-image {
                width: 250px;
                height: 250px;
            }

            .info-row {
                flex-direction: column;
                text-align: left;
            }

            .info-label {
                margin-bottom: 5px;
            }
        }
    </style>
    <script>
        function printQRCode() {
            window.print();
        }

        function downloadQRCode() {
            const qrImage = document.querySelector('.qrcode-image img');
            if (qrImage) {
                const link = document.createElement('a');
                link.href = qrImage.src;
                link.download = 'qrcode_${qrCode.qrCodeId}.png';
                link.click();
            }
        }

        // ページ読み込み時に画像の存在を確認
        window.onload = function() {
            const qrImage = document.querySelector('.qrcode-image img');
            if (qrImage) {
                qrImage.onerror = function() {
                    // 画像が読み込めない場合の処理
                    this.style.display = 'none';
                    const parent = this.parentElement;
                    parent.innerHTML = '<div class="loading-spinner"></div><p style="margin-top: 20px;">QRコード生成中...</p>';

                    // 3秒後にページをリロード
                    setTimeout(function() {
                        location.reload();
                    }, 3000);
                };
            }
        };
    </script>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>入場QRコード</h1>
            <p>このQRコードをイベント会場で提示してください</p>
        </div>

        <div class="qrcode-container">
            <c:choose>
                <c:when test="${not empty qrCode}">
                    <!-- QRコードデータのデバッグ情報（本番環境では削除） -->
                    <!--
                    <div style="background: #f0f0f0; padding: 10px; margin: 10px; font-size: 12px; text-align: left;">
                        <p>Debug Info:</p>
                        <p>QR Code ID: ${qrCode.qrCodeId}</p>
                        <p>QR Data: ${qrCode.qrCodeData}</p>
                        <p>Image Path: ${qrCode.qrCodeImagePath}</p>
                        <p>Context Path: ${pageContext.request.contextPath}</p>
                    </div>
                    -->

                    <div class="qrcode-image">
                        <c:choose>
                            <c:when test="${not empty qrCode.qrCodeImagePath}">
                                <img src="${pageContext.request.contextPath}${qrCode.qrCodeImagePath}"
                                     alt="QRコード"
                                     id="qrCodeImage" />
                            </c:when>
                            <c:otherwise>
                                <!-- QRコード画像パスが設定されていない場合 -->
                                <div class="loading-spinner"></div>
                                <p style="margin-top: 20px;">QRコード生成中...</p>
                                <script>
                                    // 3秒後にページをリロード
                                    setTimeout(function() {
                                        location.reload();
                                    }, 3000);
                                </script>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div class="qrcode-info">
                        <div class="info-row">
                            <span class="info-label">QRコードID:</span>
                            <span class="info-value">${qrCode.qrCodeId}</span>
                        </div>

                        <div class="info-row">
                            <span class="info-label">イベントID:</span>
                            <span class="info-value">${qrCode.eventId}</span>
                        </div>

                        <div class="info-row">
                            <span class="info-label">発行日時:</span>
                            <span class="info-value">${qrCode.issuedDateTime}</span>
                        </div>

                        <div class="info-row">
                            <span class="info-label">有効期限:</span>
                            <span class="info-value">${qrCode.expirationDateTime}</span>
                        </div>

                        <div class="info-row">
                            <span class="info-label">ステータス:</span>
                            <span class="info-value">
                                <c:choose>
                                    <c:when test="${qrCode.usageStatus == 0}">
                                        <span class="status-badge status-unused">未使用</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="status-badge status-used">使用済み</span>
                                    </c:otherwise>
                                </c:choose>
                            </span>
                        </div>

                        <c:if test="${qrCode.usageStatus == 1}">
                            <div class="info-row">
                                <span class="info-label">使用日時:</span>
                                <span class="info-value">${qrCode.usedDateTime}</span>
                            </div>
                        </c:if>
                    </div>

                    <c:if test="${qrCode.usageStatus == 1}">
                        <div class="warning-message">
                            <strong>注意:</strong> このQRコードは既に使用されています。
                        </div>
                    </c:if>

                    <div class="btn-container">
                        <button class="btn" onclick="printQRCode()">印刷</button>
                        <button class="btn btn-secondary" onclick="downloadQRCode()">ダウンロード</button>
                        <a href="${pageContext.request.contextPath}/eventportal/entry/entry_menu.jsp?eventId=${eventId}"
                           class="btn btn-secondary">戻る</a>
                    </div>

                </c:when>
                <c:otherwise>
                    <div class="error-message">
                        <strong>エラー:</strong> QRコードが見つかりません。
                    </div>
                    <div class="btn-container">
                        <a href="${pageContext.request.contextPath}/eventportal/entry/entry_menu.jsp"
                           class="btn">メニューへ戻る</a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</body>
</html>