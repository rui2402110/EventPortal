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
        System.out.println("=== イベント一覧表示処理開始 ===");

        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        if (user == null) {
            System.out.println("未ログイン：ログイン画面にリダイレクト");
            res.sendRedirect(req.getContextPath() + "/eventportal/auth/EntryLogin.action");
            return;
        }

        System.out.println("ユーザーID: " + user.getUser_id());

        try {
            EventDao eventDao = new EventDao();
            List<Event> list = eventDao.filter(null);

            System.out.println("取得したイベント数: " + list.size());

            TicketDao ticketDao = new TicketDao();
            for (Event event : list) {
                boolean hasTicket = ticketDao.getByEventAndUser(event.getEventId(), user.getUser_id()) != null;
                event.setHasTicket(hasTicket);

                System.out.println("イベント: " + event.getEventId() +
                                 " (" + event.getEventName() + ") - " +
                                 "チケット所持: " + (hasTicket ? "あり" : "なし"));
            }

            req.setAttribute("list", list);

            String successMessage = (String) session.getAttribute("successMessage");
            if (successMessage != null) {
                req.setAttribute("successMessage", successMessage);
                session.removeAttribute("successMessage");
            }

            req.getRequestDispatcher("/eventportal/entry/entry_event_manage.jsp").forward(req, res);

        } catch (Exception e) {
            System.err.println("イベント一覧表示エラー: " + e.getMessage());
            e.printStackTrace();
            req.setAttribute("errorMessage", "エラーが発生しました: " + e.getMessage());
            req.getRequestDispatcher("/error.jsp").forward(req, res);
        }
    }
}