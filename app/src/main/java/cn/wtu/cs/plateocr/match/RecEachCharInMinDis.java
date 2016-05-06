package cn.wtu.cs.plateocr.match;

import android.graphics.Bitmap;
import android.graphics.Color;

/*
 * @功能 识别车牌号
 */
public class RecEachCharInMinDis {

	//截取Bitmap部分区域
	public static Bitmap CreateBmp(int ALeft, int ARight, int ATop, int ABottom, Bitmap ABmp) {
		
		int W = ARight - ALeft + 1, H = ABottom - ATop + 1;
		Bitmap bitmap = Bitmap.createBitmap(W, H, Bitmap.Config.ARGB_8888);
		int CQ[] = new int[W * H];
		int posCQ = 0;
		bitmap.getPixels(CQ, 0, W, 0, 0, W, H);
		
		int CPW = ABmp.getWidth(), CPH = ABmp.getHeight();
		int CP[] = new int[CPW * CPH];
		int posCP = 0;
		ABmp.getPixels(CP, 0, CPW, 0, 0, CPW, CPH);
		
		int IX = 0, IY = 0;
		
		for (IY = ATop; IY <= ABottom; IY++) {
			for (IX = ALeft; IX <= ARight; IX++) {		
				posCQ = (IY - ATop) * W + (IX - ALeft);	
				posCP = IY * CPW + IX;	
				CQ[posCQ] = CP[posCP];
			}
		}
		
		bitmap.setPixels(CQ, 0, W, 0, 0, W, H);
		
		return bitmap;
	}

	public static Bitmap GetRegion(Bitmap Bmp) {
		
		int X = 0, Y = 0, Top = Bmp.getHeight(), Bottom = -1, Left = Bmp.getWidth(), Right = -1;
	    int W = Bmp.getWidth(), H = Bmp.getHeight();
		
	    int dst[] = new int[W * H];
		int pos = 0, pixColor = 0;
		//获取图片的像素点
		Bmp.getPixels(dst, 0, W, 0, 0, W, H);

		//逐行扫描
	    for (Y = 0; Y < H; Y++) {
	    	for (X = 0; X < W; X++) {
	    		pos = Y * W + X;
	    		pixColor = dst[pos];
	    		int ib = Color.blue(pixColor);
	    		if (ib != 255) {
	    			if (Y < Top) Top = Y;
	    			if (Y > Bottom) Bottom = Y;
	    			if (X < Left) Left = X;
	    			if (X > Right) Right = X;
	    		}
	    	}
	    }
		
	    return CreateBmp(Left, Right, Top, Bottom, Bmp);
	    
	}
	
	public static Bitmap Zoom(Bitmap Bmp) {
	
		return Bitmap.createScaledBitmap(Bmp, 32, 32, false);
	}

