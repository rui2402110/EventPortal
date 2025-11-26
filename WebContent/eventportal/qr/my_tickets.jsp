<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>マイチケット | イベントポータル</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/common/style.css">
  <style>
    .ticket-list-container {
      max-width: 1000px;
      margin: 20px auto;
      padding: 0 20px;
    }
    .page-header {
      text-align: center;
      margin-bottom: 30px;
    }
    .filter-tabs {
      display: flex;
      justify-content: center;
      gap: 10px;
      margin-bottom: 30px;
      flex-wrap: wrap;
    }
    .filter-tab {
      padding: 10px 20px;
      border: 2px solid #ddd;
      background: white;
      border-radius: 20px;
      cursor: pointer;
      font-weight: bold;
    }
    .filter-tab.active {
      background: #667eea;
      color: white;
      border-color: #667eea;
    }
    .ticket-card {
      background: white;
      border-radius: 12px;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
      margin-bottom: 20px;
      overflow: hidden;
    }
    .ticket-card-header {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
      padding: 15px 20px;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
    .ticket-card-body {
      padding: 20px;
    }
    .ticket-info {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
      gap: 15px;
      margin-bottom: 15px;
    }
    .info-item {
      display: flex;
      align-items: start;
    }
    .info-icon {
      margin-right: 10px;
      font-size: 18px;
    }
    .info-label {
      font-size: 12px;
      color: #999;
    }
    .info-value {
      font-size: 14px;
      color: #333;
      font-weight: 500;
    }
    .ticket-actions {
      display: flex;
      gap: 10px;
      padding-top: 15px;
      border-top: 1px solid #eee;
    }
    .action-btn {
      flex: 1;
      padding: 10px;
      border: none;
      border-radius: 6px;
      cursor: pointer;
      font-size: 14px;
      font-weight: bold;
      text-align: center;
      text-decoration: none;
      display: block;
    }
    .btn-view-qr {
      background: #667eea;
      color: white;
    }
    .btn-view-detail {
      background: #f5f5f5;
      color: #333;
    }
    .empty-state {
      text-align: center;
      padding: 60px 20px;
      color: #999;
    }
    .empty-state-icon {
      font-size: 64px;
      margin-bottom: 20px;
    }
  </style>
</head>
<body>
  <div class="container">
    <div class="header">イベントポータル - マイチケット</div>
    <div class="ticket-list-container">
      <div class="page-header">
        <h2>マイチケット</h2>
        <p>参加予定のイベントチケット一覧</p>
      </div>
      <div class="filter-tabs">
        <button class="filter-tab active" onclick="filterTickets('all', this)">すべて</button>
        <button class="filter-tab" onclick="filterTickets('valid', this)">有効なチケット</button>
        <button class="filter-tab" onclick="filterTickets('used', this)">使用済み</button>
      </div>
      <div id="ticketList">
        <c:choose>
          <c:when test="${not empty tickets}">
            <c:forEach var="ticket" items="${tickets}">
              <div class="ticket-card" data-status="${ticket.status}">
                <div class="ticket-card-header">
                  <div>
                    <h3 style="margin: 0;">${ticket.event.eventName}</h3>
                    <span style="font-size: 11px; background: rgba(255,255,255,0.2); padding: 2px 8px; border-radius: 4px;">ID: ${ticket.ticketId}</span>
                  </div>
                  <c:choose>
                    <c:when test="${ticket.status == 1}">
                      <span style="padding: 4px 12px; border-radius: 12px; font-size: 12px; background: rgba(255,255,255,0.3);">有効</span>
                    </c:when>
                    <c:when test="${ticket.status == 2}">
                      <span style="padding: 4px 12px; border-radius: 12px; font-size: 12px; background: rgba(255,235,238,0.3); color: #ffcdd2;">使用済み</span>
                    </c:when>
                  </c:choose>
                </div>
                <div class="ticket-card-body">
                  <div class="ticket-info">
                    <div class="info-item">
                      <span class="info-icon">📅</span>
                      <div>
                        <div class="info-label">開催日時</div>
                        <div class="info-value">${ticket.event.holdingDate} ${ticket.event.holdingTime}</div>
                      </div>
                    </div>
                    <div class="info-item">
                      <span class="info-icon">📍</span>
                      <div>
                        <div class="info-label">会場</div>
                        <div class="info-value">${ticket.event.address}</div>
                      </div>
                    </div>
                  </div>
                  <div class="ticket-actions">
                    <c:choose>
                      <c:when test="${ticket.status == 1}">
                        <a href="${pageContext.request.contextPath}/eventportal/entrymenu/ViewTicket.action?ticketId=${ticket.ticketId}" class="action-btn btn-view-qr">QRコードを表示</a>
                      </c:when>
                      <c:otherwise>
                        <button class="action-btn" style="background: #e0e0e0; color: #999; cursor: not-allowed;" disabled>使用済み</button>
                      </c:otherwise>
                    </c:choose>
                  </div>
                </div>
              </div>
            </c:forEach>
          </c:when>
          <c:otherwise>
            <div class="empty-state">
              <div class="empty-state-icon">🎫</div>
              <h3>チケットがありません</h3>
              <p>まだイベントに参加していません。</p>
            </div>
          </c:otherwise>
        </c:choose>
      </div>
    </div>
    <div style="text-align: center; margin: 30px 0;">
      <button class="btn" style="background: #757575; color: white; padding: 12px 24px; border: none; border-radius: 6px; cursor: pointer;"
              onclick="location.href='${pageContext.request.contextPath}/eventportal/entrymenu/EntryMenu.action'">メニューに戻る</button>
    </div>
    <div class="footer">@2025.................................................</div>
  </div>
  <script>
    function filterTickets(filter, element) {
      document.querySelectorAll('.filter-tab').forEach(tab => tab.classList.remove('active'));
      element.classList.add('active');

      const tickets = document.querySelectorAll('.ticket-card');
      tickets.forEach(ticket => {
        const status = ticket.getAttribute('data-status');
        let show = false;

        if (filter === 'all') {
          show = true;
        } else if (filter === 'valid') {
          show = (status == '1');
        } else if (filter === 'used') {
          show = (status == '2');
        }

        ticket.style.display = show ? 'block' : 'none';
      });
    }
  </script>
</body>
</html>