package cn.wtu.cs.plateocr.match;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/*
 * @功能 分割
 */
public class CharSegmentation {

	public static Bitmap Math(int PlateX, int PlateY, Bitmap oriBmp) {
			
		int DrawP = 0, K = 0, MinJump = 0, KN = 0, X = 0, TX = 0, JG = 0, Y = 0, SZ = 0;
		int[] GroupX = new int[300];
		
		int dst[] = new int[PlateX * PlateY];
		int pos, pixColor, b;
		
		oriBmp.getPixels(dst, 0, PlateX, 0, 0, PlateX, PlateY);
		
		for (X = 0; X <= PlateX; X++) GroupX[X] = 0;
		
		for (Y = 0; Y < PlateY; Y++) {
			for (X = 0; X < PlateX; X++) {
				pos = Y * PlateX + X;
				pixColor = dst[pos]; 
				b = Color.blue(pixColor); 
				GroupX[X] = GroupX[X] + b;
			    if (Y == PlateY - 1) if (GroupX[X] > MinJump) MinJump = GroupX[X];
			}
		}
		
		Paint paint = new Paint();
		paint.setStrokeWidth(1.0f);
		paint.setColor(Color.RED);
		
		Bitmap bitmap = Bitmap.createBitmap(PlateX, PlateY, Bitmap.Config.ARGB_8888);
		bitmap.setPixels(dst, 0, PlateX, 0, 0, PlateX, PlateY);
		
		Canvas canvas = new Canvas(bitmap);
		
		KN = 0; K = 0; TX = 0; JG = 0; SZ = 0;
		for (X = 0; X < PlateX; X++) {
			DrawP = 0;
			if (Math.abs(GroupX[X] - MinJump) < 5 ) {
				if (X - KN > 4) 
					DrawP = 1;
				else if ((X - KN > 2) && (Math.abs(GroupX[X] - GroupX[X - 1]) >= 1020)) 
					DrawP=1;
				if (DrawP == 1) {
					if (TX == 0) {
						if (X < 6) 
							TX = X; 
						else {
							if (X > 15) {
								if (TX > (X- TX - 4) / 2) {
									canvas.drawLine(TX, 0, TX, PlateY, paint);
									K ++; PlateNumberGroup.PlateXState[K] = TX; JG = X - TX - 4; TX = X;
								}
							}
							canvas.drawLine(X, 0, X, PlateY, paint);
							K ++; PlateNumberGroup.PlateXState[K] = X; TX = X;
						}
					} else {
						if (JG == 0) {
							canvas.drawLine(X, 0, X, PlateY, paint);
							K ++; PlateNumberGroup.PlateXState[K] = X; JG = X - TX - 4; TX = X;
						} else {
							if ((SZ == 0) && (X - TX > JG)) {
								canvas.drawLine(X, 0, X, PlateY, paint);
								K ++; PlateNumberGroup.PlateXState[K] = X; TX = X; SZ = 1;
							} else {
								if (SZ != 0) {
									if (K > 12) break;
									canvas.drawLine(X, 0, X, PlateY, paint);
									K ++; PlateNumberGroup.PlateXState[K] = X; TX = X;
								}
							}
						}
					}
				}
				KN = X;
		    }
		}
		
	    canvas.save(Canvas.ALL_SAVE_FLAG);  
	    canvas.restore();
		
	    if (PlateX - PlateNumberGroup.PlateXState[K] > 10) { K ++; PlateNumberGroup.PlateXState[K] = PlateX; }
	    PlateNumberGroup.PlateXState[PlateNumberGroup.PlateXState.length - 1] = K;   
	    
	    return bitmap;
	}
}