package eventportal.host;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Event;
import bean.User;
import dao.EventDao;
import dao.TicketDao;
import tool.Action;

public class HostQRScannerAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        // セッションからユーザー情報を取得
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || user.getUser_type() != 2) {
            res.sendRedirect(req.getContextPath() + "/eventportal/auth/HostLogin.action");
            return;
        }

        // パラメータからイベントIDを取得
        String eventId = req.getParameter("eventId");

        if (eventId == null || eventId.isEmpty()) {
            req.setAttribute("error", "イベントIDが指定されていません");
            req.getRequestDispatcher("/eventportal/host/error.jsp").forward(req, res);
            return;
        }

        // DAOインスタンス作成
        EventDao eventDao = new EventDao();
        TicketDao ticketDao = new TicketDao();

        // イベント情報を取得
        Event event = eventDao.get(eventId);

        if (event == null) {
            req.setAttribute("error", "イベントが見つかりません");
            req.getRequestDispatcher("/eventportal/host/error.jsp").forward(req, res);
            return;
        }

        // イベントの主催者確認
        if (!event.getUserId().equals(user.getUser_id())) {
            req.setAttribute("error", "このイベントにアクセスする権限がありません");
            req.getRequestDispatcher("/eventportal/host/error.jsp").forward(req, res);
            return;
        }

        // 入場済み人数を取得
        int admittedCount = ticketDao.getAdmittedCount(eventId);

        // JSPにデータを渡す
        req.setAttribute("event", event);
        req.setAttribute("admittedCount", admittedCount);

        // JSPへフォワード
        req.getRequestDispatcher("/eventportal/host/host_qr_scanner.jsp").forward(req, res);
    }
}