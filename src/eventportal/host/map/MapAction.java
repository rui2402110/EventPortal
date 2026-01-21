package eventportal.host.map;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import bean.Map;
import bean.MapPin;
import dao.eventmap.MapDao;
import dao.eventmap.MapPinDao;

/**
 * マップ管理のコントローラクラス
 */
public class MapAction extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "list":
                    showMapList(request, response);
                    break;
                case "new":
                    showMapForm(request, response, null);
                    break;
                case "edit":
                    showMapForm(request, response, request.getParameter("mapId"));
                    break;
                case "delete":
                    deleteMap(request, response);
                    break;
                default:
                    showMapList(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "エラーが発生しました: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        try {
            switch (action) {
                case "save":
                    saveMap(request, response);
                    break;
                default:
                    showMapList(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "エラーが発生しました: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    /**
     * マップ一覧を表示
     */
    private void showMapList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {

        MapDao MapDao = new MapDao();

        // セッションからユーザー情報を取得
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");

        List<Map> maps;
        if (userId != null && !userId.isEmpty()) {
            // ユーザーが作成したマップのみ表示
            maps = MapDao.findByCreatedBy(userId);
        } else {
            // 全マップを表示
            maps = MapDao.findAll();
        }

        request.setAttribute("maps", maps);
        request.getRequestDispatcher("/mapList.jsp").forward(request, response);
    }

    /**
     * マップ編集フォームを表示
     */
    private void showMapForm(HttpServletRequest request, HttpServletResponse response, String mapIdStr)
            throws ServletException, IOException, Exception {

        MapDao MapDao = new MapDao();
        MapPinDao pinDAO = new MapPinDao();

        Map map = new Map();
        List<MapPin> pins = null;

        if (mapIdStr != null && !mapIdStr.isEmpty()) {
            // 編集モード
            int mapId = Integer.parseInt(mapIdStr);
            map = MapDao.findById(mapId);

            if (map == null) {
                request.setAttribute("errorMessage", "指定されたマップが見つかりません。");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }

            // マップに紐づくピンを取得
            pins = pinDAO.findByMapId(mapId);
        }

        request.setAttribute("map", map);
        request.setAttribute("pins", pins);
        request.getRequestDispatcher("/mapEdit.jsp").forward(request, response);
    }

    /**
     * マップを保存（新規登録または更新）
     */
    private void saveMap(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {

        MapDao MapDao = new MapDao();
        MapPinDao pinDAO = new MapPinDao();

        // マップ情報の取得
        String mapIdStr = request.getParameter("mapId");
        String mapName = request.getParameter("mapName");
        String description = request.getParameter("description");

        // セッションからユーザー情報を取得
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            userId = "guest"; // デフォルトユーザー
        }

        Map map = new Map();
        map.setMapName(mapName);
        map.setDescription(description);
        map.setCreatedBy(userId);

        int mapId;

        try {
            if (mapIdStr != null && !mapIdStr.isEmpty()) {
                // 更新モード
                mapId = Integer.parseInt(mapIdStr);
                map.setMapId(mapId);

                // デフォルト表示位置の更新
                String defaultLatStr = request.getParameter("defaultLat");
                String defaultLngStr = request.getParameter("defaultLng");
                String defaultZoomStr = request.getParameter("defaultZoom");

                if (defaultLatStr != null && !defaultLatStr.isEmpty()) {
                    map.setDefaultLat(Double.parseDouble(defaultLatStr));
                }
                if (defaultLngStr != null && !defaultLngStr.isEmpty()) {
                    map.setDefaultLng(Double.parseDouble(defaultLngStr));
                }
                if (defaultZoomStr != null && !defaultZoomStr.isEmpty()) {
                    map.setDefaultZoom(Integer.parseInt(defaultZoomStr));
                }

                MapDao.update(map);

                // 既存のピンを削除（再登録するため）
                pinDAO.deleteByMapId(mapId);

            } else {
                // 新規登録モード
                mapId = MapDao.insert(map);
            }

            // ピンデータの保存
            String pinsJson = request.getParameter("pinsData");
            if (pinsJson != null && !pinsJson.isEmpty()) {
                Gson gson = new Gson();
                MapPin[] pinsArray = gson.fromJson(pinsJson, MapPin[].class);

                for (int i = 0; i < pinsArray.length; i++) {
                    MapPin pin = pinsArray[i];
                    pin.setMapId(mapId);
                    pin.setDisplayOrder(i + 1); // 表示順序を設定
                    pinDAO.insert(pin);
                }
            }

            // 成功メッセージをセッションに保存
            session.setAttribute("successMessage", "マップを保存しました。");

            // マップ一覧にリダイレクト
            response.sendRedirect("MapAction?action=list");

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * マップを削除
     */
    private void deleteMap(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {

        String mapIdStr = request.getParameter("mapId");
        if (mapIdStr == null || mapIdStr.isEmpty()) {
            request.setAttribute("errorMessage", "マップIDが指定されていません。");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        int mapId = Integer.parseInt(mapIdStr);

        try {
            MapDao MapDao = new MapDao();

            // マップ削除（ON DELETE CASCADEでピンも自動削除される）
            int deletedRows = MapDao.delete(mapId);

            HttpSession session = request.getSession();
            if (deletedRows > 0) {
                session.setAttribute("successMessage", "マップを削除しました。");
            } else {
                session.setAttribute("errorMessage", "マップの削除に失敗しました。");
            }

            response.sendRedirect("MapAction?action=list");

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}