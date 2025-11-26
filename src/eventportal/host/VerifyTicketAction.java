package eventportal.host;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import bean.Event;
import bean.Ticket;
import bean.User;
import dao.EventDao;
import dao.TicketDao;
import dao.UserDao;
import tool.Action;

public class VerifyTicketAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String ticketId = req.getParameter("ticketId");
        String eventId = req.getParameter("eventId");

        TicketDao ticketDao = new TicketDao();
        EventDao eventDao = new EventDao();
        UserDao userDao = new UserDao();

        Map<String, Object> result = new HashMap<>();

        try {
            // チケット検証ロジック
            Ticket ticket = ticketDao.get(ticketId);

            if (ticket != null && ticket.getEventId().equals(eventId)) {
                if (ticket.getStatus() == 1) {
                    // 有効なチケット
                    result.put("valid", true);
                    result.put("ticketId", ticket.getTicketId());

                    User ticketUser = userDao.get(ticket.getUserId(), 1);
                    result.put("userName", ticketUser != null ? ticketUser.getUser_name() : "不明");

                    Event event = eventDao.get(eventId);
                    result.put("eventName", event != null ? event.getEventName() : "不明");
                } else if (ticket.getStatus() == 2) {
                    // 使用済み
                    result.put("valid", false);
                    result.put("alreadyUsed", true);

                    User ticketUser = userDao.get(ticket.getUserId(), 1);
                    result.put("userName", ticketUser != null ? ticketUser.getUser_name() : "不明");

                    if (ticket.getUsedAt() != null) {
                        result.put("usedTime", ticket.getUsedAt().toString());
                    } else {
                        result.put("usedTime", "不明");
                    }
                } else {
                    // 無効
                    result.put("valid", false);
                    result.put("errorMessage", "このチケットは無効です");
                }
            } else {
                result.put("valid", false);
                result.put("errorMessage", "チケットが見つかりません");
            }
        } catch (Exception e) {
            result.put("valid", false);
            result.put("errorMessage", "エラーが発生しました: " + e.getMessage());
        }

        // JSON形式で返却
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        res.getWriter().write(new Gson().toJson(result));
    }
}