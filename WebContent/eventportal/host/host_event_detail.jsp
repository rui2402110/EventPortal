<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>イベント詳細 | イベントポータル</title>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background: #f5f5f5;
            margin: 0;
            padding: 0;
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
            max-width: 1200px;
            margin: 30px auto;
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
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

    <!-- 以下、既存のコンテンツ -->
    <div class="container">
        <!-- 既存の内容をそのまま維持 -->
    </div>
</body>
</html>