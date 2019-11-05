package com.knowledge.mnlin.page.factory;

import android.util.Log;

import com.knowledge.mnlin.page.agents.PageTransAnimAgent;
import com.knowledge.mnlin.page.interfaces.PageTransAnimation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created on 2019/10/29  19:38
 * function : Page transition animation
 * <p>
 * 1. Factory mode building animation objects
 * 2. Singleton mode
 * 3. External annotative injection
 *
 * @author mnlin0905@gmail.com
 */
public class PageTransAnimFactory {
    private static final String TAG = "PageTransAnimFactory";

    /**
     * soft-reference,holder all page'animations-obj
     */
    private volatile static SoftReference<PageTransAnimFactory> sAnimFactory;

    /**
     * a holder of animation,When there is not enough memory, it may be recycled.
     */
    private final HashMap<String, PageTransAnimation> animationHolder = new HashMap<>();

    private PageTransAnimFactory() {
    }

    /**
     * @return create single instance
     */
    public static PageTransAnimFactory getInstance() {
        PageTransAnimFactory entity;
        if (sAnimFactory == null) {
            synchronized (PageTransAnimFactory.class) {
                if (sAnimFactory == null) {
                    entity = new PageTransAnimFactory();
                    sAnimFactory = new SoftReference<>(entity);
                } else {
                    return getInstance();
                }
            }
        } else {
            entity = sAnimFactory.get();
            if (entity == null) {
                sAnimFactory = null;
                return getInstance();
            }
        }

        return entity;
    }

    /**
     * @param animationKey the key of animation {@link PageTransAnimProvider}
     * @return the instance required
     */
    @Nullable
    public PageTransAnimation createPageTransAnimation(@NotNull String animationKey) {
        PageTransAnimation animation = animationHolder.get(animationKey);
        if (animation == null) {
            animation = PageTransAnimProvider.createPageTransAnimation(animationKey);
            animationHolder.put(animationKey, animation);
        }
        return animation;
    }

    /**
     * @param animationKeys the keys of animation {@link PageTransAnimProvider}
     * @return the instance required
     */
    @Nullable
    public PageTransAnimation createPageTransAnimation(@NotNull String... animationKeys) {
        if (animationKeys.length == 0) {
            return null;
        } else if (animationKeys.length == 1) {
            return createPageTransAnimation(animationKeys[0]);
        } else {
            List<PageTransAnimation> combine = new LinkedList<>();
            PageTransAnimation animation;
            for (String key : animationKeys) {
                animation = createPageTransAnimation(key);
                if (animation == null) {
                    Log.w(TAG, "create " + key + " animation failed,you should check whether you have inject it");
                }else {
                    combine.add(animation);
                }
            }

            int size = combine.size();
            if (size == 0) {
                return null;
            } else {
                return new PageTransAnimAgent(combine.toArray(new PageTransAnimation[size]));
            }
        }
    }

    /**
     * @param animationKey the key of animation {@link PageTransAnimKeys}
     * @param animation    the animation will be injected
     * @return he previous value associated with <tt>key</tt>, or <tt>null</tt> if there was no mapping for <tt>key</tt>; see {@link HashMap#put(java.lang.Object, java.lang.Object)}
     */
    @Nullable
    public PageTransAnimation injectPageTransAnimation(@NotNull String animationKey, @NotNull PageTransAnimation animation) {
        return animationHolder.put(animationKey, animation);
    }
}
