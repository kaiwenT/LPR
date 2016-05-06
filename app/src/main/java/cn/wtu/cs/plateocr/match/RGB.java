package cn.wtu.cs.plateocr.match;

import android.graphics.Color;

/*
 * @功能 RGB存放类
 */
public final class RGB {

	public float R;
	public float G;
	public float B;
	
	public int iR;
	public int iG;
	public int iB;
	
	RGB(float fR, float fG, float fB) {
		R = fR; G = fG; B = fB;
	}
	
	RGB(int color) {
		iR = Color.red(color);
		iG = Color.green(color);
		iB = Color.blue(color);
	}
	
}