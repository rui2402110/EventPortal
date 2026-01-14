package eventportal.host;

import java.util.List;

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
 * 発行済みチケット一覧表示アクション
 */
public class TicketListAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || user.getUser_type() != 2) {
            res.sendRedirect(req.getContextPath() + "/eventportal/auth/HostLogin.action");
            return;
        }

        String eventId = req.getParameter("eventId");
        if (eventId == null || eventId.isEmpty()) {
            req.setAttribute("error", "イベントIDが指定されていません");
            res.sendRedirect(req.getContextPath() + "/eventportal/host/HostMain.action");
            return;
        }

        EventDao eventDao = new EventDao();
        TicketDao ticketDao = new TicketDao();

        Event event = eventDao.get(eventId);
        if (event == null) {
            req.setAttribute("error", "イベントが見つかりません");
            res.sendRedirect(req.getContextPath() + "/eventportal/host/HostMain.action");
            return;
        }

        if (!event.getUserId().equals(user.getUser_id())) {
            req.setAttribute("error", "このイベントにアクセスする権限がありません");
            res.sendRedirect(req.getContextPath() + "/eventportal/host/HostMain.action");
            return;
        }

        List<Ticket> tickets = ticketDao.getByEventId(eventId);
        int totalCount = ticketDao.getTotalTicketCount(eventId);
        int validCount = ticketDao.getValidTicketCount(eventId);
        int admittedCount = ticketDao.getAdmittedCount(eventId);

        String successMessage = (String) session.getAttribute("successMessage");
        if (successMessage != null) {
            req.setAttribute("successMessage", successMessage);
            session.removeAttribute("successMessage");
        }

        req.setAttribute("event", event);
        req.setAttribute("tickets", tickets);
        req.setAttribute("totalCount", totalCount);
        req.setAttribute("validCount", validCount);
        req.setAttribute("admittedCount", admittedCount);

        req.getRequestDispatcher("/eventportal/host/host_ticket_list.jsp").forward(req, res);
    }
}