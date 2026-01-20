package eventportal.host.order;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Product;
import bean.Ticket;
import bean.User;
import dao.ProductDao;
import dao.TicketDao;
import tool.Action;

/**
 * 参加者用商品一覧表示アクション
 * QRコード入場済みのユーザーのみアクセス可能
 */
public class EntryProductListAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || user.getUser_type() != 1) {
            res.sendRedirect(req.getContextPath() + "/eventportal/auth/EntryLogin.action");
            return;
        }

        String eventId = req.getParameter("eventId");
        String ticketId = req.getParameter("ticketId");

        if (eventId == null || eventId.isEmpty()) {
            req.setAttribute("error", "イベントIDが指定されていません");
            res.sendRedirect(req.getContextPath() + "/eventportal/entrymenu/MyTickets.action");
            return;
        }

        try {
            TicketDao ticketDao = new TicketDao();
            ProductDao productDao = new ProductDao();

            // チケットの確認（入場済みかチェック）
            Ticket ticket = null;
            if (ticketId != null && !ticketId.isEmpty()) {
                ticket = ticketDao.get(ticketId);
            } else {
                // チケットIDが指定されていない場合は、ユーザーとイベントから取得
                ticket = ticketDao.getByEventAndUser(eventId, user.getUser_id());
            }

            if (ticket == null) {
                req.setAttribute("error", "チケットが見つかりません");
                res.sendRedirect(req.getContextPath() + "/eventportal/entrymenu/MyTickets.action");
                return;
            }

            // チケットが使用済み（入場済み）かチェック
            if (ticket.getStatus() != 2) {
                req.setAttribute("error", "このイベントにまだ入場していません。QRコードで入場してください。");
                req.getRequestDispatcher("/eventportal/entry/order/not_admitted.jsp").forward(req, res);
                return;
            }

            // 商品一覧を取得
            List<Product> products = productDao.getByEventId(eventId);

            req.setAttribute("products", products);
            req.setAttribute("ticket", ticket);
            req.setAttribute("eventId", eventId);
            req.getRequestDispatcher("/eventportal/entry/order/entry_product_list.jsp").forward(req, res);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "エラーが発生しました: " + e.getMessage());
            req.getRequestDispatcher("/eventportal/common/error.jsp").forward(req, res);
        }
    }
}