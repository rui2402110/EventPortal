<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="eventportal.entity.Event" %>
<%@ page import="eventportal.entity.Ticket" %>
<%
    Event event = (Event) request.getAttribute("event");
    Ticket ticket = (Ticket) request.getAttribute("ticket");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>QRコード表示 - <%= event.getEventName() %></title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Helvetica Neue', Arial, 'Hiragino Kaku Gothic ProN', 'Hiragino Sans', Meiryo, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
        }

        .container {
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
            padding: 40px;
            max-width: 500px;
            width: 100%;
            text-align: center;
        }

        h1 {
            color: #333;
            margin-bottom: 10px;
            font-size: 24px;
        }

        .event-info {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 10px;
            margin: 20px 0;
            text-align: left;
        }

        .event-info p {
            margin: 8px 0;
            color: #555;
            font-size: 14px;
        }

        .event-info strong {
            color: #333;
            display: inline-block;
            min-width: 100px;
        }

        .qr-container {
            background: white;
            padding: 30px;
            border-radius: 15px;
            margin: 30px 0;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }

        .qr-code {
            width: 280px;
            height: 280px;
            margin: 0 auto;
            display: block;
        }

        .ticket-id {
            margin-top: 15px;
            color: #666;
            font-size: 12px;
            font-family: 'Courier New', monospace;
        }

        .error-message {
            background: #fff3cd;
            border: 1px solid #ffc107;
            color: #856404;
            padding: 20px;
            border-radius: 10px;
            margin: 20px 0;
        }

        .error-message h2 {
            font-size: 18px;
            margin-bottom: 10px;
        }

        .btn {
            display: inline-block;
            padding: 12px 30px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            text-decoration: none;
            border-radius: 25px;
            font-weight: bold;
            transition: transform 0.2s, box-shadow 0.2s;
            border: none;
            cursor: pointer;
            font-size: 16px;
            margin-top: 20px;
        }

        .btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);
        }

        @media (max-width: 600px) {
            .container {
                padding: 20px;
            }

            .qr-code {
                width: 220px;
                height: 220px;
            }

            h1 {
                font-size: 20px;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>入場用QRコード</h1>

        <div class="event-info">
            <p><strong>イベント名:</strong> <%= event.getEventName() %></p>
            <p><strong>開催日:</strong> <%= event.getEventDate() %></p>
            <p><strong>会場:</strong> <%= event.getLocation() %></p>
        </div>

        <% if (ticket != null) { %>
            <div class="qr-container">
                <img src="data:image/png;base64,<%= ticket.getQrCodeData() %>"
                     alt="QRコード"
                     class="qr-code">
                <p class="ticket-id">Ticket ID: <%= ticket.getTicketId() %></p>
            </div>
            <p style="color: #666; font-size: 14px;">
                このQRコードを入場時にスタッフにご提示ください。
            </p>
        <% } else { %>
            <div class="error-message">
                <h2>チケット未発行</h2>
                <p>このイベントのチケットはまだ発行されていません。</p>
                <p>主催者がチケットを発行するまでお待ちください。</p>
            </div>
        <% } %>

        <form action="EntryEventDetail.action" method="post" style="margin-top: 30px;">
            <input type="hidden" name="eventId" value="<%= event.getEventId() %>">
            <button type="submit" class="btn">イベント詳細に戻る</button>
        </form>
    </div>
</body>
</html>