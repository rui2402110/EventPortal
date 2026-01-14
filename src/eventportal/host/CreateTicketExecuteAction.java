package eventportal.host;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Event;
import bean.Ticket;
import bean.User;
import dao.EventDao;
import dao.TicketDao;
import dao.UserDaoEx;
import tool.Action;
import util.QRCodeGenerator;

/**
 * チケット作成実行アクション
 */
public class CreateTicketExecuteAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || user.getUser_type() != 2) {
            res.sendRedirect(req.getContextPath() + "/eventportal/auth/HostLogin.action");
            return;
        }

        try {
            String eventId = req.getParameter("eventId");
            String userId = req.getParameter("userId");
            String ticketInfo = req.getParameter("ticketInfo");

            if (eventId == null || eventId.isEmpty() ||
                userId == null || userId.isEmpty()) {
                req.setAttribute("error", "イベントとユーザーを選択してください");
                req.getRequestDispatcher("/eventportal/host/CreateTicket.action").forward(req, res);
                return;
            }

            EventDao eventDao = new EventDao();
            TicketDao ticketDao = new TicketDao();
            UserDaoEx userDaoEx = new UserDaoEx();

            Event event = eventDao.get(eventId);
            if (event == null) {
                req.setAttribute("error", "イベントが見つかりません");
                req.getRequestDispatcher("/eventportal/host/CreateTicket.action").forward(req, res);
                return;
            }

            if (!event.getUserId().equals(user.getUser_id())) {
                req.setAttribute("error", "このイベントにアクセスする権限がありません");
                req.getRequestDispatcher("/eventportal/host/CreateTicket.action").forward(req, res);
                return;
            }

            User entryUser = userDaoEx.get(userId);
            if (entryUser == null) {
                req.setAttribute("error", "ユーザーが見つかりません");
                req.getRequestDispatcher("/eventportal/host/CreateTicket.action").forward(req, res);
                return;
            }

            if (entryUser.getUser_type() != 1) {
                req.setAttribute("error", "選択されたユーザーは参加者ではありません");
                req.getRequestDispatcher("/eventportal/host/CreateTicket.action").forward(req, res);
                return;
            }

            Ticket existingTicket = ticketDao.getByEventAndUser(eventId, userId);
            if (existingTicket != null) {
                req.setAttribute("error", "このユーザーは既にこのイベントのチケットを持っています");
                req.setAttribute("existingTicket", existingTicket);
                req.getRequestDispatcher("/eventportal/host/CreateTicket.action").forward(req, res);
                return;
            }

            String ticketId = ticketDao.generateTicketId();
            String qrImageData = QRCodeGenerator.generateQRCodeBase64(ticketId);

            String qrImagePath = null;
            try {
                String outputPath = req.getServletContext().getRealPath("/qr");
                if (outputPath != null) {
                    qrImagePath = QRCodeGenerator.generateTicketQRCode(ticketId, outputPath);
                }
            } catch (Exception e) {
                System.err.println("QRコード画像ファイル保存エラー（Base64は保存済み）: " + e.getMessage());
            }

            Ticket ticket = new Ticket();
            ticket.setTicketId(ticketId);
            ticket.setUserId(userId);
            ticket.setEventId(eventId);
            ticket.setQrImagePath(qrImagePath);
            ticket.setQrImageData(qrImageData);
            ticket.setStatus(1);
            ticket.setTicketInfo(ticketInfo != null ? ticketInfo : "");
            ticket.setCreatedAt(LocalDateTime.now());

            boolean created = ticketDao.create(ticket);

            if (created) {
                System.out.println("チケット作成成功: " + ticketId);
                session.setAttribute("successMessage",
                    "チケットを発行しました（チケットID: " + ticketId + "）");
                res.sendRedirect(req.getContextPath() +
                    "/eventportal/host/TicketList.action?eventId=" + eventId);
            } else {
                req.setAttribute("error", "チケットの作成に失敗しました");
                req.getRequestDispatcher("/eventportal/host/CreateTicket.action").forward(req, res);
            }

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "エラーが発生しました: " + e.getMessage());
            req.getRequestDispatcher("/eventportal/host/CreateTicket.action").forward(req, res);
        }
    }
}