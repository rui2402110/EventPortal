package eventportal.host.hostdetail;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Product;
import dao.ProductDao;
import dao.ProductHoldDao;
import tool.Action;

public class ProductHoldingExecuteAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        // パラメータ取得
        String eventId = req.getParameter("eventId");
        String[] itemIds = req.getParameterValues("itemIds");

        // DAOのインスタンス化
        ProductDao proDao = new ProductDao();
        ProductHoldDao holdDao = new ProductHoldDao();

        if (itemIds != null) {
            for (String itemId : itemIds) {
                // JSPのname属性 "soldCount_${product.itemId}" から値を取得
                String countStr = req.getParameter("soldCount_" + itemId);
                int soldCount = 0;

                try {
                    soldCount = Integer.parseInt(countStr);
                } catch (NumberFormatException e) {
                    continue; // 数値以外が入力された場合はスキップ
                }

                // 売れた個数が0より大きい場合のみ処理を実行
                if (soldCount > 0) {
                    // 1. 商品の単価を取得するために商品情報を取得
                    // ※productGet(eventId)等から該当のitemIdを探す、もしくは専用のgetメソッドを想定
                    List<Product> proList = proDao.productGet(eventId);
                    int price = 0;
                    for (Product p : proList) {
                        if (p.getItemId().equals(itemId)) {
                            price = p.getPrice();
                            break;
                        }
                    }

                    // 2. 売上金額の計算
                    int salesAmount = price * soldCount;

                    // 3. DAOに「売れた数」と「算出した売上金額」を渡して更新
                    holdDao.updateStockAndTotalPay(eventId, itemId, soldCount, salesAmount);
                }
            }
        }

        // 完了後、元のグッズ管理画面（開催中）へリダイレクト
        // 直接JSPを指定せずActionを経由することで最新の在庫状況を表示します
        res.sendRedirect("HostProduct.action?eventId=" + eventId);
    }
}