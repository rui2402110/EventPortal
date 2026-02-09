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

/**
 * イベント作成実行アクション（超詳細ログ版）
 */
public class HostEventCreateExecuteAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        System.out.println("\n\n");
        System.out.println("████████████████████████████████████████████████████████████████");
        System.out.println("███                                                          ███");
        System.out.println("███          イベント作成処理開始                            ███");
        System.out.println("███                                                          ███");
        System.out.println("████████████████████████████████████████████████████████████████");
        System.out.println();

        HttpSession session = req.getSession(false);

        System.out.println("【STEP 1】セッション取得");
        if (session == null) {
            System.out.println("  ✗ セッションがnull");
            res.sendRedirect(req.getContextPath() + "/eventportal/auth/HostLogin.action");
            return;
        }
        System.out.println("  ✓ セッション取得成功");

        User user = (User) session.getAttribute("user");

        System.out.println("\n【STEP 2】ユーザー認証");
        if (user == null) {
            System.out.println("  ✗ ユーザーがnull（未ログイン）");
            res.sendRedirect(req.getContextPath() + "/eventportal/auth/HostLogin.action");
            return;
        }
        System.out.println("  ✓ ユーザー取得成功");
        System.out.println("    - ユーザーID: " + user.getUser_id());
        System.out.println("    - ユーザー名: " + user.getUser_name());
        System.out.println("    - ユーザー種別: " + user.getUser_type());

        if (user.getUser_type() != 2) {
            System.out.println("  ✗ ユーザー種別エラー（user_type=" + user.getUser_type() + "）");
            System.out.println("    主催者ではありません（2以外）");
            res.sendRedirect(req.getContextPath() + "/eventportal/auth/HostLogin.action");
            return;
        }
        System.out.println("  ✓ 主催者権限確認OK");

        try {
            System.out.println("\n【STEP 3】パラメータ取得");
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

            System.out.println("  受信パラメータ:");
            System.out.println("    - eventName: [" + eventName + "]");
            System.out.println("    - holdingDate: [" + holdingDate + "]");
            System.out.println("    - holdingTime: [" + holdingTime + "]");
            System.out.println("    - address: [" + address + "]");
            System.out.println("    - maxCount: [" + maxCountStr + "]");
            System.out.println("    - eventHoldState: [" + eventHoldState + "]");
            System.out.println("    - phoneNumber: [" + phoneNumber + "]");
            System.out.println("    - link: [" + link + "]");
            System.out.println("    - eventOverview: [" + (eventOverview != null ? eventOverview.substring(0, Math.min(50, eventOverview.length())) + "..." : "null") + "]");
            System.out.println("    - categoryId: [" + categoryId + "]");
            System.out.println("    - mapInHall: [" + mapInHall + "]");
            System.out.println("    - mapOutOfHall: [" + mapOutOfHall + "]");
            System.out.println("    - ticketInfo: [" + ticketInfo + "]");

            System.out.println("\n【STEP 4】バリデーション");

            if (eventName == null || eventName.trim().isEmpty()) {
                System.out.println("  ✗ イベント名が空");
                req.setAttribute("errorMessage", "イベント名を入力してください。");
                req.getRequestDispatcher("/eventportal/host/host_event_create.jsp").forward(req, res);
                return;
            }
            System.out.println("  ✓ イベント名OK");

            if (holdingDate == null || holdingDate.trim().isEmpty()) {
                System.out.println("  ✗ 開催日が空");
                req.setAttribute("errorMessage", "開催日を入力してください。");
                req.getRequestDispatcher("/eventportal/host/host_event_create.jsp").forward(req, res);
                return;
            }
            System.out.println("  ✓ 開催日OK");

            if (holdingTime == null || holdingTime.trim().isEmpty()) {
                System.out.println("  ✗ 開催時刻が空");
                req.setAttribute("errorMessage", "開催時刻を入力してください。");
                req.getRequestDispatcher("/eventportal/host/host_event_create.jsp").forward(req, res);
                return;
            }
            System.out.println("  ✓ 開催時刻OK");

            if (address == null || address.trim().isEmpty()) {
                System.out.println("  ✗ 住所が空");
                req.setAttribute("errorMessage", "開催場所を入力してください。");
                req.getRequestDispatcher("/eventportal/host/host_event_create.jsp").forward(req, res);
                return;
            }
            System.out.println("  ✓ 開催場所OK");

            if (maxCountStr == null || maxCountStr.trim().isEmpty()) {
                System.out.println("  ✗ 定員が空");
                req.setAttribute("errorMessage", "定員を入力してください。");
                req.getRequestDispatcher("/eventportal/host/host_event_create.jsp").forward(req, res);
                return;
            }
            System.out.println("  ✓ 定員入力OK");

            if (eventOverview == null || eventOverview.trim().isEmpty()) {
                System.out.println("  ✗ イベント概要が空");
                req.setAttribute("errorMessage", "イベント概要を入力してください。");
                req.getRequestDispatcher("/eventportal/host/host_event_create.jsp").forward(req, res);
                return;
            }
            System.out.println("  ✓ イベント概要OK");

            System.out.println("  ✓ 全項目バリデーションOK");

            System.out.println("\n【STEP 5】定員の数値変換");
            int maxCount = 0;
            try {
                maxCount = Integer.parseInt(maxCountStr);
                System.out.println("  ✓ 定員変換成功: " + maxCount);
            } catch (NumberFormatException e) {
                System.out.println("  ✗ 定員変換エラー: " + e.getMessage());
                req.setAttribute("errorMessage", "定員には数値を入力してください。");
                req.getRequestDispatcher("/eventportal/host/host_event_create.jsp").forward(req, res);
                return;
            }

            if (maxCount < 1 || maxCount > 10000) {
                System.out.println("  ✗ 定員範囲エラー: " + maxCount);
                req.setAttribute("errorMessage", "定員は1〜10000人の範囲で入力してください。");
                req.getRequestDispatcher("/eventportal/host/host_event_create.jsp").forward(req, res);
                return;
            }
            System.out.println("  ✓ 定員範囲チェックOK");

            System.out.println("\n【STEP 6】イベントID自動生成");
            String eventId = generateEventId();
            System.out.println("  ✓ イベントID生成成功: " + eventId);

            System.out.println("\n【STEP 7】Eventオブジェクト作成");
            Event event = new Event();
            event.setEventId(eventId);
            event.setEventName(eventName.trim());
            event.setHoldingDate(holdingDate);
            event.setHoldingTime(holdingTime);
            event.setAddress(address.trim());
            event.setMaxCount(maxCount);
            event.setEventHoldState(eventHoldState != null && !eventHoldState.isEmpty() ? eventHoldState : "1");
            event.setPhoneNumber(phoneNumber != null && !phoneNumber.trim().isEmpty() ? phoneNumber.trim() : null);
            event.setLink(link != null && !link.trim().isEmpty() ? link.trim() : null);
            event.setEventOverview(eventOverview.trim());
            event.setHostId(user.getUser_id());
            event.setCategoryId(categoryId != null && !categoryId.trim().isEmpty() ? categoryId.trim() : null);
            event.setMapInHall(mapInHall != null && !mapInHall.trim().isEmpty() ? mapInHall.trim() : null);
            event.setMapOutOfHall(mapOutOfHall != null && !mapOutOfHall.trim().isEmpty() ? mapOutOfHall.trim() : null);
            event.setTicketInfo(ticketInfo != null && !ticketInfo.trim().isEmpty() ? ticketInfo.trim() : null);
            event.setUserId(user.getUser_id());
            event.setTotalPayment(0);

            System.out.println("  ✓ Eventオブジェクト作成完了");
            System.out.println("    - EventID: " + event.getEventId());
            System.out.println("    - EventName: " + event.getEventName());
            System.out.println("    - HoldingDate: " + event.getHoldingDate());
            System.out.println("    - HoldingTime: " + event.getHoldingTime());
            System.out.println("    - Address: " + event.getAddress());
            System.out.println("    - MaxCount: " + event.getMaxCount());
            System.out.println("    - EventHoldState: " + event.getEventHoldState());
            System.out.println("    - HostId: " + event.getHostId());
            System.out.println("    - EventOverview: " + event.getEventOverview().substring(0, Math.min(50, event.getEventOverview().length())) + "...");

            System.out.println("\n【STEP 8】データベース登録");
            EventDao eventDao = new EventDao();
            System.out.println("  ✓ EventDao作成完了");

            System.out.println("  → save()メソッド呼び出し中...");
            int count = eventDao.save(event);
            System.out.println("  ← save()メソッド完了");

            System.out.println("  登録結果: " + count + "件");

            if (count > 0) {
                System.out.println("\n████████████████████████████████████████████████████████████████");
                System.out.println("███                                                          ███");
                System.out.println("███          ✓✓✓ イベント作成成功！ ✓✓✓                  ███");
                System.out.println("███                                                          ███");
                System.out.println("████████████████████████████████████████████████████████████████");
                System.out.println();

                session.setAttribute("successMessage", "イベント「" + event.getEventName() + "」を作成しました。");
                res.sendRedirect(req.getContextPath() + "/eventportal/host/HostMenu.action");
            } else {
                System.out.println("\n████████████████████████████████████████████████████████████████");
                System.out.println("███                                                          ███");
                System.out.println("███          ✗✗✗ イベント作成失敗（登録0件） ✗✗✗        ███");
                System.out.println("███                                                          ███");
                System.out.println("████████████████████████████████████████████████████████████████");
                System.out.println();

                req.setAttribute("errorMessage", "イベントの作成に失敗しました。");
                req.setAttribute("event", event);
                req.getRequestDispatcher("/eventportal/host/host_event_create.jsp").forward(req, res);
            }

        } catch (NumberFormatException e) {
            System.err.println("\n✗✗✗ 数値変換エラー ✗✗✗");
            System.err.println("エラー内容: " + e.getMessage());
            e.printStackTrace();
            req.setAttribute("errorMessage", "定員には数値を入力してください。");
            req.getRequestDispatcher("/eventportal/host/host_event_create.jsp").forward(req, res);
        } catch (Exception e) {
            System.err.println("\n████████████████████████████████████████████████████████████████");
            System.err.println("███                                                          ███");
            System.err.println("███          ✗✗✗ イベント作成エラー ✗✗✗                  ███");
            System.err.println("███                                                          ███");
            System.err.println("████████████████████████████████████████████████████████████████");
            System.err.println();
            System.err.println("エラークラス: " + e.getClass().getName());
            System.err.println("エラーメッセージ: " + e.getMessage());
            System.err.println("スタックトレース:");
            e.printStackTrace();
            System.err.println();

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
        System.out.println("  → イベントID生成処理開始");

        Dao dao = new Dao();
        Connection connection = dao.getConnection();
        System.out.println("    ✓ DB接続成功");

        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String sql = "SELECT event_id FROM EVENTS ORDER BY event_id DESC LIMIT 1";
            System.out.println("    SQL: " + sql);

            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();

            int nextNumber = 1;

            if (resultSet.next()) {
                String lastEventId = resultSet.getString("event_id");
                System.out.println("    最新イベントID: " + lastEventId);

                String numberPart = lastEventId.substring(3);
                nextNumber = Integer.parseInt(numberPart) + 1;

                System.out.println("    数値部分: " + numberPart);
                System.out.println("    次の番号: " + nextNumber);
            } else {
                System.out.println("    既存イベントなし、初回作成");
            }

            String newEventId = String.format("EVT%03d", nextNumber);
            System.out.println("    生成ID: " + newEventId);

            return newEventId;

        } finally {
            if (resultSet != null) {
                resultSet.close();
                System.out.println("    ✓ ResultSetクローズ");
            }
            if (statement != null) {
                statement.close();
                System.out.println("    ✓ Statementクローズ");
            }
            if (connection != null) {
                connection.close();
                System.out.println("    ✓ Connectionクローズ");
            }
        }
    }
}