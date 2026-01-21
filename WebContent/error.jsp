<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isErrorPage="true" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>エラー - イベントポータル</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/common/style.css">
    <style>
        .error-container {
            max-width: 600px;
            margin: 100px auto;
            padding: 40px;
            background: white;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            text-align: center;
        }

        .error-icon {
            font-size: 60px;
            color: #dc3545;
            margin-bottom: 20px;
        }

        .error-title {
            font-size: 24px;
            font-weight: bold;
            color: #333;
            margin-bottom: 15px;
        }

        .error-message {
            font-size: 16px;
            color: #666;
            margin-bottom: 30px;
            line-height: 1.6;
        }

        .error-details {
            background: #f8f9fa;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 30px;
            text-align: left;
            font-size: 14px;
            color: #666;
        }

        .btn-back {
            display: inline-block;
            padding: 12px 30px;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            font-size: 16px;
            transition: background-color 0.3s;
        }

        .btn-back:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
    <div class="error-container">
        <div class="error-icon">⚠️</div>
        <h1 class="error-title">エラーが発生しました</h1>

        <div class="error-message">
            <%
            String errorMessage = (String) request.getAttribute("errorMessage");
            if (errorMessage != null) {
                out.print(errorMessage);
            } else {
                out.print("申し訳ございません。処理中にエラーが発生しました。");
            }
            %>
        </div>

        <% if (exception != null && request.getParameter("debug") != null) { %>
        <div class="error-details">
            <strong>エラー詳細:</strong><br>
            <%= exception.getClass().getName() %><br>
            <%= exception.getMessage() != null ? exception.getMessage() : "" %>
        </div>
        <% } %>

        <a href="${pageContext.request.contextPath}/eventportal/auth/AuthPage.action" class="btn-back">
            トップページに戻る
        </a>
    </div>
</body>
</html>