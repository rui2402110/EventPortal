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
            User user = (User)session.getAttribute("user");

            // パラメータ取得
            String eventId = req.getParameter("eventId");
            // Dao生成
            EntryEventDao dao = new EntryEventDao();


            // イベント参加登録
            boolean result = dao.join(eventId , user.getUser_id());

            if (result == true) {
                // 登録成功
                req.setAttribute("successMessage", "イベントへの参加登録が完了しました");
                res.sendRedirect("entry_event_list.jsp?eventId=" + eventId);
            } else {
                // 登録失敗
                req.setAttribute("errorMessage", "参加登録に失敗しました");
                req.getRequestDispatcher("entry_join.jsp").forward(req, res);
            }
	}
}