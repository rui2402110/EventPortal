package eventportal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tool.Action;
public class SigninPageAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		System.out.println("サインイン選択画面表示");
        // サインイン画面を表示するだけで、特別なロジックは不要
		req.getRequestDispatcher("/eventportal/auth/auth_08.jsp").forward(req, res);
        // このパスをFront Controllerが受け取り、auth_08.jspへフォワードする

	}
}
