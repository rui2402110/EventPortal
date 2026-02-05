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
 * 参加イベント一覧表示アクション
 */
public class EntryEventManageAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        System.out.println("eventportal.entrymenu.EntryEventManageAction");

        // セッションからユーザー情報を取得
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        // ログインチェック
        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/eventportal/auth/EntryLogin.action");
            return;
        }

        System.out.println(user.getUser_id());

        try {
            // 全イベント取得
            EventDao eventDao = new EventDao();
            List<Event> list = eventDao.filter(null);

            // 各イベントについて、ユーザーが参加済みかチェック
            TicketDao ticketDao = new TicketDao();
            for (Event event : list) {
                // このイベントのチケットを持っているかチェック
                boolean hasTicket = ticketDao.getByEventAndUser(event.getEventId(), user.getUser_id()) != null;
                event.setHasTicket(hasTicket);
            }

            System.out.println(list);

            // リクエストスコープに設定
            req.setAttribute("list", list);

            // JSPにフォワード
            req.getRequestDispatcher("/eventportal/entry/entry_event_manage.jsp").forward(req, res);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", "エラーが発生しました: " + e.getMessage());
            req.getRequestDispatcher("/error.jsp").forward(req, res);
        }
    }
}