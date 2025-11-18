package eventportal.hostmenu;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tool.Action;

public class HostEventCreateAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		System.out.println("イベント登録画面表示");
        // 画面を表示するだけで、特別なロジックは不要
		req.getRequestDispatcher("/eventportal/host/host_event_create.jsp").forward(req, res);
        // host_event.jspへフォワードする
	}
}
