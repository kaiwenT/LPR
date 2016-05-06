package cn.wtu.cs.plateocr.match;

import android.graphics.Bitmap;
import android.graphics.Color;

/*
 * @功能 去边框
 */
public class CutFrame {

	public static int Accum(Bitmap ABmp, int[][] ASign, int AX, int AY, int AAccumValue) {
		
		AAccumValue = AAccumValue + 1;
		ASign[AX][AY] = 1;
		if ((AX + 1 < ABmp.getWidth()) && (ABmp.getPixel(AX + 1, AY) == Color.BLACK) && (ASign[AX + 1][AY] == 0)) {
			return Accum(ABmp, ASign, AX + 1, AY, AAccumValue);
		} else {
			return AAccumValue;
		}
	}
	
	public static Bitmap Math(int PlateX, int PlateY, Bitmap oriBmp) {
		
		int[][] Sign = new int[PlateX + 6][PlateY + 6];
		int MiddleY = PlateY / 2;
		int MiddleX = PlateX / 2;
		int DMY = (int)(PlateY * 0.35f);
		int DMX = (int)(PlateX * 0.4f);
		int XJump = 0, VMax = 0, AccumValue = 0, AccumMax = 0, X = 0, Y = 0;
		int R = 0, G = 0, B = 0;
		int qB = 0;
		
		int dst[] = new int[PlateX * PlateY];
		int pos, pixColor;
		
		oriBmp.getPixels(dst, 0, PlateX, 0, 0, PlateX, PlateY);
		
		for (X = 0; X <= PlateX; X++) for (Y = 0; Y <= PlateY; Y++) Sign[X][Y] = 0; 

		for (Y = 0; Y < PlateY; Y++) {
			if (Math.abs(Y - MiddleY) < DMY) continue;
		    AccumMax = 0; XJump = 0;
		    for (X = 0; X < PlateX; X++) {
		    	pos = Y * PlateX + X;
				pixColor = dst[pos];
				R = Color.red(pixColor); G = Color.green(pixColor); B = Color.blue(pixColor); 
				if (pos > 0) {
					qB = Color.blue(dst[pos - 1]);
				} else {
					qB = B;
				}
				AccumValue = 0;
				if ((B == 0) && (Sign[X][Y] == 0)) {
					AccumValue = Accum(oriBmp, Sign, X + 1, Y, AccumValue);
				}
		    	if ((X > 1) && (B != qB)) {
		    		if ((X > MiddleX - DMX) && (XJump == 0) || (X - MiddleX > 0) && (XJump < 5)) {
		    			B = qB;
		    			G = B;
		    			R = B;
		    			dst[pos] = Color.rgb(R, G, B);
		    		} else {
		    			XJump ++;
		    		}
		    	}
		    	if (AccumMax < AccumValue) AccumMax = AccumValue;
		    }
		    if ((AccumMax > 15) || (XJump < 8)) {
		    	for (X = 0; X < PlateX; X++) {
		    		B = 255;
		    		G = B;
	  				R = B;
	  				pos = Y * PlateX + X;
	  				dst[pos] = Color.rgb(R, G, B);
		    	}
		    }
		    if (AccumMax > VMax) VMax = AccumMax;
		}

		Bitmap bitmap = Bitmap.createBitmap(PlateX, PlateY, Bitmap.Config.ARGB_8888);
		
		bitmap.setPixels(dst, 0, PlateX, 0, 0, PlateX, PlateY);
		
		return bitmap;
	}
	
}