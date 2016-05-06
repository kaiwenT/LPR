package cn.wtu.cs.plateocr.match;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 *
 * @功能 K值聚内
 */
public class ColorKMeans {

	public static Bitmap Math(Bitmap oriBmp) {
	
		float [] PP = new float[3];
		float MinDis = 0.0f, Distance = 0.0f;
		float R = 0.0f, G = 0.0f, B = 0.0f;
		float H = 0.0f, L = 0.0f, S = 0.0f;
		int WHColor = 0, X = 0, Y = 0, I = 0, K = 0;  
		int iR = 0, iG = 0, iB = 0;
		
		int Width = oriBmp.getWidth();
		int Height = oriBmp.getHeight();

		int dst[] = new int[Width * Height];
		int pos, pixColor;
		
		oriBmp.getPixels(dst, 0, Width, 0, 0, Width, Height);
		
		for (Y = 0; Y < Height; Y++) {
			for (X = 0; X < Width; X++) {
				pos = Y * Width + X;
				pixColor = dst[pos]; 
				iR = Color.red(pixColor); iG = Color.green(pixColor); iB = Color.blue(pixColor); 
				R = (float)iR/255.0f; G = (float)iG/255.0f; B = (float)iB/255.0f; 
				S = 0.0f; L = 0.0f; H = 0.0f;
				HLS hls = HLSConvert.RGBToHLS(R, G, B);
				S = hls.S; L = hls.L; H = hls.H;
				PP[0] = H; PP[1] = L; PP[2] = S; 
				WHColor = 0; MinDis = 10000000.0f;
				for (I = 0; I < 4; I++) {
					Distance = 0.0f;
					for (K = 0; K < 3; K++) {
						Distance = Distance + HLS.Deta[K] * HLS.Deta[K] * (PP[K] - HLS.HLSColor[I * 3 + K]) * (PP[K] - HLS.HLSColor[I * 3 + K]);
					}
					if (MinDis > Distance) { MinDis = Distance; WHColor = I; }
				}
				if (WHColor == 0) { iR = 255; iG = 255; iB = 255; }
      	      	if (WHColor == 1) { iR = 0;   iG = 0;   iB = 255; }
      	      	if (WHColor == 2) { iR = 255; iG = 255; iB = 0;   } 
      	      	if (WHColor == 3) { iR = 0;   iG = 0;   iB = 0;   } 
				dst[pos] = Color.rgb(iR, iG, iB);
			}
		}
			
		Bitmap bitmap = Bitmap.createBitmap(Width, Height, Bitmap.Config.ARGB_8888);
		
		bitmap.setPixels(dst, 0, Width, 0, 0, Width, Height);
		
		return bitmap;
	}
	
}