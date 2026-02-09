<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>エラー | イベントポータル</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
        }

        .error-container {
            background: white;
            border-radius: 20px;
            padding: 40px;
            max-width: 600px;
            width: 100%;
            box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
            text-align: center;
        }

        .error-icon {
            font-size: 80px;
            margin-bottom: 20px;
        }

        .error-title {
            color: #e74c3c;
            font-size: 28px;
            font-weight: bold;
            margin-bottom: 20px;
        }

        .error-message {
            color: #555;
            font-size: 16px;
            line-height: 1.6;
            margin-bottom: 30px;
            padding: 20px;
            background: #fee;
            border-radius: 10px;
            border-left: 4px solid #e74c3c;
            text-align: left;
        }

        .error-details {
            background: #f8f9fa;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 30px;
            text-align: left;
            font-size: 14px;
            color: #666;
            max-height: 200px;
            overflow-y: auto;
        }

        .error-details pre {
            white-space: pre-wrap;
            word-wrap: break-word;
            margin: 0;
        }

        .button-group {
            display: flex;
            gap: 15px;
            justify-content: center;
        }

        .btn {
            padding: 12px 30px;
            border: none;
            border-radius: 10px;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
            transition: all 0.3s;
            text-decoration: none;
            display: inline-block;
        }

        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }

        .btn-secondary {
            background: #6c757d;
            color: white;
        }

        .btn-secondary:hover {
            background: #5a6268;
        }
    </style>
</head>
<body>
    <div class="error-container">
        <div class="error-icon">⚠️</div>
        <h1 class="error-title">エラーが発生しました</h1>

        <c:choose>
            <c:when test="${not empty errorMessage}">
                <div class="error-message">
                    ${errorMessage}
                </div>
            </c:when>
            <c:when test="${not empty pageContext.exception}">
                <div class="error-message">
                    エラーが発生しました。<br>
                    エラーの種類: ${pageContext.exception.class.name}
                </div>
                <div class="error-details">
                    <strong>詳細:</strong><br>
                    <pre>${pageContext.exception.message}</pre>
                </div>
            </c:when>
            <c:otherwise>
                <div class="error-message">
                    予期しないエラーが発生しました。<br>
                    お手数ですが、もう一度お試しください。
                </div>
            </c:otherwise>
        </c:choose>

        <div class="button-group">
            <a href="javascript:history.back()" class="btn btn-secondary">
                ← 前のページに戻る
            </a>
            <c:choose>
                <c:when test="${not empty sessionScope.user}">
                    <c:choose>
                        <c:when test="${sessionScope.user.user_type == 2}">
                            <a href="${pageContext.request.contextPath}/eventportal/host/HostMenu.action" class="btn btn-primary">
                                🏠 トップページ
                            </a>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/eventportal/entrymenu/EntryMenu.action" class="btn btn-primary">
                                🏠 トップページ
                            </a>
                        </c:otherwise>
                    </c:choose>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-primary">
                        🏠 トップページ
                    </a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <script>
        // コンソールにエラー情報を出力（デバッグ用）
        console.error('=== エラー情報 ===');
        console.error('エラーメッセージ:', '${errorMessage}');
        <c:if test="${not empty pageContext.exception}">
        console.error('例外クラス:', '${pageContext.exception.class.name}');
        console.error('例外メッセージ:', '${pageContext.exception.message}');
        </c:if>
        console.error('==================');
    </script>
</body>
</html>
