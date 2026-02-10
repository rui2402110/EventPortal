package eventportal.entrymenu;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Event;
import bean.Menu;
import bean.User;
import dao.EventDao;
import dao.MenuDao;
import tool.Action;

/**
 * 参加者用メニュー表示アクション
 */
public class EntryMenuViewAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        System.out.println("\n========================================");
        System.out.println("=== 参加者用メニュー表示 ===");
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

        String eventId = req.getParameter("eventId");
        System.out.println("受信パラメータ eventId: " + eventId);

        if (eventId == null || eventId.isEmpty()) {
            System.out.println("✗ イベントIDが空");
            req.setAttribute("errorMessage", "イベントIDが指定されていません。");
            req.getRequestDispatcher("/error.jsp").forward(req, res);
            return;
        }

        System.out.println("✓ イベントID: " + eventId);

        try {
            // イベント情報を取得
            System.out.println("\n【STEP 1】イベント情報取得");
            EventDao eventDao = new EventDao();
            Event event = eventDao.get(eventId);

            if (event == null) {
                System.out.println("✗ イベントが見つかりません");
                req.setAttribute("errorMessage", "イベントが見つかりません。");
                req.getRequestDispatcher("/error.jsp").forward(req, res);
                return;
            }

            System.out.println("✓ イベント取得成功: " + event.getEventName());

            // メニュー一覧を取得
            System.out.println("\n【STEP 2】メニュー一覧取得");
            MenuDao menuDao = new MenuDao();
            List<Menu> menuList = menuDao.getByEventId(eventId);

            System.out.println("✓ メニュー件数: " + menuList.size());

            for (Menu menu : menuList) {
                System.out.println("  - " + menu.getMenuId() + ": " + menu.getMenuName() + " (¥" + menu.getPrice() + ")");
            }

            // リクエストに設定
            req.setAttribute("event", event);
            req.setAttribute("menuList", menuList);

            // 成功メッセージがあれば表示
            String successMessage = (String) session.getAttribute("successMessage");
            if (successMessage != null) {
                System.out.println("✓ 成功メッセージあり: " + successMessage);
                req.setAttribute("successMessage", successMessage);
                session.removeAttribute("successMessage");
            }

            System.out.println("\n✓✓✓ entry_menu_view.jsp にフォワード ✓✓✓");
            System.out.println("========================================\n");

            req.getRequestDispatcher("/eventportal/entry/entry_menu_view.jsp").forward(req, res);

        } catch (Exception e) {
            System.err.println("\n✗✗✗ メニュー表示エラー ✗✗✗");
            System.err.println("エラークラス: " + e.getClass().getName());
            System.err.println("エラーメッセージ: " + e.getMessage());
            System.err.println("スタックトレース:");
            e.printStackTrace();
            System.err.println("========================================\n");

            req.setAttribute("errorMessage", "エラーが発生しました: " + e.getMessage());
            req.getRequestDispatcher("/error.jsp").forward(req, res);
        }
    }
}