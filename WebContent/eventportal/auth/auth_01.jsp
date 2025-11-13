<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>イベントポータル - 認証</title>
</head>
<body>
  <div class="container">
    <div class="header">イベントポータル</div>

    <div class="content">
      <button class="btn" onclick="location.href='${pageContext.request.contextPath}/eventportal/auth/LoginPage.action'">① ログインはこちら</button>
      <button class="btn" onclick="location.href='${pageContext.request.contextPath}/eventportal/auth/SigninPage.action'">② サインインはこちら</button>
    </div>

    <div class="footer">@2025.................................................</div>
  </div>
</body>
</html>