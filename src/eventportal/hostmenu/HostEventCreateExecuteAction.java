package eventportal.hostmenu;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import bean.Event;
import bean.User;
import dao.FileDao;
import dao.HostEventDao;
import tool.Action;

public class HostEventCreateExecuteAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		System.out.println("イベント作成実行開始");
		//メソッドとスタブ
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute("user");
		Event event = null ;

		// 使用するDAOを定義
		HostEventDao hosEvtDao = new HostEventDao();
		FileDao fileDao = new FileDao();

            // イベント基本情報の取得
            String eventName = req.getParameter("event_name");
            String content = req.getParameter("content");

            // 日時情報の取得
            String year = req.getParameter("event_year");
            String month = req.getParameter("event_month");
            String day = req.getParameter("event_day");
            String hour = req.getParameter("event_hour");
            String minute = req.getParameter("event_minute");

         // LocalDateとLocalTimeに変換
            LocalDate holdingDate = LocalDate.of(
                Integer.parseInt(year),
                Integer.parseInt(month),
                Integer.parseInt(day)
            );

            LocalTime holdingTime = LocalTime.of(
                Integer.parseInt(hour),
                Integer.parseInt(minute)
            );

            // 住所情報の取得
            String postalCode = req.getParameter("postalCode");
            String prefecture = req.getParameter("prefecture");
            String city = req.getParameter("city");
            String street = req.getParameter("street");
            String building = req.getParameter("building");

            // その他の情報
            String maxCount = req.getParameter("maxcount");
            String category = req.getParameter("category");
            String phoneNumber = req.getParameter("phonenumber");
            String link = req.getParameter("link");
            String credit = req.getParameter("credit");

         // 画像ファイルの取得と保存
            Part mapImagePart = req.getPart("eventMapImage");
            Part innerMapImagePart = req.getPart("eventInnerMapImage");

            String mapImagePath = null;
            String innerMapImagePath = null;

            // 会場マップ画像の処理
            if (mapImagePart != null && mapImagePart.getSize() > 0) {
                String mapFileName = fileDao.getFileName(mapImagePart);
                mapImagePath = fileDao.saveUploadedFile(mapImagePart, mapFileName, req);
            }

            // 会場内マップ画像の処理
            if (innerMapImagePart != null && innerMapImagePart.getSize() > 0) {
                String innerMapFileName = fileDao.getFileName(innerMapImagePart);
                innerMapImagePath = fileDao.saveUploadedFile(innerMapImagePart, innerMapFileName, req);
            }

            LocalDate today = LocalDate.now();

            // セッターでEventクラスにデータを纏めていく
            event.setEventId(hosEvtDao.eventIdGet());
            event.setEventName(eventName);
            event.setEventOverview(content);
            event.setAddress(postalCode + prefecture + city + street + building);
            event.setUserId(user.getUser_id());
            event.setMapOutOfHall(mapImagePath);
            event.setMapInHall(innerMapImagePath);
            event.setEventAddDate(today);
            event.setEventHoldState("1");
            event.setHoldingDate(holdingDate);
            event.setHoldingTime(holdingTime);
            event.setLink(link);
            event.setMaxCount(Integer.parseInt(maxCount));
            event.setPhoneNumber(phoneNumber);

            // チケットのシステムを作るまで一時的に適当に保存しておく
            event.setTicketInfo(mapImagePath);

            event.setCredit(credit);

            // INSERT処理を実行
            hosEvtDao.eventCreate(event);

            // イベント作成後、作成完了ページに飛ぶ
            req.getRequestDispatcher("event_create_done.jsp").forward(req, res);


	}

}
