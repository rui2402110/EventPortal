<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>グッズ・フード在庫管理（開催中）</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/common/style.css">
</head>
<body>
<div class="container">
    <h2>在庫・売上管理</h2>
    <div class="action-buttons">
        <a href="${pageContext.request.contextPath}/eventportal/host/HostEventDetail.action?eventId=${eventId}" class="btn btn-secondary">戻る</a>
    </div>

    <form action="ProductHoldingExecute.action" method="post">
        <input type="hidden" name="eventId" value="${eventId}">
        <table class="product-table">
            <thead>
                <tr>
                    <th>商品ID</th>
                    <th>商品名</th>
                    <th>単価</th>
                    <th>現在の在庫</th>
                    <th>売上個数入力</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="product" items="${proList}">
                    <tr>
                        <td>${product.itemId}</td>
                        <td>${product.productName}</td>
                        <td><fmt:formatNumber value="${product.price}" />円</td>
                        <td>
                            <c:choose>
                                <c:when test="${product.stock == 0}"><span style="color:red;">在庫切れ</span></c:when>
                                <c:otherwise>${product.stock}</c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <input type="number" name="soldCount_${product.itemId}" min="0" max="${product.stock}" value="0" class="form-control">
                            <input type="hidden" name="itemIds" value="${product.itemId}">
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <div style="margin-top: 20px;">
            <button type="submit" class="btn btn-primary" onclick="return confirm('売上を確定し、在庫を更新しますか？')">確定</button>
        </div>
    </form>
</div>
</body>
</html>