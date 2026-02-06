package eventportal.host.hostdetail;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import bean.Product;
import dao.FileDao;
import dao.ProductDao;
import tool.Action;

@MultipartConfig
public class ProductEditExecuteAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        System.out.println("商品更新実行開始");

        // 使用するDAOを定義
        ProductDao productDao = new ProductDao();
        FileDao fileDao = new FileDao();

        // フォーム情報の取得
        String itemId = req.getParameter("itemId");
        String productName = req.getParameter("productName");
        String overview = req.getParameter("overview");
        String eventId = req.getParameter("eventId");
        String currentImage = req.getParameter("currentImage"); // 既存の画像パス

        // デバッグ用ログ
        System.out.println("取得した値:");
        System.out.println("itemId: " + itemId);
        System.out.println("productName: " + productName);
        System.out.println("overview: " + overview);
        System.out.println("eventId: " + eventId);
        System.out.println("currentImage: " + currentImage);

        // 数値項目の取得
        int price = Integer.parseInt(req.getParameter("price"));
        int stock = Integer.parseInt(req.getParameter("stock"));

        System.out.println("price: " + price);
        System.out.println("stock: " + stock);

        // 画像ファイルの取得と保存
        Part imagePart = req.getPart("imageFile");
        String imagePath = currentImage; // デフォルトは既存の画像パス

        // 新しい画像がアップロードされた場合のみ処理
        if (imagePart != null && imagePart.getSize() > 0) {
            // ファイル名を取得
            String fileName = fileDao.getFileName(imagePart);
            System.out.println("新しい画像がアップロードされました: " + fileName);

            // ファイルを保存し、そのパス（またはファイル名）を取得
            imagePath = fileDao.saveUploadedFile(imagePart, fileName, req);
            System.out.println("保存されたパス: " + imagePath);
        } else {
            System.out.println("画像は変更されていません。既存の画像を使用します。");
        }

        // Beanにデータをセット
        Product product = new Product();
        product.setItemId(itemId);
        product.setProductName(productName);
        product.setOverview(overview);
        product.setImage(imagePath); // 新しい画像または既存の画像パス
        product.setPrice(price);
        product.setStock(stock);

        // DBを更新
        boolean isSuccess = productDao.productUpdate(product, eventId);

        if (isSuccess) {
            System.out.println("商品更新成功");
            // 完了後、商品一覧へリダイレクト
            res.sendRedirect(req.getContextPath() + "/eventportal/host/hostdetail/HostProduct.action?eventId=" + eventId);
        } else {
            System.out.println("商品更新失敗");
            // 失敗時のエラー処理
            req.setAttribute("error", "商品の更新に失敗しました。");
            req.getRequestDispatcher("error.jsp").forward(req, res);
        }
    }
}
