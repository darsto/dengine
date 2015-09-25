package tk.approach.dengine.android.examples.menus;

import tk.approach.dengine.android.DActivity;
import tk.approach.dengine.android.ResourceHandler;
import tk.approach.dengine.android.activatable.Activatable;
import tk.approach.dengine.android.activatable.ActivatableHandler;
import tk.approach.dengine.android.examples.R;

public class MainActivity extends DActivity implements ResourceHandler, ActivatableHandler {

    private Activatable[] activatables;

    public static final int MENU_ACTIVATABLE_ID = 0;
    public static final int SUBMENU_ACTIVATABLE_ID = 1;

    @Override
    protected void init() {
        this.activatables = new Activatable[2];
        this.setActivatableHandler(this);
        this.setResourceHandler(this);
    }

    @Override
    public Activatable getActivatable(int id) {
        if (activatables[id] == null) {
            switch(id) {
                case 0:
                    activatables[0] = new MenuActivatable(this.dcore);
                    break;
                case 1:
                    activatables[1] = new SubMenuActivatable(this.dcore);
                    break;
            }
        }
        return activatables[id];
    }

    @Override
    public int[] getMusics() {
        return new int[] { R.raw.bgm, R.raw.menu };
    }

    @Override
    public int[] getSounds() {
        return new int[] { R.raw.click1, R.raw.click2 };
    }

    @Override
    public int[] getTextures() {
        /**
         * 1st position - terrain texture atlas (unused in this example)
         * 2nd position - gui texture atlas
         * 3rd position - font texture atlas
         */
        return new int[] { R.drawable.terrain, R.drawable.gui, R.drawable.font };
    }
}
