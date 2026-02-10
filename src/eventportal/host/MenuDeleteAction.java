package eventportal.host;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.User;
import dao.MenuDao;
import tool.Action;

/**
 * メニュー削除アクション
 */
public class MenuDeleteAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        System.out.println("=== メニュー削除処理開始 ===");

        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        if (user == null || user.getUser_type() != 2) {
            System.out.println("エラー：未ログインまたは権限なし");
            res.sendRedirect(req.getContextPath() + "/eventportal/auth/HostLogin.action");
            return;
        }

        String menuId = req.getParameter("menuId");
        String eventId = req.getParameter("eventId");

        if (menuId == null || menuId.isEmpty() || eventId == null || eventId.isEmpty()) {
            System.out.println("エラー：パラメータが不正です");
            session.setAttribute("errorMessage", "パラメータが不正です。");
            res.sendRedirect(req.getContextPath() + "/eventportal/host/MenuList.action?eventId=" + eventId);
            return;
        }

        System.out.println("メニューID: " + menuId);
        System.out.println("イベントID: " + eventId);

        try {
            MenuDao menuDao = new MenuDao();
            int count = menuDao.delete(menuId);

            if (count > 0) {
                System.out.println("✓ メニュー削除成功");
                session.setAttribute("successMessage", "メニューを削除しました。");
            } else {
                System.out.println("✗ メニュー削除失敗");
                session.setAttribute("errorMessage", "メニューの削除に失敗しました。");
            }

            res.sendRedirect(req.getContextPath() + "/eventportal/host/MenuList.action?eventId=" + eventId);

        } catch (Exception e) {
            System.err.println("メニュー削除エラー: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "エラーが発生しました: " + e.getMessage());
            res.sendRedirect(req.getContextPath() + "/eventportal/host/MenuList.action?eventId=" + eventId);
        }
    }
}