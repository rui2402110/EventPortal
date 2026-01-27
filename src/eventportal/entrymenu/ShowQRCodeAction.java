package eventportal.entrymenu;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import dao.EventDao;
import dao.TicketDao;
import tool.Action;

public class ShowQRCodeAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        // セッションチェック
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            return mapping.findForward("login");
        }

        Integer userId = (Integer) session.getAttribute("userId");

        // イベントID取得
        String eventIdStr = request.getParameter("eventId");
        if (eventIdStr == null || eventIdStr.isEmpty()) {
            request.setAttribute("errorMessage", "イベントIDが指定されていません。");
            return mapping.findForward("error");
        }

        Integer eventId = Integer.parseInt(eventIdStr);

        // イベント情報取得
        EventDao eventDao = new EventDao();
        Event event = eventDao.get(eventId);
        if (event == null) {
            request.setAttribute("errorMessage", "指定されたイベントが見つかりません。");
            return mapping.findForward("error");
        }

        // チケット情報取得
        TicketDao ticketDao = new TicketDao();
        Ticket ticket = ticketDao.getByUserAndEvent(userId, eventId);

        // リクエストスコープに設定
        request.setAttribute("event", event);
        request.setAttribute("ticket", ticket);

        return mapping.findForward("success");
    }
}