	/**
	 *
	 * @param Bmp
	 * @return
	 */
	public static Bitmap ClearSmall(Bitmap Bmp) {
		
		int X = 0, Y = 0, X0 = 0, Y0 = 0, X1 = 0, Y1 = 0;
		int[] STX = new int[8000], STY = new int[8000];
		int M = 0, MaxST = 0, MaxNumber = 0, MaxColor = 0;
		int ClassNum = Color.BLACK;
		int W = Bmp.getWidth(), H = Bmp.getHeight();
		
		Bitmap bitmap = Bitmap.createBitmap(Bmp);
		//
		for (Y = 0; Y < H; Y++) {
			for (X = 0; X < W; X++) {
				if (bitmap.getPixel(X, Y) != Color.BLACK) continue;
				ClassNum = ClassNum + 50;
				bitmap.setPixel(X, Y, ClassNum);
				M = 0; STX[M] = X; STY[M] = Y; MaxST = 0;
				while (M != -1) {			
					X0 = STX[M]; Y0 = STY[M]; M --;			
			        X1 = X0 + 1; Y1 = Y0;
			        if ((X1 < W) && (bitmap.getPixel(X1, Y1) == Color.BLACK)) {
			        	bitmap.setPixel(X1, Y1, ClassNum);
			            M ++; STX[M] = X1; STY[M] = Y1;
			        }    
			        X1 = X0 - 1; Y1 = Y0;
			        if ((X1 >= 0) && (bitmap.getPixel(X1, Y1) == Color.BLACK)) {
			        	bitmap.setPixel(X1, Y1, ClassNum);
			            M ++; STX[M] = X1; STY[M] = Y1;
			        }
			        X1 = X0; Y1 = Y0 + 1;
			        if ((Y1 < H) && (bitmap.getPixel(X1, Y1) == Color.BLACK)) {
			        	bitmap.setPixel(X1, Y1, ClassNum);
			            M ++; STX[M] = X1; STY[M] = Y1;
			        }
			        X1 = X0; Y1 = Y0 - 1;
			        if ((Y1 >= 0) && (bitmap.getPixel(X1, Y1) == Color.BLACK)) {
			        	bitmap.setPixel(X1, Y1, ClassNum);
			            M ++; STX[M] = X1; STY[M] = Y1;
			        }
			        if (MaxST < M) MaxST = M;
				}
				if (MaxNumber < MaxST) {
					MaxColor = ClassNum;
					MaxNumber = MaxST;
				}
			}
		}
		for (Y = 0; Y < H; Y++) {
			for (X = 0; X < W; X++) {      
				if (bitmap.getPixel(X, Y) == MaxColor) 
					bitmap.setPixel(X, Y, Color.BLACK);
				else
					bitmap.setPixel(X, Y, Color.WHITE);
			}
		}
		
		return bitmap;
	}
	
	public static int RGBToColor(int R, int G, int B) {
		return Color.rgb(R, G, B);
	}

	/**
	 *
	 * @param FBmp
	 * @param AMinDis
	 * @param AN1
	 * @return	最小距离存放数组
	 */
	public static MinDisN1 GetJieShiDis(Bitmap FBmp, int[][] AMinDis, int AN1) {
		
		int FX = 0, FY = 0, FDisColor = 0, FMindisTry = 0, FDisTry = 0, FI = 0, FJ = 0, FX0 = 0, FY0 = 0;
		boolean FFlag = false;
		
		AN1 = 0;
		int W = FBmp.getWidth(), H = FBmp.getHeight();
		
		for (FY = 0; FY < H; FY++) {
			for (FX = 0; FX < W; FX++) {
				if (FBmp.getPixel(FX, FY) == Color.WHITE) {
					AMinDis[FY][FX] = 0; AN1 ++;
				} else {
					AMinDis[FY][FX] = -1;
				}
			}
		}
		
		FFlag = false;
		FDisColor = 0;
		
		while (FFlag == false) {
			FDisColor = FDisColor + 40;
			FFlag = true;
	        for (FY = 0; FY < H; FY++) {
	        	for (FX = 0; FX < W; FX++) {
	        		if (AMinDis[FY][FX] != -1) continue;
	        		FMindisTry = 1000;
	        		for (FI = -1; FI <= 1; FI++) {
	        			for (FJ = -1; FJ <= 1; FJ++) {
	        				if ((FI == 0) && (FJ == 0)) continue;
	        				FX0 = FX + FI + 1; FY0 = FY + FJ + 1;
	        				if ((FX0 < 1) || (FX0 > W) || (FY0 < 1) || (FY0 > H)) continue;
	        				if (AMinDis[FY0 - 1][FX0 - 1] == -1) continue;
	        				if (FBmp.getPixel(FX0 - 1, FY0 - 1) != RGBToColor(FDisColor - 40, FDisColor - 40, FDisColor - 40)) continue;
	        				FDisTry = Math.abs(FI) + Math.abs(FJ) + AMinDis[FY0 - 1][FX0 - 1];
	        				if (FMindisTry >= FDisTry) FMindisTry = FDisTry;
	        			}
	        		}
	        		if (FMindisTry < 1000) {
	        			AMinDis[FY][FX] = FMindisTry; FFlag = false;
	        			FBmp.setPixel(FX, FY, RGBToColor(FDisColor, FDisColor, FDisColor));
	        		}
	        	}
	        }
		}
		
		return new MinDisN1(AMinDis, AN1);
	}

