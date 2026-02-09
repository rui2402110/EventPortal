package eventportal.host;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Event;
import bean.User;
import dao.Dao;
import dao.EventDao;
import tool.Action;
public class HostEventCreateExecuteAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        System.out.println("=== イベント作成処理開始 ===");

        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        if (user == null || user.getUser_type() != 2) {
            System.out.println("エラー：未ログインまたは権限なし");
            res.sendRedirect(req.getContextPath() + "/eventportal/auth/HostLogin.action");
            return;
        }

        try {
            // パラメータ取得
            String eventName = req.getParameter("eventName");
            String holdingDate = req.getParameter("holdingDate");
            String holdingTime = req.getParameter("holdingTime");
            String address = req.getParameter("address");
            String maxCountStr = req.getParameter("maxCount");
            String eventHoldState = req.getParameter("eventHoldState");
            String phoneNumber = req.getParameter("phoneNumber");
            String link = req.getParameter("link");
            String eventOverview = req.getParameter("eventOverview");
            String categoryId = req.getParameter("categoryId");
            String mapInHall = req.getParameter("mapInHall");
            String mapOutOfHall = req.getParameter("mapOutOfHall");
            String ticketInfo = req.getParameter("ticketInfo");

            System.out.println("イベント名: " + eventName);
            System.out.println("主催者ID: " + user.getUser_id());

            // バリデーション
            if (eventName == null || eventName.trim().isEmpty() ||
                holdingDate == null || holdingDate.trim().isEmpty() ||
                holdingTime == null || holdingTime.trim().isEmpty() ||
                address == null || address.trim().isEmpty() ||
                maxCountStr == null || maxCountStr.trim().isEmpty() ||
                eventOverview == null || eventOverview.trim().isEmpty()) {

                System.out.println("エラー：必須項目が入力されていません");
                req.setAttribute("errorMessage", "必須項目が入力されていません。");
                req.getRequestDispatcher("/eventportal/host/host_event_create.jsp").forward(req, res);
                return;
            }

            int maxCount = Integer.parseInt(maxCountStr);

            if (maxCount < 1 || maxCount > 10000) {
                req.setAttribute("errorMessage", "定員は1〜10000人の範囲で入力してください。");
                req.getRequestDispatcher("/eventportal/host/host_event_create.jsp").forward(req, res);
                return;
            }

            // イベントID自動生成
            String eventId = generateEventId();
            System.out.println("生成されたイベントID: " + eventId);

            // Eventオブジェクト作成
            Event event = new Event();
            event.setEventId(eventId);
            event.setEventName(eventName.trim());
            event.setHoldingDate(holdingDate);
            event.setHoldingTime(holdingTime);
            event.setAddress(address.trim());
            event.setMaxCount(maxCount);
            event.setEventHoldState(eventHoldState != null ? eventHoldState : "1");
            event.setPhoneNumber(phoneNumber != null ? phoneNumber.trim() : null);
            event.setLink(link != null ? link.trim() : null);
            event.setEventOverview(eventOverview.trim());
            event.setHostId(user.getUser_id()); // ★重要：主催者IDを設定
            event.setCategoryId(categoryId != null ? categoryId.trim() : null);
            event.setMapInHall(mapInHall != null ? mapInHall.trim() : null);
            event.setMapOutOfHall(mapOutOfHall != null ? mapOutOfHall.trim() : null);
            event.setTicketInfo(ticketInfo != null ? ticketInfo.trim() : null);
            event.setUserId(user.getUser_id());
            event.setTotalPayment(0);

            // データベース登録
            EventDao eventDao = new EventDao();
            int count = eventDao.save(event);

            if (count > 0) {
                System.out.println("✓ イベント作成成功！");
                session.setAttribute("successMessage", "イベントを作成しました。");
                res.sendRedirect(req.getContextPath() + "/eventportal/host/HostMenu.action");
            } else {
                System.out.println("✗ イベント作成失敗");
                req.setAttribute("errorMessage", "イベントの作成に失敗しました。");
                req.setAttribute("event", event);
                req.getRequestDispatcher("/eventportal/host/host_event_create.jsp").forward(req, res);
            }

        } catch (NumberFormatException e) {
            System.err.println("数値変換エラー: " + e.getMessage());
            req.setAttribute("errorMessage", "定員には数値を入力してください。");
            req.getRequestDispatcher("/eventportal/host/host_event_create.jsp").forward(req, res);
        } catch (Exception e) {
            System.err.println("イベント作成エラー: " + e.getMessage());
            e.printStackTrace();
            req.setAttribute("errorMessage", "エラーが発生しました: " + e.getMessage());
            req.getRequestDispatcher("/eventportal/host/host_event_create.jsp").forward(req, res);
        }
    }

    /**
     * イベントID自動生成
     * @return 新しいイベントID
     * @throws Exception
     */
    private String generateEventId() throws Exception {
        Dao dao = new Dao();
        Connection connection = dao.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String sql = "SELECT event_id FROM EVENTS ORDER BY event_id DESC LIMIT 1";
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();

            int nextNumber = 1;

            if (resultSet.next()) {
                String lastEventId = resultSet.getString("event_id");
                // EVT001 → 001 → 1 → 2 → 002 → EVT002
                String numberPart = lastEventId.substring(3);
                nextNumber = Integer.parseInt(numberPart) + 1;
            }

            return String.format("EVT%03d", nextNumber);

        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    }
}