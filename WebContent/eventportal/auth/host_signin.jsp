<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>イベントポータル - 主催者サインイン</title>
    <!-- 共通CSS -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/common/style.css">
</head>
<body>
  <div class="container">
    <div class="header" id="screen_title">
      <div class="role-label">主催者</div>
      イベントポータル
    </div>

    <div class="content">
      <form method="post" action="${pageContext.request.contextPath}/eventportal/auth/HostSigninExecute.action">
        <input type="hidden" name="role" value="participant">

        <div class="form-group">
          <label for="id">ID</label>
          <input type="text" id="textbox" name="id" placeholder="IDを入力する">
        </div>

        <div class="form-group">
          <label for="id">ユーザー名</label>
          <input type="text" id="textbox" name="user_name" placeholder="ユーザー名を入力する">
        </div>

        <div class="form-group">
          <label for="password">PASSWORD</label>
          <input type="password" id="password" name="password" placeholder="パスワードを入力する">
        </div>

        <div class="form-group">
          <label for="password_conf">PASSWORD確認</label>
          <input type="password" id="password_conf" name="password_conf" placeholder="パスワードを再度入力する">
        </div>

        <div class="form-group">
          <label for="mail_address">メールアドレス</label>
          <input type="text" id="mail_address" name="mail_address" placeholder="メールアドレスを入力する">
        </div>

		<div class="form-group">
          <label for="phone_number">電話番号</label>
          <input type="text" id="phone_number" name="phone_number" placeholder="電話番号を入力する">
        </div>

        <!-- エラー1 -->
        <% if (request.getAttribute("error1") != null) { %>
        <div class="error" id="error_1">① 入力されていません</div>
        <% } %>

        <!-- エラー2 -->
        <% if (request.getAttribute("error2") != null) { %>
        <div class="error" id="error_2">② IDかパスワードが間違っています</div>
        <% } %>

        <button type="submit" class="btn" id="login_button" name="login_button">SIGNIN</button>
      </form>
    </div>

    <div class="footer">@2025..................................................</div>
  </div>
</body>
</html>