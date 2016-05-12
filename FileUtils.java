package com.dreamori.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.content.Context;
import android.content.res.AssetManager;

public class FileUtils {
	
	public static void copyAssert(Context context, String srcPath, String dstPath) {
	    AssetManager assetManager = context.getAssets();
	    try {
	    	String assets[] = assetManager.list(srcPath);
	        if (assets.length == 0) {
	    	    InputStream in = assetManager.open(srcPath);
	    	    OutputStream out = new FileOutputStream(dstPath);

		        byte[] buffer = new byte[1024];
		        int read;
		        while ((read = in.read(buffer)) != -1) {
		            out.write(buffer, 0, read);
		        }
		        in.close();
		        in = null;
		        out.flush();
		        out.close();
		        out = null;
	        } else {
	            File dir = new File(dstPath);
	            if (!dir.exists())
	                dir.mkdir();
	            for (int i = 0; i < assets.length; ++i) {
	            	copyAssert(context, srcPath + File.separator + assets[i], dstPath + File.separator + assets[i]);
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	/**
	 * Unzip a zip file.  Will overwrite existing files.
	 * 
	 * @param zipFile Full path of the zip file you'd like to unzip.
	 * @param location Full path of the directory you'd like to unzip to (will be created if it doesn't exist).
	 * @throws IOException
	 */
	private static final int BUFFER_SIZE = 4096;
	public static boolean unzip(String zipFile, String location){
	    int size;
	    byte[] buffer = new byte[BUFFER_SIZE];

	    try {
	        if ( !location.endsWith(File.separator) ) {
	            location += File.separator;
	        }
	        File f = new File(location);
	        if(!f.isDirectory()) {
	            f.mkdirs();
	        }
	        ZipInputStream zin = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile), BUFFER_SIZE));
	        try {
	            ZipEntry ze = null;
	            while ((ze = zin.getNextEntry()) != null) {
	                String path = location + ze.getName();
	                File unzipFile = new File(path);

	                if (ze.isDirectory()) {
	                    if(!unzipFile.isDirectory()) {
	                        unzipFile.mkdirs();
	                    }
	                } else {
	                    // check for and create parent directories if they don't exist
	                    File parentDir = unzipFile.getParentFile();
	                    if ( null != parentDir ) {
	                        if ( !parentDir.isDirectory() ) {
	                            parentDir.mkdirs();
	                        }
	                    }

	                    // unzip the file
	                    FileOutputStream out = new FileOutputStream(unzipFile, false);
	                    BufferedOutputStream fout = new BufferedOutputStream(out, BUFFER_SIZE);
	                    try {
	                        while ( (size = zin.read(buffer, 0, BUFFER_SIZE)) != -1 ) {
	                            fout.write(buffer, 0, size);
	                        }

	                        zin.closeEntry();
	                    }
	                    finally {
	                        fout.flush();
	                        fout.close();
	                    }
	                }
	            }
	        }
	        finally {
	            zin.close();
	        }
	        return true;
	    }
	    catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public static void DeleteFiles(File fileOrDirectory) {
		try {
		    if (fileOrDirectory.isDirectory())
		        for (File child : fileOrDirectory.listFiles())
		        	DeleteFiles(child);

		    fileOrDirectory.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
