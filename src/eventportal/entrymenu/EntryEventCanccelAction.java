package eventportal.entrymenu;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.User;
import dao.EntryEventDao;
import tool.Action;

public class EntryEventCanccelAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// jspから送られてきたデータを取得
		String eventId = req.getParameter("eventId");
		// ユーザーIDを取得
		HttpSession session = req.getSession(false);
		User user = (User) session.getAttribute("user");
		String userId = user.getUser_id();
		System.out.println(userId);
		// DAOを再定義
		EntryEventDao entryDao = new EntryEventDao();
		// キャンセル処理
		boolean isAttend = entryDao.eventCanncel(eventId, userId);
		// かっこいいので参考演算子で処理
		String message = isAttend ? "キャンセルしました" : "キャンセルに失敗しました";
		req.setAttribute("message", message);
		req.getRequestDispatcher("/eventportal/entrymenu/EntryEventManage.action").forward(req, res);
	}
}
