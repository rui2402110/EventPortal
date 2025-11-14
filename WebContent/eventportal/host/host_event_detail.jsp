<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <div class="user-info">
        <p>ログインユーザー: <strong>${sessionScope.user.user_id}</strong></p>
        <p>イベント詳細</p>
    </div>

    <div class="content">
          <table>
            <thead>
              <tr>
                <th>イベントID</th>
                <th>イベント名</th>
                <th>開催日時</th>
                <th>場所</th>
                <th>定員</th>
                <th>状態</th>
                <th>概要</th>
                <th>電話番号</th>
                <th>リンク</th>
                <th>単位</th>
                <th>担当者ID</th>
                <th>登録日</th>
                <th>合計支払額</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>${evt.eventId}</td>
                <td>${evt.eventName}</td>
                <td>${evt.holdingDate}</td>
                <td>${evt.address}</td>
                <td>${evt.maxCount}</td>
                <td>${evt.eventHoldState}</td>
                <td>${evt.eventOverview}</td>
                <td>${evt.phoneNumber}</td>
                <td class="link-cell">
                  <c:if test="${not empty evt.link}">
                    <a href="${evt.link}" target="_blank">${evt.link}</a>
                  </c:if>
                </td>
                <td>${evt.credit}</td>
                <td>${evt.userId}</td>
                <td>${evt.eventAddDate}</td>
                <td>${evt.totalPayment}円</td>
                <td>
                </td>
              </tr>
            </tbody>
          </table>
          <button>イベント開催</button>
          <button>イベント終了</button>
          <button>グッズ・フード管理</button>
          <button>QRコード読み取り</button>

    </div>

    <div class="footer">@2025.................................................</div>
  </div>
</body>
</html>