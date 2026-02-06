package eventportal.entrymenu;

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

import bean.Event;
import bean.Ticket;
import bean.User;
import dao.EventDao;
import dao.TicketDao;
import tool.Action;

/**
 * QRコード表示アクション（自動生成機能付き）
 */
public class ShowQRCodeAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        System.out.println("QRコード表示処理開始");

        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        if (user == null) {
            System.out.println("未ログイン：ログイン画面にリダイレクト");
            res.sendRedirect(req.getContextPath() + "/eventportal/auth/EntryLogin.action");
            return;
        }

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
            EventDao eventDao = new EventDao();
            Event event = eventDao.get(eventId);

            if (event == null) {
                System.out.println("エラー：イベントが見つかりません");
                req.setAttribute("errorMessage", "指定されたイベントが見つかりません。");
                req.getRequestDispatcher("/error.jsp").forward(req, res);
                return;
            }

            System.out.println("イベント名: " + event.getEventName());

            TicketDao ticketDao = new TicketDao();
            Ticket ticket = ticketDao.getByEventAndUser(eventId, user.getUser_id());

            if (ticket == null) {
                System.out.println("エラー：チケットが見つかりません");
                req.setAttribute("errorMessage", "このイベントのチケットが見つかりません。");
                req.getRequestDispatcher("/error.jsp").forward(req, res);
                return;
            }

            System.out.println("チケットID: " + ticket.getTicketId());

            // QRコードが未生成の場合は自動生成
            if (ticket.getQrImageData() == null || ticket.getQrImageData().isEmpty()) {
                System.out.println("QRコード未生成 → 自動生成開始");

                try {
                    String qrContent = ticket.getTicketId();

                    QRCodeWriter qrCodeWriter = new QRCodeWriter();
                    BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 300, 300);
                    BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(bufferedImage, "PNG", baos);
                    byte[] imageBytes = baos.toByteArray();
                    String base64Image = Base64.getEncoder().encodeToString(imageBytes);

                    ticketDao.updateQRImage(ticket.getTicketId(), base64Image);
                    ticket.setQrImageData(base64Image);

                    System.out.println("QRコード生成完了！");

                } catch (Exception e) {
                    System.err.println("QRコード生成エラー: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("QRコード有無: あり");
            }

            req.setAttribute("event", event);
            req.setAttribute("ticket", ticket);

            req.getRequestDispatcher("/eventportal/qr/showQRCode.jsp").forward(req, res);

        } catch (Exception e) {
            System.err.println("QRコード表示エラー: " + e.getMessage());
            e.printStackTrace();
            req.setAttribute("errorMessage", "エラーが発生しました: " + e.getMessage());
            req.getRequestDispatcher("/error.jsp").forward(req, res);
        }
    }
}