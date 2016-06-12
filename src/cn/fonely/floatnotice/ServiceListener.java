package cn.fonely.floatnotice;

import cn.fonely.floatnotice.service.NoticService;

public interface ServiceListener {
    void registerService(NoticService service);
    void unregisterService();
   }
