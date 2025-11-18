package dao;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

public class FileDao extends Dao {
	/**
     * アップロードされたファイルの名前を取得
     */
	public String getFileName(Part part) {
	    // Java EE 7以降
	    String fileName = part.getSubmittedFileName();

	    if (fileName == null || fileName.isEmpty()) {
	        return null;
	    }

	    // フルパスからファイル名のみを取得（念のため）
	    int lastIndexOfSlash = Math.max(
	        fileName.lastIndexOf('/'),
	        fileName.lastIndexOf('\\')
	    );

	    if (lastIndexOfSlash >= 0) {
	        fileName = fileName.substring(lastIndexOfSlash + 1);
	    }

	    return fileName;
	}

	/**
     * アップロードされたファイルを保存
     */
    public String saveUploadedFile(Part filePart, String fileName, HttpServletRequest req) throws IOException {
        if (filePart == null || fileName == null || fileName.isEmpty()) {
            return null;
        }

        // WebContent/image フォルダの実際のパスを取得
        String uploadDir = req.getServletContext().getRealPath("/image");
        File uploadDirFile = new File(uploadDir);

        // ディレクトリが存在しない場合は作成
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();
        }

        // ユニークなファイル名を生成
        String uniqueFileName = System.currentTimeMillis() + "_" + fileName;

        // 保存先の絶対パス
        String filePath = uploadDir + File.separator + uniqueFileName;

        System.out.println("保存先パス: " + filePath); // デバッグ用

        // ファイルを保存
        filePart.write(filePath);

        // 相対パス（HTML/JSPからアクセスするパス）を返す
        return "image/" + uniqueFileName;
    }

}
