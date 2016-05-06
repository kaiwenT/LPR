package cn.wtu.cs.plateocr.match;

import android.graphics.Color;

/*
 * @功能 HLS处理函数群
 */
public final class HLSConvert {

	public static int RGB(int r, int g, int b) {

		return Color.rgb(r, g, b);
	}
	
	public static float MyMax(float A, float B) {
		
		if (A >= B) return A; else return B;
	}

	public static float MyMin(float A, float B) {
	
		if (A <= B) return A; else return B;
	}

	public static HLS RGBToHLS(float R, float G, float B) {
		
		float CB = 0.0f, CR = 0.0f, CG = 0.0f, M1 = 0.0f, M2 = 0.0f;
		float H = 0.0f, L = 0.0f, S = 0.0f;
		
		M1 = MyMax(R, G); M1 = MyMax(M1, B);
		M2 = MyMin(R, G); M2 = MyMin(M2, B);
		L = (M1 + M2) / 2.0f;
		if (M1 == M2) {
			S = 0.0f;
			H = 360.0f;
		} else {
			if (L <= 0.5f) S = (M1 - M2) / (M1 + M2); else S = (M1 - M2) / (2.0f - M1 - M2);
			CR = (M1 - R) / (M1 - M2);
			CG = (M1 - G) / (M1 - M2);
			CB = (M1 - B) / (M1 - M2);
			if (R == M1) H = CB - CG;
			if (G == M1) H = 2.0f + CR - CB;
			if (B == M1) H = 4.0f + CG - CR;
			H = 60.0f * H;
			if (H < 0.0f) H = H + 360.0f;
		}
		
		return new HLS(H, L, S);
	}

	public static float MyRGB(float H, float M1, float M2) {
		
		float Value = 0.0f;
		
		if (H < 0.0f) H = H + 360.0f;
		if (H > 360.0f) H = H - 360.0f;
		if (H < 60.0f) Value = M1 + (M2 - M1) * H / 60.0f;
		if ((H >= 60.0f) && (H < 180.0f)) Value = M2;
		if ((H >= 180.0f) && (H < 240.0f)) Value = M1 + (M2 - M1) * (240.0f - H) / 60.0f;
		if ((H >= 240.0f) && (H <= 360.0f)) Value = M1;
		
		return Value;
	}

	public static RGB HLStoRGB(float H, float L, float S) {
		
		float M1 = 0.0f, M2 = 0.0f, Value = 0.0f;
		float R = 0.0f, G = 0.0f, B = 0.0f;

		if (L <= 0.5f) M2 = L * (1.0f + S); else M2 = L + S - L * S;
		M1 = 2.0f * L - M2;
		if (S == 0.0f) {
			if (H == 360.0f) {
				R = L;
				G = L;
				B = L;
			} 
		} else {
			Value = 0.0f;
			Value = MyRGB(H + 180.0f, M1, M2);
			R = Value;
			Value = MyRGB(H, M1, M2);
			G = Value;
			Value = MyRGB(H + 240.0f, M1, M2); 
			B = Value;
		}
		
		return new RGB(R, G, B);
	}

}