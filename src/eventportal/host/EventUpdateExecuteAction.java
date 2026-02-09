package eventportal.host;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import bean.Event;
import bean.User;
import dao.EventDao;
import tool.Action;

/**
 * イベント更新実行アクション（画像アップロード対応）
 */
public class EventUpdateExecuteAction extends Action {

    private static final String UPLOAD_DIR = "uploads/maps";
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        System.out.println("\n========================================");
        System.out.println("=== イベント更新処理開始 ===");
        System.out.println("========================================");

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
            String eventId = req.getParameter("eventId");
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
            System.out.println("  - eventId: " + eventId);
            System.out.println("  - eventName: " + eventName);

            // バリデーション
            if (eventId == null || eventId.trim().isEmpty()) {
                req.setAttribute("errorMessage", "イベントIDが指定されていません。");
                req.getRequestDispatcher("/eventportal/host/host_event_update.jsp").forward(req, res);
                return;
            }

            if (eventName == null || eventName.trim().isEmpty() ||
                holdingDate == null || holdingDate.trim().isEmpty() ||
                holdingTime == null || holdingTime.trim().isEmpty() ||
                address == null || address.trim().isEmpty() ||
                maxCountStr == null || maxCountStr.trim().isEmpty() ||
                eventOverview == null || eventOverview.trim().isEmpty()) {

                req.setAttribute("errorMessage", "必須項目が入力されていません。");
                req.getRequestDispatcher("/eventportal/host/host_event_update.jsp").forward(req, res);
                return;
            }

            System.out.println("✓ バリデーションOK");

            int maxCount = Integer.parseInt(maxCountStr);

            // 既存イベント情報を取得
            EventDao eventDao = new EventDao();
            Event existingEvent = eventDao.get(eventId);

            if (existingEvent == null) {
                req.setAttribute("errorMessage", "イベントが見つかりません。");
                req.getRequestDispatcher("/error.jsp").forward(req, res);
                return;
            }

            // 権限チェック
            if (!existingEvent.getHostId().equals(user.getUser_id())) {
                req.setAttribute("errorMessage", "このイベントを編集する権限がありません。");
                req.getRequestDispatcher("/error.jsp").forward(req, res);
                return;
            }

            System.out.println("✓ 権限チェックOK");

            // 画像アップロード処理
            String mapInHallPath = existingEvent.getMapInHall(); // 既存のパスを保持
            String mapOutOfHallPath = existingEvent.getMapOutOfHall(); // 既存のパスを保持

            try {
                Part mapInHallPart = req.getPart("mapInHall");
                if (mapInHallPart != null && mapInHallPart.getSize() > 0) {
                    // 新しい画像がアップロードされた場合
                    mapInHallPath = saveUploadedFile(mapInHallPart, req, eventId + "_in");
                    System.out.println("✓ 会場内マップ更新: " + mapInHallPath);
                }

                Part mapOutOfHallPart = req.getPart("mapOutOfHall");
                if (mapOutOfHallPart != null && mapOutOfHallPart.getSize() > 0) {
                    // 新しい画像がアップロードされた場合
                    mapOutOfHallPath = saveUploadedFile(mapOutOfHallPart, req, eventId + "_out");
                    System.out.println("✓ 会場外マップ更新: " + mapOutOfHallPath);
                }
            } catch (Exception e) {
                System.err.println("✗ 画像アップロードエラー: " + e.getMessage());
                // 画像アップロードエラーでも更新は続行
            }

            // Eventオブジェクト作成
            Event event = new Event();
            event.setEventId(eventId);
            event.setEventName(eventName.trim());
            event.setHoldingDate(holdingDate);
            event.setHoldingTime(holdingTime);
            event.setAddress(address.trim());
            event.setMaxCount(maxCount);
            event.setEventHoldState(eventHoldState);
            event.setPhoneNumber(phoneNumber);
            event.setLink(link);
            event.setEventOverview(eventOverview.trim());
            event.setCategoryId(categoryId);
            event.setMapInHall(mapInHallPath);
            event.setMapOutOfHall(mapOutOfHallPath);

            System.out.println("\n✓ Eventオブジェクト作成完了");

            // データベース更新
            int count = eventDao.update(event);

            System.out.println("更新結果: " + count + "件");

            if (count > 0) {
                System.out.println("✓✓✓ イベント更新成功！ ✓✓✓");
                System.out.println("========================================\n");

                session.setAttribute("successMessage", "イベント「" + event.getEventName() + "」を更新しました。");
                res.sendRedirect(req.getContextPath() + "/eventportal/host/HostMenu.action");
            } else {
                System.out.println("✗ イベント更新失敗");
                req.setAttribute("errorMessage", "更新に失敗しました。");
                req.setAttribute("event", event);
                req.getRequestDispatcher("/eventportal/host/host_event_update.jsp").forward(req, res);
            }

        } catch (NumberFormatException e) {
            System.err.println("✗ 数値変換エラー: " + e.getMessage());
            req.setAttribute("errorMessage", "定員には数値を入力してください。");
            req.getRequestDispatcher("/eventportal/host/host_event_update.jsp").forward(req, res);
        } catch (Exception e) {
            System.err.println("✗✗✗ イベント更新エラー ✗✗✗");
            System.err.println("エラー内容: " + e.getMessage());
            e.printStackTrace();

            req.setAttribute("errorMessage", "エラーが発生しました: " + e.getMessage());
            req.getRequestDispatcher("/eventportal/host/host_event_update.jsp").forward(req, res);
        }
    }

    /**
     * アップロードされた画像ファイルを保存
     */
    private String saveUploadedFile(Part part, HttpServletRequest req, String prefix) throws Exception {
        if (part.getSize() > MAX_FILE_SIZE) {
            throw new Exception("ファイルサイズが大きすぎます（最大5MB）");
        }

        String fileName = getFileName(part);
        if (fileName == null || fileName.isEmpty()) {
            return null;
        }

        String extension = "";
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = fileName.substring(dotIndex);
        }

        String uniqueFileName = prefix + "_" + UUID.randomUUID().toString() + extension;

        String uploadPath = req.getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String filePath = uploadPath + File.separator + uniqueFileName;
        try (InputStream input = part.getInputStream()) {
            Files.copy(input, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
        }

        return UPLOAD_DIR + "/" + uniqueFileName;
    }

    /**
     * Partからファイル名を取得
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
}