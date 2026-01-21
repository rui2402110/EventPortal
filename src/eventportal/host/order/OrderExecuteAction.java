package eventportal.host.order;

import java.time.LocalDateTime;
import java.util.List;  // ← この行を追加
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Order;
import bean.OrderItem;
import bean.Product;
import bean.User;
import dao.OrderDao;
import dao.OrderItemDao;
import dao.ProductDao;
import tool.Action;

/**
 * 注文確定アクション
 */
public class OrderExecuteAction extends Action {
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

        if (eventId == null || ticketId == null) {
            req.setAttribute("error", "必要な情報が不足しています");
            res.sendRedirect(req.getContextPath() + "/eventportal/entrymenu/MyTickets.action");
            return;
        }

        try {
            // カートの取得（セッションから）
            @SuppressWarnings("unchecked")
            Map<String, Integer> cart = (Map<String, Integer>) session.getAttribute("cart");

            if (cart == null || cart.isEmpty()) {
                req.setAttribute("error", "カートが空です");
                res.sendRedirect(req.getContextPath() +
                    "/eventportal/entry/order/EntryProductList.action?eventId=" + eventId + "&ticketId=" + ticketId);
                return;
            }

            OrderDao orderDao = new OrderDao();
            OrderItemDao orderItemDao = new OrderItemDao();
            ProductDao productDao = new ProductDao();

            // 合計金額を計算
            int totalAmount = 0;
            for (Map.Entry<String, Integer> entry : cart.entrySet()) {
                String itemId = entry.getKey();
                int quantity = entry.getValue();

                Product product = productDao.get(itemId);
                if (product != null) {
                    // イベント商品から価格を取得
                    List<Product> eventProducts = productDao.getByEventId(eventId);
                    for (Product ep : eventProducts) {
                        if (ep.getItemId().equals(itemId)) {
                            totalAmount += ep.getPrice() * quantity;
                            break;
                        }
                    }
                }
            }

            // 注文を作成
            String orderId = orderDao.generateOrderId();
            Order order = new Order();
            order.setOrderId(orderId);
            order.setUserId(user.getUser_id());
            order.setEventId(eventId);
            order.setTicketId(ticketId);
            order.setOrderDate(LocalDateTime.now());
            order.setTotalAmount(totalAmount);
            order.setStatus(1); // 注文済み

            boolean orderCreated = orderDao.create(order);

            if (orderCreated) {
                // 注文明細を作成
                for (Map.Entry<String, Integer> entry : cart.entrySet()) {
                    String itemId = entry.getKey();
                    int quantity = entry.getValue();

                    // イベント商品から価格を取得
                    List<Product> eventProducts = productDao.getByEventId(eventId);
                    for (Product ep : eventProducts) {
                        if (ep.getItemId().equals(itemId)) {
                            String orderItemId = orderItemDao.generateOrderItemId();
                            OrderItem item = new OrderItem();
                            item.setOrderItemId(orderItemId);
                            item.setOrderId(orderId);
                            item.setItemId(itemId);
                            item.setQuantity(quantity);
                            item.setUnitPrice(ep.getPrice());

                            orderItemDao.create(item);

                            // 在庫を減らす
                            productDao.decreaseStock(eventId, itemId, quantity);
                            break;
                        }
                    }
                }

                // カートをクリア
                session.removeAttribute("cart");

                System.out.println("注文作成成功: " + orderId);
                session.setAttribute("successMessage", "注文が完了しました（注文ID: " + orderId + "）");
                res.sendRedirect(req.getContextPath() +
                    "/eventportal/entry/order/OrderComplete.action?orderId=" + orderId);
            } else {
                req.setAttribute("error", "注文の作成に失敗しました");
                res.sendRedirect(req.getContextPath() +
                    "/eventportal/entry/order/EntryProductList.action?eventId=" + eventId + "&ticketId=" + ticketId);
            }

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "エラーが発生しました: " + e.getMessage());
            req.getRequestDispatcher("/error.jsp").forward(req, res);  // パスも修正
        }
    }
}