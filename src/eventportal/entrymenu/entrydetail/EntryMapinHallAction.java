package eventportal.entrymenu.entrydetail;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.EntryEventDao;
import tool.Action;

public class EntryMapinHallAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// jspから送られてきたデータを取得
		String eventId = req.getParameter("eventId");
		//
		// DAOを再定義
		EntryEventDao entryDao = new EntryEventDao();
		//会場内マップのurlを取得(会場内なのでareaTypeは1を選択)
		String mapUrl = entryDao.urlGet(eventId, 1);
		System.out.println(mapUrl);

		req.setAttribute("mapUrl",mapUrl);
		req.getRequestDispatcher("/eventportal/entry/entry_detail/entry_mapinhall.jsp").forward(req, res);
	}
}
