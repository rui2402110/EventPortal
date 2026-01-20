package eventportal.host.order;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Order;
import bean.OrderItem;
import bean.User;
import dao.OrderDao;
import dao.OrderItemDao;
import tool.Action;

/**
 * 注文完了表示アクション
 */
public class OrderCompleteAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || user.getUser_type() != 1) {
            res.sendRedirect(req.getContextPath() + "/eventportal/auth/EntryLogin.action");
            return;
        }

        String orderId = req.getParameter("orderId");

        if (orderId == null || orderId.isEmpty()) {
            req.setAttribute("error", "注文IDが指定されていません");
            res.sendRedirect(req.getContextPath() + "/eventportal/entrymenu/MyTickets.action");
            return;
        }

        try {
            OrderDao orderDao = new OrderDao();
            OrderItemDao orderItemDao = new OrderItemDao();

            Order order = orderDao.get(orderId);

            if (order == null) {
                req.setAttribute("error", "注文が見つかりません");
                res.sendRedirect(req.getContextPath() + "/eventportal/entrymenu/MyTickets.action");
                return;
            }

            // 注文者の確認
            if (!order.getUserId().equals(user.getUser_id())) {
                req.setAttribute("error", "この注文にアクセスする権限がありません");
                res.sendRedirect(req.getContextPath() + "/eventportal/entrymenu/MyTickets.action");
                return;
            }

            // 注文明細を取得
            List<OrderItem> items = orderItemDao.getByOrderId(orderId);
            order.setItems(items);

            String successMessage = (String) session.getAttribute("successMessage");
            if (successMessage != null) {
                req.setAttribute("successMessage", successMessage);
                session.removeAttribute("successMessage");
            }

            req.setAttribute("order", order);
            req.getRequestDispatcher("/eventportal/entry/order/order_complete.jsp").forward(req, res);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "エラーが発生しました: " + e.getMessage());
            req.getRequestDispatcher("/eventportal/common/error.jsp").forward(req, res);
        }
    }
}