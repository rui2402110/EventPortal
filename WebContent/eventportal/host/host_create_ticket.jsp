<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>チケット作成 - イベントポータル</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: Arial, sans-serif;
            background-color: #f5f5f5;
            padding: 20px;
        }

        .container {
            max-width: 700px;
            margin: 0 auto;
            background: white;
            border: 1px solid #ddd;
        }

        .header {
            background-color: #4a5568;
            color: white;
            padding: 20px;
            border-bottom: 3px solid #2d3748;
        }

        .header h1 {
            font-size: 22px;
            margin-bottom: 5px;
        }

        .header p {
            font-size: 13px;
            opacity: 0.9;
        }

        .content {
            padding: 30px;
        }

        .error-message {
            background-color: #fee;
            border: 1px solid #fcc;
            color: #c33;
            padding: 12px;
            margin-bottom: 20px;
            border-radius: 3px;
        }

        .info-box {
            background-color: #fff3cd;
            border: 1px solid #ffeaa7;
            color: #856404;
            padding: 12px;
            margin-top: 15px;
            border-radius: 3px;
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            display: block;
            font-weight: bold;
            margin-bottom: 6px;
            color: #333;
        }

        .form-group label .required {
            color: red;
        }

        .form-group select,
        .form-group textarea {
            width: 100%;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 3px;
            font-size: 14px;
        }

        .form-group select:focus,
        .form-group textarea:focus {
            outline: none;
            border-color: #4a5568;
        }

        .form-group textarea {
            resize: vertical;
            min-height: 80px;
            font-family: Arial, sans-serif;
        }

        .help-text {
            font-size: 12px;
            color: #666;
            margin-top: 4px;
        }

        .button-group {
            display: flex;
            gap: 10px;
            margin-top: 25px;
            padding-top: 20px;
            border-top: 1px solid #eee;
        }

        .btn {
            padding: 10px 20px;
            border: none;
            border-radius: 3px;
            font-size: 14px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            text-align: center;
        }

        .btn-primary {
            background-color: #4a5568;
            color: white;
            flex: 1;
        }

        .btn-primary:hover {
            background-color: #2d3748;
        }

        .btn-secondary {
            background-color: #e2e8f0;
            color: #333;
            flex: 1;
        }

        .btn-secondary:hover {
            background-color: #cbd5e0;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>チケット作成</h1>
            <p>参加者にチケットを発行します</p>
        </div>

        <div class="content">
            <c:if test="${not empty error}">
                <div class="error-message">
                    ${error}
                </div>
            </c:if>

            <c:if test="${not empty existingTicket}">
                <div class="info-box">
                    <strong>既存のチケット情報</strong><br>
                    チケットID: ${existingTicket.ticketId}<br>
                    ステータス: ${existingTicket.statusText}<br>
                    発行日時: ${existingTicket.createdAt}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/eventportal/host/CreateTicketExecute.action" method="post">
                <div class="form-group">
                    <label for="eventId">
                        イベント選択 <span class="required">*</span>
                    </label>
                    <select name="eventId" id="eventId" required>
                        <option value="">-- イベントを選択 --</option>
                        <c:forEach var="event" items="${events}">
                            <option value="${event.eventId}"
                                    ${selectedEvent != null && selectedEvent.eventId == event.eventId ? 'selected' : ''}>
                                ${event.eventName} (${event.eventId})
                            </option>
                        </c:forEach>
                    </select>
                    <div class="help-text">チケットを発行するイベントを選択してください</div>
                </div>

                <div class="form-group">
                    <label for="userId">
                        参加者選択 <span class="required">*</span>
                    </label>
                    <select name="userId" id="userId" required>
                        <option value="">-- 参加者を選択 --</option>
                        <c:forEach var="entryUser" items="${entryUsers}">
                            <option value="${entryUser.user_id}">
                                ${entryUser.user_name} (${entryUser.user_id})
                            </option>
                        </c:forEach>
                    </select>
                    <div class="help-text">チケットを発行する参加者を選択してください</div>
                </div>

                <div class="form-group">
                    <label for="ticketInfo">
                        チケット情報（任意）
                    </label>
                    <textarea name="ticketInfo" id="ticketInfo"
                              placeholder="座席番号、特別情報など（例：A-12、VIP席）"></textarea>
                    <div class="help-text">座席番号などの追加情報（省略可）</div>
                </div>

                <div class="button-group">
                    <button type="submit" class="btn btn-primary">
                        チケット発行
                    </button>
                    <a href="${pageContext.request.contextPath}/eventportal/host/HostMain.action"
                       class="btn btn-secondary">
                        戻る
                    </a>
                </div>
            </form>
        </div>
    </div>
</body>
</html>