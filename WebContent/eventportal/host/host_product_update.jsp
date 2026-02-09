<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>グッズ編集 - イベントポータル</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        .balloon-error {
            position: absolute; right: 2rem; top: 35%;
            background: #ffffff; border: 2px solid #1e3a8a;
            border-radius: 4px; padding: 15px; display: none;
            z-index: 10;
        }
        .balloon-error::after {
            content: ''; position: absolute; top: 100%; left: 50%;
            margin-left: -10px; border-width: 10px; border-style: solid;
            border-color: #1e3a8a transparent transparent transparent;
        }
        .current-image {
            max-width: 100%;
            max-height: 100%;
            object-fit: contain;
        }
    </style>
</head>
<body class="bg-gray-50">
    <header class="w-full bg-[#d0e4ff] py-4 border-b border-gray-300 mb-8 text-center">
        <h1 class="font-bold text-lg">イベントポータル</h1>
        <p class="text-sm">グッズ・フード編集ページ</p>
    </header>

    <main class="max-w-4xl mx-auto px-4">
        <form id="productForm" action="${pageContext.request.contextPath}/eventportal/host/hostdetail/ProductEditExecute.action" method="post" enctype="multipart/form-data" class="bg-white border border-gray-400 p-8 rounded relative shadow-lg">

            <!-- 商品名（初期値を設定） -->
            <input type="text" name="productName" id="itemName" value="${product.productName}" placeholder="グッズ・商品名を入力" class="w-full md:w-2/3 border border-gray-400 p-2 mb-8 block">

            <div class="grid grid-cols-1 md:grid-cols-2 gap-8 mb-8 relative">
                <!-- 画像プレビュー（既存画像を表示） -->
                <div class="relative border-2 border-dashed border-gray-400 h-48 bg-gray-50 flex flex-col items-center justify-center text-gray-400 overflow-hidden">
                    <!-- 既存の画像を表示 -->
                    <c:choose>
                        <c:when test="${not empty product.image}">
                            <!-- 画像パスの処理 -->
                            <c:set var="imagePath" value="${product.image}" />
                            <c:if test="${!fn:startsWith(imagePath, '/')}">
                                <c:set var="imagePath" value="/${imagePath}" />
                            </c:if>
                            <c:set var="imagePath" value="${fn:replace(imagePath, ' ', '%20')}" />

                            <img id="preview" class="absolute inset-0 w-full h-full object-contain" src="${pageContext.request.contextPath}${imagePath}">
                            <div id="upload-prompt" class="text-center p-4 opacity-0">
                                <p class="text-xs mb-2">クリックして画像を変更</p>
                                <input type="file" name="imageFile" id="imageInput" accept="image/*" class="absolute inset-0 opacity-0 cursor-pointer">
                                <span class="text-sm">画像を変更できます</span>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <img id="preview" class="absolute inset-0 w-full h-full object-contain hidden" src="">
                            <div id="upload-prompt" class="text-center p-4">
                                <p class="text-xs mb-2">クリックして画像を選択</p>
                                <input type="file" name="imageFile" id="imageInput" accept="image/*" class="absolute inset-0 opacity-0 cursor-pointer">
                                <span class="text-sm">画像を表示できます</span>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- 概要（初期値を設定） -->
                <textarea name="overview" placeholder="概要を入力" class="border border-gray-400 h-48 p-4 w-full">${product.overview}</textarea>

                <div id="errorBox" class="balloon-error">
                    <p class="text-blue-900 font-bold">入力が行われていません</p>
                </div>
            </div>

            <div class="flex flex-wrap items-end justify-between gap-4">
                <div class="flex gap-4">
                    <div>
                        <label class="block text-xs mb-1 font-bold">値段を入力</label>
                        <!-- 価格の初期値を設定 -->
                        <input type="number" name="price" value="${product.price}" class="border border-gray-400 p-2 w-32">
                    </div>
                    <div>
                        <label class="block text-xs mb-1 font-bold">残り個数</label>
                        <!-- 在庫の初期値を設定 -->
                        <input type="number" name="stock" value="${product.stock}" class="border border-gray-400 p-2 w-24">
                    </div>
                </div>

                <div class="flex gap-3">
                    <a href="${pageContext.request.contextPath}/eventportal/host/hostdetail/HostProduct.action?eventId=${product.eventId}" class="border border-gray-400 px-10 py-2 bg-white hover:bg-gray-100 text-center">戻る</a>
                    <button type="button" onclick="checkInput()" class="bg-[#3b82f6] text-white px-10 py-2 font-bold shadow-md hover:bg-blue-600">更新</button>
                </div>
            </div>

            <!-- 隠しフィールド -->
            <input type="hidden" name="itemId" value="${product.itemId}">
            <input type="hidden" name="eventId" value="${product.eventId}">
            <!-- 既存の画像パスを保持（画像が変更されない場合に使用） -->
            <input type="hidden" name="currentImage" value="${product.image}">
        </form>
    </main>

    <script>
        // 画像プレビューのスクリプト
        document.getElementById('imageInput').addEventListener('change', function(e) {
            const file = e.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    document.getElementById('preview').src = e.target.result;
                    document.getElementById('preview').classList.remove('hidden');
                    document.getElementById('upload-prompt').classList.add('opacity-0');
                }
                reader.readAsDataURL(file);
            }
        });

        // 入力チェックと送信
        function checkInput() {
            const name = document.getElementById('itemName').value;
            const errorBox = document.getElementById('errorBox');

            if(!name) {
                errorBox.style.display = 'block';
            } else {
                errorBox.style.display = 'none';
                // フォームを送信
                document.getElementById('productForm').submit();
            }
        }
    </script>
</body>
</html>
