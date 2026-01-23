package eventportal.auth;

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
        String phone_number = "";

        // Daoを再定義
        UserDao userDao = new UserDao();
        User user = null;

        //リクエストパラメータ―の取得
        id = req.getParameter("id");
        user_name = req.getParameter("user_name");
        password = req.getParameter("password");
        mail_address = req.getParameter("mail_address");
        phone_number = req.getParameter("phone_number");

        // 入力チェック：必須項目が空の場合
        if (id == null || id.trim().isEmpty() ||
            user_name == null || user_name.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            mail_address == null || mail_address.trim().isEmpty() ||
            phone_number == null || phone_number.trim().isEmpty()) {

            // エラー1: 入力されていません
            req.setAttribute("error1", true);
            req.setAttribute("id", id);
            req.setAttribute("user_name", user_name);
            req.setAttribute("mail_address", mail_address);
            req.setAttribute("phone_number", phone_number);

            //フォワード
            url = "/eventportal/auth/entry_signin.jsp";
            req.getRequestDispatcher(url).forward(req, res);
            return;
        }

        // userデータを検索し、取得(参加者のみなので引数に1を選択)
        user = userDao.signin(id, user_name, mail_address, password, phone_number, 1);

        if (user != null){
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
            // エラー2: IDかパスワードが間違っています
            req.setAttribute("error2", true);
            req.setAttribute("id", id);
            req.setAttribute("user_name", user_name);
            req.setAttribute("mail_address", mail_address);
            req.setAttribute("phone_number", phone_number);

            //フォワード
            url = "/eventportal/auth/entry_signin.jsp";
            req.getRequestDispatcher(url).forward(req, res);
        }
    }
}