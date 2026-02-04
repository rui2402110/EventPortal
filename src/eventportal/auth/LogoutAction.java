package eventportal.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tool.Action;

/**
 * ログアウト処理アクション
 */
public class LogoutAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		System.out.println("ログアウト処理開始");

		// セッションを取得（新規作成しない）
		HttpSession session = req.getSession(false);

		if (session != null) {
			// セッションを破棄
			session.invalidate();
			System.out.println("セッション破棄完了");
		}

		// 認証選択画面にリダイレクト
		String redirectUrl = req.getContextPath() + "/eventportal/auth/AuthPage.action";
		System.out.println("リダイレクト先: " + redirectUrl);
		res.sendRedirect(redirectUrl);
	}
}