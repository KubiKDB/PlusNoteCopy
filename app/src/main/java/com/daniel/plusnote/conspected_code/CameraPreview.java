package com.daniel.plusnote.conspected_code;//package com.example.plusnote.activities;
//
//import android.content.Context;
//import android.content.pm.ActivityInfo;
//import android.hardware.Camera;
//import android.view.Surface;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//
//import androidx.camera.core.AspectRatio;
//
//import java.io.IOException;
//import java.lang.reflect.Method;
//
//public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
//    private SurfaceHolder mHolder;
//    private Camera mCamera;
//
//    public CameraPreview(Context context, Camera camera) {
//        super(context);
//        mCamera = camera;
////        mCamera.setDisplayOrientation(90);
//        // Install a SurfaceHolder.Callback so we get notified when the
//        // underlying surface is created and destroyed.
//        mHolder = getHolder();
//        mHolder.addCallback(this);
//    }
//
//    public void surfaceCreated(SurfaceHolder holder) {
//        // The Surface has been created, now tell the camera where to draw the preview.
//
//        try {
//            mCamera.setPreviewDisplay(holder);
//            mCamera.startPreview();
//        } catch (IOException e) {
//        }
//    }
//
//    public void surfaceDestroyed(SurfaceHolder holder) {
//        // empty. Take care of releasing the Camera preview in your activity.
//    }
//
//    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
//        if (mHolder.getSurface() == null) {
//            return;
//        }
//        try {
//            mCamera.stopPreview();
//        } catch (Exception e) {
//        }
//        Camera.CameraInfo info = new Camera.CameraInfo();
//        Camera.getCameraInfo(findFrontFacingCameraID(), info);
//        setDisplayOrientation(mCamera, 90);
////        Camera.Parameters parameters = mCamera.getParameters();
////        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
////        mCamera.setParameters(parameters);
////        Camera.Parameters parameters = mCamera.getParameters();
////        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
////        parameters.setPreviewSize(1920, 1080);
////        parameters.setPictureSize(3840, 2160);
////        parameters.setRotation(90);
////        mCamera.setParameters(parameters);
//        try {
//            mCamera.setPreviewDisplay(mHolder);
//            mCamera.startPreview();
//
//        } catch (Exception e) {
//        }
//    }
//
//    protected void setDisplayOrientation(Camera camera, int angle) {
//        Method downPolymorphic;
//        try {
//            downPolymorphic = camera.getClass().getMethod("setDisplayOrientation", new Class[]{int.class});
//            if (downPolymorphic != null)
//                downPolymorphic.invoke(camera, new Object[]{angle});
//        } catch (Exception e1) {
//        }
//    }
//
//    private int findFrontFacingCameraID() {
//        int cameraId = -1;
//        int numberOfCameras = Camera.getNumberOfCameras();
//        for (int i = 0; i < numberOfCameras; i++) {
//            Camera.CameraInfo info = new Camera.CameraInfo();
//            Camera.getCameraInfo(i, info);
//            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
//                cameraId = i;
//                break;
//            }
//        }
//        return cameraId;
//    }
//}