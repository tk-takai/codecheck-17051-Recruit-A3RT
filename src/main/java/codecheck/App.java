package codecheck;

import codecheck.mypackage.MyUtil;

public class App {
	public static void main(String[] args) {
		System.out.println(MyUtil.getImageInfluence(API_KEY_INFLUENCE, "1", "...../images/sample.img"));
	}
}
