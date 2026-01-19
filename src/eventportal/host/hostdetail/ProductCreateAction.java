package eventportal.host.hostdetail;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tool.Action;

public class ProductCreateAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		System.out.println("グッズ・フード新規作成画面表示");

		req.getRequestDispatcher("/eventportal/host/host_product_create.jsp").forward(req, res);

	}
}
