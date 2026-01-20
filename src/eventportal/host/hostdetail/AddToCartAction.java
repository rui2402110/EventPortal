package eventportal.host.hostdetail;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.User;
import tool.Action;

/**
 * カート追加アクション
 */
public class AddToCartAction extends Action {
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
        String itemId = req.getParameter("itemId");
        String quantityStr = req.getParameter("quantity");

        if (itemId == null || quantityStr == null) {
            res.sendRedirect(req.getContextPath() +
                "/eventportal/entry/order/EntryProductList.action?eventId=" + eventId + "&ticketId=" + ticketId);
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityStr);

            if (quantity <= 0) {
                session.setAttribute("error", "数量は1以上を指定してください");
                res.sendRedirect(req.getContextPath() +
                    "/eventportal/entry/order/EntryProductList.action?eventId=" + eventId + "&ticketId=" + ticketId);
                return;
            }

            // カートを取得（なければ作成）
            @SuppressWarnings("unchecked")
            Map<String, Integer> cart = (Map<String, Integer>) session.getAttribute("cart");

            if (cart == null) {
                cart = new HashMap<>();
            }

            // カートに商品を追加（既にある場合は数量を加算）
            if (cart.containsKey(itemId)) {
                cart.put(itemId, cart.get(itemId) + quantity);
            } else {
                cart.put(itemId, quantity);
            }

            session.setAttribute("cart", cart);
            session.setAttribute("successMessage", "カートに追加しました");

            res.sendRedirect(req.getContextPath() +
                "/eventportal/entry/order/EntryProductList.action?eventId=" + eventId + "&ticketId=" + ticketId);

        } catch (NumberFormatException e) {
            session.setAttribute("error", "数量が不正です");
            res.sendRedirect(req.getContextPath() +
                "/eventportal/entry/order/EntryProductList.action?eventId=" + eventId + "&ticketId=" + ticketId);
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "エラーが発生しました: " + e.getMessage());
            res.sendRedirect(req.getContextPath() +
                "/eventportal/entry/order/EntryProductList.action?eventId=" + eventId + "&ticketId=" + ticketId);
        }
    }
}