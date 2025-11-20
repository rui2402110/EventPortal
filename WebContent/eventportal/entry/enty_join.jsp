<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>イベントポータル</title>
<style>
    body {
        font-family: "Segoe UI", sans-serif;
        background: #f2f2f2;
        margin: 0;
        padding: 0;
    }

    .header {
        background: #c7d5e2;
        color: white;
        padding: 16px;
        text-align: center;
        font-size: 20px;
        font-weight: bold;
    }

    .container {
        width: 90%;
        max-width: 450px;
        background: white;
        margin: 30px auto;
        padding: 20px;
        border-radius: 10px;
        box-shadow: 0 0 10px rgba(0,0,0,0.1);
    }

    .event-box {
        border: 1px solid #ccc;
        padding: 15px;
        border-radius: 8px;
        margin-bottom: 20px;
        background: #fafafa;
    }

    .event-title {
        font-size: 16px;
        font-weight: bold;
        margin-bottom: 10px;
    }

    .label {
        display: inline-block;
        width: 100px;
        color: #555;
        font-size: 14px;
    }

    .btn {
        padding: 8px 14px;
        border-radius: 6px;
        border: none;
        cursor: pointer;
        font-size: 14px;
        margin-top: 10px;
    }

    .btn-attend {
        background: #007bff;
        color: white;
    }

    .btn-return {
        background: #777;
        color: white;
    }

    .popup {
        display: none;
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(0,0,0,0.5);
        justify-content: center;
        align-items: center;
    }

    .popup-content {
        background: white;
        padding: 20px;
        width: 85%;
        max-width: 350px;
        border-radius: 10px;
        text-align: center;
    }

    .error-box {
        background: #ffe0e0;
        border-left: 5px solid #ff5555;
        padding: 10px;
        margin-top: 10px;
        border-radius: 5px;
        color: #c00;
        font-size: 14px;
    }

    footer { text-align: center; margin-top: 40px; color: #666; font-size: 12px; }


.footer-bar { position: fixed; left: 0; bottom: 0; width: 100%; background: #e9e9e9; padding: 8px 0; box-shadow: 0 -2px 0 rgba(0,0,0,0.06); }
.footer-inner { width: 90%; max-width: 1100px; margin: 0 auto; text-align: center; color: #666; font-size: 12px; }
.footer-inner .year { margin-right: 8px; font-weight: 600; }
.footer-inner .dots { display: inline-block; width: 60%; vertical-align: middle; border-bottom: 1px dotted #666; margin-left: 8px; transform: translateY(-3px); }

</style>
</head>
<body>

<div class="header">イベントポータル</div>

<div class="container">
    <div class="event-box">
        <div class="event-title">イベント名：＊＊＊＊＊</div>

        <div><span class="label">開催日時：</span>＊＊＊＊＊</div>
        <div><span class="label">会場名：</span>＊＊＊＊＊</div>
        <div><span class="label">主催者名：</span>＊＊＊＊＊</div>
        <div><span class="label">ステータス：</span>＊＊＊＊＊</div>

        <button class="btn btn-attend" onclick="openPopup()">参加する</button>
    </div>
</div>

<!-- ポップアップ -->
<div class="popup" id="popup">
    <div class="popup-content">
        <p>このイベントに参加しますか？</p>
        <form action="EntryJoinExecuteAction" method="post" id="attendForm">
           <input type="hidden" name="eventId" value="" />
            <button type="button" class="btn btn-return" onclick="closePopup()">戻る</button>
            <button type="submit" class="btn btn-attend">参加する</button>
        </form>
    </div>
</div>

<footer>
  <div class="footer-bar">
    <div class="footer-inner">
      <span class="year">@2025</span><span class="dots" aria-hidden="true"></span>
    </div>
  </div>
</footer>

<script>
function openPopup() {
    document.getElementById("popup").style.display = "flex";
}

function closePopup() {
    document.getElementById("popup").style.display = "none";
}
</script>

</body>
</html>
