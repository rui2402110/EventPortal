<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>未入場 - イベントポータル</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f5f5f5;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            margin: 0;
        }

        .container {
            background: white;
            padding: 40px;
            border-radius: 10px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
            text-align: center;
            max-width: 500px;
        }

        .icon {
            font-size: 60px;
            margin-bottom: 20px;
        }

        h1 {
            color: #721c24;
            margin-bottom: 20px;
        }

        p {
            color: #666;
            line-height: 1.6;
            margin-bottom: 30px;
        }

        .btn {
            display: inline-block;
            padding: 12px 30px;
            background-color: #4a5568;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            margin: 5px;
        }

        .btn:hover {
            background-color: #2d3748;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="icon">🚫</div>
        <h1>まだ入場していません</h1>
        <p>
            このイベントの商品を購入するには、まずQRコードで入場する必要があります。<br>
            会場でQRコードをスキャンしてから、再度お試しください。
        </p>
        <a href="${pageContext.request.contextPath}/eventportal/entrymenu/MyTickets.action" class="btn">
            マイチケットに戻る
        </a>
    </div>
</body>
</html>