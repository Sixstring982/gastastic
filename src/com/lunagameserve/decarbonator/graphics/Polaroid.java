package com.lunagameserve.decarbonator.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.lunagameserve.decarbonator.physics.PhysUtil;
import com.lunagameserve.decarbonator.physics.Vector2;
import com.lunagameserve.decarbonator.util.Screen;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sixstring982 on 2/21/15.
 */
public class Polaroid {

    private final static int BUTTON_WIDTH = 88;
    private final static int BUTTON_HEIGHT = 107;

    private final static int WIDTH = 176;
    private final static int HEIGHT = 214;
    private final static int INTERNAL_X = 9;
    private final static int INTERNAL_Y = 9;
    private final static int INTERNAL_W = 158;
    private final static int INTERNAL_H = 158;

    /* < 1, the lower the more friction. */
    private final static float FRICTION = 0.88f;

    private final static float ROTATIONAL_FRICTION = 0.9f;

    private static Map<Integer, Bitmap> polaroidCache =
            new HashMap<Integer, Bitmap>();

    private final Bitmap bmp;
    private Vector2 velocity = new Vector2(0, 0);
    private Vector2 position = new Vector2(0, 0);
    private float rotation = 0.0f;
    private float rotationVelocity = 0.0f;

    private Runnable onStopCallback = null;

    public Polaroid(Context context, int resId) {
        if (polaroidCache.get(resId) != null) {
            bmp = polaroidCache.get(resId);
        } else {
            bmp = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888);
            Bitmap base = BitmapFactory.decodeResource(context.getResources(),
                                                       resId);
            new Draw(bmp, Color.WHITE)
                   .clear()
                   .image(base, INTERNAL_X, INTERNAL_Y, INTERNAL_W, INTERNAL_H);
            polaroidCache.put(resId, bmp);
        }
    }

    public void setPosition(float x, float y) {
        this.position = new Vector2(x, y);
    }

    public void setVelocity(float x, float y) {
        this.velocity = new Vector2(x, y);
    }

    public void setVelocity(float theta, float thetaError,
                            float speed) {
        theta += (Screen.getRand().nextFloat() - 0.5f) * 2f * thetaError;
        float x = (float)Math.cos(theta) * speed;
        float y = (float)Math.sin(theta) * speed;

        setVelocity(x, y);
    }

    public void setRotationVelocity(float theta) {
        this.rotationVelocity = theta;
    }

    public float getX() {
        return position.getX();
    }

    public float getY() {
        return position.getY();
    }

    public void setRotationVelocity(float theta, float thetaError) {
        this.rotationVelocity =
                theta +
                (Screen.getRand().nextFloat() - 0.5f) * 2.0f * thetaError;
    }

    public void setOnStopCallback(Runnable onStopCallback) {
        this.onStopCallback = onStopCallback;
    }

    public void update(@Nullable ImageButton button) {
        if (isMoving()) {
            velocity = velocity.mult(FRICTION);
            position = position.add(velocity);

            rotationVelocity *= ROTATIONAL_FRICTION;
            rotation += rotationVelocity;
            if (button != null) {
                detectCollision(button);
                synchronizeButton(button);
            } else {
                detectCollision();
            }
        } else {
            if (onStopCallback != null) {
                onStopCallback.run();
            }
        }
    }

    private void detectCollision() {
        float dpWidth = WIDTH / 2f;
        float dpHeight = HEIGHT / 2f;

        float left = position.getX() - dpWidth;
        float right = position.getX() + dpWidth;
        float top = position.getY() - dpHeight;
        float bot = position.getY() + dpHeight;

        if ((left < 0 && velocity.getX() < 0) ||
                (right >= Screen.getWidth() && velocity.getX() > 0)) {
            undoMove();
            velocity = new Vector2(-1 * velocity.getX(), velocity.getY());
            position = position.add(velocity);
        }

        if ((top < 0 && velocity.getY() < 0) ||
                (bot > Screen.getHeight() && velocity.getY() > 0)) {
            undoMove();
            velocity = new Vector2(velocity.getX(), -1 * velocity.getY());
            position = position.add(velocity);
        }
    }

    private void detectCollision(ImageButton button) {
        float dpWidth = Screen.pxToDP(button.getWidth()) / 2f;
        float dpHeight = Screen.pxToDP(button.getHeight()) / 2f;

        float left = position.getX() - dpWidth;
        float right = position.getX() + dpWidth;
        float top = position.getY() - dpHeight;
        float bot = position.getY() + dpHeight;

        if ((left < 0 && velocity.getX() < 0) ||
             (right >= Screen.getWidthDP() && velocity.getX() > 0)) {
            undoMove();
            velocity = new Vector2(-1 * velocity.getX(), velocity.getY());
            position = position.add(velocity);
            Log.d("Polaroid", "X edge, changing velocity.");
        }

        if ((top < 0 && velocity.getY() < 0) ||
            (bot > Screen.getHeightDP() && velocity.getY() > 0)) {
            undoMove();
            velocity = new Vector2(velocity.getX(), -1 * velocity.getY());
            position = position.add(velocity);
        }
    }

    private void undoMove() {
        position = position.add(velocity.mult(-1));
    }

    public boolean isMoving() {
        return  rotationVelocity >  0.01f ||
                rotationVelocity < -0.01f ||
                velocity.getX()  >  1.0f ||
                velocity.getX()  < -1.0f ||
                velocity.getY()  >  1.0f ||
                velocity.getY()  < -1.0f;
    }

    public void synchronizeButton(ImageButton btn) {
        btn.setVisibility(View.VISIBLE);
        btn.setImageBitmap(getRotatedBitmap());

        float xScale = Math.abs((float)Math.cos(rotation));
        float yScale = Math.abs((float) Math.sin(rotation));

        float dpWidth = (xScale * BUTTON_WIDTH + yScale * BUTTON_HEIGHT);
        float dpHeight = (xScale * BUTTON_HEIGHT + yScale * BUTTON_WIDTH);

        ViewGroup.LayoutParams parms = btn.getLayoutParams();

        parms.width = (int)Screen.dpToPx(dpWidth);
        parms.height = (int)Screen.dpToPx(dpHeight);

        btn.setLayoutParams(parms);

        btn.setX(Screen.dpToPx(position.getX() - (dpWidth / 2f)));
        btn.setY(Screen.dpToPx(position.getY() - (dpHeight / 2f)));
    }

    public void synchronizeTextView(TextView textView) {
        float xScale = Math.abs((float)Math.cos(rotation));
        float yScale = Math.abs((float) Math.sin(rotation));

        float dpWidth = (xScale * BUTTON_WIDTH + yScale * BUTTON_HEIGHT);
        float dpHeight = (xScale * BUTTON_HEIGHT + yScale * BUTTON_WIDTH);
        textView.setX(Screen.dpToPx(position.getX() - dpWidth / 3f));
        textView.setY(Screen.dpToPx(position.getY() + dpHeight / 2f));
        textView.setVisibility(View.VISIBLE);
    }

    public Bitmap getRotatedBitmap() {
        Matrix mtx = new Matrix();
        mtx.setRotate((float)PhysUtil.radToDeg(rotation));
        return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(),
                                   mtx, true);
    }
}
