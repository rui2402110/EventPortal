<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>イベント終了確認</title>
</head>
<body>
    <div class="header">イベントポータル</div>
    <div class="user-info">
        <p>ログインユーザー: <strong>${sessionScope.user.user_id}</strong></p>
    </div>

    <h2>イベント状態の確認</h2>

    <p>
        このイベントは<strong>${massage1}</strong>です。${massage2}
    </p>

    <form action="${pageContext.request.contextPath}/eventportal/host/hostdetail/EventEndExecute.action" method="post">
        <%-- eventIdを次のActionに渡すために隠しフィールドとして保持（リクエストから取得） --%>
        <input type="hidden" name="eventId" value="${param.eventId}">

        <%--
            event_hold_stateが "2" (開催中) 以外のときは
            disabled 属性を付与してボタンを押せなくする
        --%>
        <button type="submit"
            <c:if test="${event_hold_state != '2'}">disabled</c:if>
        >
            イベント終了
        </button>
    </form>

    <br>
    <a href="javascript:history.back()">戻る</a>

</body>
</html>