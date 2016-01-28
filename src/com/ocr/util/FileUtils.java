package com.ocr.util;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;




/**
 * 
 * @author isuru
 *
 */
public class FileUtils {


	

	/**
	 * 
	 * 
	 * @param propFilePath
	 * @return
	 */
	public static Properties loadPropertiesFile( String propFilePath ) {
		Properties props = null;
		try {
			props = new Properties();
			FileInputStream in = new FileInputStream(propFilePath);
			props.load(in);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return props;
	}
	
	
	
	
	
	/**
	 * Creates or modifies a single property of the given properties file
	 * 
	 * @param propFilePath
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean setProperty( String propFilePath, String key, String value ) {
		Properties props = loadPropertiesFile(propFilePath);
		try {
			FileOutputStream out = new FileOutputStream(propFilePath);
			props.setProperty( key, value );
			props.store(out, null);
			out.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	
	
	/**
	 * 
	 * 
	 * @param propFilePath
	 * @param propPairs
	 * @return
	 */
	public static boolean setMultipleProperties( String propFilePath, HashMap<String,String> propPairs ) {
		Properties props = loadPropertiesFile(propFilePath);
		try {
			FileOutputStream out = new FileOutputStream(propFilePath);
			Set<String> keys = propPairs.keySet();
			for( String key: keys ) {
				String value = propPairs.get(key);
				props.setProperty( key, value );
			}
			props.store(out, null);
			out.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	
	/**
	 * 
	 * @param propFilePath
	 * @param key
	 * @return 
	 */
	public static boolean deleteProperty( String propFilePath, String key ) {
		Properties props = loadPropertiesFile(propFilePath);
		try {
			FileOutputStream out = new FileOutputStream(propFilePath);
			props.remove(key);
			props.store(out, null);
			out.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	

	/**
	 * Appends to end of file
	 * 
	 * @param sb
	 * @param outputFile
	 * @return
	 */
	public static boolean appendToFile( StringBuilder sb, String outputFile ) {
		byte data[] = null;
		try {
			data = sb.toString().getBytes("UTF8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
	    Path p = Paths.get(outputFile);
	    try ( OutputStream out = new BufferedOutputStream(
	    		Files.newOutputStream( p, CREATE, APPEND ) 
	    		)
	    ) {
	    	out.write( data, 0, data.length );
	    	return true;
	    } catch (IOException e) {
	    	e.printStackTrace();
	    	return false;
	    }
	}
	
	
	
	/**
	 * Overwrites existing data in file
	 * 
	 * @param s
	 * @param outputFile
	 * @return
	 */
	public static boolean writeToFile( StringBuilder sb, String outputFile ) {
		byte data[] = null;
		try {
			data = sb.toString().getBytes("UTF8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
	    Path p = Paths.get(outputFile);
	    try ( BufferedOutputStream out = new BufferedOutputStream(
	    		Files.newOutputStream( p, CREATE, TRUNCATE_EXISTING ) 
	    		)
	    ) {
	    	out.write( data, 0, data.length );
	    	return true;
	    } catch (IOException e) {
	    	e.printStackTrace();
	    	return false;
	    }
	}
	
	
}
