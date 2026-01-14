package eventportal.entrymenu;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.EntryQRCode;
import dao.QRCodeDao;
import eventportal.common.EventPortalAction;
import util.QRCodeGenerator;

public class EntryQRCodeViewAction extends EventPortalAction {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // ユーザータイプの確認（参加者：1）
        Integer userType = (Integer) session.getAttribute("user_type");
        if (userType == null || userType != 1) {
            return "/eventportal/common/error.jsp";
        }

        // ユーザーIDとイベントIDを取得
        String userId = (String) session.getAttribute("user_id");
        String eventId = request.getParameter("eventId");

        if (userId == null || eventId == null) {
            request.setAttribute("errorMessage", "必要な情報が不足しています。");
            return "/eventportal/common/error.jsp";
        }

        try {
            QRCodeDao qrCodeDao = new QRCodeDao();

            // メソッドの引数順序を確認（userId, eventId の順序）
            EntryQRCode qrCode = qrCodeDao.getQRCodeByUserAndEvent(userId, eventId);

            // QRコードが存在しない場合は新規作成
            if (qrCode == null) {
                // QRコードを生成（eventId, userId の順序に注意）
                String qrCodeId = qrCodeDao.createQRCode(eventId, userId);

                // 生成したQRコードを再取得
                qrCode = qrCodeDao.getQRCodeByUserAndEvent(userId, eventId);

                if (qrCode != null) {
                    // QRコード画像を生成
                    String contextPath = request.getServletContext().getRealPath("");
                    String qrCodeImagePath = "/image/qrcodes/" + qrCodeId + ".png";
                    String fullPath = contextPath + qrCodeImagePath;

                    // QRコード画像を生成
                    boolean success = QRCodeGenerator.generateQRCode(
                        qrCode.getQrCodeData(),
                        fullPath
                    );

                    if (success) {
                        // 画像パスを設定（コンテキストパスは含めない）
                        qrCode.setQrCodeImagePath(qrCodeImagePath);
                    } else {
                        System.err.println("QRコード画像の生成に失敗しました: " + fullPath);
                    }
                }
            } else {
                // 既存のQRコードがある場合、画像が存在するか確認
                if (qrCode.getQrCodeImagePath() == null || qrCode.getQrCodeImagePath().isEmpty()) {
                    // 画像パスが設定されていない場合は生成
                    String contextPath = request.getServletContext().getRealPath("");
                    String qrCodeImagePath = "/image/qrcodes/" + qrCode.getQrCodeId() + ".png";
                    String fullPath = contextPath + qrCodeImagePath;

                    boolean success = QRCodeGenerator.generateQRCode(
                        qrCode.getQrCodeData(),
                        fullPath
                    );

                    if (success) {
                        qrCode.setQrCodeImagePath(qrCodeImagePath);
                    }
                }
            }

            // QRコードオブジェクトをリクエストに設定
            request.setAttribute("qrCode", qrCode);
            request.setAttribute("eventId", eventId);

            // 表示用JSPへフォワード
            return "/eventportal/entry/entry_qrcode_view.jsp";

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "QRコードの処理中にエラーが発生しました: " + e.getMessage());
            return "/eventportal/common/error.jsp";
        }
    }
}