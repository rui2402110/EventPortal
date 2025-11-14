package eventportal.entrymenu;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Event;
import dao.EventDao;
import tool.Action;

public class EntryEventListAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		List<Event> event=null;
		EventDao evtDao=new EventDao();

		event=evtDao.joinedFilter();
		System.out.println(event);

		req.setAttribute("event",event);
		req.getRequestDispatcher("/eventportal/entry/entry_event_list.jsp").forward(req, res);


	}
}