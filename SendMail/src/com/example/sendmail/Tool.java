package com.example.sendmail;

public class Tool {

	public static boolean checkEmail(String email) {
		String reg = "\\w+[\\w]*@[\\w]+\\.[\\w]+$";
		if (email.matches(reg)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static String secret(String content) {
		byte[] b = content.getBytes();
		for (int i = 0; i < b.length; i++) {
			b[i] += 1;
		}
		return new String(b);
	}
}
