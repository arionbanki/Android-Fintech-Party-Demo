package is.arionbanki.arionfintechdemo.controllers;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import is.arionbanki.arionfintechdemo.FinTechApplication;

/**
 * Created by aegirt on 24.05.2016.
 */
public abstract class BaseController {
    private final String TAG = BaseController.class.getSimpleName();
    public static final int MESSAGE_ERROR = 0;
    protected FinTechApplication application;

    public FinTechApplication getApplication() {
        return application;
    }

    private final List<Handler> handlers = new ArrayList<>();

    public BaseController(FinTechApplication context) {
        this.application = context;
    }

    public abstract boolean handleMessage(int what, Object data);

    public final void addHandler(Handler handler) {
        handlers.add(handler);
    }

    public final void removeHandler(Handler handler) {
        handlers.remove(handler);
    }

    protected final void notifyMessageHandlers(int what, int arg1, int arg2, Object obj) {
        if (!handlers.isEmpty()) {
            for (Handler handler : handlers) {
                Message msg = Message.obtain(handler, what, arg1, arg2, obj);
                msg.sendToTarget();
            }
        }
    }

    public void clearHandlers() {
        handlers.clear();
    }
}
