package cn.wtu.cs.plateocr.match;

/*
 * @功能 HLS存放类
 */
public final class HLS {

	public static final float[] HLSColor = new float[] {
		219.709360000f, 0.535206288f, 0.033237215f,
		204.967970000f, 0.306038829f, 0.760536935f,
		 49.598122000f, 0.432360512f, 0.806774168f,
		245.932270000f, 0.101161231f, 0.029748341f	
	};
	
	public static float[] Deta = new float[] { 
		0.8f, 260.0f, 300.0f, 0.0f
	};
	
	public float H;
	public float L;
	public float S;
	
	HLS(float fH, float fL, float fS) {
		H = fH; L = fL; S = fS;
	}

}