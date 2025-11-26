package eventportal.entrymenu;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Ticket;
import bean.User;
import dao.TicketDao;
import tool.Action;

public class MyTicketsAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        // セッションからユーザー情報を取得
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/eventportal/auth/EntryLogin.action");
            return;
        }

        // DAOインスタンス作成
        TicketDao ticketDao = new TicketDao();

        // ユーザーの全チケットを取得
        List<Ticket> tickets = ticketDao.getByUserId(user.getUser_id());

        // JSPにデータを渡す
        req.setAttribute("tickets", tickets);

        // JSPへフォワード
        req.getRequestDispatcher("/eventportal/entry/my_tickets.jsp").forward(req, res);
    }
}