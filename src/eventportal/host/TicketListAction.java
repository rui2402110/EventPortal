package eventportal.host;

import java.util.List;

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
 * チケット一覧表示アクション（主催者用）
 */
public class TicketListAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        System.out.println("=== チケット一覧表示処理開始 ===");

        // セッションからユーザー情報を取得
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        // ログインチェック（主催者のみ）
        if (user == null || user.getUser_type() != 2) {
            System.out.println("エラー：未ログインまたは権限なし");
            res.sendRedirect(req.getContextPath() + "/eventportal/auth/HostLogin.action");
            return;
        }

        // イベントIDを取得
        String eventId = req.getParameter("eventId");

        if (eventId == null || eventId.isEmpty()) {
            System.out.println("エラー：イベントIDが指定されていません");
            req.setAttribute("errorMessage", "イベントIDが指定されていません。");
            req.getRequestDispatcher("/error.jsp").forward(req, res);
            return;
        }

        System.out.println("イベントID: " + eventId);
        System.out.println("主催者ID: " + user.getUser_id());

        try {
            EventDao eventDao = new EventDao();
            Event event = eventDao.get(eventId);

            if (event == null) {
                System.out.println("エラー：イベントが見つかりません");
                req.setAttribute("errorMessage", "イベントが見つかりません。");
                req.getRequestDispatcher("/error.jsp").forward(req, res);
                return;
            }

            // 権限チェック：自分のイベントか確認
            if (!event.getHostId().equals(user.getUser_id())) {
                System.out.println("エラー：このイベントの主催者ではありません");
                req.setAttribute("errorMessage", "このイベントのチケット一覧を表示する権限がありません。");
                req.getRequestDispatcher("/error.jsp").forward(req, res);
                return;
            }

            // チケット一覧を取得
            TicketDao ticketDao = new TicketDao();
            List<Ticket> tickets = ticketDao.getByEventId(eventId);

            System.out.println("チケット数: " + tickets.size());

            // 統計情報を取得
            int admittedCount = ticketDao.getAdmittedCount(eventId);
            int validCount = 0;
            int usedCount = 0;
            int invalidCount = 0;

            for (Ticket ticket : tickets) {
                switch (ticket.getStatus()) {
                    case 1:
                        validCount++;
                        break;
                    case 2:
                        usedCount++;
                        break;
                    case 3:
                        invalidCount++;
                        break;
                }
            }

            System.out.println("有効チケット: " + validCount);
            System.out.println("使用済みチケット: " + usedCount);
            System.out.println("無効チケット: " + invalidCount);

            // リクエストスコープに設定
            req.setAttribute("event", event);
            req.setAttribute("tickets", tickets);
            req.setAttribute("validCount", validCount);
            req.setAttribute("usedCount", usedCount);
            req.setAttribute("invalidCount", invalidCount);
            req.setAttribute("totalCount", tickets.size());

            // JSPにフォワード
            req.getRequestDispatcher("/eventportal/host/ticket_list.jsp").forward(req, res);

        } catch (Exception e) {
            System.err.println("チケット一覧表示エラー: " + e.getMessage());
            e.printStackTrace();
            req.setAttribute("errorMessage", "エラーが発生しました: " + e.getMessage());
            req.getRequestDispatcher("/error.jsp").forward(req, res);
        }
    }
}