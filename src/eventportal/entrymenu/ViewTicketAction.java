package eventportal.entrymenu;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Event;
import bean.Ticket;
import bean.User;
import dao.EventDao;
import dao.TicketDao;
import tool.Action;

public class ViewTicketAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        // セッションからユーザー情報を取得
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/eventportal/auth/EntryLogin.action");
            return;
        }

        // パラメータからチケットIDを取得
        String ticketId = req.getParameter("ticketId");

        if (ticketId == null || ticketId.isEmpty()) {
            req.setAttribute("error", "チケットIDが指定されていません");
            req.getRequestDispatcher("/eventportal/entry/error.jsp").forward(req, res);
            return;
        }

        // DAOインスタンス作成
        TicketDao ticketDao = new TicketDao();
        EventDao eventDao = new EventDao();

        // チケット情報を取得
        Ticket ticket = ticketDao.get(ticketId);

        if (ticket == null) {
            req.setAttribute("error", "チケットが見つかりません");
            req.getRequestDispatcher("/eventportal/entry/error.jsp").forward(req, res);
            return;
        }

        // チケットの所有者確認
        if (!ticket.getUserId().equals(user.getUser_id())) {
            req.setAttribute("error", "このチケットにアクセスする権限がありません");
            req.getRequestDispatcher("/eventportal/entry/error.jsp").forward(req, res);
            return;
        }

        // イベント情報を取得
        Event event = eventDao.get(ticket.getEventId());

        // JSPにデータを渡す
        req.setAttribute("ticket", ticket);
        req.setAttribute("event", event);
        req.setAttribute("user", user);

        // JSPへフォワード
        req.getRequestDispatcher("/eventportal/entry/entry_ticket.jsp").forward(req, res);
    }
}