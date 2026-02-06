<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>イベントポータル - グッズ・フード</title>
  <!-- 共通CSS -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/common/style.css">
  <style>
  	.product-image {
    width: 80px;
    height: 80px;
    object-fit: cover;
    border-radius: 4px;
    border: 1px solid #ddd;
    background-color: #f5f5f5;
    display: block;
	}

	.product-image-wrapper {
	    width: 80px;
	    height: 80px;
	    background-color: #f5f5f5;
	    border: 1px solid #ddd;
	    border-radius: 4px;
	    display: flex;
	    align-items: center;
	    justify-content: center;
	    font-size: 10px;
	    color: #999;
	    text-align: center;
	}
  </style>
</head>
<body>
<div>
    <div class="action-buttons">
        <a href="${pageContext.request.contextPath}/eventportal/host/HostEventDetail.action?eventId=${param.eventId}" class="btn btn-secondary">イベント詳細に戻る</a>
        <a href="${pageContext.request.contextPath}/eventportal/host/hostdetail/ProductCreate.action?eventId=${param.eventId}" class="btn btn-success">新規商品登録</a>
    </div>
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
                                        <!-- 画像パスの処理 -->
                                        <c:set var="imagePath" value="${product.image}" />

                                        <!-- 先頭にスラッシュがない場合は追加 -->
                                        <c:if test="${!fn:startsWith(imagePath, '/')}">
                                            <c:set var="imagePath" value="/${imagePath}" />
                                        </c:if>

                                        <!-- スペースを%20に変換（URLエンコード） -->
                                        <c:set var="imagePath" value="${fn:replace(imagePath, ' ', '%20')}" />

                                        <!-- onerrorで存在しない画像を呼び出さず、代替表示に切り替え -->
                                        <img src="${pageContext.request.contextPath}${imagePath}"
                                             alt="${product.productName}"
                                             class="product-image"
                                             onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';" />
                                        <div class="product-image-wrapper" style="display:none;">
                                            画像<br>読込失敗
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="product-image-wrapper">
                                            画像なし
                                        </div>
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
                                <a href="${pageContext.request.contextPath}/eventportal/host/hostdetail/ProductEdit.action?itemId=${product.itemId}"
                                   class="btn btn-edit">編集</a>

                                <a href="${pageContext.request.contextPath}/eventportal/host/hostdetail/ProductDelete.action?itemId=${product.itemId}"
                                   class="btn btn-danger">削除</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <div class="no-data">
                登録されている商品がありません。<br/>
                「新規商品登録」ボタンから商品を登録してください。
            </div>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>
