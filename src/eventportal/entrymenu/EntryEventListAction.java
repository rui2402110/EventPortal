package eventportal.entrymenu;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Event;
import bean.User;
import dao.EventDao;
import tool.Action;

public class EntryEventListAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/eventportal/auth/EntryLogin.action");
            return;
        }

        EventDao evtDao = new EventDao();
        List<Event> event = evtDao.filter(null);

        System.out.println(event);
        req.setAttribute("event", event);
        req.getRequestDispatcher("/eventportal/entry/entry_event_list.jsp").forward(req, res);
    }
}