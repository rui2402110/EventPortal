package eventportal.host;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Menu;
import bean.User;
import dao.Dao;
import dao.MenuDao;
import tool.Action;

/**
 * メニュー作成実行アクション
 */
public class MenuCreateExecuteAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        System.out.println("\n========================================");
        System.out.println("=== メニュー作成処理開始 ===");
        System.out.println("========================================");

        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        if (user == null || user.getUser_type() != 2) {
            System.out.println("✗ エラー：未ログインまたは権限なし");
            res.sendRedirect(req.getContextPath() + "/eventportal/auth/HostLogin.action");
            return;
        }

        System.out.println("✓ ユーザー認証OK: " + user.getUser_id());

        try {
            // パラメータ取得
            String eventId = req.getParameter("eventId");
            String menuName = req.getParameter("menuName");
            String menuType = req.getParameter("menuType");
            String priceStr = req.getParameter("price");
            String stockQuantityStr = req.getParameter("stockQuantity");
            String description = req.getParameter("description");

            System.out.println("\n受信パラメータ:");
            System.out.println("  - eventId: " + eventId);
            System.out.println("  - menuName: " + menuName);
            System.out.println("  - menuType: " + menuType);
            System.out.println("  - price: " + priceStr);
            System.out.println("  - stockQuantity: " + stockQuantityStr);

            // バリデーション
            if (eventId == null || eventId.isEmpty() ||
                menuName == null || menuName.trim().isEmpty() ||
                menuType == null || menuType.isEmpty() ||
                priceStr == null || priceStr.isEmpty() ||
                stockQuantityStr == null || stockQuantityStr.isEmpty()) {

                System.out.println("✗ 必須項目が入力されていません");
                req.setAttribute("errorMessage", "必須項目が入力されていません。");
                req.getRequestDispatcher("/eventportal/host/menu_create.jsp").forward(req, res);
                return;
            }

            System.out.println("✓ バリデーションOK");

            int price = Integer.parseInt(priceStr);
            int stockQuantity = Integer.parseInt(stockQuantityStr);

            // メニューID自動生成
            String menuId = generateMenuId();
            System.out.println("✓ メニューID生成: " + menuId);

            // Menuオブジェクト作成
            Menu menu = new Menu();
            menu.setMenuId(menuId);
            menu.setEventId(eventId);
            menu.setMenuName(menuName.trim());
            menu.setMenuType(menuType);
            menu.setPrice(price);
            menu.setStockQuantity(stockQuantity);
            menu.setDescription(description != null && !description.trim().isEmpty() ? description.trim() : null);

            System.out.println("\n✓ Menuオブジェクト作成完了");

            // データベース登録
            MenuDao menuDao = new MenuDao();
            int count = menuDao.save(menu);

            System.out.println("登録結果: " + count + "件");

            if (count > 0) {
                System.out.println("✓✓✓ メニュー作成成功！ ✓✓✓");
                System.out.println("========================================\n");

                session.setAttribute("successMessage", "メニュー「" + menu.getMenuName() + "」を追加しました。");
                res.sendRedirect(req.getContextPath() + "/eventportal/host/MenuList.action?eventId=" + eventId);
            } else {
                System.out.println("✗ メニュー作成失敗");
                req.setAttribute("errorMessage", "メニューの作成に失敗しました。");
                req.getRequestDispatcher("/eventportal/host/menu_create.jsp").forward(req, res);
            }

        } catch (NumberFormatException e) {
            System.err.println("✗ 数値変換エラー: " + e.getMessage());
            req.setAttribute("errorMessage", "価格または在庫数には数値を入力してください。");
            req.getRequestDispatcher("/eventportal/host/menu_create.jsp").forward(req, res);
        } catch (Exception e) {
            System.err.println("✗✗✗ メニュー作成エラー ✗✗✗");
            System.err.println("エラー内容: " + e.getMessage());
            e.printStackTrace();
            System.err.println("========================================\n");

            req.setAttribute("errorMessage", "エラーが発生しました: " + e.getMessage());
            req.getRequestDispatcher("/eventportal/host/menu_create.jsp").forward(req, res);
        }
    }

    /**
     * メニューID自動生成
     * @return 新しいメニューID
     * @throws Exception
     */
    private String generateMenuId() throws Exception {
        Dao dao = new Dao();
        Connection connection = dao.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String sql = "SELECT menu_id FROM MENUS ORDER BY menu_id DESC LIMIT 1";
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();

            int nextNumber = 1;

            if (resultSet.next()) {
                String lastMenuId = resultSet.getString("menu_id");
                String numberPart = lastMenuId.substring(4);
                nextNumber = Integer.parseInt(numberPart) + 1;
            }

            return String.format("MENU%03d", nextNumber);

        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    }
}