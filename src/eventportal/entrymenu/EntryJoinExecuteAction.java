package eventportal.entrymenu;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.EntryEventDao;

public class EntryJoinExecuteAction extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // セッション取得
        HttpSession session = request.getSession();

        // パラメータ取得
        String eventId = request.getParameter("eventId");

        // バリデーション
        if (eventId == null || eventId.isEmpty()) {
            request.setAttribute("errorMessage", "イベントIDが指定されていません");
            request.getRequestDispatcher("entry_join.jsp").forward(request, response);
            return;
        }

        // セッションからユーザーIDを確認
        String userId = (String) session.getAttribute("userId");
        if (userId == null || userId.isEmpty()) {
            request.setAttribute("errorMessage", "ログインしてください");
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            // Dao生成
            EntryEventDao dao = new EntryEventDao();

            // イベント参加登録
            int result = dao.insertEntry(session, eventId);

            if (result > 0) {
                // 登録成功
                request.setAttribute("successMessage", "イベントへの参加登録が完了しました");
                response.sendRedirect("entryComplete.jsp?eventId=" + eventId);
            } else {
                // 登録失敗
                request.setAttribute("errorMessage", "参加登録に失敗しました");
                request.getRequestDispatcher("entry_join.jsp").forward(request, response);
            }

        } catch (IllegalStateException e) {
            // セッション関連のエラー
            e.printStackTrace();
            request.setAttribute("errorMessage", "セッション情報が取得できません");
            response.sendRedirect("login.jsp");

        } catch (Exception e) {
            // その他のエラー
            e.printStackTrace();
            request.setAttribute("errorMessage", "データベースエラーが発生しました");
            request.getRequestDispatcher("entry_join.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // GETリクエストは受け付けない
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "GETメソッドは許可されていません");
    }
}