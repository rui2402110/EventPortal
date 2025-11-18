package eventportal.auth;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.User;
import dao.UserDao;
import tool.Action;

public class HostSigninExecuteAction extends Action {
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

        // userデータを検索し、取得(主催者のみなので引数に2を選択)
        user = userDao.signin(id, user_name, mail_address, password, phone_number, 2);

        if (user != null){
            System.out.println("認証成功");
            // セッション情報を取得
            HttpSession session = req.getSession(true);
            // セッションにログイン情報を保存
            session.setAttribute("user", user);

            //リダイレクト
            url = "../host/HostMenu.action";
            res.sendRedirect(url);

        } else {
            // 認証失敗の場合
            // エラーメッセージをセット
            List<String> errors = new ArrayList<>();
            errors.add("登録に失敗しました。IDが既に使用されている可能性があります");
            req.setAttribute("errors", errors);
            // 入力されたユーザーIDをセット
            req.setAttribute("user_id", id);

            //フォワード
            url = "/eventportal/auth/host_signin.jsp";
            req.getRequestDispatcher(url).forward(req, res);
        }
    }
}