	/**
	 *
	 * @param AZFNum
	 * @param AFName
	 * @param AE
	 * @param ABmp
	 * @return
	 */
	public static String XDisRec(int AZFNum, String AFName, String[] AE, Bitmap ABmp) {
		
		int W = 32, H = 32;
		int N1 = 0, N2 = 0, X = 0, Y = 0, I = 0;
		int[][] MinDis = new int[33][33], PPMindis = new int[33][33];
		int PiPeiNum = 0;
	    float PiPeiSub = 0, PiPeiDis = 0, PiPeiDis1 = 0, PiPeiDis2 = 0, PiPeiMin = 0;

		//
	    MinDisN1 mdn = GetJieShiDis(ABmp, MinDis, N1); MinDis = mdn.MinDis; N1 = mdn.N1;
	    MinDisN1 mdn1 = null;
	    Bitmap ocrBmp = null;
	    
	    int dst[] = new int[32 * 32], dst1[] = new int[32 * 32];
		ABmp.getPixels(dst, 0, W, 0, 0, W, H);
		
		RGB oriRGB, ocrRGB;
	    
	    PiPeiMin = 100000; PiPeiNum = -1;
	    //
	    for (I = 0; I <= AZFNum; I++) {
	    	ocrBmp = AssetsResource.getBitmap(AFName + String.valueOf(I) + ".bmp");
	    	mdn1 = GetJieShiDis(ocrBmp, PPMindis, N2); PPMindis = mdn1.MinDis; N2 = mdn1.N1;
	    	PiPeiDis1 = 0; PiPeiSub = 0; PiPeiDis2 = 0;
	    	ocrBmp.getPixels(dst1, 0, 32, 0, 0, 32, 32);
	    	for (Y = 0; Y < H; Y++) {
	    		for (X = 0; X < W; X++) {
	    			oriRGB = new RGB(dst[Y * W + X]);
	    			ocrRGB = new RGB(dst1[Y * W + X]);
	    			if (oriRGB.iB == 255 && ocrRGB.iB == 255) continue;
	    			if (ocrRGB.iB == 0) PiPeiDis2 = PiPeiDis2 + MinDis[Y][X]; 
	    			if (oriRGB.iB == 0) PiPeiDis1 = PiPeiDis1 + PPMindis[Y][X];
	    		}
	    	}
			PiPeiDis1 = PiPeiDis1 / N1;
			PiPeiDis2 = PiPeiDis2 / N2;
			PiPeiDis = (PiPeiDis1 + PiPeiDis2) / 2;
			for (Y = 0; Y < H; Y++) {
	    		for (X = 0; X < W; X++) {
	    			ocrRGB = new RGB(dst1[Y * W + X]);
	    			if (ocrRGB.iB == 255) continue;
	    			PiPeiSub = PiPeiSub + (MinDis[Y][X] - PiPeiDis) * (MinDis[Y][X] - PiPeiDis);
	    		}
			}   
			PiPeiSub = PiPeiSub / N1; PiPeiDis = PiPeiSub; 
			if (PiPeiMin > PiPeiDis) { PiPeiMin = PiPeiDis; PiPeiNum = I; }
	    }
	    
	    return AE[PiPeiNum];
	}

