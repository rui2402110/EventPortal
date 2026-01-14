package eventportal.host;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import bean.Ticket;
import bean.User;
import dao.TicketDao;
import dao.UserDao;
import tool.Action;

public class AdmitEntryAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String ticketId = req.getParameter("ticketId");
        String eventId = req.getParameter("eventId");

        TicketDao ticketDao = new TicketDao();
        UserDao userDao = new UserDao();

        Map<String, Object> result = new HashMap<>();

        try {
            // チケットを使用済みに更新
            boolean success = ticketDao.markAsUsed(ticketId, LocalDateTime.now());

            if (success) {
                result.put("success", true);

                // 入場履歴データを作成
                Ticket ticket = ticketDao.get(ticketId);
                if (ticket != null) {
                    Map<String, String> entry = new HashMap<>();
                    entry.put("ticketId", ticketId);

                    User ticketUser = userDao.get(ticket.getUserId(), 1);
                    entry.put("userName", ticketUser != null ? ticketUser.getUser_name() : "不明");
                    entry.put("time", LocalDateTime.now().toString());

                    result.put("entry", entry);
                }
            } else {
                result.put("success", false);
                result.put("message", "入場記録に失敗しました");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "エラーが発生しました: " + e.getMessage());
        }

        // JSON形式で返却
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        res.getWriter().write(new Gson().toJson(result));
    }
}