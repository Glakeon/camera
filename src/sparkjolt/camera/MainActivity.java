package sparkjolt.camera;

import java.io.IOException;
import android.app.Activity;
import android.graphics.*;
import android.hardware.Camera;
import android.hardware.Camera.*;
import android.os.Bundle;
import android.view.*;
import android.widget.ImageView;

public class MainActivity extends Activity {
	
	private Camera camera;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		camera = Camera.open(findFrontFacingCamera());
	}
	
	private int findFrontFacingCamera() {
		int id = -1;
		int numCameras = Camera.getNumberOfCameras();
		
		for (int i = 0; i < numCameras; i++) {
			CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(i, info);
			if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
				id = i;
				break;
			}
		}
		
		return id;
	}
	
	public void takePicture(View v) {
		try {
			camera.setPreviewDisplay(((SurfaceView) findViewById(R.id.surface_view)).getHolder());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        camera.startPreview();
		camera.takePicture(null, null, new PhotoHandler());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.camera, menu);
		return true;
	}
	
	public class PhotoHandler implements PictureCallback {

		@Override
		public void onPictureTaken(byte[] data, android.hardware.Camera camera) {
			Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			if (bitmap != null) {
				Matrix matrix = new Matrix();
				matrix.postRotate(270);
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				ImageView v = (ImageView) findViewById(R.id.picture);
				v.setImageBitmap(bitmap);
			}
		}
	} 

}
