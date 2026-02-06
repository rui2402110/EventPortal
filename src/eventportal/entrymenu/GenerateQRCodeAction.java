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

import bean.Ticket;
import bean.User;
import dao.TicketDao;
import tool.Action;

/**
 * QRコード生成アクション
 */
public class GenerateQRCodeAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/eventportal/auth/EntryLogin.action");
            return;
        }

        String eventId = req.getParameter("eventId");
        if (eventId == null || eventId.isEmpty()) {
            req.setAttribute("errorMessage", "イベントIDが指定されていません。");
            req.getRequestDispatcher("/error.jsp").forward(req, res);
            return;
        }

        try {
            TicketDao ticketDao = new TicketDao();
            Ticket ticket = ticketDao.getByEventAndUser(eventId, user.getUser_id());

            if (ticket == null) {
                req.setAttribute("errorMessage", "このイベントのチケットが見つかりません。");
                req.getRequestDispatcher("/error.jsp").forward(req, res);
                return;
            }

            // QRコードが未生成の場合は生成
            if (ticket.getQrImageData() == null || ticket.getQrImageData().isEmpty()) {
                String qrContent = ticket.getTicketId();

                // QRコード生成
                QRCodeWriter qrCodeWriter = new QRCodeWriter();
                BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 300, 300);
                BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

                // Base64エンコード
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, "PNG", baos);
                byte[] imageBytes = baos.toByteArray();
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);

                // データベースに保存
                ticket.setQrImageData(base64Image);
                ticketDao.updateQRImage(ticket.getTicketId(), base64Image);
            }

            // ShowQRCodeActionにリダイレクト
            res.sendRedirect(req.getContextPath() + "/eventportal/entrymenu/ShowQRCode.action?eventId=" + eventId);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", "QRコード生成中にエラーが発生しました: " + e.getMessage());
            req.getRequestDispatcher("/error.jsp").forward(req, res);
        }
    }
}