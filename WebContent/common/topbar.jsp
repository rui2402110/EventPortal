<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
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

    @media (max-width: 768px) {
        .top-bar {
            flex-direction: column;
            gap: 10px;
        }

        .top-bar-right {
            flex-wrap: wrap;
            justify-content: center;
        }
    }
</style>

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