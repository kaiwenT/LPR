package cn.wtu.cs.plateocr.match;


import android.graphics.Bitmap;
import android.graphics.Color;

/*
 * @功能 定位车牌区域
 */
public class CheckPlate {

	public static Bitmap Math(int XMin, int YMin, int XMax, int YMax, Bitmap tagBmp) {
		
		int JumpColorL = 0, JumpColorR = 0, X = 0, Y = 0, YJump = 0, JNum = 0;
		int W = XMax - XMin, H = YMax - YMin;
		
		Bitmap oriBmp = Bitmap.createBitmap(tagBmp, XMin, YMin, W + 1, H);
		
		int oriDst[] = new int[(W + 1) * H];
		int oriPos, oriPixColor, oriPixColor1;
		int oriR = 0, oriG = 0, oriB = 0, oriR1 = 0, oriG1 = 0, oriB1 = 0;
		oriBmp.getPixels(oriDst, 0, W + 1, 0, 0, W + 1, H);
		
		Bitmap bitmap = Bitmap.createBitmap(W, H, Bitmap.Config.ARGB_8888);
		int tagDst[] = new int[W * H];
		int tagPos, tagPixColor;
		int tagR = 0, tagG = 0, tagB = 0;
		bitmap.getPixels(tagDst, 0, W, 0, 0, W, H);
		
		JNum = 0;
		for (Y = 0; Y < H; Y++) {
		    YJump = 0; 
		    for (X = 0; X < W; X++) {
		    	oriPos = Y * (W + 1) + X;
		    	oriPixColor = oriDst[oriPos]; 
		    	oriR = Color.red(oriPixColor); oriG = Color.green(oriPixColor); oriB = Color.blue(oriPixColor); 
		    	oriPixColor1 = oriDst[oriPos + 1]; 
		    	oriR1 = Color.red(oriPixColor1); oriG1 = Color.green(oriPixColor1); oriB1 = Color.blue(oriPixColor1); 
		    	
		    	tagPos = Y * W + X;
		    	tagPixColor = tagDst[tagPos]; 
		    	tagR = Color.red(tagPixColor); tagG = Color.green(tagPixColor); tagB = Color.blue(tagPixColor); 
		    	
		    	JumpColorL = (int) Math.round(oriR * 0.3f + oriG * 0.59f + oriB * 0.11f);
			    JumpColorR = (int) Math.round(oriR1 * 0.3f + oriG1 * 0.59f + oriB1 * 0.11f);
		    	
			    tagR = JumpColorL; tagG = JumpColorL; tagB = JumpColorL;  
			    
			    tagDst[tagPos] = Color.rgb(tagR, tagG, tagB);

			    if (Math.abs(JumpColorL - JumpColorR) > 20) YJump ++;
		    }
		    if (YJump > 10) JNum ++;
		}
		
		bitmap.setPixels(tagDst, 0, W, 0, 0, W, H);
		
		
		if (JNum > 8) {
		
			int PlateX = W, PlateY = H;
			
			bitmap = OtsuAlgorithm.Math(PlateX, PlateY, bitmap);
			bitmap = CutFrame.Math(PlateX, PlateY, bitmap);
			bitmap = ClearWhite.Math(bitmap);
			
			PlateX = bitmap.getWidth(); PlateY = bitmap.getHeight();
			
			bitmap = CharSegmentation.Math(PlateX, PlateY, bitmap);
			
			int hingPlateXState = PlateNumberGroup.PlateXState[PlateNumberGroup.PlateXState.length - 1]; 
			if ((hingPlateXState > 6) && ( hingPlateXState <= 12)) PlateNumberGroup.AlreadyChecked = true;
		}
		
		return bitmap;
	}
	
}