package cn.wtu.cs.plateocr.match;


import android.graphics.Bitmap;
import android.graphics.Color;

/*
 * @功能 二值化
 */
public class OtsuAlgorithm {
	
	public static int getGray(int color) {  
   
        int r = Color.red(color);  
        int g = Color.green(color);  
        int b = Color.blue(color); 

        return (r + g + b) / 3;  
    }
	
	public static Bitmap Math(int PlateX, int PlateY, Bitmap oriBmp) {

		  int Row = 0, Col = 0;
		  int I = 0, T = 0;
		  double[] N = new double[257], P = new double[257]; 
		  double[] Sum_Pi = new double[257], Sum_Pi_i = new double[257];
		  double W1 = 0.0d, W2 = 0.0d, U1 = 0.0d, U2 = 0.0d;
		  double Sigma_1_square = 0.0d, Sigma_2_square = 0.0d, Sigma_w_square = 0.0d;
		  double MinVariance = 0.0d;
		  int BlackNum = 0, WhiteNum = 0, OptialT = 0;
		  int R = 0, G = 0, B = 0;
		  
		  MinVariance = 1.7e+308;
		  int SumPixel = PlateX * PlateY;
		  
		  int dst[] = new int[SumPixel];
		  int pos, pixColor, pixGray;
		  //RGB pixGray;
			
		  oriBmp.getPixels(dst, 0, PlateX, 0, 0, PlateX, PlateY);
		
		  for (I = 0; I <= 256; I++) N[I] = 0.0d;
		  for (Row = 0; Row < PlateY; Row++) {
		    for (Col = 0; Col < PlateX; Col++) { 
		    	pos = Row * PlateX + Col;
				pixColor = dst[pos]; 
				pixGray = getGray(pixColor);
				N[pixGray] = N[pixGray] + 1;
		    }
		  }
		  
		  for (I = 0; I < 256; I++) P[I] = N[I]/SumPixel;
		  
		  Sum_Pi[0]   = P[0];
		  Sum_Pi_i[0] = 0.0d;
		  
		  for (I = 1; I < 256; I++) {
			  Sum_Pi[I]   = Sum_Pi[I - 1] + P[I];
			  Sum_Pi_i[I] = Sum_Pi_i[I-1] + P[I] * I;
		  }
		  
		  for (T = 1; T < 255; T++) {
			  W1 = Sum_Pi[T];
			  if (Math.abs(W1) < 1e-9) W1 = 1e-9;
			  W2 = 1.0d - W1;
			  if (Math.abs(W2) < 1e-9) W2 = 1e-9;
			  U1 = Sum_Pi_i[T] / W1;
			  U2 = (Sum_Pi_i[255] - Sum_Pi_i[T]) / W2;
			  Sigma_1_square = 0.0d;
			  for (I = 0; I <= T; I++) Sigma_1_square = Sigma_1_square + ((double)I - U1) * ((double)I - U1) * P[I];
			  Sigma_1_square = Sigma_1_square / W1;
			  Sigma_2_square = 0.0d;
			  for (I = T + 1; I <= 255; I++) Sigma_2_square = Sigma_2_square + ((double)I - U2) * ((double)I - U2) * P[I];
			  Sigma_2_square = Sigma_2_square / W2;
			  Sigma_w_square = W1 * Sigma_1_square + W2 * Sigma_2_square;
			  if (Sigma_w_square < MinVariance) { MinVariance = Sigma_w_square; OptialT = T; }
		  }
		  
		  BlackNum = 0; WhiteNum = 0;
		  for (Row = 0; Row < PlateY; Row++) {
			  for (Col = 0; Col < PlateX; Col++) {
				  pos = Row * PlateX + Col;
				  pixColor = dst[pos]; 
				  R = Color.red(pixColor);
				  G = Color.green(pixColor);
				  B = Color.blue(pixColor);
			      if (B > OptialT) {
				      R = 255;
				      G = 255;
				      B = 255;
				      WhiteNum ++;
			      } else {
			    	  R = 0;
				      G = 0;
				      B = 0;
				      BlackNum ++;
			      }
			      dst[pos] = Color.rgb(R, G, B);
			  }
		  }
		  
		  Bitmap bitmap = Bitmap.createBitmap(PlateX, PlateY, Bitmap.Config.ARGB_8888);
			
		  bitmap.setPixels(dst, 0, PlateX, 0, 0, PlateX, PlateY);
		  
		  if (BlackNum > WhiteNum) {
			  bitmap = AntiColor.Match(PlateY, PlateX, bitmap);
			  PlateNumberGroup.CPYS = "[蓝牌]";
		  } else {
			  PlateNumberGroup.CPYS = "[黄牌]";
		  }
		  
		  return bitmap;
	}
	
}