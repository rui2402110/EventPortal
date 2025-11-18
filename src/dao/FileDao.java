package dao;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

public class FileDao extends Dao {
	/**
     * アップロードされたファイルの名前を取得
     */
    public String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        for (String content : contentDisposition.split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

	/**
     * アップロードされたファイルを保存
     */
    public String saveUploadedFile(Part part, String fileName, HttpServletRequest request)
            throws IOException {

        // 保存先ディレクトリの設定
        String uploadPath = request.getServletContext().getRealPath("") + "/uploads/event_images";
        java.io.File uploadDir = new java.io.File(uploadPath);

        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // ファイル名の重複を避けるためにタイムスタンプを追加
        String timestamp = String.valueOf(System.currentTimeMillis());
        String extension = "";
        if (fileName != null && fileName.lastIndexOf(".") > 0) {
            extension = fileName.substring(fileName.lastIndexOf("."));
        }
        String uniqueFileName = timestamp + "_" + fileName;

        // ファイル保存
        String filePath = uploadPath + "/" + uniqueFileName;
        part.write(filePath);

        // データベースに保存するパス（相対パス）
        return "/uploads/event_images/" + uniqueFileName;
    }

}
