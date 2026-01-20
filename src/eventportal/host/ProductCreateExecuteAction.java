package eventportal.host.hostdetail;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import bean.Product;
import dao.FileDao;
import dao.ProductDao;
import tool.Action;

public class ProductCreateExecuteAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        System.out.println("グッズ・フード新規作成実行");

        // パラメータ取得
        String eventId = req.getParameter("eventId");
        String productName = req.getParameter("product_name");
        String overview = req.getParameter("overview");
        String priceStr = req.getParameter("price");
        String stockStr = req.getParameter("stock");

        // バリデーション
        if (eventId == null || eventId.isEmpty() ||
            productName == null || productName.isEmpty() ||
            priceStr == null || priceStr.isEmpty() ||
            stockStr == null || stockStr.isEmpty()) {
            req.setAttribute("error", "必須項目を入力してください");
            req.setAttribute("eventId", eventId);
            req.getRequestDispatcher("/eventportal/host/host_product_create.jsp").forward(req, res);
            return;
        }

        try {
            int price = Integer.parseInt(priceStr);
            int stock = Integer.parseInt(stockStr);

            // DAOインスタンス
            ProductDao productDao = new ProductDao();
            FileDao fileDao = new FileDao();

            // 画像ファイルの処理
            Part imagePart = req.getPart("product_image");
            String imagePath = null;

            if (imagePart != null && imagePart.getSize() > 0) {
                String imageFileName = fileDao.getFileName(imagePart);
                imagePath = fileDao.saveUploadedFile(imagePart, imageFileName, req);
            }

            // 商品IDを生成
            String itemId = productDao.generateItemId();

            // 商品情報を設定
            Product product = new Product();
            product.setItemId(itemId);
            product.setProductName(productName);
            product.setOverview(overview);
            product.setImage(imagePath);

            // 商品を登録
            boolean productCreated = productDao.create(product);

            if (productCreated) {
                // イベント商品（価格・在庫）を登録
                boolean eventProductAdded = productDao.addEventProduct(eventId, itemId, price, stock);

                if (eventProductAdded) {
                    System.out.println("商品登録成功: " + itemId);
                    res.sendRedirect(req.getContextPath() +
                        "/eventportal/host/hostdetail/HostProduct.action?eventId=" + eventId);
                } else {
                    req.setAttribute("error", "イベント商品の登録に失敗しました");
                    req.getRequestDispatcher("/eventportal/host/host_product_create.jsp").forward(req, res);
                }
            } else {
                req.setAttribute("error", "商品の登録に失敗しました");
                req.getRequestDispatcher("/eventportal/host/host_product_create.jsp").forward(req, res);
            }

        } catch (NumberFormatException e) {
            req.setAttribute("error", "価格と在庫は数値で入力してください");
            req.getRequestDispatcher("/eventportal/host/host_product_create.jsp").forward(req, res);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "エラーが発生しました: " + e.getMessage());
            req.getRequestDispatcher("/eventportal/host/host_product_create.jsp").forward(req, res);
        }
    }
}