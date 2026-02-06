package eventportal.host;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Event;
import bean.Ticket;
import bean.User;
import dao.EventDao;
import dao.TicketDao;
import tool.Action;

/**
 * チケット検証・入場処理アクション
 */
public class AdmitEntryAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        System.out.println("=== 入場処理開始 ===");

        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        if (user == null) {
            System.out.println("エラー：未ログイン");
            res.sendRedirect(req.getContextPath() + "/eventportal/auth/HostLogin.action");
            return;
        }

        String eventId = req.getParameter("eventId");
        String ticketId = req.getParameter("ticketId");

        System.out.println("イベントID: " + eventId);
        System.out.println("チケットID: " + ticketId);

        if (eventId == null || eventId.isEmpty() || ticketId == null || ticketId.isEmpty()) {
            System.out.println("エラー：パラメータ不足");
            req.setAttribute("result", "error");
            req.setAttribute("errorMessage", "イベントIDまたはチケットIDが指定されていません。");
            req.setAttribute("eventId", eventId);
            req.getRequestDispatcher("/eventportal/host/verify_result.jsp").forward(req, res);
            return;
        }

        try {
            TicketDao ticketDao = new TicketDao();
            EventDao eventDao = new EventDao();

            Ticket ticket = ticketDao.get(ticketId);

            if (ticket == null) {
                System.out.println("エラー：チケットが見つかりません");
                req.setAttribute("result", "error");
                req.setAttribute("errorMessage", "チケットID「" + ticketId + "」が見つかりません。");
                req.setAttribute("eventId", eventId);
                req.getRequestDispatcher("/eventportal/host/verify_result.jsp").forward(req, res);
                return;
            }

            if (!eventId.equals(ticket.getEventId())) {
                System.out.println("エラー：イベントIDが一致しません");
                req.setAttribute("result", "error");
                req.setAttribute("errorMessage", "このチケットは別のイベント用です。");
                req.setAttribute("eventId", eventId);
                req.getRequestDispatcher("/eventportal/host/verify_result.jsp").forward(req, res);
                return;
            }

            Event event = eventDao.get(eventId);

            if (ticket.getStatus() == 2) {
                System.out.println("警告：使用済みチケット");
                req.setAttribute("result", "used");
                req.setAttribute("ticket", ticket);
                req.setAttribute("event", event);
                req.setAttribute("eventId", eventId);
                req.getRequestDispatcher("/eventportal/host/verify_result.jsp").forward(req, res);
                return;
            }

            if (ticket.getStatus() != 1) {
                System.out.println("エラー：無効なチケット");
                req.setAttribute("result", "error");
                req.setAttribute("errorMessage", "このチケットは無効です。");
                req.setAttribute("eventId", eventId);
                req.getRequestDispatcher("/eventportal/host/verify_result.jsp").forward(req, res);
                return;
            }

            Timestamp now = new Timestamp(System.currentTimeMillis());
            int updateCount = ticketDao.updateStatus(ticketId, 2, now);

            if (updateCount > 0) {
                System.out.println("入場処理成功");

                ticket = ticketDao.get(ticketId);

                req.setAttribute("result", "success");
                req.setAttribute("ticket", ticket);
                req.setAttribute("event", event);
                req.setAttribute("eventId", eventId);
                req.getRequestDispatcher("/eventportal/host/verify_result.jsp").forward(req, res);
            } else {
                System.out.println("エラー：データベース更新失敗");
                req.setAttribute("result", "error");
                req.setAttribute("errorMessage", "入場処理に失敗しました。");
                req.setAttribute("eventId", eventId);
                req.getRequestDispatcher("/eventportal/host/verify_result.jsp").forward(req, res);
            }

        } catch (Exception e) {
            System.err.println("入場処理エラー: " + e.getMessage());
            e.printStackTrace();
            req.setAttribute("result", "error");
            req.setAttribute("errorMessage", "システムエラーが発生しました: " + e.getMessage());
            req.setAttribute("eventId", eventId);
            req.getRequestDispatcher("/eventportal/host/verify_result.jsp").forward(req, res);
        }
    }
}