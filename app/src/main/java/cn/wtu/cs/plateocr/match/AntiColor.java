package cn.wtu.cs.plateocr.match;

import android.graphics.Bitmap;
import android.graphics.Color;

/*
 * @功能 反色
 */
public class AntiColor {

	public static Bitmap Match(int H, int W, Bitmap oriBmp) {
		
		int X = 0, Y = 0;
		int iR = 0, iG = 0, iB = 0;
		int dst[] = new int[W * H];
		int pos = 0, pixColor = 0;
		
		oriBmp.getPixels(dst, 0, W, 0, 0, W, H);
		
		for (Y = 0; Y < H; Y++) {
			for (X = 0; X < W; X++) {
				pos = Y * W + X;
				pixColor = dst[pos]; 
				iR = Color.red(pixColor); iG = Color.green(pixColor); iB = Color.blue(pixColor); 
				if (iB == 0) {
					iB = 255; iG = 255; iR = 255;
				} else {
					iB = 0; iG = 0; iR = 0;
				}
				dst[pos] = Color.rgb(iR, iG, iB);
			}
		}
		
		Bitmap bitmap = Bitmap.createBitmap(W, H, Bitmap.Config.ARGB_8888);
		
		bitmap.setPixels(dst, 0, W, 0, 0, W, H);
		
		return bitmap;
	}
	
}
