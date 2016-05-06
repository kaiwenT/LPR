package cn.wtu.cs.plateocr;

import cn.wtu.cs.plateocr.R;

import cn.wtu.cs.plateocr.match.AssetsResource;
import cn.wtu.cs.plateocr.match.ColorKMeans;
import cn.wtu.cs.plateocr.match.Oritenation;
import cn.wtu.cs.plateocr.match.PlateNumberGroup;
import cn.wtu.cs.plateocr.match.RecEachCharInMinDis;
import cn.wtu.cs.plateocr.match.SegInEachChar;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * @author Tankai
 * @function 主界面Activity
 */
public class PlateocrActivity extends Activity {

	//原图
	private Bitmap bitmap = null;
	//定位后的车牌图片
	private Bitmap curbitmap = null;
    //分割后的7个字符图片
	private Bitmap[] bitmaps = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        final ImageView imageView1 = (ImageView)findViewById(R.id.imageView1);
        final EditText editText1 = (EditText)findViewById(R.id.editText1);
        
        final ImageView imageP1 = (ImageView)findViewById(R.id.imageP1);
        final ImageView imageP2 = (ImageView)findViewById(R.id.imageP2);
        final ImageView imageP3 = (ImageView)findViewById(R.id.imageP3);
        final ImageView imageP4 = (ImageView)findViewById(R.id.imageP4);
        final ImageView imageP5 = (ImageView)findViewById(R.id.imageP5);
        final ImageView imageP6 = (ImageView)findViewById(R.id.imageP6);
        final ImageView imageP7 = (ImageView)findViewById(R.id.imageP7);
        
        final EditText editP = (EditText)findViewById(R.id.editP);
        
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Config.ARGB_8888;
        bitmap = BitmapFactory.decodeFile("/sdcard/xjhxcp/"+editText1.getText().toString().trim()+".jpg", options);
        curbitmap = bitmap;

		//显示原图
        imageView1.setImageBitmap(bitmap);

		//选择图片
        Button button3 = (Button)findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {		
			public void onClick(View arg0) {
				BitmapFactory.Options options = new BitmapFactory.Options();
		        options.inPreferredConfig = Config.ARGB_8888;
		        bitmap = BitmapFactory.decodeFile("/sdcard/xjhxcp/"+editText1.getText().toString().trim()+".jpg", options);
		        curbitmap = bitmap;
		        imageView1.setImageBitmap(bitmap);
			}
		});

		//车牌定位
        Button button1 = (Button)findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {		
			public void onClick(View arg0) {
				curbitmap = ColorKMeans.Math(bitmap);		
				curbitmap = Oritenation.Math(curbitmap, bitmap);
				imageView1.setImageBitmap(curbitmap);
			}
		});

		//车牌字符分割
        Button button2 = (Button)findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {		
			public void onClick(View arg0) {
				if (PlateNumberGroup.AlreadyChecked) {
					//分割获取单字
					bitmaps = SegInEachChar.Math(curbitmap);
					imageP1.setImageBitmap(bitmaps[0]);
					imageP2.setImageBitmap(bitmaps[1]);
					imageP3.setImageBitmap(bitmaps[2]);

					//这样会导致第三个和第四个字符相同，why
					Bitmap Bmp = RecEachCharInMinDis.ClearSmall(bitmaps[2]);
				    Bmp = RecEachCharInMinDis.GetRegion(Bmp);
				    Bmp = RecEachCharInMinDis.Zoom(Bmp);
				    imageP4.setImageBitmap(Bmp);
				    
					//imageP4.setImageBitmap(bitmaps[3]);
					imageP5.setImageBitmap(bitmaps[4]);
					imageP6.setImageBitmap(bitmaps[5]);
					imageP7.setImageBitmap(bitmaps[6]);
				}
			}
		});

		//车牌字符识别
        Button button4 = (Button)findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {		
			public void onClick(View arg0) {
				if (PlateNumberGroup.AlreadyChecked) {
					AssetsResource.context = PlateocrActivity.this;
					String cph = RecEachCharInMinDis.Math(bitmaps);
					editP.setText(cph);
				}
			}
        });
    }
}