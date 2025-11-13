package eventportal.auth;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.User;
import dao.UserDao;
import tool.Action;

public class HostLoginExecuteAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		//ローカル変数の宣言
		String url = "";
		String id = "";
		String password = "";

		// Daoを再定義
		UserDao UserDao = new UserDao();
		// 変数 userを宣言
		User user = null;

		//リクエストパラメータ―の取得
		id = req.getParameter("id");// 参加者ID
		password = req.getParameter("password");//パスワード

		//userデータを検索し、取得(主催者のみなので引数に2を選択)
		user = UserDao.login(id, password, 2);

		if (user != null){
			System.out.println("認証成功");
			// セッション情報を取得
			HttpSession session = req.getSession(true);
			// 認証済みフラグをtrueに
			user.setAuth(true);
			// セッションにログイン情報を保存
			session.setAttribute("user", user);

			//リダイレクト
			url = "/eventportal/entrymenu/EntryMenu.Action";
			res.sendRedirect(url);

		} else {
			// 認証失敗の場合
			// エラーメッセージをセット
			List<String> errors = new ArrayList<>();
			errors.add("IDまたはパスワードが確認できませんでした");
			req.setAttribute("errors", errors);
			// 入力された教員IDをセット
			req.setAttribute("user_id", id);

			//フォワード
			url = "/eventportal/auth/auth_03.jsp";
			req.getRequestDispatcher(url).forward(req, res);
		}
	}

}
