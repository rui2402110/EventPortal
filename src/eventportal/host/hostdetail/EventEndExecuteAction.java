package eventportal.host.hostdetail;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.HostEventDao;
import tool.Action;

public class EventEndExecuteAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// jspからデータ取得
		String eventId = req.getParameter("eventId");
		// Daoを定義
		HostEventDao hosDao = new HostEventDao();

		boolean result = hosDao.incrementEventHoldState(eventId);

		// 画面遷移のとき何書いて画面遷移してたか忘れた
		req.getRequestDispatcher("/eventportal/host/event_end_done.jsp").forward(req, res);
	}

}