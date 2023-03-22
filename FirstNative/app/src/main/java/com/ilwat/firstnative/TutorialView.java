package com.ilwat.firstnative;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.opengles.GL10;

class TutorialView extends GLSurfaceView
{
    protected int redSize = 8;
    protected int greenSize = 8;
    protected int blueSize = 8 ;
    protected int alphaSize = 8;
    protected int depthSize = 16;
    protected int sampleSize = 4;
    protected int stencilSize = 0;
    protected int[] value = new int [1];

    public TutorialView(Context context)
    {
        super(context);

        setEGLContextFactory(new ContextFactory());

        setEGLConfigChooser(new ConfigChooser());

        setRenderer(new Renderer());
    }

    private static class ContextFactory implements GLSurfaceView.EGLContextFactory
    {
        public EGLContext createContext(EGL10 egl, EGLDisplay display, EGLConfig eglConfig)
        {
            final int EGL_CONTEXT_CLIENT_VERSION = 0x3098;

            int[] attrib_list = {EGL_CONTEXT_CLIENT_VERSION, 2, EGL10.EGL_NONE };

            EGLContext context = egl.eglCreateContext(display, eglConfig, EGL10.EGL_NO_CONTEXT, attrib_list);

            return context;
        }

        public void destroyContext(EGL10 egl, EGLDisplay display, EGLContext context)
        {
            egl.eglDestroyContext(display,  context);
        }
    }

    protected class ConfigChooser implements GLSurfaceView.EGLConfigChooser
    {
        public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display)
        {
            final int EGL_OPENGL_ES2_BIT = 4;
            int[] configAttributes =
                    {
                            EGL10.EGL_RED_SIZE, redSize,
                            EGL10.EGL_GREEN_SIZE, greenSize,
                            EGL10.EGL_BLUE_SIZE, blueSize,
                            EGL10.EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT,
                            EGL10.EGL_SAMPLES, sampleSize,
                            EGL10.EGL_DEPTH_SIZE, depthSize,
                            EGL10.EGL_STENCIL_SIZE, stencilSize,
                            EGL10.EGL_NONE
                    };

            int[] num_config = new int[1];
            egl.eglChooseConfig(display, configAttributes, null, 0, num_config);

            int numConfigs = num_config[0];

            EGLConfig[] configs = new EGLConfig[numConfigs];
            egl.eglChooseConfig(display, configAttributes, configs, numConfigs, num_config);

            return selectConfig(egl, display, configs);
        }

        public EGLConfig selectConfig(EGL10 egl, EGLDisplay display, EGLConfig[] configs)
        {
            for(EGLConfig config : configs)
            {
                int d = getConfigAttrib(egl, display, config, EGL10.EGL_DEPTH_SIZE, 0);
                int s = getConfigAttrib(egl, display, config, EGL10.EGL_GREEN_SIZE, 0);
                int r = getConfigAttrib(egl, display, config, EGL10.EGL_RED_SIZE,0);
                int g = getConfigAttrib(egl, display, config, EGL10.EGL_GREEN_SIZE, 0);
                int b = getConfigAttrib(egl, display, config, EGL10.EGL_BLUE_SIZE, 0);
                int a = getConfigAttrib(egl, display, config, EGL10.EGL_ALPHA_SIZE, 0);

                if (r == redSize && g == greenSize && b == blueSize && a == alphaSize && d >= depthSize && s >= stencilSize)
                    return config;
            }

            return null;
        }

        private int getConfigAttrib(EGL10 egl, EGLDisplay display, EGLConfig config,
                                    int attribute, int defaultValue)
        {
            if (egl.eglGetConfigAttrib(display, config, attribute, value))
                return value[0];

            return defaultValue;
        }
    }

    private static class Renderer implements GLSurfaceView.Renderer
    {
        public void onDrawFrame(GL10 gl)
        {
            NativeLibrary.step();
        }

        public void onSurfaceChanged(GL10 gl, int width, int height)
        {
            NativeLibrary.init(width,height);
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config)
        {

        }
    }
}
