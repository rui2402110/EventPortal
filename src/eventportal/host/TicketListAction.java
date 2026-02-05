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
        System.out.println("=== マイチケット一覧表示処理開始 ===");

        // セッションからユーザー情報を取得
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        // ログインチェック
        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/eventportal/auth/EntryLogin.action");
            return;
        }

        try {
            TicketDao ticketDao = new TicketDao();

            // このユーザーの全チケットを取得
            List<Ticket> tickets = ticketDao.getByUserId(user.getUser_id());

            System.out.println("チケット数: " + tickets.size());

            // リクエストスコープに設定
            req.setAttribute("tickets", tickets);

            // エラーメッセージがあれば表示
            String errorMessage = (String) session.getAttribute("errorMessage");
            if (errorMessage != null) {
                req.setAttribute("errorMessage", errorMessage);
                session.removeAttribute("errorMessage");
            }

            // JSPにフォワード
            req.getRequestDispatcher("/eventportal/entry/my_tickets.jsp").forward(req, res);

        } catch (Exception e) {
            System.err.println("マイチケット一覧表示エラー: " + e.getMessage());
            e.printStackTrace();
            req.setAttribute("errorMessage", "エラーが発生しました: " + e.getMessage());
            req.getRequestDispatcher("/error.jsp").forward(req, res);
        }
    }
}