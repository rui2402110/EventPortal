<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>イベントポータル - 主催者ログイン</title>
</head>
<body>
  <div class="container">
    <div class="header" id="screen_title">
      <div class="role-label">主催者</div>
      イベントポータル
    </div>

    <div class="content">
      <form method="post" action="auth/login.action">
        <input type="hidden" name="role" value="organizer">

        <div class="form-group">
          <label for="id_input">ID</label>
          <input type="text" id="id_input" name="id_input_text" placeholder="IDを入力する">
        </div>

        <div class="form-group">
          <label for="password_input">PASSWORD</label>
          <input type="password" id="password_input" name="password_input_text" placeholder="パスワードを入力する">
        </div>

        <!-- エラー1 -->
        <% if (request.getAttribute("error1") != null) { %>
        <div class="error" id="error_1">① 入力されていません</div>
        <% } %>

        <!-- エラー2 -->
        <% if (request.getAttribute("error2") != null) { %>
        <div class="error" id="error_2">② IDかパスワードが間違っています</div>
        <% } %>

        <button type="submit" class="btn" id="login_button" name="login_button">LOGIN</button>
      </form>
    </div>

    <div class="footer">@2025..................................................</div>
  </div>
</body>
</html>