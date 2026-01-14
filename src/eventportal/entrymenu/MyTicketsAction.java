package eventportal.entrymenu;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Ticket;
import bean.User;
import dao.TicketDao;
import tool.Action;

/**
 * マイチケット一覧表示アクション
 */
public class MyTicketsAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/eventportal/auth/EntryLogin.action");
            return;
        }

        TicketDao ticketDao = new TicketDao();
        List<Ticket> tickets = ticketDao.getByUserId(user.getUser_id());

        System.out.println("マイチケット取得: ユーザーID=" + user.getUser_id() + ", チケット数=" + tickets.size());

        req.setAttribute("tickets", tickets);
        req.getRequestDispatcher("/eventportal/qr/my_tickets.jsp").forward(req, res);
    }
}