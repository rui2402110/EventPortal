package eventportal.host.hostdetail;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Product;
import dao.ProductDao;
import tool.Action;

public class ProductDeleteAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        System.out.println("商品削除確認画面表示開始");

        // DAOを定義
        ProductDao productDao = new ProductDao();

        // リクエストパラメータから商品IDを取得
        String itemId = req.getParameter("itemId");
        System.out.println("削除対象の商品ID: " + itemId);

        // 商品IDを使って商品情報を取得
        Product product = productDao.productGetById(itemId);

        if (product != null) {
            System.out.println("商品情報取得成功: " + product.getProductName());

            // 取得した商品情報をリクエスト属性に設定
            req.setAttribute("product", product);

            // 削除確認画面に遷移
            req.getRequestDispatcher("/eventportal/host/host_product_delete.jsp").forward(req, res);
        } else {
            System.out.println("商品情報取得失敗");
            // 商品が見つからない場合のエラー処理
            req.setAttribute("error", "指定された商品が見つかりませんでした。");
            req.getRequestDispatcher("error.jsp").forward(req, res);
        }
    }
}
