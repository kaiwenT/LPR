package cn.wtu.cs.plateocr.match;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

/*
 * @功能 定位
 */
public class Oritenation {

	public static Bitmap Math(Bitmap oriBmp, Bitmap tagBmp) {

		PlateNumberGroup.AlreadyChecked = false;
		
		Bitmap bitmap = Bitmap.createBitmap(tagBmp);
		
		int W = oriBmp.getWidth(), H = oriBmp.getHeight();
		int dst[] = new int[W * H];

		oriBmp.getPixels(dst, 0, W, 0, 0, W, H);
		
		int AlreadyFound = 150;
		int DX = 0, DY = 0, X0 = 0, Y0 = 0, M = 0, XT = 0, YT = 0, X = 0, Y = 0;
		int XMax = 0, YMax = 0, XMin = 0, YMin = 0;
		int BColor = 0, FColor = 0, TColor = 0, Y0X0Color = 0;
		int[] StackX = new int[160000], StackY = new int[160000];
		int Len = 0, Wid = 0;
		
		RGB P, Q, QT, PT;
		int[] PTYs = new int[3];
		
		boolean Loop = false;
		
		for (Y = 1; Y <= H - 2; Y++) {
			for (X = 1; X <= W - 2; X++) {
				Q = new RGB(dst[Y * W + X]);  
				BColor = Q.iB + Q.iG + Q.iR;
				if (BColor == 450) continue;
				M = 0; StackX[M] = X; StackY[M] = Y;
				P = new RGB(dst[Y * W + X]);
				TColor = P.iB + P.iG + P.iR;
				XMax = -1; YMax = -1; XMin = X; YMin = Y;
				Loop = false;
				while (M != -1) {
					X0 = StackX[M]; Y0 = StackY[M]; M --;
					QT = new RGB(dst[Y0 * W + X0]); 
					Y0X0Color = QT.iB + QT.iG + QT.iR;
					if (Y0X0Color != TColor) continue;
					if (Y0X0Color == 450) continue;
					QT.iR = AlreadyFound; QT.iG = AlreadyFound; QT.iB = AlreadyFound; 
					dst[Y0 * W + X0] = Color.rgb(QT.iR, QT.iG, QT.iB);
					if ((Y0 - 1 <= 0) || (Y0 + 1 >= H - 1)) continue;
					PTYs[0] = Y0 - 1; PTYs[1] = Y0; PTYs[2] = Y0 + 1;
					for (DY = -1; DY <= 1; DY++) {
						for (DX = -1; DX <= 1; DX++) {
							if ((DX + DY == 2) || (DX + DY == 0) || (DX + DY == -2)) continue;
							XT = X0 + DX; YT = Y0 + DY;
							if ((XT <= 0) || (XT > W - 1)) continue;
							PT = new RGB(dst[PTYs[DY + 1] * W + XT]);
							FColor = PT.iB + PT.iG + PT.iR;
							if (FColor == TColor) {
								Q = new RGB(dst[Y * W + XT]);
								Q.iR = AlreadyFound; Q.iG = AlreadyFound; Q.iB = AlreadyFound;
								dst[Y * W + XT] = Color.rgb(Q.iR, Q.iG, Q.iB);
								M ++; StackX[M] = XT; StackY[M] = YT;
								XMax = Math.max(XMax, XT); XMin = Math.min(XMin, XT);
					            YMax = Math.max(YMax, YT); YMin = Math.min(YMin, YT);
							}
						}
					}
					if (M > 5999) { Loop = true; break; }
				}
				if (!Loop) {
					if ((XMin >= XMax) || (YMin >= YMax)) continue;
					Len = XMax - XMin; Wid = YMax - YMin;
				    if ((Len <= PlateNumberGroup.LenMin) || (Len >= PlateNumberGroup.LenMax)) continue;
				    if ((Wid <= PlateNumberGroup.WidMin) || (Wid >= PlateNumberGroup.WidMax)) continue;
				    if (Math.abs(Len / Wid - PlateNumberGroup.PlateDiv) > PlateNumberGroup.DDiv) continue;
				    
				    Log.i("plateocr", "PlateArea：XMin:"+String.valueOf(XMin)+", YMin:"+String.valueOf(YMin)+", XMax:"+String.valueOf(XMax)+", YMax:"+String.valueOf(YMax));
				    
				    bitmap = CheckPlate.Math(XMin, YMin, XMax, YMax, tagBmp);
				    
				    if (PlateNumberGroup.AlreadyChecked) return bitmap;
				}
			}
		}
		
		return bitmap;
	}
	
}