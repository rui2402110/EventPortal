package eventportal.host.hostdetail;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tool.Action;

public class EventEndAction extends Action{
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// jspから送られてきたデータを取得
		String eventHoldState = req.getParameter("event_hold_state");
		String eventId = req.getParameter("eventId");
		// 変数を定義
		String massage1 = "";
		String massage2 = "";
		switch (eventHoldState){
		case "1":
			massage1 = "開催前";
			massage2 = "イベントはまだ開催されていません。";
			break;
		case "2":
			massage1 = "開催中";
			massage2 = "イベントを終了しますか？";
			break;
		case "3":
			massage1 = "開催後";
			massage2 = "イベントは既に終了しています。";
			break;
		default:
			massage1 = "不明な状態";
			massage2 = "イベントの状態が確認できません。";
			break;
		}
		// JSPに送るデータをセット
		req.setAttribute("event_hold_state", eventHoldState);
		req.setAttribute("eventId", eventId);
		req.setAttribute("massage1", massage1);
		req.setAttribute("massage2", massage2);
		// 画面遷移のとき何書いて画面遷移してたか忘れた
		req.getRequestDispatcher("/eventportal/host/event_end.jsp").forward(req, res);
	}
}