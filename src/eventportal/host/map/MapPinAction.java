package eventportal.host.map;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import bean.MapPin;
import dao.eventmap.MapDao;
import dao.eventmap.MapPinDao;

/**
 * ピン管理のコントローラクラス（AJAX用）
 */
public class MapPinAction extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            switch (action) {
                case "list":
                    getPinList(request, response);
                    break;
                case "get":
                    getPin(request, response);
                    break;
                default:
                    sendErrorResponse(response, "Invalid action");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(response, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        try {
            switch (action) {
                case "add":
                    addPin(request, response);
                    break;
                case "update":
                    updatePin(request, response);
                    break;
                case "delete":
                    deletePin(request, response);
                    break;
                case "updateOrder":
                    updateDisplayOrder(request, response);
                    break;
                default:
                    sendErrorResponse(response, "Invalid action");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(response, e.getMessage());
        }
    }

    /**
     * 指定マップのピン一覧を取得（JSON形式で返却）
     */
    private void getPinList(HttpServletRequest request, HttpServletResponse response)
            throws Exception, IOException {

        String mapIdStr = request.getParameter("mapId");
        if (mapIdStr == null || mapIdStr.isEmpty()) {
            sendErrorResponse(response, "マップIDが指定されていません");
            return;
        }

        int mapId = Integer.parseInt(mapIdStr);

        MapPinDao pinDAO = new MapPinDao();
        List<MapPin> pins = pinDAO.findByMapId(mapId);

        // JSON形式で返却
        Gson gson = new Gson();
        String json = gson.toJson(pins);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }

    /**
     * ピン情報を1件取得（JSON形式で返却）
     */
    private void getPin(HttpServletRequest request, HttpServletResponse response)
            throws Exception, IOException {

        String pinIdStr = request.getParameter("pinId");
        if (pinIdStr == null || pinIdStr.isEmpty()) {
            sendErrorResponse(response, "ピンIDが指定されていません");
            return;
        }

        int pinId = Integer.parseInt(pinIdStr);

        MapPinDao pinDAO = new MapPinDao();
        MapPin pin = pinDAO.findById(pinId);

        if (pin == null) {
            sendErrorResponse(response, "ピンが見つかりません");
            return;
        }

        // JSON形式で返却
        Gson gson = new Gson();
        String json = gson.toJson(pin);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }

    /**
     * ピンを追加
     */
    private void addPin(HttpServletRequest request, HttpServletResponse response)
            throws Exception, IOException {

        // リクエストパラメータから値を取得
        int mapId = Integer.parseInt(request.getParameter("mapId"));
        String pinName = request.getParameter("pinName");
        double latitude = Double.parseDouble(request.getParameter("latitude"));
        double longitude = Double.parseDouble(request.getParameter("longitude"));
        String address = request.getParameter("address");
        String description = request.getParameter("description");
        String pinColor = request.getParameter("pinColor");
        String iconType = request.getParameter("iconType");

        MapPin pin = new MapPin();
        pin.setMapId(mapId);
        pin.setPinName(pinName);
        pin.setLatitude(latitude);
        pin.setLongitude(longitude);
        pin.setAddress(address);
        pin.setDescription(description);
        pin.setPinColor(pinColor != null ? pinColor : "blue");
        pin.setIconType(iconType);

        // 表示順序を設定（既存のピン数+1）
        MapPinDao pinDAO = new MapPinDao();
        int count = pinDAO.countByMapId(mapId);
        pin.setDisplayOrder(count + 1);

        // ピンを登録
        int pinId = pinDAO.insert(pin);
        pin.setPinId(pinId);

        // 成功レスポンス
        sendSuccessResponse(response, pin);
    }

    /**
     * ピンを更新
     */
    private void updatePin(HttpServletRequest request, HttpServletResponse response)
            throws Exception, IOException {

        int pinId = Integer.parseInt(request.getParameter("pinId"));
        String pinName = request.getParameter("pinName");
        double latitude = Double.parseDouble(request.getParameter("latitude"));
        double longitude = Double.parseDouble(request.getParameter("longitude"));
        String address = request.getParameter("address");
        String description = request.getParameter("description");
        String pinColor = request.getParameter("pinColor");
        String iconType = request.getParameter("iconType");

        MapPinDao pinDAO = new MapPinDao();
        MapPin pin = pinDAO.findById(pinId);

        if (pin == null) {
            sendErrorResponse(response, "ピンが見つかりません");
            return;
        }

        pin.setPinName(pinName);
        pin.setLatitude(latitude);
        pin.setLongitude(longitude);
        pin.setAddress(address);
        pin.setDescription(description);
        pin.setPinColor(pinColor);
        pin.setIconType(iconType);

        pinDAO.update(pin);

        // 成功レスポンス
        sendSuccessResponse(response, pin);
    }

    /**
     * ピンを削除
     */
    private void deletePin(HttpServletRequest request, HttpServletResponse response)
            throws Exception, IOException {

        String pinIdStr = request.getParameter("pinId");
        if (pinIdStr == null || pinIdStr.isEmpty()) {
            sendErrorResponse(response, "ピンIDが指定されていません");
            return;
        }

        int pinId = Integer.parseInt(pinIdStr);

        MapDao pinDAO = new MapDao();
        int deletedRows = pinDAO.delete(pinId);

        if (deletedRows > 0) {
            sendSuccessResponse(response, "ピンを削除しました");
        } else {
            sendErrorResponse(response, "ピンの削除に失敗しました");
        }
    }

    /**
     * 表示順序を更新
     */
    private void updateDisplayOrder(HttpServletRequest request, HttpServletResponse response)
            throws Exception, IOException {

        int pinId = Integer.parseInt(request.getParameter("pinId"));
        int displayOrder = Integer.parseInt(request.getParameter("displayOrder"));

        MapPinDao pinDAO = new MapPinDao();
        pinDAO.updateDisplayOrder(pinId, displayOrder);

        sendSuccessResponse(response, "表示順序を更新しました");
    }

    /**
     * 成功レスポンスを返却
     */
    private void sendSuccessResponse(HttpServletResponse response, Object data) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Gson gson = new Gson();
        String json = gson.toJson(new ApiResponse(true, "success", data));

        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }

    /**
     * エラーレスポンスを返却
     */
    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        Gson gson = new Gson();
        String json = gson.toJson(new ApiResponse(false, message, null));

        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }

    /**
     * APIレスポンス用の内部クラス
     */
    private class ApiResponse {
        private boolean success;
        private String message;
        private Object data;

        public ApiResponse(boolean success, String message, Object data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }

        @SuppressWarnings("unused")
        public boolean isSuccess() {
            return success;
        }

        @SuppressWarnings("unused")
        public String getMessage() {
            return message;
        }

        @SuppressWarnings("unused")
        public Object getData() {
            return data;
        }
    }
}