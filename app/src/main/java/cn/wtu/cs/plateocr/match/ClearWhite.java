package cn.wtu.cs.plateocr.match;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

/*
 * @功能 去除上下左右的空白
 */
public final class ClearWhite {
	
	public static Bitmap Math(Bitmap oriBmp) {
		
		int X, Y, Y1, Y2, X1, X2, WX1, WX2, WY1, WY2, Whites;
		
		int Width = oriBmp.getWidth();
		int Height = oriBmp.getHeight();
		
		Y1 = -1; Y2 = -1; X1 = -1; X2 = -1;
		for (Y = 0; Y < Height; Y++) {
		    for (X = 0; X < Width; X++) {	
		    	if (oriBmp.getPixel(X, Y) == Color.BLACK) {
		    		if (Y1 == -1) Y1 = Y;
		    		Y2 = Y;
		    		if (X1 == -1) X1 = X; else if (X1 > X) X1 = X;
		    		if (X2 == -1) X2 = X; else if (X2 < X) X2 = X;
		    	}
		    }
		}
		
		if (Y1 == -1 || Y2 == -1 || X1 == -1 || X2 == -1) return oriBmp;
		
		WY1 = Y1;
		for (Y = WY1; Y <= WY1 + 20; Y++) {
			Whites = 0;
		    for (X = 0; X < Width; X++) 
		    	if (oriBmp.getPixel(X, Y) == Color.WHITE) Whites++;
		    if (Whites == Width) if (Y == WY1) Y1 = WY1; else Y1 = Y + 1;
		}		
		WY2 = Y2;
		for (Y = WY2; Y >= WY2 - 21; Y--) {
			Whites = 0;
		    for (X = 0; X < Width; X++) 
		    	if (oriBmp.getPixel(X, Y) == Color.WHITE) Whites++;
		    if (Whites == Width) if (Y == WY2) Y2 = WY2; else Y2 = Y - 1;
		}

		WX1 = X1;
		for (X = WX1; X <= WX1 + 6; X++) {
			Whites = 0;
		    for (Y = Y1; Y <= Y2; Y++)
		    	if (oriBmp.getPixel(X, Y) == Color.WHITE) Whites++;
		    if (Whites == (Y2 - Y1 + 1)) if (X == WX1) X1 = WX1; else X1 = X + 1;
		}
		WX2 = X2;
		for (X = WX2; X >= WX2 - 7; X--) {
			Whites = 0;
			for (Y = Y1; Y <= Y2; Y++)
				if (oriBmp.getPixel(X, Y) == Color.WHITE) Whites++;
		    if (Whites == (Y2 - Y1 + 1)) if (X == WX2) X2 = WX2; else X2 = X - 1;
		}
		
		int sWidth = X2 - X1 + 1, sHeight = Y2 - Y1 + 1; 
		
		Bitmap sBitmap = Bitmap.createBitmap(sWidth, sHeight, Bitmap.Config.ARGB_8888);
		
		Canvas canvas = new Canvas(sBitmap);
		canvas.drawBitmap(oriBmp, - X1, - Y1, null);
	    canvas.save(Canvas.ALL_SAVE_FLAG);  
	    canvas.restore();
	    
	    return sBitmap;
	}
}
