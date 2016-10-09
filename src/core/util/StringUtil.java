/**
 * Project Name:cxkm
 * File Name:StringUtil.java
 * Package Name:cn.cxkm.util
 *
 *   ver     createdate      	author			mail
 * ─────────────────────────────────────────────────────────────────
 *   1.0	2013-4-7上午9:51:49		lidl 		lidaling@foxmail.com
 *
 * Copyright (c) 2012,  All Rights Reserved by lidl.
 */

package core.util;

 

import java.io.IOException;
import java.io.StringReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sun.misc.BASE64Decoder;

 

/**
 * ClassName:StringUtil.java
 * Function: 
 * ADD FUNCTION Reason:  
 *
 * @author lidl
 *
 * @Date 2013-4-7上午9:51:49
 */
public class StringUtil {

	/**
	 * MD5加密类
	 * @param str 要加密的字符串
	 * @return    加密后的字符串
	 */
	public static String toMD5(String str){
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte[]byteDigest = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < byteDigest.length; offset++) {
				i = byteDigest[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			//32位加密
			return buf.toString();
			// 16位的加密
			//return buf.toString().substring(8, 24); 
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	

	// 将 s 进行 BASE64 编码
		public static String getBASE64(byte[] s) {
			if (s == null)
				return null;
			return (new sun.misc.BASE64Encoder()).encode(s );
		}

		// 将 BASE64 编码的字符串 s 进行解码
		public static byte[] getFromBASE64(String s) {
			if (s == null)
				return null;
			BASE64Decoder decoder = new BASE64Decoder();
			try {
				byte[] b = decoder.decodeBuffer(s);
				return b;
			} catch (Exception e) {
				return null;
			}
		}
	
	/**
	 * 去掉null
	 * 
	 * @param des
	 * @return
	 */
	public static String trimNull(Object des) {
		try {
			if (des == null)
				return "";
			else
				return des.toString().trim();
		} catch (Exception npe) {
			return "";
		}
	}
	
	/**
	 * 
	 *  Function:
	 *  @author TongXin  DateTime 2013年7月9日 上午9:13:02
	 *  @param des
	 *  @return
	 */ 
	/*public static String trimZero(Object des) {
		try {
			if (des == null)
				return "0";
			else
				return des.toString().trim();
		} catch (Exception npe) {
			return "0";
		}
	}*/
	 
	/**
	 * 
	 *  Function:
	 *  @author TongXin  DateTime 2013-5-28 下午06:34:30
	 *  @param str
	 *  @return
	 */
	public static String replaceBlank(String str) {  
		  
        String dest = "";  
  
        if (str != null) {  
  
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");  
  
            Matcher m = p.matcher(str);  
  
            dest = m.replaceAll("");  
  
        }  
  
        return dest;  
  
    }  
	
	/**
	 * 
	 *  Function:替换斜杠
	 *  @author TongXin  DateTime 2015年5月22日 上午11:41:45
	 *  @param str
	 *  @return
	 */
	public static String replaceSlash(String str) {  
		  
        String dest = "";  
  
        if (str != null) {  
  
            Pattern p = Pattern.compile("\\\\");  
  
            Matcher m = p.matcher(str);  
  
            dest = m.replaceAll("");  
  
        }  
  
        return dest;  
  
    }  
	
	public static void main(String[] args) {
		 
		String str=StringUtil.toMD5("123456");
		System.out.println(str);
	 
	}
	
	/**
	 * 
	 *  Function:
	 *  @author TongXin  DateTime 2015年12月2日 上午11:30:06
	 *  @param jsonStr
	 *  @return
	 */
	 public static String format(String jsonStr) {
		    int level = 0;
		    StringBuffer jsonForMatStr = new StringBuffer();
		    for(int i=0;i<jsonStr.length();i++){
		      char c = jsonStr.charAt(i);
		      if(level>0&&'\n'==jsonForMatStr.charAt(jsonForMatStr.length()-1)){
		        jsonForMatStr.append(getLevelStr(level));
		      }
		      switch (c) {
		      case '{': 
		      case '[':
		        jsonForMatStr.append(c+"\n");
		        level++;
		        break;
		      case ',': 
		        jsonForMatStr.append(c+"\n");
		        break;
		      case '}':
		      case ']':
		        jsonForMatStr.append("\n");
		        level--;
		        jsonForMatStr.append(getLevelStr(level));
		        jsonForMatStr.append(c);
		        break;
		      default:
		        jsonForMatStr.append(c);
		        break;
		      }
		    }
		    
		    return jsonForMatStr.toString();

		  }
	 
	 private static String getLevelStr(int level){
		    StringBuffer levelStr = new StringBuffer();
		    for(int levelI = 0;levelI<level ; levelI++){
		      levelStr.append("\t");
		    }
		    return levelStr.toString();
		  }
	
 
}

