package eventportal.host;

import java.io.PrintWriter;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Ticket;
import dao.TicketDao;
import tool.Action;

/**
 * 入場処理アクション（JSON対応版）
 */
public class AdmitEntryAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        System.out.println("=== 入場処理開始 ===");

        String ticketId = req.getParameter("ticketId");
        String eventId = req.getParameter("eventId");

        System.out.println("チケットID: " + ticketId);
        System.out.println("イベントID: " + eventId);

        // JSON形式でレスポンス
        res.setContentType("application/json; charset=UTF-8");
        PrintWriter out = res.getWriter();

        try {
            if (ticketId == null || ticketId.isEmpty()) {
                out.print("{\"success\":false,\"message\":\"チケットIDが不正です\"}");
                return;
            }

            TicketDao ticketDao = new TicketDao();
            Ticket ticket = ticketDao.get(ticketId);

            if (ticket == null) {
                System.out.println("エラー：チケットが見つかりません");
                out.print("{\"success\":false,\"message\":\"チケットが見つかりません\"}");
                return;
            }

            if (!ticket.getEventId().equals(eventId)) {
                System.out.println("エラー：イベントIDが一致しません");
                out.print("{\"success\":false,\"message\":\"このイベントのチケットではありません\"}");
                return;
            }

            if (ticket.getStatus() == 2) {
                System.out.println("エラー：既に使用済みです");
                out.print("{\"success\":false,\"message\":\"このチケットは既に使用済みです\"}");
                return;
            }

            if (ticket.getStatus() != 1) {
                System.out.println("エラー：無効なチケットです");
                out.print("{\"success\":false,\"message\":\"無効なチケットです\"}");
                return;
            }

            // ステータスを「使用済み」に更新
            Timestamp now = new Timestamp(System.currentTimeMillis());
            ticketDao.updateStatus(ticketId, 2, now);

            System.out.println("✓ 入場承認成功");
            out.print("{\"success\":true,\"message\":\"入場を承認しました\",\"participantName\":\"" +
                     ticket.getParticipantName() + "\",\"ticketId\":\"" + ticketId + "\"}");

        } catch (Exception e) {
            System.err.println("入場処理エラー: " + e.getMessage());
            e.printStackTrace();
            out.print("{\"success\":false,\"message\":\"エラーが発生しました: " + e.getMessage() + "\"}");
        } finally {
            out.flush();
        }
    }
}