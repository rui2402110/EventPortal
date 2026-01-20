package eventportal.host.order;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Event;
import bean.Order;
import bean.User;
import dao.EventDao;
import dao.OrderDao;
import tool.Action;

/**
 * 主催者用注文一覧表示アクション
 */
public class HostOrderListAction extends Action {
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
            res.sendRedirect(req.getContextPath() + "/eventportal/host/HostMenu.action");
            return;
        }

        try {
            EventDao eventDao = new EventDao();
            OrderDao orderDao = new OrderDao();

            Event event = eventDao.get(eventId);

            if (event == null) {
                req.setAttribute("error", "イベントが見つかりません");
                res.sendRedirect(req.getContextPath() + "/eventportal/host/HostMenu.action");
                return;
            }

            if (!event.getUserId().equals(user.getUser_id())) {
                req.setAttribute("error", "このイベントにアクセスする権限がありません");
                res.sendRedirect(req.getContextPath() + "/eventportal/host/HostMenu.action");
                return;
            }

            List<Order> orders = orderDao.getByEvent(eventId);
            int orderCount = orderDao.getOrderCount(eventId);
            int totalOrderAmount = orderDao.getTotalOrderAmount(eventId);

            String successMessage = (String) session.getAttribute("successMessage");
            if (successMessage != null) {
                req.setAttribute("successMessage", successMessage);
                session.removeAttribute("successMessage");
            }

            req.setAttribute("event", event);
            req.setAttribute("orders", orders);
            req.setAttribute("orderCount", orderCount);
            req.setAttribute("totalOrderAmount", totalOrderAmount);

            req.getRequestDispatcher("/eventportal/entry/order/host_order_list.jsp").forward(req, res);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "エラーが発生しました: " + e.getMessage());
            req.getRequestDispatcher("/eventportal/common/error.jsp").forward(req, res);
        }
    }
}