package eventportal.host;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Event;
import bean.User;
import dao.EventDao;
import tool.Action;

/**
 * 主催者メニュー画面表示アクション（完全版）
 */
public class HostMenuAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        System.out.println("=== 主催者メニュー表示処理開始 ===");

        // セッション情報を取得
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        // ログインチェック
        if (user == null || user.getUser_type() != 2) {
            System.out.println("エラー：未ログインまたは権限なし");
            res.sendRedirect(req.getContextPath() + "/eventportal/auth/HostLogin.action");
            return;
        }

        System.out.println("主催者ID: " + user.getUser_id());
        System.out.println("主催者名: " + user.getUser_name());

        try {
            // DAOを再定義
            EventDao evtDao = new EventDao();

            // 自分が主催するイベントを取得
            List<Event> event = evtDao.getByHostId(user.getUser_id());
            System.out.println("主催イベント数: " + event.size());

            // JSPに送るデータをセット
            req.setAttribute("event", event);

            // 成功メッセージがあれば表示
            String successMessage = (String) session.getAttribute("successMessage");
            if (successMessage != null) {
                req.setAttribute("successMessage", successMessage);
                session.removeAttribute("successMessage");
                System.out.println("成功メッセージ: " + successMessage);
            }

            // エラーメッセージがあれば表示
            String errorMessage = (String) session.getAttribute("errorMessage");
            if (errorMessage != null) {
                req.setAttribute("errorMessage", errorMessage);
                session.removeAttribute("errorMessage");
                System.out.println("エラーメッセージ: " + errorMessage);
            }

            System.out.println("=== 主催者メニュー表示処理完了 ===");

            // フォワード
            req.getRequestDispatcher("/eventportal/host/host_menu.jsp").forward(req, res);

        } catch (Exception e) {
            System.err.println("主催者メニュー表示エラー: " + e.getMessage());
            e.printStackTrace();
            req.setAttribute("errorMessage", "エラーが発生しました: " + e.getMessage());
            req.getRequestDispatcher("/error.jsp").forward(req, res);
        }
    }
}