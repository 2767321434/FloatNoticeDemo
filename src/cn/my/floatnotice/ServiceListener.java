package cn.my.floatnotice;

import cn.my.floatnotice.service.NoticService;

public interface ServiceListener {
    void registerService(NoticService service);
    void unregisterService();
   }
