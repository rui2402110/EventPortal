<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>商品削除確認 - イベントポータル</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-50">
    <header class="w-full bg-[#d0e4ff] py-4 border-b border-gray-300 mb-8 text-center">
        <h1 class="font-bold text-lg">イベントポータル</h1>
        <p class="text-sm">商品削除確認</p>
    </header>

    <main class="max-w-2xl mx-auto px-4">
        <div class="bg-white border border-gray-400 p-8 rounded shadow-lg">
            <!-- 警告メッセージ -->
            <div class="bg-red-50 border-l-4 border-red-500 p-4 mb-6">
                <div class="flex items-center">
                    <svg class="h-6 w-6 text-red-500 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"/>
                    </svg>
                    <p class="font-bold text-red-700">この商品を削除してもよろしいですか？</p>
                </div>
                <p class="text-sm text-red-600 mt-2 ml-9">削除した商品は元に戻すことができません。</p>
            </div>

            <!-- 商品情報表示 -->
            <div class="mb-8">
                <h2 class="text-lg font-bold mb-4 border-b pb-2">削除対象の商品情報</h2>

                <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <!-- 左側: 商品画像 -->
                    <div class="flex items-center justify-center">
                        <c:choose>
                            <c:when test="${not empty product.image}">
                                <!-- 画像パスの処理 -->
                                <c:set var="imagePath" value="${product.image}" />
                                <c:if test="${!fn:startsWith(imagePath, '/')}">
                                    <c:set var="imagePath" value="/${imagePath}" />
                                </c:if>
                                <c:set var="imagePath" value="${fn:replace(imagePath, ' ', '%20')}" />

                                <img src="${pageContext.request.contextPath}${imagePath}"
                                     alt="${product.productName}"
                                     class="max-w-full h-48 object-contain border border-gray-300 rounded">
                            </c:when>
                            <c:otherwise>
                                <div class="w-48 h-48 bg-gray-100 border border-gray-300 rounded flex items-center justify-center text-gray-400">
                                    画像なし
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <!-- 右側: 商品詳細 -->
                    <div class="space-y-4">
                        <div>
                            <p class="text-xs text-gray-500 mb-1">商品ID</p>
                            <p class="font-mono text-sm"><c:out value="${product.itemId}" /></p>
                        </div>

                        <div>
                            <p class="text-xs text-gray-500 mb-1">商品名</p>
                            <p class="font-bold text-lg"><c:out value="${product.productName}" /></p>
                        </div>

                        <div>
                            <p class="text-xs text-gray-500 mb-1">価格</p>
                            <p class="text-xl font-semibold text-blue-600">
                                <fmt:formatNumber value="${product.price}" pattern="#,###" />円
                            </p>
                        </div>

                        <div>
                            <p class="text-xs text-gray-500 mb-1">在庫数</p>
                            <p class="text-lg"><c:out value="${product.stock}" />個</p>
                        </div>

                        <c:if test="${not empty product.overview}">
                            <div>
                                <p class="text-xs text-gray-500 mb-1">概要</p>
                                <p class="text-sm text-gray-700"><c:out value="${product.overview}" /></p>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>

            <!-- ボタンエリア -->
            <div class="flex gap-4 justify-center pt-6 border-t">
                <a href="${pageContext.request.contextPath}/eventportal/host/hostdetail/HostProduct.action?eventId=${product.eventId}"
                   class="bg-gray-500 text-white px-8 py-3 rounded font-bold hover:bg-gray-600 transition">
                    キャンセル
                </a>

                <form action="${pageContext.request.contextPath}/eventportal/host/hostdetail/ProductDeleteExecute.action" method="post" class="inline">
                    <input type="hidden" name="itemId" value="${product.itemId}">
                    <input type="hidden" name="eventId" value="${product.eventId}">
                    <button type="submit"
                            class="bg-red-600 text-white px-8 py-3 rounded font-bold hover:bg-red-700 transition">
                        削除する
                    </button>
                </form>
            </div>
        </div>
    </main>
</body>
</html>
