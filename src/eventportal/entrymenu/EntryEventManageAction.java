package eventportal.entrymenu;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Event;
import bean.User;
import dao.EventDao;
import dao.TicketDao;
import tool.Action;

/**
 * 参加イベント一覧表示アクション（完全版）
 */
public class EntryEventManageAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        System.out.println("=== イベント一覧表示処理開始 ===");

        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        if (user == null) {
            System.out.println("未ログイン：ログイン画面にリダイレクト");
            res.sendRedirect(req.getContextPath() + "/eventportal/auth/EntryLogin.action");
            return;
        }

        System.out.println("ユーザーID: " + user.getUser_id());
        System.out.println("ユーザー名: " + user.getUser_name());

        try {
            EventDao eventDao = new EventDao();
            TicketDao ticketDao = new TicketDao();

            // 全イベントを取得
            List<Event> list = eventDao.filter(null);
            System.out.println("取得したイベント数: " + list.size());

            // 各イベントについてチケット所持状態をチェック
            for (Event event : list) {
                boolean hasTicket = ticketDao.getByEventAndUser(event.getEventId(), user.getUser_id()) != null;
                event.setHasTicket(hasTicket);

                System.out.println("  イベント: " + event.getEventId() +
                                 " (" + event.getEventName() + ") - " +
                                 "チケット所持: " + (hasTicket ? "あり ✓" : "なし ✗"));
            }

            // リクエストスコープに設定
            req.setAttribute("list", list);

            // 成功メッセージがあれば表示
            String successMessage = (String) session.getAttribute("successMessage");
            if (successMessage != null) {
                req.setAttribute("successMessage", successMessage);
                session.removeAttribute("successMessage");
                System.out.println("成功メッセージ: " + successMessage);
            }

            // エラーメッセージがあれば表示
            String errorMessage = (String) session.getAttribute("errorMessage");
            if (errorMessage != null) {
                req.setAttribute("errorMessage", errorMessage);
                session.removeAttribute("errorMessage");
                System.out.println("エラーメッセージ: " + errorMessage);
            }

            System.out.println("=== イベント一覧表示処理完了 ===");

            // JSPにフォワード
            req.getRequestDispatcher("/eventportal/entry/entry_event_manage.jsp").forward(req, res);

        } catch (Exception e) {
            System.err.println("イベント一覧表示エラー: " + e.getMessage());
            e.printStackTrace();
            req.setAttribute("errorMessage", "エラーが発生しました: " + e.getMessage());
            req.getRequestDispatcher("/error.jsp").forward(req, res);
        }
    }
}