package com.warsade.core.menu.registry;

import com.warsade.core.menu.Menu;
import me.saiintbrisson.minecraft.ViewFrame;

import java.util.HashMap;
import java.util.Optional;

public class MenuRegistry {

    HashMap<Class<? extends Menu<?>>, ViewFrame> menus = new HashMap<>();

    public Optional<ViewFrame> getViewFrameByMenuClass(Class<? extends Menu<?>> clazz) {
        return Optional.ofNullable(menus.get(clazz));
    }

    /**
     * Method to register a menu in the memory
     *
     * @param key is the menu class used to query the menu instance later
     * @param viewFrame the view frame instance
     */
    public void register(Class<? extends Menu<?>> key, ViewFrame viewFrame) {
        viewFrame.register();
        menus.put(key, viewFrame);
    }

    public void unregister(Class<? extends Menu<?>> key) {
        Optional.ofNullable(menus.get(key)).ifPresent(ViewFrame::unregister);
        menus.remove(key);
    }

}
