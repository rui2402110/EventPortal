package eventportal.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tool.Action;

public class EntrySigninAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		System.out.println("参加者アカウント作成画面表示");
        // サインイン画面を表示するだけで、特別なロジックは不要
		req.getRequestDispatcher("/eventportal/auth/entry_signin.jsp").forward(req, res);
        // auth_02.jspへフォワードする
	}

}
