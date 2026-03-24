package com.github.chore3.targetscompass.client.cache;

import net.minecraft.core.GlobalPos;
import org.openjdk.nashorn.internal.objects.Global;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class ClientTagTargetPosCache {
    private static final ClientTagTargetPosCache INSTANCE = new ClientTagTargetPosCache();
    private final Map<String, GlobalPos> cache = new HashMap<>();

    public static ClientTagTargetPosCache getInstance() {
        return INSTANCE;
    }

    public void put(String tag, @Nullable GlobalPos pos){
        cache.put(tag, pos);
    }

    @Nullable public GlobalPos get(String tag){
        return cache.get(tag);
    }

    public void clear(){
        cache.clear();
    }


}
