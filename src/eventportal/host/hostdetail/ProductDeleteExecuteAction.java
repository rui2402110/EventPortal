package eventportal.host.hostdetail;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.ProductDao;
import tool.Action;

public class ProductDeleteExecuteAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        System.out.println("商品削除実行開始");

        // DAOを定義
        ProductDao productDao = new ProductDao();

        // リクエストパラメータから商品IDとイベントIDを取得
        String itemId = req.getParameter("itemId");
        String eventId = req.getParameter("eventId");

        System.out.println("削除対象の商品ID: " + itemId);
        System.out.println("イベントID: " + eventId);

        // 削除処理を実行
        boolean isSuccess = productDao.productDelete(itemId, eventId);

        if (isSuccess) {
            System.out.println("商品削除成功");
            // 削除成功後、商品一覧へリダイレクト
            res.sendRedirect(req.getContextPath() + "/eventportal/host/hostdetail/HostProduct.action?eventId=" + eventId);
        } else {
            System.out.println("商品削除失敗");
            // 失敗時のエラー処理
            req.setAttribute("error", "商品の削除に失敗しました。");
            req.getRequestDispatcher("error.jsp").forward(req, res);
        }
    }
}
