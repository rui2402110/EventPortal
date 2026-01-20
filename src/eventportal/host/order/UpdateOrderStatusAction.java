package eventportal.host.order;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.User;
import dao.OrderDao;
import tool.Action;

/**
 * 注文ステータス更新アクション
 */
public class UpdateOrderStatusAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || user.getUser_type() != 2) {
            res.sendRedirect(req.getContextPath() + "/eventportal/auth/HostLogin.action");
            return;
        }

        String orderId = req.getParameter("orderId");
        String eventId = req.getParameter("eventId");
        String statusStr = req.getParameter("status");

        if (orderId == null || eventId == null || statusStr == null) {
            session.setAttribute("error", "必要な情報が不足しています");
            res.sendRedirect(req.getContextPath() + "/eventportal/host/HostMenu.action");
            return;
        }

        try {
            int newStatus = Integer.parseInt(statusStr);
            OrderDao orderDao = new OrderDao();

            boolean updated = orderDao.updateStatus(orderId, newStatus);

            if (updated) {
                session.setAttribute("successMessage", "注文ステータスを更新しました");
            } else {
                session.setAttribute("error", "注文ステータスの更新に失敗しました");
            }

            res.sendRedirect(req.getContextPath() +
                "/eventportal/host/order/HostOrderList.action?eventId=" + eventId);

        } catch (NumberFormatException e) {
            session.setAttribute("error", "ステータスの値が不正です");
            res.sendRedirect(req.getContextPath() +
                "/eventportal/host/order/HostOrderList.action?eventId=" + eventId);
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "エラーが発生しました: " + e.getMessage());
            res.sendRedirect(req.getContextPath() +
                "/eventportal/host/order/HostOrderList.action?eventId=" + eventId);
        }
    }
}