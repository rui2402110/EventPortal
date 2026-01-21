package tool;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.User;

/**
 * 認証フィルター
 * ログインが必要なページへのアクセスを制御
 */
@WebFilter(urlPatterns = {
    "/eventportal/entrymenu/*",
    "/eventportal/host/*",
    "/eventportal/entry/*"
})
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("AuthFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // セッションを取得（新規作成しない）
        HttpSession session = httpRequest.getSession(false);

        // ユーザー情報を取得
        User user = null;
        if (session != null) {
            user = (User) session.getAttribute("user");
        }

        // リクエストURIを取得
        String uri = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();

        // 認証が必要なパスかチェック
        boolean requiresAuth = uri.startsWith(contextPath + "/eventportal/entrymenu") ||
                               uri.startsWith(contextPath + "/eventportal/host") ||
                               uri.startsWith(contextPath + "/eventportal/entry");

        if (requiresAuth) {
            // ログインチェック
            if (user == null || !user.isAuth()) {
                // 未ログインの場合は認証画面へリダイレクト
                System.out.println("Unauthorized access to: " + uri);
                httpResponse.sendRedirect(contextPath + "/eventportal/auth/AuthPage.action");
                return;
            }

            // ユーザータイプのチェック
            if (uri.startsWith(contextPath + "/eventportal/entrymenu") ||
                uri.startsWith(contextPath + "/eventportal/entry")) {
                // 参加者用ページ
                if (user.getUser_type() != 1) {
                    System.out.println("Access denied (not entry user): " + uri);
                    httpResponse.sendRedirect(contextPath + "/eventportal/auth/AuthPage.action");
                    return;
                }
            } else if (uri.startsWith(contextPath + "/eventportal/host")) {
                // 主催者用ページ
                if (user.getUser_type() != 2) {
                    System.out.println("Access denied (not host user): " + uri);
                    httpResponse.sendRedirect(contextPath + "/eventportal/auth/AuthPage.action");
                    return;
                }
            }
        }

        // 認証OK、次のフィルターまたはサーブレットへ
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        System.out.println("AuthFilter destroyed");
    }
}