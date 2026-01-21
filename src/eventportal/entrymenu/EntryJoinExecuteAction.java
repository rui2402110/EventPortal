package eventportal.entrymenu;

import java.io.File;
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

        // パラメータ取得
        String eventId = req.getParameter("eventId");

        // Dao生成
        EntryEventDao dao = new EntryEventDao();
        TicketDao ticketDao = new TicketDao();

        try {
            // イベント参加登録
            boolean result = dao.join(user.getUser_id(), eventId);

            if (result) {
                // 登録成功
                System.out.println("参加登録成功");

                // チケットを作成
                String ticketId = ticketDao.generateTicketId();

                // QRコード画像をBase64形式で生成
                String qrImageData = QRCodeGenerator.generateQRCodeBase64(ticketId);

                // QRコード画像ファイルも作成
                String qrImagePath = null;
                try {
                    String outputPath = req.getServletContext().getRealPath("/qr");
                    if (outputPath != null) {
                        // ディレクトリが存在しない場合は作成
                        File qrDir = new File(outputPath);
                        if (!qrDir.exists()) {
                            qrDir.mkdirs();
                            System.out.println("QRコード保存ディレクトリを作成: " + outputPath);
                        }

                        // QRコード画像を生成して保存
                        String fileName = QRCodeGenerator.generateTicketQRCode(ticketId, outputPath);
                        if (fileName != null) {
                            qrImagePath = "/qr/" + fileName;
                            System.out.println("QRコード画像保存成功: " + qrImagePath);
                        }
                    } else {
                        System.err.println("QRコード保存パスが取得できませんでした");
                    }
                } catch (Exception e) {
                    System.err.println("QRコード画像ファイル保存エラー: " + e.getMessage());
                    e.printStackTrace();
                }

                // チケット情報を設定
                Ticket ticket = new Ticket();
                ticket.setTicketId(ticketId);
                ticket.setUserId(user.getUser_id());
                ticket.setEventId(eventId);
                ticket.setQrImagePath(qrImagePath);
                ticket.setQrImageData(qrImageData);
                ticket.setStatus(1); // 有効
                ticket.setTicketInfo("");
                ticket.setCreatedAt(LocalDateTime.now());

                // チケットをDBに保存
                boolean ticketCreated = ticketDao.create(ticket);

                if (ticketCreated) {
                    System.out.println("チケット作成成功: " + ticketId);

                    // QRコード表示画面へリダイレクト
                    res.sendRedirect(req.getContextPath() +
                        "/eventportal/entrymenu/entrydetail/EntryQrDisp.action?eventId=" + eventId);
                } else {
                    System.err.println("チケット作成失敗");
                    req.setAttribute("errorMessage", "チケット作成に失敗しました");
                    req.getRequestDispatcher("entry_join.jsp").forward(req, res);
                }
            } else {
                // 登録失敗
                System.out.println("参加登録失敗");
                req.setAttribute("errorMessage", "参加登録に失敗しました");
                req.getRequestDispatcher("entry_join.jsp").forward(req, res);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", "エラーが発生しました: " + e.getMessage());
            req.getRequestDispatcher("entry_join.jsp").forward(req, res);
        }
    }
}