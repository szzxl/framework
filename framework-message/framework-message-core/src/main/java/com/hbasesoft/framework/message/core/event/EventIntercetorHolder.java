/**************************************************************************************** 
 Copyright © 2003-2012 hbasesoft Corporation. All rights reserved. Reproduction or       <br>
 transmission in whole or in part, in any form or by any means, electronic, mechanical <br>
 or otherwise, is prohibited without the prior written consent of the copyright owner. <br>
 ****************************************************************************************/
package com.hbasesoft.framework.message.core.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <Description> <br>
 * 
 * @author 王伟<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate Jul 9, 2020 <br>
 * @since V1.0<br>
 * @see com.hbasesoft.framework.message.core.event <br>
 */
public final class EventIntercetorHolder {

    /**
     * interceptor holder
     */
    private static Map<String, List<EventInterceptor>> intercetorHolder = new HashMap<String, List<EventInterceptor>>();

    /**
     * Description: <br>
     * 
     * @author 王伟<br>
     * @taskId <br>
     * @param interceptor interceptor
     * @param events <br>
     */
    public static synchronized void registInterceptor(final EventInterceptor interceptor, final String... events) {
        for (String event : events) {
            List<EventInterceptor> interceptors = intercetorHolder.get(event);
            if (interceptors == null) {
                interceptors = new ArrayList<>();
            }
            if (!interceptors.contains(interceptor)) {
                interceptors.add(interceptor);
            }
            if (interceptors.size() >= 2) {
                Collections.sort(interceptors, (i1, i2) -> i1.order() - i2.order());
            }
            intercetorHolder.put(event, interceptors);
        }
    }

    /**
     * Description: 获取拦截器<br>
     * 
     * @author 王伟<br>
     * @taskId <br>
     * @param event
     * @return <br>
     */
    public static synchronized List<EventInterceptor> getInterceptors(final String event) {
        return intercetorHolder.get(event);
    }
}
