package com.daniel.plusnote.conspected_code;//package com.example.plusnote.activities;
//
//import static CreateImageNote.outputFile;
//import static com.example.plusnote.activities.PhotoActivity.getCameraInstance;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.constraintlayout.widget.ConstraintLayout;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.content.pm.ActivityInfo;
//import android.content.pm.PackageManager;
//import android.database.Cursor;
//import android.hardware.Camera;
//import android.media.CamcorderProfile;
//import android.media.MediaRecorder;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.SystemClock;
//import android.provider.MediaStore;
//import android.util.Log;
//import android.view.View;
//import android.widget.Chronometer;
//import android.widget.ImageButton;
//import android.widget.VideoView;
//
//import com.example.plusnote.R;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//public class VideoActivity extends AppCompatActivity {
//    private static final int REQUEST_CODE_SELECT_IMAGE = 1;
//    private static final int REQUEST_CODE_STORAGE_PERMISSION = 2;
//    private ImageButton
//            select_existing_image_video,
//            take_photo_video,
//            record_video_video,
//            change_camera;
//    private Camera mCamera;
//    private CameraPreview mPreview;
//    public static boolean change = false;
//    MediaRecorder mediaRecorder;
//    private boolean isRecording = false;
//    public static boolean isVideo = false;
//    boolean isPlaying = false;
//    private Chronometer chronometer;
//    public static String timer_string;
//    ConstraintLayout preview_video, camera_preview_video;
//    private int frontCameraID, mCameraID;
//    public static boolean fromGallery = false;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.video_record_layout);
//        select_existing_image_video = findViewById(R.id.select_existing_image_video);
//        take_photo_video = findViewById(R.id.take_photo_video);
//        record_video_video = findViewById(R.id.record_video_video);
//        change_camera = findViewById(R.id.change_camera_video);
//        preview_video = findViewById(R.id.preview_video);
//        chronometer = findViewById(R.id.chronometer_video);
//        take_photo_video.setOnClickListener(view -> {
//            finish();
//        });
//        record_video_video.setOnClickListener(view1 -> {
//            if (isRecording) {
//                mediaRecorder.stop();
//                chronometer.stop();
//                releaseMediaRecorder();
//                mCamera.lock();
//                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(outputFile)));
//                record_video_video.setBackgroundResource(R.drawable.ic_video);
//                isRecording = false;
//                isVideo = true;
//                timer_string = String.valueOf(chronometer.getText());
//                Intent intent = new Intent(getApplicationContext(), CreateImageNote.class);
//                startActivityForResult(intent, 1010);
//            } else {
//                if (prepareVideoRecorder()) {
//                    mediaRecorder.start();
//                    Camera.Parameters parameters = mCamera.getParameters();
//                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
//                    mCamera.setParameters(parameters);
//                    chronometer.setBase(SystemClock.elapsedRealtime());
//                    chronometer.start();
//                    chronometer.setVisibility(View.VISIBLE);
//                    record_video_video.setBackgroundResource(R.drawable.ic_video_recording);
//                    isRecording = true;
//                    select_existing_image_video.setEnabled(false);
//                    take_photo_video.setEnabled(false);
//                    chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
//                        @Override
//                        public void onChronometerTick(Chronometer chronometer) {
//                            if (chronometer.getText().toString().equals("00:15")){
//                                mediaRecorder.stop();
//                                chronometer.stop();
//                                releaseMediaRecorder();
//                                mCamera.lock();
//                                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(outputFile)));
//                                record_video_video.setBackgroundResource(R.drawable.ic_video);
//                                isRecording = false;
//                                isVideo = true;
//                                timer_string = String.valueOf(chronometer.getText());
//                                Intent intent = new Intent(getApplicationContext(), CreateImageNote.class);
//                                startActivityForResult(intent, 1010);
//                            }
//                        }
//                    });
//                } else {
//                    releaseMediaRecorder();
//                }
//            }
//        });
//        mCamera = getCameraInstance();
////        Camera.Parameters p = mCamera.getParameters();
////        p.setRotation(90);
////        mCamera.setParameters(p);
//        mPreview = new CameraPreview(this, mCamera);
//        preview_video.addView(mPreview);
//
//        change_camera.setOnClickListener(view -> {
//            mCamera.stopPreview();
//            mCamera.release();
//            mCamera = changeCamera();
//            mPreview = new CameraPreview(this, mCamera);
//            preview_video.addView(mPreview);
//        });
//    }
//
//    public static final int MEDIA_TYPE_IMAGE = 1;
//    public static final int MEDIA_TYPE_VIDEO = 2;
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
//                        fromGallery = true;
//                        Intent intent = new Intent();
//                        setResult(RESULT_OK, intent);
//                        finish();
//                    } catch (Exception ignored) {
//                    }
//                }
//            }
//        }
//        if (requestCode == 1010 && resultCode == RESULT_OK) {
//            Intent intent = new Intent();
//            setResult(RESULT_OK, intent);
//            finish();
//        }
//        if (requestCode == 1010 && resultCode == RESULT_CANCELED) {
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
//    private Uri getOutputMediaFileUri(int type) {
//        return Uri.fromFile(getOutputMediaFile(type));
//    }
//
//    private File getOutputMediaFile(int type) {
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
//        } else {
//            return null;
//        }
//        return mediaFile;
//    }
//
//    public Camera getCameraInstance() {
//        Camera c = null;
//        try {
//            c = Camera.open(); // attempt to get a Camera instance
//            Camera.Parameters parameters = c.getParameters();
//            mCameraID = 0;
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
//    public Camera changeCamera() {
//        Camera c = null;
//        try {
//            if (!change) {
//                c = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
//                mCameraID = 1;
//                Camera.Parameters parameters = c.getParameters();
//                parameters.setRotation(270);
//                parameters.setPictureSize(1920, 1080);
//                c.setParameters(parameters);
//            } else {
//                c = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
//                mCameraID = 0;
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
//
//    private boolean prepareVideoRecorder() {
//
//        mCamera = getCameraInstance();
//        mCamera.setDisplayOrientation(90);
//        mediaRecorder = new MediaRecorder();
//
//        // Step 1: Unlock and set camera to MediaRecorder
//        mCamera.unlock();
//        mediaRecorder.setCamera(mCamera);
//
//        // Step 2: Set sources
//        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
//        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
//        if (mCameraID == Camera.CameraInfo.CAMERA_FACING_FRONT){
//            mediaRecorder.setOrientationHint(270);
//        } else mediaRecorder.setOrientationHint(90);
//
//        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
//        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_1080P));
//        mediaRecorder.setVideoSize(3840, 2160);
//
//        // Step 4: Set output file
//        outputFile = getOutputMediaFile(MEDIA_TYPE_VIDEO);
//        mediaRecorder.setOutputFile(outputFile.getAbsolutePath());
//        mediaRecorder.setMaxDuration(15000);
//
//        // Step 5: Set the preview output
//        mediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());
//
//        // Step 6: Prepare configured MediaRecorder
//        try {
//            mediaRecorder.prepare();
//        } catch (IllegalStateException | IOException e) {
//            releaseMediaRecorder();
//            return false;
//        }
//        return true;
//    }
//
//    private void releaseMediaRecorder() {
//        if (mediaRecorder != null) {
//            mediaRecorder.reset();   // clear recorder configuration
//            mediaRecorder.release(); // release the recorder object
//            mediaRecorder = null;
//            mCamera.lock();           // lock camera for later use
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
//        releaseCamera();              // release the camera immediately on pause event
//    }
//
//}