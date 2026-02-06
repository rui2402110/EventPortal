package eventportal.host;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import bean.Ticket;
import bean.User;
import dao.TicketDao;
import tool.Action;

/**
 * チケット作成実行アクション
 */
public class CreateTicketExecuteAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        System.out.println("=== チケット作成処理開始 ===");

        // セッションからユーザー情報を取得
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        // ログインチェック
        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/eventportal/auth/HostLogin.action");
            return;
        }

        // パラメータ取得
        String ticketId = req.getParameter("ticketId");
        String eventId = req.getParameter("eventId");
        String userId = req.getParameter("userId");
        String participantName = req.getParameter("participantName");
        String statusStr = req.getParameter("status");

        System.out.println("チケットID: " + ticketId);
        System.out.println("イベントID: " + eventId);
        System.out.println("ユーザーID: " + userId);
        System.out.println("参加者名: " + participantName);

        // バリデーション
        if (ticketId == null || ticketId.trim().isEmpty() ||
            eventId == null || eventId.trim().isEmpty() ||
            userId == null || userId.trim().isEmpty()) {
            req.setAttribute("errorMessage", "必須項目が入力されていません。");
            req.getRequestDispatcher("/eventportal/host/create_ticket.jsp").forward(req, res);
            return;
        }

        try {
            // ステータスのパース
            int status = 1; // デフォルトは有効
            if (statusStr != null && !statusStr.isEmpty()) {
                status = Integer.parseInt(statusStr);
            }

            // Ticketオブジェクトの作成
            Ticket ticket = new Ticket();
            ticket.setTicketId(ticketId.trim());
            ticket.setEventId(eventId.trim());
            ticket.setUserId(userId.trim());
            ticket.setParticipantName(participantName != null ? participantName.trim() : "");
            ticket.setStatus(status);

            // QRコード生成
            String qrContent = ticketId.trim();
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 300, 300);
            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            // Base64エンコード
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "PNG", baos);
            byte[] imageBytes = baos.toByteArray();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            ticket.setQrImageData(base64Image);

            // データベースに登録
            TicketDao ticketDao = new TicketDao();
            int count = ticketDao.insert(ticket);

            if (count > 0) {
                System.out.println("チケット作成成功");
                req.setAttribute("successMessage", "チケットを作成しました。");
                res.sendRedirect(req.getContextPath() + "/eventportal/host/TicketList.action?eventId=" + eventId);
            } else {
                System.out.println("チケット作成失敗");
                req.setAttribute("errorMessage", "チケットの作成に失敗しました。");
                req.getRequestDispatcher("/eventportal/host/create_ticket.jsp").forward(req, res);
            }

        } catch (Exception e) {
            System.err.println("チケット作成エラー: " + e.getMessage());
            e.printStackTrace();
            req.setAttribute("errorMessage", "エラーが発生しました: " + e.getMessage());
            req.getRequestDispatcher("/eventportal/host/create_ticket.jsp").forward(req, res);
        }
    }
}