package eventportal.host;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Event;
import bean.User;
import dao.EventDao;
import tool.Action;

/**
 * イベント更新画面表示アクション
 */
public class HostEventUpdateAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        System.out.println("=== イベント更新画面表示 ===");

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

            System.out.println("イベント名: " + event.getEventName());
            System.out.println("✓ 権限チェックOK");

            req.setAttribute("event", event);
            req.getRequestDispatcher("/eventportal/host/host_event_update.jsp").forward(req, res);

        } catch (Exception e) {
            System.err.println("イベント更新画面表示エラー: " + e.getMessage());
            e.printStackTrace();
            req.setAttribute("errorMessage", "エラーが発生しました: " + e.getMessage());
            req.getRequestDispatcher("/error.jsp").forward(req, res);
        }
    }
}