package com.daniel.plusnote.conspected_code;//package com.example.plusnote.activities;
//
//import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
//
//import static CreateImageNote.RotateBitmap;
//import static com.example.plusnote.activities.VideoActivity.isVideo;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.constraintlayout.widget.ConstraintLayout;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.hardware.Camera;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.util.Log;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//
//import com.example.plusnote.R;
//import Note;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//public class PhotoActivity extends AppCompatActivity {
//    private static final int REQUEST_CODE_STORAGE_PERMISSION = 2;
//    private static final int REQUEST_CODE_SELECT_IMAGE = 1;
//    private ImageButton
//            select_existing_image,
//            take_photo,
//            record_video,
//            change_camera;
//    private Camera mCamera;
//    private CameraPreview mPreview;
//    public static boolean change = false;
//    ConstraintLayout camera_preview, preview;
//    public static boolean isPhoto = false;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.photo_capture_layout);
//        select_existing_image = findViewById(R.id.select_existing_image);
//        take_photo = findViewById(R.id.take_photo);
//        record_video = findViewById(R.id.record_video);
//        change_camera = findViewById(R.id.change_camera);
//        preview = findViewById(R.id.preview);
//        record_video.setOnClickListener(view -> {
//            Intent intent = new Intent(getApplicationContext(), VideoActivity.class);
//            startActivityForResult(intent, 1001);
//        });
//        mCamera = getCameraInstance();
//
//        isVideo = false;
//
//        change_camera.setOnClickListener(view -> {
//            mCamera.stopPreview();
//            mCamera.release();
//            mCamera = changeCamera();
//            mPreview = new CameraPreview(this, mCamera);
//            preview.addView(mPreview);
//        });
//
//        Camera.PictureCallback mPicture = (data, camera) -> {
//            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
//
//            if (pictureFile == null) {
//                return;
//            }
//
//            try {
//                FileOutputStream fos = new FileOutputStream(pictureFile);
//                fos.write(data);
//                fos.close();
//                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(pictureFile)));
////                CreateImageNote.selectedImagePath = pictureFile.getAbsolutePath();
//            } catch (FileNotFoundException e) {
//                Log.e("Error", "file not found e");
//            } catch (IOException e) {
//                Log.e("Error", e.getLocalizedMessage());
//            }
//            releaseCamera();
//            Intent intent = new Intent(getApplicationContext(), CreateImageNote.class);
//            startActivityForResult(intent, 1001);
//        };
//        take_photo.setOnClickListener(v -> {
//            mCamera.takePicture(null, null, mPicture);
//            isPhoto = true;
//        });
//        mPreview = new CameraPreview(this, mCamera);
//        preview.addView(mPreview);
//
//        select_existing_image.setOnClickListener(view -> {
//            if (ContextCompat.checkSelfPermission(
//                    getApplicationContext(),
//                    Manifest.permission.READ_EXTERNAL_STORAGE
//            ) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(
//                        PhotoActivity.this,
//                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                        REQUEST_CODE_STORAGE_PERMISSION
//                );
//            } else {
//                selectImage();
//            }
//        });
//    }
//
//    public static final int MEDIA_TYPE_IMAGE = 1;
//    public static final int MEDIA_TYPE_VIDEO = 2;
//    public static final int MEDIA_TYPE_AUDIO = 3;
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK) {
//            if (data != null) {
//                Uri selectedImageUri = data.getData();
//                if (selectedImageUri != null) {
//                    try {
//                        InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
//                        CreateImageNote.selectedImagePath = getPathFromUri(selectedImageUri);
//                        VideoActivity.fromGallery = true;
//                        startActivityForResult(
//                                new Intent(getApplicationContext(),
//                                        CreateImageNote.class),
//                                1001
//                                );
//
//                    } catch (Exception ignored) {
//                    }
//                }
//            }
//        }
//        if (requestCode == 1001 && resultCode == RESULT_OK) {
//            Intent intent = new Intent();
//            setResult(RESULT_OK, intent);
//            finish();
//        }
//        if (requestCode == 1001 && resultCode == RESULT_CANCELED) {
//            Intent intent = new Intent();
//            setResult(RESULT_CANCELED, intent);
//            recreate();
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.length > 0) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                selectImage();
//            }
//        }
//    }
//
//    private void selectImage() {
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
//        }
//    }
//
//    private String getPathFromUri(Uri contentUri) {
//        String filePath;
//        Cursor cursor = getContentResolver().query(contentUri,
//                null, null, null, null);
//        if (cursor == null) {
//            filePath = contentUri.getPath();
//        } else {
//            cursor.moveToFirst();
//            int index = cursor.getColumnIndex("_data");
//            filePath = cursor.getString(index);
//            cursor.close();
//        }
//        return filePath;
//    }
//
//    private static Uri getOutputMediaFileUri(int type) {
//        return Uri.fromFile(getOutputMediaFile(type));
//    }
//
//    public static File getOutputMediaFile(int type) {
//        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES), "PlusNote");
//        if (!mediaStorageDir.exists()) {
//            if (!mediaStorageDir.mkdirs()) {
//                Log.e("PlusNote", "failed to create directory");
//                return null;
//            }
//        }
//        // Create a media file name
//        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        File mediaFile;
//        if (type == MEDIA_TYPE_IMAGE) {
//            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
//                    "IMG_" + timeStamp + ".jpg");
//        } else if (type == MEDIA_TYPE_VIDEO) {
//            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
//                    "VID_" + timeStamp + ".mp4");
//        } else if (type == MEDIA_TYPE_AUDIO) {
//            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
//                    "AUD_" + timeStamp + ".3gp");
//        } else {
//            return null;
//        }
//        return mediaFile;
//    }
//
//    public static Camera getCameraInstance() {
//        Camera c = null;
//        try {
//            c = Camera.open(); // attempt to get a Camera instance
//            Camera.Parameters parameters = c.getParameters();
//            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
//            parameters.setPreviewSize(1920, 1080);
//            parameters.setPictureSize(3840, 2160);
//            parameters.setRotation(90);
//            c.setParameters(parameters);
//        } catch (Exception e) {
//            // Camera is not available (in use or does not exist)
//        }
//        return c; // returns null if camera is unavailable
//    }
//
//    public static Camera changeCamera() {
//        Camera c = null;
//        try {
//            if (!change) {
//                c = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
//                Camera.Parameters parameters = c.getParameters();
//                parameters.setRotation(270);
//                parameters.setPictureSize(1920, 1080);
//                c.setParameters(parameters);
//            } else {
//                c = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
//                Camera.Parameters parameters = c.getParameters();
//                parameters.setRotation(90);
//                parameters.setPictureSize(3840, 2160);
//                c.setParameters(parameters);
//            }
//            change = !change;
//        } catch (Exception e) {
//            // Camera is not available (in use or does not exist)
//        }
//        return c; // returns null if camera is unavailable
//    }
//
//    private void releaseCamera() {
//        if (mCamera != null) {
//            mCamera.release();
//            mCamera = null;
//        }
//    }
//}