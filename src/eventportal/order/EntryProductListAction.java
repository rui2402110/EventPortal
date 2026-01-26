package eventportal.order;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Event;
import bean.Product;
import bean.Ticket;
import bean.User;
import dao.EventDao;
import dao.ProductDao;
import dao.TicketDao;
import tool.Action;

/**
 * 商品一覧表示アクション
 */
public class EntryProductListAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/eventportal/auth/EntryLogin.action");
            return;
        }

        String eventId = req.getParameter("eventId");

        if (eventId == null || eventId.isEmpty()) {
            req.setAttribute("error", "イベントIDが指定されていません");
            res.sendRedirect(req.getContextPath() + "/eventportal/entrymenu/EntryEventManage.action");
            return;
        }

        try {
            EventDao eventDao = new EventDao();
            ProductDao productDao = new ProductDao();
            TicketDao ticketDao = new TicketDao();

            // イベント情報を取得
            Event event = eventDao.get(eventId);
            if (event == null) {
                req.setAttribute("error", "イベントが見つかりません");
                res.sendRedirect(req.getContextPath() + "/eventportal/entrymenu/EntryEventManage.action");
                return;
            }

            // チケットを確認（入場済みかチェック）
            Ticket ticket = ticketDao.getByEventAndUser(eventId, user.getUser_id());
            if (ticket == null) {
                req.setAttribute("errorMessage", "このイベントのチケットがありません");
                req.getRequestDispatcher("/eventportal/entry/order/not_admitted.jsp").forward(req, res);
                return;
            }

            // チケットが使用済み（入場済み）でない場合はエラー
            if (ticket.getStatus() != 2) {
                req.setAttribute("errorMessage", "まだ入場していません。QRコードで入場してから購入してください。");
                req.getRequestDispatcher("/eventportal/entry/order/not_admitted.jsp").forward(req, res);
                return;
            }

            // 商品一覧を取得
            List<Product> products = productDao.getByEventId(eventId);

            // リクエストに設定
            req.setAttribute("event", event);
            req.setAttribute("ticket", ticket);
            req.setAttribute("products", products);
            req.setAttribute("eventId", eventId);

            // JSPへフォワード
            req.getRequestDispatcher("/eventportal/entry/order/entry_product_list.jsp").forward(req, res);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "エラーが発生しました: " + e.getMessage());
            res.sendRedirect(req.getContextPath() + "/eventportal/entrymenu/EntryEventManage.action");
        }
    }
}