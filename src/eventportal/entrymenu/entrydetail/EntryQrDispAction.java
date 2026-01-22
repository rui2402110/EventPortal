package eventportal.entrymenu.entrydetail;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Event;
import bean.Ticket;
import bean.User;
import dao.EventDao;
import dao.TicketDao;
import tool.Action;

public class EntryQrDispAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/eventportal/auth/EntryLogin.action");
            return;
        }

        String eventId = req.getParameter("eventId");

        if (eventId == null || eventId.isEmpty()) {
            req.setAttribute("errorMessage", "イベントIDが指定されていません");
            req.getRequestDispatcher("/eventportal/common/error.jsp").forward(req, res);
            return;
        }

        try {
            TicketDao ticketDao = new TicketDao();
            EventDao eventDao = new EventDao();

            // ユーザーのチケットを取得
            Ticket ticket = ticketDao.getByEventAndUser(eventId, user.getUser_id());

            if (ticket == null) {
                req.setAttribute("errorMessage", "チケットが見つかりません");
                req.getRequestDispatcher("/eventportal/common/error.jsp").forward(req, res);
                return;
            }

            // イベント情報を取得
            Event event = eventDao.get(eventId);

            // QRコード画像パスを設定（相対パス）
            if (ticket.getQrImagePath() != null && !ticket.getQrImagePath().isEmpty()) {
                // ファイル名だけの場合は相対パスに変換
                if (!ticket.getQrImagePath().startsWith("/")) {
                    ticket.setQrImagePath("/qr/" + ticket.getQrImagePath());
                }
            }

            // リクエストに設定
            req.setAttribute("ticket", ticket);
            req.setAttribute("event", event);
            req.setAttribute("qrCode", ticket); // entry_qrcode_view.jspとの互換性のため

            // JSPへフォワード
            req.getRequestDispatcher("/eventportal/qr/entry_qrcode_view.jsp").forward(req, res);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", "エラーが発生しました: " + e.getMessage());
            req.getRequestDispatcher("/eventportal/common/error.jsp").forward(req, res);
        }
    }
}