package tk.approach.dengine.android;

import android.util.SparseArray;
import android.view.MotionEvent;

/**
 * A part of DEngine project.
 * Created by Darek Stojaczyk.
 */
public class TouchManager {

    protected DCore dapplication;
    protected SparseArray<TouchPoint> activePointers;

    public TouchManager(DCore dapplication) {
        this.dapplication = dapplication;
        this.activePointers = new SparseArray<>();
    }

    public void processTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                TouchPoint f = new TouchPoint(event.getX(event.getActionIndex()), event.getY(event.getActionIndex()));
                activePointers.put(event.getPointerId(event.getActionIndex()), f);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                for (int j = 0; j < event.getPointerCount(); j++) {
                    TouchPoint point = activePointers.get(event.getPointerId(j));
                    if (point != null && !point.isToBeDeleted()) {
                        point.setX(event.getX(j));
                        point.setY(event.getY(j));
                    }
                }
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL: {
                if (activePointers.get(event.getPointerId(event.getActionIndex())) != null)
                    activePointers.get(event.getPointerId(event.getActionIndex())).setToBeDeleted(true);
                break;
            }
        }
    }

    public SparseArray<TouchPoint> getActivePointers() {
        return this.activePointers;
    }
}
