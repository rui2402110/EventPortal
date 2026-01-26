package eventportal.entrymenu;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.User;
import dao.EntryEventDao;
import tool.Action;

public class EntryJoinExecuteAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        // セッション取得
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        // ユーザーチェック
        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/eventportal/auth/EntryLogin.action");
            return;
        }

        // パラメータ取得
        String eventId = req.getParameter("eventId");

        if (eventId == null || eventId.isEmpty()) {
            req.setAttribute("errorMessage", "イベントIDが指定されていません");
            req.getRequestDispatcher("entry_join.jsp").forward(req, res);
            return;
        }

        // DAO生成
        EntryEventDao entryDao = new EntryEventDao();

        try {
            // イベント参加登録のみ実行（チケットは別処理）
            boolean result = entryDao.join(user.getUser_id(), eventId);

            if (result) {
                // 登録成功
                System.out.println("参加登録成功: ユーザーID=" + user.getUser_id() + ", イベントID=" + eventId);

                // 成功メッセージをセッションに保存
                session.setAttribute("successMessage", "イベントへの参加登録が完了しました");

                // イベント管理画面へリダイレクト
                res.sendRedirect(req.getContextPath() + "/eventportal/entrymenu/EntryEventManage.action");
            } else {
                // 登録失敗
                System.out.println("参加登録失敗");
                req.setAttribute("errorMessage", "参加登録に失敗しました（既に参加済みの可能性があります）");
                req.getRequestDispatcher("entry_join.jsp").forward(req, res);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", "エラーが発生しました: " + e.getMessage());
            req.getRequestDispatcher("entry_join.jsp").forward(req, res);
        }
    }
}
