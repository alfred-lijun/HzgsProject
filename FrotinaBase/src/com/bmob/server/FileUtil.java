package com.bmob.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;

public class FileUtil {
	
	public static boolean ShowAdd = true;
	
	public static String getSdCardPath(){
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}
	
	public static void addFile(String text) {
		addFile(text, Environment.getExternalStorageDirectory().getPath()+ "/apmt/", "a.txt");
	}

	public static void addFile(String text, String path, String fileName) {
		File file = new File(path + fileName);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}

		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(path + fileName);
			fileWriter.write(text);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fileWriter != null) {
					fileWriter.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void delete(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}
		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {
				file.delete();
				return;
			}
			for (int i = 0; i < childFiles.length; i++) {
				delete(childFiles[i]);
			}
			file.delete();
		}
	}
	
	@SuppressWarnings("deprecation")
	public static long getSDFreeSize() {
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		long blockSize = sf.getBlockSize();
		long freeBlocks = sf.getAvailableBlocks();
		return (freeBlocks * blockSize) / 1024 / 1024; // ��λMB
	}

	public static Bitmap getVideoThumbnail(String videoPath, int width,int height) {
		int kind = MediaStore.Images.Thumbnails.MICRO_KIND;
		Bitmap bitmap = null;
		// 获取视频的缩略图
		bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

	public static int adjustFontSize(int screenWidth, int screenHeight) {
		if (screenWidth <= 240) { // 240X320 屏幕
			return 10;
		} else if (screenWidth <= 320) { // 320X480 屏幕
			return 14;
		} else if (screenWidth <= 480) { // 480X800 或 480X854 屏幕
			return 18;
		} else if (screenWidth <= 540) { // 540X960 屏幕
			return 22;
		} else if (screenWidth <= 800) { // 800X1280 屏幕
			return 26;
		} else { // 大于 800X1280
			return 32;
		}
	}

	private static final String AssetsSName = "temp.avi";
	private static final String VideoDir = Environment.getExternalStorageDirectory().toString() + "/videocenter/";
	private static final String VideoPath = VideoDir + AssetsSName;

	public static String initAssetsFile(Context context) {
		File file = new File(VideoDir);
		if (!file.exists()) {
			file.mkdir();
		}

		file = new File(VideoPath);
		if (!file.exists()) {
			try {
				InputStream input = context.getAssets().open(AssetsSName);
				write2SDFromInput(VideoPath, input);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return VideoPath;
	}

	private static File write2SDFromInput(String path, InputStream input) {
		File file = null;
		OutputStream output = null;
		try {
			file = createFileInSDCard(path + ".temp");
			output = new FileOutputStream(file);
			byte buffer[] = new byte[4 * 1024];
			int temp;
			while ((temp = input.read(buffer)) != -1) {
				output.write(buffer, 0, temp);
			}
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
				if (GetFileSize(file) != 0) {
					file.renameTo(new File(path));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	@SuppressWarnings("resource")
	private static int GetFileSize(File file) throws IOException {
		int fileLen = 0;
		if (file.isDirectory()) {
			fileLen = 0;
		} else if (file.isFile()) {
			File dF = file;
			FileInputStream fis;
			fis = new FileInputStream(dF);
			fileLen = fis.available();
		}
		return fileLen;
	}

	private static File createFileInSDCard(String path) throws IOException {
		File file = new File(path);
		file.createNewFile();
		return file;
	}

	public static void RecursionDeleteFile(File file) {

		if (file.isFile()) {
			file.delete();
			return;
		}
		if (file.isDirectory()) {
			File[] childFile = file.listFiles();
			if (childFile == null || childFile.length == 0) {
				file.delete();
				return;
			}
			for (File f : childFile) {
				RecursionDeleteFile(f);
			}
			file.delete();
		}
	}
	
	public static boolean comp( String oldFilePath , String newFilePath , int w , int h ) {
		boolean ret = false;
		do {
			try {
				Bitmap image = BitmapFactory.decodeFile(oldFilePath);
		        FileOutputStream  m_fileOutPutStream = null;
		        m_fileOutPutStream = new FileOutputStream(newFilePath);//写入的文件路径
		        createBitmapBySize(image,w,h).compress(CompressFormat.PNG, 10, m_fileOutPutStream);
		        m_fileOutPutStream.flush();
		        m_fileOutPutStream.close();
			}catch(Exception e){
				ret = false;
				break;
			}
	        ret = true;
		} while (false);
        return ret;
    }
	
	private static Bitmap createBitmapBySize(Bitmap bitmap, int width, int height){    
		return Bitmap.createScaledBitmap(bitmap, width, height, true);    
	} 

	public static String getVersionName( Context context ) {
	    try {
	        PackageManager manager = context.getPackageManager();
	        PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
	        String version = info.versionName;
	        return version;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "1";
	    }
	}
	
	public static void showNotifiDialog( Context context , String msg ){
		android.app.AlertDialog.Builder builder = new Builder( context );
		builder.setMessage( msg );
		builder.setTitle( "温馨提示" );
		builder.setPositiveButton( "确定", new DialogInterface.OnClickListener( ) {
			@Override
			public void onClick(DialogInterface dialog, int which ) {
				dialog.dismiss( );
			}
		});
		builder.setNegativeButton( "取消", new DialogInterface.OnClickListener( ) {
			@Override
			public void onClick( DialogInterface dialog, int which ) {
				dialog.dismiss( );
			}
		});
		android.app.AlertDialog notifDialog = builder.create( );
		notifDialog.setCancelable( false );
		notifDialog.show( );
	}
}
