package eventportal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tool.Action;

public class LoginPageAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		System.out.println("ログイン選択画面表示");
        // ログイン画面を表示するだけで、特別なロジックは不要
		req.getRequestDispatcher("/eventportal/auth/auth_11.jsp").forward(req, res);
        // このパスをFront Controllerが受け取り、auth_11.jspへフォワードする
    }

}
