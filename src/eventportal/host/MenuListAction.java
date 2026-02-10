package eventportal.host;

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
 * メニュー一覧表示アクション
 */
public class MenuListAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        System.out.println("=== メニュー一覧表示 ===");

        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        if (user == null || user.getUser_type() != 2) {
            System.out.println("エラー：未ログインまたは権限なし");
            res.sendRedirect(req.getContextPath() + "/eventportal/auth/HostLogin.action");
            return;
        }

        String eventId = req.getParameter("eventId");

        if (eventId == null || eventId.isEmpty()) {
            System.out.println("エラー：イベントIDが指定されていません");
            req.setAttribute("errorMessage", "イベントIDが指定されていません。");
            req.getRequestDispatcher("/error.jsp").forward(req, res);
            return;
        }

        System.out.println("イベントID: " + eventId);

        try {
            // イベント情報を取得
            EventDao eventDao = new EventDao();
            Event event = eventDao.get(eventId);

            if (event == null) {
                System.out.println("エラー：イベントが見つかりません");
                req.setAttribute("errorMessage", "イベントが見つかりません。");
                req.getRequestDispatcher("/error.jsp").forward(req, res);
                return;
            }

            // 権限チェック
            if (!event.getHostId().equals(user.getUser_id())) {
                System.out.println("エラー：このイベントの主催者ではありません");
                req.setAttribute("errorMessage", "このイベントを編集する権限がありません。");
                req.getRequestDispatcher("/error.jsp").forward(req, res);
                return;
            }

            // メニュー一覧を取得
            MenuDao menuDao = new MenuDao();
            List<Menu> menuList = menuDao.getByEventId(eventId);

            System.out.println("メニュー件数: " + menuList.size());

            req.setAttribute("event", event);
            req.setAttribute("menuList", menuList);

            // 成功メッセージがあれば表示
            String successMessage = (String) session.getAttribute("successMessage");
            if (successMessage != null) {
                req.setAttribute("successMessage", successMessage);
                session.removeAttribute("successMessage");
            }

            req.getRequestDispatcher("/eventportal/host/menu_list.jsp").forward(req, res);

        } catch (Exception e) {
            System.err.println("メニュー一覧表示エラー: " + e.getMessage());
            e.printStackTrace();
            req.setAttribute("errorMessage", "エラーが発生しました: " + e.getMessage());
            req.getRequestDispatcher("/error.jsp").forward(req, res);
        }
    }
}