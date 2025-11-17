package eventportal.auth;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.User;
import dao.UserDao;
import tool.Action;

public class EntrySigninExecuteAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		//ローカル変数の宣言
		String url = "";
		String id = "";
		String user_name = "";
		String password = "";
		String mail_address = "";
		String phone_number = "" ;
		boolean signin_judge = false ;

		// Daoを再定義
		UserDao UserDao = new UserDao();
		// 変数 userを宣言
		User user = null;

		//リクエストパラメータ―の取得
		id = req.getParameter("id");// 参加者ID
		user_name = req.getParameter("user_name");// 参加者名
		password = req.getParameter("password");//パスワード
		mail_address = req.getParameter("mail_address");//めあど
		phone_number = req.getParameter("phone_number");//でんば

		// userデータを検索し、取得(参加者のみなので引数に1を選択)
		signin_judge = UserDao.signin(id, user_name, mail_address, password, phone_number, 1);

		if (signin_judge == true){
			System.out.println("認証成功");
			// セッション情報を取得
			HttpSession session = req.getSession(true);
			// セッションにログイン情報を保存
			session.setAttribute("user", user);

			//リダイレクト
			url = "../entrymenu/EntryMenu.action";
			res.sendRedirect(url);

		} else {
			// 認証失敗の場合
			// エラーメッセージをセット
			List<String> errors = new ArrayList<>();
			errors.add("IDまたはパスワードが確認できませんでした");
			req.setAttribute("errors", errors);
			// 入力されたユーザーIDをセット
			req.setAttribute("user_id", id);

			//フォワード
			url = "/eventportal/auth/entry_signin.jsp";
			req.getRequestDispatcher(url).forward(req, res);
		}

	}
}
