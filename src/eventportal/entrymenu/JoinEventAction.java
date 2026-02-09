package eventportal.entrymenu;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import dao.Dao;
import dao.TicketDao;
import tool.Action;

/**
 * イベント参加登録アクション
 */
public class JoinEventAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        System.out.println("=== イベント参加登録処理開始 ===");

        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        if (user == null) {
            System.out.println("未ログイン：ログイン画面にリダイレクト");
            res.sendRedirect(req.getContextPath() + "/eventportal/auth/EntryLogin.action");
            return;
        }

        String eventId = req.getParameter("eventId");
        if (eventId == null || eventId.isEmpty()) {
            req.setAttribute("errorMessage", "イベントIDが指定されていません。");
            req.getRequestDispatcher("/error.jsp").forward(req, res);
            return;
        }

        System.out.println("イベントID: " + eventId);
        System.out.println("ユーザーID: " + user.getUser_id());

        try {
            TicketDao ticketDao = new TicketDao();

            Ticket existingTicket = ticketDao.getByEventAndUser(eventId, user.getUser_id());
            if (existingTicket != null) {
                System.out.println("既に参加済みです");
                res.sendRedirect(req.getContextPath() + "/eventportal/entrymenu/EntryEventDetail.action?eventId=" + eventId);
                return;
            }

            String ticketId = generateTicketId();
            System.out.println("生成されたチケットID: " + ticketId);

            Ticket ticket = new Ticket();
            ticket.setTicketId(ticketId);
            ticket.setEventId(eventId);
            ticket.setUserId(user.getUser_id());
            ticket.setParticipantName(user.getUser_name() + "様");
            ticket.setStatus(1);

            System.out.println("QRコード生成開始...");
            try {
                QRCodeWriter qrCodeWriter = new QRCodeWriter();
                BitMatrix bitMatrix = qrCodeWriter.encode(ticketId, BarcodeFormat.QR_CODE, 300, 300);
                BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, "PNG", baos);
                byte[] imageBytes = baos.toByteArray();
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);

                ticket.setQrImageData(base64Image);
                System.out.println("QRコード生成成功！");

            } catch (Exception qrError) {
                System.err.println("QRコード生成エラー: " + qrError.getMessage());
            }

            int count = ticketDao.insert(ticket);

            if (count > 0) {
                System.out.println("チケット登録成功！");
                session.setAttribute("successMessage", "イベントへの参加登録が完了しました！");
                res.sendRedirect(req.getContextPath() + "/eventportal/entrymenu/EntryEventManage.action?joined=true");
            } else {
                System.out.println("チケット登録失敗");
                req.setAttribute("errorMessage", "参加登録に失敗しました。");
                req.getRequestDispatcher("/error.jsp").forward(req, res);
            }

        } catch (Exception e) {
            System.err.println("イベント参加登録エラー: " + e.getMessage());
            e.printStackTrace();
            req.setAttribute("errorMessage", "エラーが発生しました: " + e.getMessage());
            req.getRequestDispatcher("/error.jsp").forward(req, res);
        }
    }

    private String generateTicketId() throws Exception {
        Dao dao = new Dao();
        Connection connection = dao.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String sql = "SELECT ticket_id FROM TICKET ORDER BY ticket_id DESC LIMIT 1";
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();

            int nextNumber = 1;

            if (resultSet.next()) {
                String lastTicketId = resultSet.getString("ticket_id");
                String numberPart = lastTicketId.substring(3);
                nextNumber = Integer.parseInt(numberPart) + 1;
            }

            return String.format("TKT%05d", nextNumber);

        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    }
}