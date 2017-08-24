/**************************************************************************************** 
 Copyright © 2003-2012 hbasesoft Corporation. All rights reserved. Reproduction or       <br>
 transmission in whole or in part, in any form or by any means, electronic, mechanical <br>
 or otherwise, is prohibited without the prior written consent of the copyright owner. <br>
 ****************************************************************************************/
package com.hbasesoft.framework.message.core.event;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.context.ApplicationContext;

import com.hbasesoft.framework.common.FrameworkException;
import com.hbasesoft.framework.common.StartupListenerAdapter;
import com.hbasesoft.framework.common.utils.CommonUtil;
import com.hbasesoft.framework.common.utils.PropertyHolder;
import com.hbasesoft.framework.common.utils.logger.LoggerUtil;
import com.hbasesoft.framework.message.core.MessageHelper;
import com.hbasesoft.framework.message.core.MessageQueue;

/**
 * <Description> <br>
 * 
 * @author 王伟<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2017年2月21日 <br>
 * @since V1.0<br>
 * @see com.hbasesoft.framework.message.core.event <br>
 */
public class EventHandlerStartupLinstener extends StartupListenerAdapter {

    private ThreadPoolExecutor executor;

    private ArrayBlockingQueue<EventConsummer> arrayBlockingQueue;

    private Map<String, EventLinsener> eventLinsenerHolder;

    private ThreadPoolExecutor lisenerExecutor;

    private boolean flag = true;

    public EventHandlerStartupLinstener() {
        int corePoolSize = PropertyHolder.getIntProperty("message.event.corePoolSize", 20); // 核心线程数
        int maximumPoolSize = PropertyHolder.getIntProperty("message.event.maximumPoolSize", 100); // 最大线程数
        long keepAliveTime = PropertyHolder.getIntProperty("message.event.keepAliveTime", 600);
        int threadSize = PropertyHolder.getIntProperty("message.event.handlerSize", 15);
        int maxConsummer = PropertyHolder.getIntProperty("message.event.maxConsummer", 10000);

        arrayBlockingQueue = new ArrayBlockingQueue<EventConsummer>(maxConsummer);

        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(threadSize));

        for (int i = 0; i < threadSize; i++) {
            executor.execute(new Runnable() {

                @Override
                public void run() {
                    try {
                        while (flag) {
                            try {
                                EventConsummer consummer = arrayBlockingQueue.poll(3, TimeUnit.SECONDS);
                                if (consummer != null) {
                                    consummer.emmit();
                                }
                            }
                            catch (InterruptedException e) {
                                LoggerUtil.error(e);
                                Thread.sleep(1000);
                            }
                        }
                    }
                    catch (Exception e) {
                        LoggerUtil.error(e);
                    }
                }
            });
        }

        eventLinsenerHolder = new ConcurrentHashMap<String, EventLinsener>();
        lisenerExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(threadSize));

        MessageQueue queue = MessageHelper.createMessageQueue();
        for (int i = 0; i < threadSize; i++) {
            lisenerExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (flag) {
                            if (CommonUtil.isNotEmpty(eventLinsenerHolder)) {
                                for (Entry<String, EventLinsener> entry : eventLinsenerHolder.entrySet()) {
                                    String event = entry.getKey();
                                    try {
                                        List<byte[]> datas = queue.pop(3, event);
                                        if (CommonUtil.isNotEmpty(datas)) {
                                            for (byte[] data : datas) {
                                                LoggerUtil.info("receive message by thread[{0}]",
                                                    Thread.currentThread().getId());
                                                arrayBlockingQueue
                                                    .put(new EventConsummer(entry.getValue(), data, event));
                                            }
                                        }
                                    }
                                    catch (Exception e) {
                                        LoggerUtil.error(e);
                                        Thread.sleep(1000);

                                    }
                                }
                            }
                            else {
                                Thread.sleep(1000);
                            }
                        }
                    }
                    catch (InterruptedException e) {
                        LoggerUtil.error(e);
                    }
                }
            });
        }
    }

    /**
     * Description: <br>
     * 
     * @author 王伟<br>
     * @taskId <br>
     * @param context
     * @throws FrameworkException <br>
     */
    @Override
    public void complete(ApplicationContext context) throws FrameworkException {
        Map<String, EventLinsener> eventLinseners = context.getBeansOfType(EventLinsener.class);
        if (CommonUtil.isNotEmpty(eventLinseners)) {
            for (Entry<String, EventLinsener> entry : eventLinseners.entrySet()) {
                EventLinsener linsener = entry.getValue();
                String[] events = linsener.events();
                if (CommonUtil.isNotEmpty(events)) {
                    for (String channel : linsener.events()) {
                        if (linsener.subscriber()) {
                            addSubscriber(channel, linsener);
                        }
                        else {
                            addConsummer(channel, linsener);
                        }
                    }
                }
            }
        }
    }

    private void addSubscriber(String channel, EventLinsener linsener) {
        MessageHelper.createMessageSubcriberFactory().registSubscriber(channel, linsener);
    }

    private void addConsummer(String channel, EventLinsener linsener) {
        eventLinsenerHolder.put(channel, linsener);
    }

    /**
     * Description: <br>
     * 
     * @author 王伟<br>
     * @taskId <br>
     *         <br>
     */
    @Override
    public void destory() {
        flag = false;
    }

    private static class EventConsummer {

        private EventLinsener linsener;

        private byte[] data;

        private String event;

        public EventConsummer(EventLinsener linsener, byte[] data, String event) {
            this.linsener = linsener;
            this.data = data;
            this.event = event;
        }

        public void emmit() {
            try {
                this.linsener.onMessage(event, data);
            }
            catch (Exception e) {
                LoggerUtil.error(e);
            }
        }

    }

}
