package eventportal;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tool.Action;
public class AuthPageAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		System.out.println("認証選択画面表示");
        // サインイン画面を表示するだけで、特別なロジックは不要
		req.getRequestDispatcher("/eventportal/auth/auth_01.jsp").forward(req, res);
        // auth_01.jspへフォワードする

	}
}
