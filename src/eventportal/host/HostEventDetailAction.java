package eventportal.host;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Event;
import dao.EventDao;
import tool.Action;

public class HostEventDetailAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		// jspから送られてきたデータを取得
		String eventId = req.getParameter("eventId");
		System.out.println(eventId);
		// 取得したデータを格納する変数を定義
		Event event =null;

		// DAOを再定義
		EventDao evtDao = new EventDao();

		// データを取得
		event = evtDao.get(eventId);
		System.out.println(event);

		// JSPに送るデータをセット
		req.setAttribute("evt", event);
		// フォワード
		req.getRequestDispatcher("/eventportal/host/host_event_detail.jsp").forward(req, res);
	}

}
