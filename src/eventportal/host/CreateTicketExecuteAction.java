package eventportal.host;

import java.time.LocalDateTime;
import java.util.Map;

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

            // バリデーション
            if (eventId == null || eventId.isEmpty() || userId == null || userId.isEmpty()) {
                req.setAttribute("error", "イベントとユーザーを選択してください");
                req.getRequestDispatcher("/eventportal/host/CreateTicket.action").forward(req, res);
                return;
            }

            EventDao eventDao = new EventDao();
            TicketDao ticketDao = new TicketDao();
            UserDaoEx userDaoEx = new UserDaoEx();

            // イベント確認
            Event event = eventDao.get(eventId);
            if (event == null) {
                req.setAttribute("error", "イベントが見つかりません");
                req.getRequestDispatcher("/eventportal/host/CreateTicket.action").forward(req, res);
                return;
            }

            // 権限確認
            if (!event.getUserId().equals(user.getUser_id())) {
                req.setAttribute("error", "このイベントにアクセスする権限がありません");
                req.getRequestDispatcher("/eventportal/host/CreateTicket.action").forward(req, res);
                return;
            }

            // ユーザー確認
            User entryUser = userDaoEx.get(userId);
            if (entryUser == null || entryUser.getUser_type() != 1) {
                req.setAttribute("error", "有効な参加者を選択してください");
                req.getRequestDispatcher("/eventportal/host/CreateTicket.action").forward(req, res);
                return;
            }

            // 重複チェック
            Ticket existingTicket = ticketDao.getByEventAndUser(eventId, userId);
            if (existingTicket != null) {
                req.setAttribute("error", "このユーザーは既にこのイベントのチケットを持っています");
                req.setAttribute("existingTicket", existingTicket);
                req.getRequestDispatcher("/eventportal/host/CreateTicket.action").forward(req, res);
                return;
            }

            // チケットIDを生成
            String ticketId = ticketDao.generateTicketId();

            // QRコードを生成（Base64とファイルの両方）
            String qrOutputDir = req.getServletContext().getRealPath("/qr");
            Map<String, String> qrResult = QRCodeGenerator.generateTicketQRCodeComplete(ticketId, qrOutputDir);

            if (qrResult == null) {
                req.setAttribute("error", "QRコードの生成に失敗しました");
                req.getRequestDispatcher("/eventportal/host/CreateTicket.action").forward(req, res);
                return;
            }

            // チケットオブジェクトを作成
            Ticket ticket = new Ticket();
            ticket.setTicketId(ticketId);
            ticket.setUserId(userId);
            ticket.setEventId(eventId);
            ticket.setQrImagePath(qrResult.get("filePath")); // ファイルパス
            ticket.setQrImageData(qrResult.get("base64"));   // Base64データ
            ticket.setStatus(1); // 有効
            ticket.setTicketInfo(ticketInfo != null ? ticketInfo : "");
            ticket.setCreatedAt(LocalDateTime.now());

            // データベースに保存
            boolean created = ticketDao.create(ticket);

            if (created) {
                System.out.println("チケット作成成功: " + ticketId);
                System.out.println("- QRファイルパス: " + qrResult.get("filePath"));
                System.out.println("- QRBase64データ: " + (qrResult.get("base64") != null ? "あり" : "なし"));

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