	/**
	 * 基于模板匹配的最小差距法识别字符
	 * @param AZFNum 省份字符个数
	 * @param AFName 字符模板文件名
	 * @param AE	包含各省份字符的数组
	 * @param ABmp 车牌字符原图像
	 * @return	返回模板中和图像差距最小那个字符
	 */
	public static String MinDisRec(int AZFNum, String AFName, String[] AE, Bitmap ABmp) {
		
		int W = 32, H = 32, X = 0, Y = 0, I = 0, MinDisNum = 0, MinDis = 0, OriDis = 0;
	    int dst[] = new int[32 * 32], dst1[] = new int[32 * 32];
		ABmp.getPixels(dst, 0, W, 0, 0, W, H);
		Bitmap ocrBmp = null;
		RGB oriRGB, ocrRGB;
		MinDis = 100000; MinDisNum = 0;
		//从0到AZFNum依次
		for (I = 0; I <= AZFNum; I++) {
			//获取第I个字符模板资源文件
			ocrBmp = AssetsResource.getBitmap(AFName + String.valueOf(I) + ".bmp");
			ocrBmp.getPixels(dst1, 0, 32, 0, 0, 32, 32);
			OriDis = 0;
			//逐行扫描
			for (Y = 0; Y < H; Y++) {
	    		for (X = 0; X < W; X++) {
	    			oriRGB = new RGB(dst[Y * W + X]);
	    			ocrRGB = new RGB(dst1[Y * W + X]);
	    			if (oriRGB.iB == ocrRGB.iB) continue;
	    			OriDis ++;
	    		}
			}
			if (MinDis > OriDis) { MinDisNum = I; MinDis = OriDis; }
		}

		//返回模板中和图像差距最小那个字符
		return AE[MinDisNum];
	}

	//获得汉字-省份简称
	public static String GetHZ(Bitmap Bmp) {
		
		String RLT1 = "", RLT2 = "";
	    String FName = "h";
		//获取字符区域
	    Bmp = GetRegion(Bmp);
		//缩放图像
	    Bmp = Zoom(Bmp);
		//最小距离识别
	    RLT1 = MinDisRec(32, FName, PlateNumberGroup.E1, Bmp);
	    //
		RLT2 = XDisRec(32, FName, PlateNumberGroup.E1, Bmp);
	    
	    return RLT1 + "," + RLT2;
	}

	/**
	 * 获得字母
	 * @param Bmp
	 * @return
	 */
	public static String GetZM(Bitmap Bmp) {
	    
		String FName = "z";
	    Bmp = ClearSmall(Bmp);
	    Bmp = GetRegion(Bmp);
	    Bmp = Zoom(Bmp);
	    return MinDisRec(25, FName, PlateNumberGroup.E3, Bmp);
	}

	/**
	 * 获得数字
	 * @param Bmp
	 * @return
	 */
	public static String GetSZ(Bitmap Bmp) {
		
	    String FName = "s";
		//
	    Bmp = ClearSmall(Bmp);
		//
	    Bmp = GetRegion(Bmp);
	    //
		Bmp = Zoom(Bmp);
	    return MinDisRec(33, FName, PlateNumberGroup.E2, Bmp);
	}

	public static String Math(Bitmap[] bitmaps) {
		
		String CPH = "", HZ1 = "", HZ2 = "", ZM = "", SZ1 = "", SZ2 = "", SZ3 = "", SZ4 = "", SZ5 = ""; 
		//识别汉字--省份简称 第1个字符
		String HZ = GetHZ(bitmaps[0]);
		String[] HZs = HZ.split(",");
		HZ1 = HZs[0]; 
		HZ2 = HZs[1];

		//识别字母 第2个字符
		ZM = GetZM(bitmaps[1]);

		//识别数字和字母 第3-7个字符
		SZ1 = GetSZ(bitmaps[2]); 
		SZ2 = GetSZ(bitmaps[3]); 
		SZ3 = GetSZ(bitmaps[4]);  
		SZ4 = GetSZ(bitmaps[5]);  
		SZ5 = GetSZ(bitmaps[6]);

		if (!HZ1.equals(HZ2) && !HZ1.equals("新") && !HZ2.equals("新")) {
		    CPH = "新" + ZM + "•" + SZ1 + SZ2 + SZ3 + SZ4 + SZ5 + PlateNumberGroup.CPYS;
		    CPH = CPH + "," + HZ2 + ZM + "•" + SZ1 + SZ2 + SZ3 + SZ4 + SZ5 + PlateNumberGroup.CPYS;
		    CPH = CPH + "," + HZ1 + ZM + "•" + SZ1 + SZ2 + SZ3 + SZ4 + SZ5 + PlateNumberGroup.CPYS;
		} else {
		    CPH = HZ1 + ZM + "•" + SZ1 + SZ2 + SZ3 + SZ4 + SZ5 + PlateNumberGroup.CPYS;
		}
		
		return CPH;
	}
}