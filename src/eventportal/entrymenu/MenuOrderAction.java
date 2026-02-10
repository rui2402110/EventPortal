package eventportal.entrymenu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Order;
import bean.User;
import dao.Dao;
import dao.OrderDao;
import tool.Action;

/**
 * メニュー注文処理アクション
 */
public class MenuOrderAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        System.out.println("\n========================================");
        System.out.println("=== メニュー注文処理開始 ===");
        System.out.println("========================================");

        HttpSession session = req.getSession(false);

        if (session == null) {
            System.out.println("✗ セッションがnull");
            res.sendRedirect(req.getContextPath() + "/eventportal/auth/EntryLogin.action");
            return;
        }

        User user = (User) session.getAttribute("user");

        if (user == null) {
            System.out.println("✗ ユーザーがnull（未ログイン）");
            res.sendRedirect(req.getContextPath() + "/eventportal/auth/EntryLogin.action");
            return;
        }

        System.out.println("✓ ユーザー認証OK: " + user.getUser_id());

        try {
            // パラメータ取得
            String menuId = req.getParameter("menuId");
            String eventId = req.getParameter("eventId");
            String quantityStr = req.getParameter("quantity");

            System.out.println("\n受信パラメータ:");
            System.out.println("  - menuId: " + menuId);
            System.out.println("  - eventId: " + eventId);
            System.out.println("  - quantity: " + quantityStr);

            // バリデーション
            if (menuId == null || menuId.isEmpty() ||
                eventId == null || eventId.isEmpty() ||
                quantityStr == null || quantityStr.isEmpty()) {

                System.out.println("✗ 必須パラメータが不足");
                session.setAttribute("errorMessage", "注文情報が不正です。");
                res.sendRedirect(req.getContextPath() + "/eventportal/entrymenu/EntryMenuView.action?eventId=" + eventId);
                return;
            }

            int quantity = Integer.parseInt(quantityStr);

            if (quantity < 1) {
                System.out.println("✗ 数量が不正");
                session.setAttribute("errorMessage", "数量は1以上を指定してください。");
                res.sendRedirect(req.getContextPath() + "/eventportal/entrymenu/EntryMenuView.action?eventId=" + eventId);
                return;
            }

            System.out.println("✓ バリデーションOK");

            // メニュー情報を取得して価格を計算
            Dao dao = new Dao();
            Connection connection = dao.getConnection();
            PreparedStatement statement = null;
            ResultSet resultSet = null;

            String menuName = "";
            int price = 0;
            int stockQuantity = 0;

            try {
                String sql = "SELECT menu_name, price, stock_quantity FROM MENUS WHERE menu_id = ?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, menuId);
                resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    menuName = resultSet.getString("menu_name");
                    price = resultSet.getInt("price");
                    stockQuantity = resultSet.getInt("stock_quantity");
                } else {
                    System.out.println("✗ メニューが見つかりません");
                    session.setAttribute("errorMessage", "メニューが見つかりません。");
                    res.sendRedirect(req.getContextPath() + "/eventportal/entrymenu/EntryMenuView.action?eventId=" + eventId);
                    return;
                }
            } finally {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            }

            System.out.println("メニュー名: " + menuName);
            System.out.println("単価: " + price);
            System.out.println("現在の在庫: " + stockQuantity);

            // 在庫確認
            if (stockQuantity < quantity) {
                System.out.println("✗ 在庫不足");
                session.setAttribute("errorMessage", "在庫が不足しています。（在庫: " + stockQuantity + "個）");
                res.sendRedirect(req.getContextPath() + "/eventportal/entrymenu/EntryMenuView.action?eventId=" + eventId);
                return;
            }

            // 注文ID生成
            String orderId = generateOrderId();
            System.out.println("✓ 注文ID生成: " + orderId);

            // 合計金額計算
            int totalPrice = price * quantity;

            // Orderオブジェクト作成
            Order order = new Order();
            order.setOrderId(orderId);
            order.setMenuId(menuId);
            order.setUserId(user.getUser_id());
            order.setEventId(eventId);
            order.setQuantity(quantity);
            order.setTotalPrice(totalPrice);

            System.out.println("\n✓ Orderオブジェクト作成完了");
            System.out.println("  - 合計金額: " + totalPrice + "円");

            // 注文登録（在庫減少処理も含む）
            OrderDao orderDao = new OrderDao();
            boolean success = orderDao.createOrder(order);

            if (success) {
                System.out.println("✓✓✓ 注文成功！ ✓✓✓");
                System.out.println("========================================\n");

                session.setAttribute("successMessage",
                    "注文を受け付けました！\n" +
                    "メニュー: " + menuName + "\n" +
                    "数量: " + quantity + "個\n" +
                    "合計: ¥" + totalPrice);
                res.sendRedirect(req.getContextPath() + "/eventportal/entrymenu/EntryMenuView.action?eventId=" + eventId);
            } else {
                System.out.println("✗ 注文失敗");
                session.setAttribute("errorMessage", "注文に失敗しました。もう一度お試しください。");
                res.sendRedirect(req.getContextPath() + "/eventportal/entrymenu/EntryMenuView.action?eventId=" + eventId);
            }

        } catch (NumberFormatException e) {
            System.err.println("✗ 数値変換エラー: " + e.getMessage());
            session.setAttribute("errorMessage", "数量には数値を入力してください。");
            res.sendRedirect(req.getContextPath() + "/eventportal/entrymenu/EntryMenuView.action");
        } catch (Exception e) {
            System.err.println("✗✗✗ 注文処理エラー ✗✗✗");
            System.err.println("エラークラス: " + e.getClass().getName());
            System.err.println("エラーメッセージ: " + e.getMessage());
            e.printStackTrace();
            System.err.println("========================================\n");

            session.setAttribute("errorMessage", "エラーが発生しました: " + e.getMessage());
            res.sendRedirect(req.getContextPath() + "/eventportal/entrymenu/EntryMenuView.action");
        }
    }

    /**
     * 注文ID自動生成
     */
    private String generateOrderId() throws Exception {
        Dao dao = new Dao();
        Connection connection = dao.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String sql = "SELECT order_id FROM ORDERS ORDER BY order_id DESC LIMIT 1";
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();

            int nextNumber = 1;

            if (resultSet.next()) {
                String lastOrderId = resultSet.getString("order_id");
                String numberPart = lastOrderId.substring(3);
                nextNumber = Integer.parseInt(numberPart) + 1;
            }

            return String.format("ORD%05d", nextNumber);

        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    }
}