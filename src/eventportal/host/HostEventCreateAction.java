package eventportal.host;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.User;
import tool.Action;

/**
 * イベント作成画面表示アクション
 */
public class HostEventCreateAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        System.out.println("=== イベント作成画面表示 ===");

        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        if (user == null || user.getUser_type() != 2) {
            System.out.println("エラー：未ログインまたは権限なし");
            res.sendRedirect(req.getContextPath() + "/eventportal/auth/HostLogin.action");
            return;
        }

        System.out.println("ユーザーID: " + user.getUser_id());
        System.out.println("ユーザー名: " + user.getUser_name());

        // イベント作成画面へフォワード
        req.getRequestDispatcher("/eventportal/host/host_event_create.jsp").forward(req, res);
    }
}
