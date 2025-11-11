<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ログイン区分選択 | イベントポータル</title>
</head>
<body>
    <div class="container">
        <div class="header">イベントポータル</div>

        <div class="content">
            <h2>ログインする区分を選択してください</h2>

            <form action="" method="get">
                <button type="submit" class="btn">参加者としてログイン</button>
            </form>

            <form action="" method="get">
                <button type="submit" class="btn">主催者としてログイン</button>
            </form>

            <div class="back">
                <a href="${pageContext.request.contextPath}/eventportal/AuthPage.action">← 戻る</a>
            </div>

        </div>

        <div class="footer">@2025.................................................</div>
    </div>
</body>
</html>