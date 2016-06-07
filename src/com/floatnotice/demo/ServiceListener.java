package com.floatnotice.demo;

import com.floatnotice.demo.service.NoticService;

public interface ServiceListener {
    void registerService(NoticService service);
    void unregisterService();
   }
