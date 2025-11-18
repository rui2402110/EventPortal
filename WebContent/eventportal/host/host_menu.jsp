<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <%-- base.jsp を読み込み --%>

  <title>イベントポータル - 認証</title>
</head>
<body>
  <div class="container">
    <div class="header">イベントポータル</div>
    <div class="user-info">
        <p>ログインユーザー: <strong>${sessionScope.user.user_id}</strong></p>
    </div>

    <a href="${pageContext.request.contextPath}/eventportal/hostmenu/HostEventCreate.action">イベント新規作成</a>

    <div class="content">

    </div>
    <c:choose>
        <c:when test="${not empty event}">
            <table>
                <thead>
                    <tr>
                        <th>イベントID</th>
                        <th>イベント名</th>
                        <th>開催日時</th>
                        <th>場所</th>
                        <th>定員</th>
                        <th>状態</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="evt" items="${event}">
                        <tr>
                            <td>${evt.eventId}</td>
                            <td>${evt.eventName}</td>
                            <td>${evt.holdingDate}</td>
                            <td>${evt.address}</td>
                            <td>${evt.maxCount}</td>
                            <td>${evt.eventHoldState}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/eventportal/hostmenu/HostEventDetail.action?eventId=${evt.eventId}">詳細</a> |
                                <a href="EventEdit.action?id=${evt.eventId}">編集</a> |
                                <a href="EventDelete.action?id=${evt.eventId}" onclick="return confirm('本当に削除しますか?')">削除</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <div class="no-data">
                <p>登録されているイベントがありません。</p>
            </div>
        </c:otherwise>
    </c:choose>

    <div class="footer">@2025.................................................</div>
  </div>
</body>
</html>