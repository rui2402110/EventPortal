package eventportal.host;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Event;
import bean.User;
import dao.EventDao;
import dao.UserDaoEx;
import tool.Action;

/**
 * チケット作成画面表示アクション
 */
public class CreateTicketAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        if (user == null || user.getUser_type() != 2) {
            res.sendRedirect(req.getContextPath() + "/eventportal/auth/HostLogin.action");
            return;
        }

        String eventId = req.getParameter("eventId");

        EventDao eventDao = new EventDao();
        UserDaoEx userDaoEx = new UserDaoEx();

        // ★ userIdFilter() → getByHostId() に変更
        List<Event> events = eventDao.getByHostId(user.getUser_id());
        List<User> entryUsers = userDaoEx.getByType(1);

        req.setAttribute("events", events);
        req.setAttribute("entryUsers", entryUsers);

        if (eventId != null && !eventId.isEmpty()) {
            Event selectedEvent = eventDao.get(eventId);
            req.setAttribute("selectedEvent", selectedEvent);
        }

        req.getRequestDispatcher("/eventportal/host/host_create_ticket.jsp").forward(req, res);
    }
}