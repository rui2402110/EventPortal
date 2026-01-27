package eventportal.host.hostdetail;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tool.Action;

public class EventHoldAction extends Action{
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// jspから送られてきたデータを取得
		String eventHoldState = req.getParameter("event_hold_state");
		String eventId = req.getParameter("eventId");
		// 変数を定義
		String massage1 = "" ;
		String massage2 = "" ;
		switch (eventHoldState){
		case "1":
			massage1 = "開催前";
			massage2 = "イベントを開催しますか？";
			break;
		case "2":
			massage1 = "開催中";
			massage2 = "イベント終了は管理画面のイベント終了ボタンから行ってください。";
			break;
		case "3":
			massage1 = "開催後";
			massage2 = "新しいイベントを作って開催してください。";
			break;
		default:
			massage1 = "不明な状態";
			massage2 = "お手数ですが新しいイベントを開催してください。";
			break;
		}
		// JSPに送るデータをセット
		req.setAttribute("event_hold_state", eventHoldState);
		req.setAttribute("eventId", eventId);
		req.setAttribute("massage1", massage1);
		req.setAttribute("massage2", massage2);
		// 画面遷移のとき何書いて画面遷移してたか忘れた
		req.getRequestDispatcher("/eventportal/host/event_hold.jsp").forward(req, res);
	}
}
