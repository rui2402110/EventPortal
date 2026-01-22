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

/**
 * チケット詳細表示アクション
 */
public class ViewTicketAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/eventportal/auth/EntryLogin.action");
            return;
        }

        String ticketId = req.getParameter("ticketId");
        if (ticketId == null || ticketId.isEmpty()) {
            req.setAttribute("error", "チケットIDが指定されていません");
            res.sendRedirect(req.getContextPath() + "/eventportal/entrymenu/MyTickets.action");
            return;
        }

        TicketDao ticketDao = new TicketDao();
        EventDao eventDao = new EventDao();

        Ticket ticket = ticketDao.get(ticketId);

        if (ticket == null) {
            req.setAttribute("error", "チケットが見つかりません");
            res.sendRedirect(req.getContextPath() + "/eventportal/entrymenu/MyTickets.action");
            return;
        }

        if (!ticket.getUserId().equals(user.getUser_id())) {
            req.setAttribute("error", "このチケットにアクセスする権限がありません");
            res.sendRedirect(req.getContextPath() + "/eventportal/entrymenu/MyTickets.action");
            return;
        }

        // イベント情報を取得
        Event event = eventDao.get(ticket.getEventId());
        ticket.setEvent(event);

        System.out.println("チケット詳細表示: チケットID=" + ticketId +
                         ", QRデータ=" + (ticket.getQrImageData() != null ? "あり" : "なし"));

        // リクエストに設定
        req.setAttribute("ticket", ticket);
        req.setAttribute("event", event);  // イベント情報を追加
        req.setAttribute("user", user);    // ユーザー情報を追加

        req.getRequestDispatcher("/eventportal/qr/view_ticket.jsp").forward(req, res);
    }
}