package eventportal.entrymenu;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tool.Action;

public class EntryEventDetailAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		req.getRequestDispatcher("/eventportal/entry/entry_event_list.jsp").forward(req, res);


	}

}
