package eventportal.entrymenu;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Ticket;
import bean.User;
import dao.EntryEventDao;
import dao.TicketDao;
import tool.Action;
import util.QRCodeGenerator;

public class EntryJoinExecuteAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        // セッション取得
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        // ユーザーチェック
        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/eventportal/auth/EntryLogin.action");
            return;
        }

        // パラメータ取得
        String eventId = req.getParameter("eventId");

        if (eventId == null || eventId.isEmpty()) {
            req.setAttribute("errorMessage", "イベントIDが指定されていません");
            req.getRequestDispatcher("entry_join.jsp").forward(req, res);
            return;
        }

        // DAO生成
        EntryEventDao entryDao = new EntryEventDao();
        TicketDao ticketDao = new TicketDao();

        try {
            // イベント参加登録
            boolean result = entryDao.join(user.getUser_id(), eventId);

            if (result) {
                // 登録成功 - チケットを作成
                System.out.println("参加登録成功 - チケット作成開始");

                // チケットIDを生成
                String ticketId = ticketDao.generateTicketId();

                // QRコード画像をBase64形式で生成
                String qrImageData = QRCodeGenerator.generateQRCodeBase64(ticketId);

                // QRコード画像ファイルも生成（オプション）
                String qrImagePath = null;
                try {
                    String outputPath = req.getServletContext().getRealPath("/qr");
                    if (outputPath != null) {
                        qrImagePath = QRCodeGenerator.generateTicketQRCode(ticketId, outputPath);
                        System.out.println("QRコード画像ファイル作成: " + qrImagePath);
                    }
                } catch (Exception e) {
                    System.err.println("QRコード画像ファイル保存エラー（Base64は保存済み）: " + e.getMessage());
                }

                // チケット情報を設定
                Ticket ticket = new Ticket();
                ticket.setTicketId(ticketId);
                ticket.setUserId(user.getUser_id());
                ticket.setEventId(eventId);
                ticket.setQrImagePath(qrImagePath);
                ticket.setQrImageData(qrImageData);
                ticket.setStatus(1); // 1: 有効
                ticket.setCreatedAt(LocalDateTime.now());

                // チケットをDBに保存
                boolean ticketCreated = ticketDao.create(ticket);

                if (ticketCreated) {
                    System.out.println("チケット作成成功: " + ticketId);
                    req.setAttribute("successMessage", "イベントへの参加登録が完了しました");
                    res.sendRedirect(req.getContextPath() + "/eventportal/entrymenu/MyTickets.action");
                } else {
                    System.err.println("チケット作成失敗");
                    req.setAttribute("errorMessage", "チケットの作成に失敗しました");
                    req.getRequestDispatcher("entry_join.jsp").forward(req, res);
                }
            } else {
                // 登録失敗
                System.out.println("参加登録失敗（既に参加済みの可能性あり）");
                req.setAttribute("errorMessage", "参加登録に失敗しました（既に参加済みの可能性があります）");
                req.getRequestDispatcher("entry_join.jsp").forward(req, res);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", "エラーが発生しました: " + e.getMessage());
            req.getRequestDispatcher("entry_join.jsp").forward(req, res);
        }
    }
}