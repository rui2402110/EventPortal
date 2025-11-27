<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>イベントポータル - グッズ・フード</title>
  <!-- 共通CSS -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/common/style.css">
</head>
<body>
<div>
    <c:choose>
        <c:when test="${not empty proList}">
            <table class="product-table">
                <thead>
                    <tr>
                        <th>画像</th>
                        <th>商品ID</th>
                        <th>商品名</th>
                        <th>価格</th>
                        <th>在庫</th>
                        <th>概要</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="product" items="${proList}">
                        <tr>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty product.image}">
                                        <img src="${pageContext.request.contextPath}${product.image}"
                                             alt="${product.productName}"
                                             class="product-image"
                                             onerror="this.src='${pageContext.request.contextPath}/images/no-image.png'" />
                                    </c:when>
                                    <c:otherwise>
                                        <img src="${pageContext.request.contextPath}/images/no-image.png"
                                             alt="画像なし"
                                             class="product-image" />
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td><c:out value="${product.itemId}" /></td>
                            <td><c:out value="${product.productName}" /></td>
                            <td class="price">
                                <fmt:formatNumber value="${product.price}" pattern="#,###" />円
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${product.stock > 10}">
                                        <span class="stock-info stock-available">
                                            在庫: ${product.stock}
                                        </span>
                                    </c:when>
                                    <c:when test="${product.stock > 0}">
                                        <span class="stock-info stock-low">
                                            在庫: ${product.stock}
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="stock-info stock-out">
                                            在庫切れ
                                        </span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty product.overview}">
                                        <c:out value="${product.overview.length() > 30 ? product.overview.substring(0, 30) += '...' : product.overview}" />
                                    </c:when>
                                    <c:otherwise>
                                        -
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/eventportal/host/hostDetail/ProductEdit.action?itemId=${product.itemId}"
                                   class="btn btn-edit">編集</a>
                                <a href="${pageContext.request.contextPath}/eventportal/host/hostDetail/ProductDelete.action?itemId=${product.itemId}"
                                   class="btn btn-danger"
                                   onclick="return confirm('本当に削除しますか？')">削除</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <div class="no-data">
                登録されている商品がありません。<br />
                「新規商品登録」ボタンから商品を登録してください。
            </div>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>