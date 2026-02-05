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
 * QRコード表示アクション
 */
public class ShowQRCodeAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        System.out.println("QRコード表示処理開始");

        // セッションからユーザー情報を取得
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        // ログインチェック
        if (user == null) {
            System.out.println("未ログイン：ログイン画面にリダイレクト");
            res.sendRedirect(req.getContextPath() + "/eventportal/auth/EntryLogin.action");
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
        System.out.println("ユーザーID: " + user.getUser_id());

        try {
            // イベント情報を取得
            EventDao eventDao = new EventDao();
            Event event = eventDao.get(eventId);

            if (event == null) {
                System.out.println("エラー：イベントが見つかりません");
                req.setAttribute("errorMessage", "指定されたイベントが見つかりません。");
                req.getRequestDispatcher("/error.jsp").forward(req, res);
                return;
            }

            System.out.println("イベント名: " + event.getEventName());

            // チケット情報を取得
            TicketDao ticketDao = new TicketDao();
            Ticket ticket = ticketDao.getByEventAndUser(eventId, user.getUser_id());

            if (ticket == null) {
                System.out.println("エラー：チケットが見つかりません");
                req.setAttribute("errorMessage", "このイベントのチケットが見つかりません。");
                req.getRequestDispatcher("/error.jsp").forward(req, res);
                return;
            }

            System.out.println("チケットID: " + ticket.getTicketId());
            System.out.println("QRデータ有無: " + (ticket.getQrImageData() != null ? "あり" : "なし"));

            // リクエストスコープに設定
            req.setAttribute("event", event);
            req.setAttribute("ticket", ticket);

            // QRコード表示JSPへフォワード
            req.getRequestDispatcher("/eventportal/qr/showQRCode.jsp").forward(req, res);

        } catch (Exception e) {
            System.err.println("QRコード表示エラー: " + e.getMessage());
            e.printStackTrace();
            req.setAttribute("errorMessage", "エラーが発生しました: " + e.getMessage());
            req.getRequestDispatcher("/error.jsp").forward(req, res);
        }
    }
}