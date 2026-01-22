<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>イベント開催確認</title>
</head>
<body>

    <h2>イベント状態の確認</h2>

    <p>
        このイベントは<strong>${massage1}</strong>です。${massage2}
    </p>

    <form action="${pageContext.request.contextPath}/eventportal/host/hostdetail/EventHoldExecute.action" method="post">
        <%-- eventIdを次のActionに渡すために隠しフィールドとして保持（リクエストから取得） --%>
        <input type="hidden" name="eventId" value="${param.eventId}">

        <%--
            event_hold_stateが "1" (開催前) 以外のときは
            disabled 属性を付与してボタンを押せなくする
        --%>
        <button type="submit"
            <c:if test="${event_hold_state != '1'}">disabled</c:if>
        >
            イベント開催
        </button>
    </form>

    <br>
    <a href="javascript:history.back()">戻る</a>

</body>
</html>