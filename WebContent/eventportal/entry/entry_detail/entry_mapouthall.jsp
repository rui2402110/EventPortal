<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>イベントポータル - 認証</title>
    <!-- 共通CSS -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/common/style.css">
</head>
<body>
<div class="header">イベントポータル</div>
<div class="user-info">
    <p>ログインユーザー: <strong>${sessionScope.user.user_id}</strong></p>
    <p>会場外マップ表示</p>
</div>
	<p>${mapUrl}</p>
 <div class="footer">@2025.................................................</div>
</body>
</html>
