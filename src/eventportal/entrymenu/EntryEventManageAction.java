package eventportal.entrymenu;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Event;
import bean.User;
import dao.EntryEventDao;
import tool.Action;

public class EntryEventManageAction  extends Action  {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// リストの定義
		List<Event> event = null;
		// DAOの定義
		EntryEventDao eeDao = new EntryEventDao();
		// ユーザーIDを取得
		HttpSession session = req.getSession(false);
		User user = (User) session.getAttribute("user");
		String userId = user.getUser_id();
		System.out.println(userId);

		event = eeDao.entryJoinedEventGet(userId);
		System.out.println(event);

		req.setAttribute("event",event);
		req.getRequestDispatcher("/eventportal/entry/entry_event_manage.jsp").forward(req, res);

	}

}
