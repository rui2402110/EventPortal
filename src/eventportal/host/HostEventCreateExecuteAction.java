package eventportal.host;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import bean.Event;
import bean.User;
import dao.Dao;
import dao.EventDao;
import tool.Action;

/**
 * イベント作成実行アクション（画像アップロード対応版）
 */
public class HostEventCreateExecuteAction extends Action {

    // 画像保存ディレクトリ（Webアプリケーションのルートからの相対パス）
    private static final String UPLOAD_DIR = "uploads/maps";
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        System.out.println("\n████████████████████████████████████████████████████████████████");
        System.out.println("███          イベント作成処理開始（画像対応）                ███");
        System.out.println("████████████████████████████████████████████████████████████████");
        System.out.println();

        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        if (user == null || user.getUser_type() != 2) {
            System.out.println("✗ エラー：未ログインまたは権限なし");
            res.sendRedirect(req.getContextPath() + "/eventportal/auth/HostLogin.action");
            return;
        }

        System.out.println("✓ ユーザー認証OK: " + user.getUser_id());

        try {
            // パラメータ取得
            String eventName = req.getParameter("eventName");
            String holdingDate = req.getParameter("holdingDate");
            String holdingTime = req.getParameter("holdingTime");
            String address = req.getParameter("address");
            String maxCountStr = req.getParameter("maxCount");
            String eventHoldState = req.getParameter("eventHoldState");
            String phoneNumber = req.getParameter("phoneNumber");
            String link = req.getParameter("link");
            String eventOverview = req.getParameter("eventOverview");
            String categoryId = req.getParameter("categoryId");

            System.out.println("\n受信パラメータ:");
            System.out.println("  - eventName: " + eventName);
            System.out.println("  - holdingDate: " + holdingDate);
            System.out.println("  - address: " + address);

            // バリデーション
            if (eventName == null || eventName.trim().isEmpty() ||
                holdingDate == null || holdingDate.trim().isEmpty() ||
                holdingTime == null || holdingTime.trim().isEmpty() ||
                address == null || address.trim().isEmpty() ||
                maxCountStr == null || maxCountStr.trim().isEmpty() ||
                eventOverview == null || eventOverview.trim().isEmpty()) {

                System.out.println("✗ 必須項目が入力されていません");
                req.setAttribute("errorMessage", "必須項目が入力されていません。");
                req.getRequestDispatcher("/eventportal/host/host_event_create.jsp").forward(req, res);
                return;
            }

            System.out.println("✓ バリデーションOK");

            int maxCount = Integer.parseInt(maxCountStr);

            if (maxCount < 1 || maxCount > 10000) {
                req.setAttribute("errorMessage", "定員は1〜10000人の範囲で入力してください。");
                req.getRequestDispatcher("/eventportal/host/host_event_create.jsp").forward(req, res);
                return;
            }

            // イベントID自動生成
            String eventId = generateEventId();
            System.out.println("✓ イベントID生成: " + eventId);

            // 画像アップロード処理
            String mapInHallPath = null;
            String mapOutOfHallPath = null;

            try {
                Part mapInHallPart = req.getPart("mapInHall");
                if (mapInHallPart != null && mapInHallPart.getSize() > 0) {
                    mapInHallPath = saveUploadedFile(mapInHallPart, req, eventId + "_in");
                    System.out.println("✓ 会場内マップ保存: " + mapInHallPath);
                }

                Part mapOutOfHallPart = req.getPart("mapOutOfHall");
                if (mapOutOfHallPart != null && mapOutOfHallPart.getSize() > 0) {
                    mapOutOfHallPath = saveUploadedFile(mapOutOfHallPart, req, eventId + "_out");
                    System.out.println("✓ 会場外マップ保存: " + mapOutOfHallPath);
                }
            } catch (Exception e) {
                System.err.println("✗ 画像アップロードエラー: " + e.getMessage());
                // 画像アップロードエラーでもイベント作成は続行
            }

            // Eventオブジェクト作成
            Event event = new Event();
            event.setEventId(eventId);
            event.setEventName(eventName.trim());
            event.setHoldingDate(holdingDate);
            event.setHoldingTime(holdingTime);
            event.setAddress(address.trim());
            event.setMaxCount(maxCount);
            event.setEventHoldState(eventHoldState != null && !eventHoldState.isEmpty() ? eventHoldState : "1");
            event.setPhoneNumber(phoneNumber != null && !phoneNumber.trim().isEmpty() ? phoneNumber.trim() : null);
            event.setLink(link != null && !link.trim().isEmpty() ? link.trim() : null);
            event.setEventOverview(eventOverview.trim());
            event.setHostId(user.getUser_id());
            event.setCategoryId(categoryId != null && !categoryId.trim().isEmpty() ? categoryId.trim() : null);
            event.setMapInHall(mapInHallPath);
            event.setMapOutOfHall(mapOutOfHallPath);
            event.setUserId(user.getUser_id());
            event.setTotalPayment(0);

            System.out.println("\n✓ Eventオブジェクト作成完了");

            // データベース登録
            EventDao eventDao = new EventDao();
            int count = eventDao.save(event);

            System.out.println("登録結果: " + count + "件");

            if (count > 0) {
                System.out.println("✓✓✓ イベント作成成功！ ✓✓✓");
                System.out.println("████████████████████████████████████████████████████████████████\n");

                session.setAttribute("successMessage", "イベント「" + event.getEventName() + "」を作成しました。");
                res.sendRedirect(req.getContextPath() + "/eventportal/host/HostMenu.action");
            } else {
                System.out.println("✗ イベント作成失敗");
                req.setAttribute("errorMessage", "イベントの作成に失敗しました。");
                req.setAttribute("event", event);
                req.getRequestDispatcher("/eventportal/host/host_event_create.jsp").forward(req, res);
            }

        } catch (NumberFormatException e) {
            System.err.println("✗ 数値変換エラー: " + e.getMessage());
            req.setAttribute("errorMessage", "定員には数値を入力してください。");
            req.getRequestDispatcher("/eventportal/host/host_event_create.jsp").forward(req, res);
        } catch (Exception e) {
            System.err.println("✗✗✗ イベント作成エラー ✗✗✗");
            System.err.println("エラー内容: " + e.getMessage());
            e.printStackTrace();
            req.setAttribute("errorMessage", "エラーが発生しました: " + e.getMessage());
            req.getRequestDispatcher("/eventportal/host/host_event_create.jsp").forward(req, res);
        }
    }

    /**
     * アップロードされた画像ファイルを保存
     * @param part アップロードファイル
     * @param req HttpServletRequest
     * @param prefix ファイル名プレフィックス
     * @return 保存されたファイルのパス（相対パス）
     * @throws Exception
     */
    private String saveUploadedFile(Part part, HttpServletRequest req, String prefix) throws Exception {
        // ファイルサイズチェック
        if (part.getSize() > MAX_FILE_SIZE) {
            throw new Exception("ファイルサイズが大きすぎます（最大5MB）");
        }

        // ファイル名取得
        String fileName = getFileName(part);
        if (fileName == null || fileName.isEmpty()) {
            return null;
        }

        // 拡張子取得
        String extension = "";
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = fileName.substring(dotIndex);
        }

        // ユニークなファイル名生成
        String uniqueFileName = prefix + "_" + UUID.randomUUID().toString() + extension;

        // 保存先ディレクトリ作成
        String uploadPath = req.getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
            System.out.println("  ディレクトリ作成: " + uploadPath);
        }

        // ファイル保存
        String filePath = uploadPath + File.separator + uniqueFileName;
        try (InputStream input = part.getInputStream()) {
            Files.copy(input, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
        }

        // 相対パスを返す（DBに保存）
        return UPLOAD_DIR + "/" + uniqueFileName;
    }

    /**
     * Partからファイル名を取得
     * @param part Part
     * @return ファイル名
     */
    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        if (contentDisposition != null) {
            for (String token : contentDisposition.split(";")) {
                if (token.trim().startsWith("filename")) {
                    return token.substring(token.indexOf('=') + 1).trim().replace("\"", "");
                }
            }
        }
        return null;
    }

    /**
     * イベントID自動生成
     * @return 新しいイベントID
     * @throws Exception
     */
    private String generateEventId() throws Exception {
        Dao dao = new Dao();
        Connection connection = dao.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String sql = "SELECT event_id FROM EVENTS ORDER BY event_id DESC LIMIT 1";
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();

            int nextNumber = 1;

            if (resultSet.next()) {
                String lastEventId = resultSet.getString("event_id");
                String numberPart = lastEventId.substring(3);
                nextNumber = Integer.parseInt(numberPart) + 1;
            }

            return String.format("EVT%03d", nextNumber);

        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    }
}