<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>エラー | イベントポータル</title>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background: #f5f5f5;
            margin: 0;
            padding: 0;
            min-height: 100vh;
        }

        .top-bar {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 15px 30px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }

        .top-bar-left {
            display: flex;
            align-items: center;
            gap: 20px;
        }

        .top-bar-right {
            display: flex;
            align-items: center;
            gap: 15px;
        }

        .user-name {
            font-weight: bold;
        }

        .top-btn {
            padding: 8px 20px;
            background: rgba(255,255,255,0.2);
            color: white;
            border: 2px solid white;
            border-radius: 5px;
            text-decoration: none;
            font-weight: bold;
            transition: all 0.3s;
            cursor: pointer;
        }

        .top-btn:hover {
            background: white;
            color: #667eea;
        }

        .error-container {
            max-width: 600px;
            margin: 80px auto;
            background: white;
            padding: 60px 40px;
            border-radius: 20px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.1);
            text-align: center;
            animation: fadeIn 0.5s;
        }

        @keyframes fadeIn {
            from {
                opacity: 0;
                transform: translateY(20px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .error-icon {
            font-size: 100px;
            margin-bottom: 30px;
            animation: bounce 1s ease-in-out;
        }

        @keyframes bounce {
            0%, 100% {
                transform: translateY(0);
            }
            50% {
                transform: translateY(-20px);
            }
        }

        .error-title {
            font-size: 32px;
            font-weight: bold;
            color: #dc3545;
            margin-bottom: 20px;
        }

        .error-message {
            font-size: 18px;
            color: #6c757d;
            margin: 20px 0;
            line-height: 1.6;
        }

        .error-details {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 10px;
            margin: 30px 0;
            text-align: left;
            border-left: 4px solid #dc3545;
        }

        .error-details p {
            margin: 10px 0;
            color: #495057;
        }

        .error-details strong {
            color: #212529;
        }

        .button-group {
            display: flex;
            gap: 15px;
            justify-content: center;
            margin-top: 40px;
            flex-wrap: wrap;
        }

        .btn {
            padding: 15px 40px;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            transition: all 0.3s;
        }

        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(102,126,234,0.4);
        }

        .btn-secondary {
            background: #6c757d;
            color: white;
        }

        .btn-secondary:hover {
            background: #545b62;
        }

        .help-text {
            margin-top: 30px;
            padding: 20px;
            background: #e7f3ff;
            border-radius: 10px;
            color: #004085;
            font-size: 14px;
        }

        .help-text h4 {
            margin-top: 0;
            color: #004085;
        }

        .help-text ul {
            text-align: left;
            margin: 10px 0;
            padding-left: 20px;
        }

        .help-text li {
            margin: 5px 0;
        }

        @media (max-width: 768px) {
            .top-bar {
                flex-direction: column;
                gap: 10px;
                padding: 15px;
            }

            .top-bar-right {
                flex-wrap: wrap;
                justify-content: center;
            }

            .error-container {
                margin: 40px 20px;
                padding: 40px 20px;
            }

            .error-icon {
                font-size: 80px;
            }

            .error-title {
                font-size: 24px;
            }

            .error-message {
                font-size: 16px;
            }

            .button-group {
                flex-direction: column;
            }

            .btn {
                width: 100%;
            }
        }
    </style>
</head>
<body>
    <!-- トップバー（ユーザー情報がある場合のみ表示） -->
    <c:if test="${user != null}">
        <div class="top-bar">
            <div class="top-bar-left">
                <span style="font-size: 24px; font-weight: bold;">
                    <c:choose>
                        <c:when test="${user.user_type == 2}">🎫 イベント管理</c:when>
                        <c:otherwise>📱 イベントポータル</c:otherwise>
                    </c:choose>
                </span>
            </div>
            <div class="top-bar-right">
                <span class="user-name">${user.user_name} 様</span>
                <c:choose>
                    <c:when test="${user.user_type == 2}">
                        <a href="${pageContext.request.contextPath}/eventportal/host/HostMenu.action"
                           class="top-btn">🏠 トップページ</a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/eventportal/entrymenu/EntryEventManage.action"
                           class="top-btn">🏠 トップページ</a>
                    </c:otherwise>
                </c:choose>
                <a href="${pageContext.request.contextPath}/eventportal/auth/Logout.action"
                   class="top-btn">🚪 ログアウト</a>
            </div>
        </div>
    </c:if>

    <div class="error-container">
        <div class="error-icon">⚠️</div>
        <div class="error-title">エラーが発生しました</div>

        <c:choose>
            <c:when test="${errorMessage != null}">
                <div class="error-message">${errorMessage}</div>
            </c:when>
            <c:otherwise>
                <div class="error-message">このイベントのチケットが見つかりません。</div>
            </c:otherwise>
        </c:choose>

        <!-- エラー詳細（あれば表示） -->
        <c:if test="${errorMessage != null}">
            <div class="error-details">
                <p><strong>エラー内容:</strong></p>
                <p>${errorMessage}</p>

                <c:if test="${exception != null}">
                    <p style="margin-top: 15px;"><strong>詳細情報:</strong></p>
                    <p style="font-size: 12px; color: #6c757d; font-family: monospace;">
                        ${exception.class.name}<br>
                        ${exception.message}
                    </p>
                </c:if>
            </div>
        </c:if>

        <div class="button-group">
            <c:choose>
                <c:when test="${user != null}">
                    <!-- ログイン済みの場合 -->
                    <c:choose>
                        <c:when test="${user.user_type == 2}">
                            <!-- 主催者 -->
                            <a href="${pageContext.request.contextPath}/eventportal/host/HostMenu.action"
                               class="btn btn-primary">
                                🏠 トップページに戻る
                            </a>
                        </c:when>
                        <c:otherwise>
                            <!-- 参加者 -->
                            <a href="${pageContext.request.contextPath}/eventportal/entrymenu/EntryEventManage.action"
                               class="btn btn-primary">
                                🏠 トップページに戻る
                            </a>
                        </c:otherwise>
                    </c:choose>
                    <a href="javascript:history.back()" class="btn btn-secondary">
                        ← 前のページに戻る
                    </a>
                </c:when>
                <c:otherwise>
                    <!-- 未ログインの場合 -->
                    <a href="${pageContext.request.contextPath}/eventportal/auth/AuthPage.action"
                       class="btn btn-primary">
                        🏠 ログインページに戻る
                    </a>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="help-text">
            <h4>💡 よくあるエラーの原因</h4>
            <ul>
                <li>指定されたデータが存在しない</li>
                <li>アクセス権限がない</li>
                <li>セッションの有効期限切れ</li>
                <li>不正なURLパラメータ</li>
            </ul>
            <p style="margin-top: 15px;">
                問題が解決しない場合は、一度ログアウトして再度ログインしてみてください。
            </p>
        </div>
    </div>

    <!-- デバッグ情報（開発環境のみ表示） -->
    <c:if test="${pageContext.request.serverName == 'localhost'}">
        <div style="max-width: 800px; margin: 20px auto; padding: 20px; background: #f8f9fa; border-radius: 10px; font-size: 12px; color: #6c757d;">
            <strong>🔧 デバッグ情報（本番環境では非表示）</strong><br>
            <hr style="margin: 10px 0;">
            Request URI: ${pageContext.request.requestURI}<br>
            Context Path: ${pageContext.request.contextPath}<br>
            <c:if test="${eventId != null}">Event ID: ${eventId}<br></c:if>
            <c:if test="${user != null}">User ID: ${user.user_id}<br></c:if>
            <c:if test="${user != null}">User Type: ${user.user_type}<br></c:if>
        </div>
    </c:if>
</body>
</html>
