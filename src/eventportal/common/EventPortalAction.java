package eventportal.common;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.User;

/**
 * イベントポータル共通アクション基底クラス
 */
public abstract class EventPortalAction {

    /**
     * アクション実行メソッド
     * @param request HTTPリクエスト
     * @param response HTTPレスポンス
     * @return フォワード先のJSPパス
     * @throws ServletException
     * @throws IOException
     */
    public abstract String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;

    /**
     * セッションからユーザー情報を取得
     * @param request HTTPリクエスト
     * @return ユーザー情報（未ログインの場合はnull）
     */
    protected User getSessionUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return (User) session.getAttribute("user");
    }

    /**
     * ログインチェック
     * @param request HTTPリクエスト
     * @return ログイン済みの場合はtrue
     */
    protected boolean isLoggedIn(HttpServletRequest request) {
        User user = getSessionUser(request);
        return user != null && user.isAuth();
    }

    /**
     * ユーザータイプチェック
     * @param request HTTPリクエスト
     * @param requiredType 必要なユーザータイプ（1:参加者, 2:主催者）
     * @return 指定されたユーザータイプの場合はtrue
     */
    protected boolean checkUserType(HttpServletRequest request, int requiredType) {
        User user = getSessionUser(request);
        if (user == null) {
            return false;
        }
        return user.getUser_type() == requiredType;
    }

    /**
     * エラーメッセージを設定
     * @param request HTTPリクエスト
     * @param message エラーメッセージ
     */
    protected void setErrorMessage(HttpServletRequest request, String message) {
        request.setAttribute("errorMessage", message);
    }

    /**
     * 成功メッセージを設定
     * @param request HTTPリクエスト
     * @param message 成功メッセージ
     */
    protected void setSuccessMessage(HttpServletRequest request, String message) {
        request.setAttribute("successMessage", message);
    }

    /**
     * セッションにメッセージを設定（リダイレクト後も保持）
     * @param request HTTPリクエスト
     * @param key メッセージキー
     * @param message メッセージ
     */
    protected void setSessionMessage(HttpServletRequest request, String key, String message) {
        HttpSession session = request.getSession();
        session.setAttribute(key, message);
    }

    /**
     * セッションからメッセージを取得して削除
     * @param request HTTPリクエスト
     * @param key メッセージキー
     * @return メッセージ（存在しない場合はnull）
     */
    protected String getAndRemoveSessionMessage(HttpServletRequest request, String key) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        String message = (String) session.getAttribute(key);
        if (message != null) {
            session.removeAttribute(key);
        }
        return message;
    }
}