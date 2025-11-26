package eventportal.host.hostdetail;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Product;
import dao.HostEventDao;
import dao.ProductDao;
import tool.Action;

public class HostProductAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		System.out.println("グッズ・フード管理画面表示");

		// パラメータ取得
        String eventId = req.getParameter("eventId");
        // 使用するDAOを定義
     	HostEventDao hosEvtDao = new HostEventDao();
     	ProductDao proDao = new ProductDao();
     	// グッズリストを作成
     	List<Product> proList = null ;

     	String eventStatement = hosEvtDao.getEventStatement(eventId);


     	switch (eventStatement) {
        case "1":
        	System.out.println("開催前グッズフード処理開始");
            // 1のときの処理
        	proList = proDao.getByEventId(eventId);

        	 req.setAttribute("proList", proList);
        	 req.getRequestDispatcher("../product_before.jsp").forward(req, res);
        case "2":
        	System.out.println("開催中グッズフード処理開始");
            // 2のときの処理
            break;
        case "3":
        	System.out.println("開催後グッズフード処理開始");
            // 3のときの処理
            break;
        default:
            // どのcaseにも当てはまらないときの処理
     	}
	}
}
