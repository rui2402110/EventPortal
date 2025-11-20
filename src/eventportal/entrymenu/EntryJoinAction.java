package eventportal.entrymenu;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Event;
import dao.EventDao;
import tool.Action;

public class EntryJoinAction extends Action{
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		System.out.println("参加処理画面表示");
        // サインイン画面を表示するだけで、特別なロジックは不要
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

				req.getRequestDispatcher("../../eventportal/entry/enty_join.jsp").forward(req, res);

}
	}