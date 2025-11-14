package eventportal.entrymenu;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tool.Action;

public class EntryMenuAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		System.out.println("参加者メニュー画面表示");
        // サインイン画面を表示するだけで、特別なロジックは不要
		req.getRequestDispatcher("/eventportal/entry/entry_menu.jsp").forward(req, res);
        // auth_03.jspへフォワードする

	}


}
