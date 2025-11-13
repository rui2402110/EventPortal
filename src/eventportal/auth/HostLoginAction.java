package eventportal.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tool.Action;

public class HostLoginAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		System.out.println("主催者認証画面表示");
        // サインイン画面を表示するだけで、特別なロジックは不要
		req.getRequestDispatcher("/eventportal/auth/auth_03.jsp").forward(req, res);
        // auth_03.jspへフォワードする
	}

}
