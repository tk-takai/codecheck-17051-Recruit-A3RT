package codecheck;

import codecheck.mypackage.MyUtil;

public class App {
	public static void main(String[] args) {
		System.out.println(MyUtil.getImageInfluence(args[0], "1", "/root/codecheck/images/sample.jpg"));
	}
}
