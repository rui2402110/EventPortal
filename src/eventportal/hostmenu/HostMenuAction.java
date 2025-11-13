package eventportal.hostmenu;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Event;
import bean.User;
import dao.EventDao;
import tool.Action;

public class HostMenuAction extends Action  {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		//セッション情報を取得
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute("user");

		List<Event> event =null;
		EventDao evtDao = new EventDao();

		event = evtDao.userIdFilter(user.getUser_id());
		System.out.println(event);

		// JSPに送るデータをセット
		req.setAttribute("event", event);
		// フォワード
		req.getRequestDispatcher("/eventportal/host/host_menu.jsp").forward(req, res);
	}
}
