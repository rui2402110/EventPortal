package eventportal.entrymenu;

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
 * チケット詳細表示アクション
 */
public class ViewTicketAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        System.out.println("=== チケット詳細表示処理開始 ===");

        // セッションからユーザー情報を取得（false: 存在しない場合は新規作成しない）
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        // ログインチェック
        if (user == null) {
            System.out.println("未ログイン：ログイン画面にリダイレクト");
            res.sendRedirect(req.getContextPath() + "/eventportal/auth/EntryLogin.action");
            return;
        }

        // チケットIDを取得
        String ticketId = req.getParameter("ticketId");

        if (ticketId == null || ticketId.isEmpty()) {
            System.out.println("エラー：チケットIDが指定されていません");
            session.setAttribute("errorMessage", "チケットIDが指定されていません。");
            res.sendRedirect(req.getContextPath() + "/eventportal/entrymenu/MyTickets.action");
            return;
        }

        System.out.println("チケットID: " + ticketId);
        System.out.println("ユーザーID: " + user.getUser_id());

        try {
            TicketDao ticketDao = new TicketDao();
            EventDao eventDao = new EventDao();

            // チケット情報を取得
            Ticket ticket = ticketDao.get(ticketId);

            if (ticket == null) {
                System.out.println("エラー：チケットが見つかりません");
                session.setAttribute("errorMessage", "チケットが見つかりません。");
                res.sendRedirect(req.getContextPath() + "/eventportal/entrymenu/MyTickets.action");
                return;
            }

            // 権限チェック：自分のチケットかどうか
            if (!ticket.getUserId().equals(user.getUser_id())) {
                System.out.println("エラー：アクセス権限なし");
                session.setAttribute("errorMessage", "このチケットにアクセスする権限がありません。");
                res.sendRedirect(req.getContextPath() + "/eventportal/entrymenu/MyTickets.action");
                return;
            }

            // イベント情報を取得
            Event event = eventDao.get(ticket.getEventId());

            if (event == null) {
                System.out.println("警告：イベント情報が見つかりません");
                // イベントが見つからなくてもチケット表示は続行
            }

            System.out.println("チケット詳細表示: チケットID=" + ticketId +
                             ", イベント=" + (event != null ? event.getEventName() : "不明") +
                             ", QRデータ=" + (ticket.getQrImageData() != null ? "あり" : "なし"));

            // リクエストスコープに設定
            req.setAttribute("ticket", ticket);
            req.setAttribute("event", event);
            req.setAttribute("user", user);

            // JSPにフォワード
            req.getRequestDispatcher("/eventportal/qr/view_ticket.jsp").forward(req, res);

        } catch (Exception e) {
            System.err.println("チケット詳細表示エラー: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "エラーが発生しました: " + e.getMessage());
            res.sendRedirect(req.getContextPath() + "/eventportal/entrymenu/MyTickets.action");
        }
    }
}