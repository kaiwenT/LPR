package cn.wtu.cs.plateocr.match;

import android.graphics.Bitmap;

/*
 * @功能 分割获取单字
 */
public class SegInEachChar {

	public static Bitmap[] Math(Bitmap oriBmp) {
		
		int X = 0, Y = 0;
		
		int M = 1;
		if (PlateNumberGroup.PlateXState[1] >= 6) { M = 0; PlateNumberGroup.PlateXState[0] = 0; }
			
		int H = oriBmp.getHeight();
		
		int W1 = PlateNumberGroup.PlateXState[M + 1];
		Bitmap bitmap1 = Bitmap.createBitmap(W1, H, Bitmap.Config.ARGB_8888);
		int W2 = PlateNumberGroup.PlateXState[M + 2];
		Bitmap bitmap2 = Bitmap.createBitmap(W2 - W1 - 1, H, Bitmap.Config.ARGB_8888);
		int W3 = PlateNumberGroup.PlateXState[M + 3];
		Bitmap bitmap3 = Bitmap.createBitmap(W3 - W2 - 1, H, Bitmap.Config.ARGB_8888);
		int W4 = PlateNumberGroup.PlateXState[M + 4];
		Bitmap bitmap4 = Bitmap.createBitmap(W4 - W3 - 1, H, Bitmap.Config.ARGB_8888);
		int W5 = PlateNumberGroup.PlateXState[M + 5];
		Bitmap bitmap5 = Bitmap.createBitmap(W5 - W4 - 1, H, Bitmap.Config.ARGB_8888);
		int W6 = PlateNumberGroup.PlateXState[M + 6];
		Bitmap bitmap6 = Bitmap.createBitmap(W6 - W5 - 1, H, Bitmap.Config.ARGB_8888);
		int W7 = PlateNumberGroup.PlateXState[M + 7];
		Bitmap bitmap7 = Bitmap.createBitmap(W7 - W6 - 1, H, Bitmap.Config.ARGB_8888);
		
		for (Y = 0; Y < H; Y++) {
			
			for (X = 0; X < W1; X++)
				bitmap1.setPixel(X, Y, oriBmp.getPixel(X, Y));
			
			for (X = W1 + 1; X < W2; X++)
				bitmap2.setPixel(X - W1 - 1, Y, oriBmp.getPixel(X, Y));	
		
			for (X = W2 + 1; X < W3; X++)
				bitmap3.setPixel(X - W2 - 1, Y, oriBmp.getPixel(X, Y));	      
			      
			for (X = W3 + 1; X < W4; X++)
				bitmap4.setPixel(X - W3 - 1, Y, oriBmp.getPixel(X, Y));	 
			
			for (X = W4 + 1; X < W5; X++)
				bitmap5.setPixel(X - W4 - 1, Y, oriBmp.getPixel(X, Y));	 
			
			for (X = W5 + 1; X < W6; X++)
				bitmap6.setPixel(X - W5 - 1, Y, oriBmp.getPixel(X, Y));	 
			
			for (X = W6 + 1; X < W7; X++)
				bitmap7.setPixel(X - W6 - 1, Y, oriBmp.getPixel(X, Y));	 
			
		}
		
		return new Bitmap[] { bitmap1, bitmap2, bitmap3, bitmap4, bitmap5, bitmap6, bitmap7 };
	}

}