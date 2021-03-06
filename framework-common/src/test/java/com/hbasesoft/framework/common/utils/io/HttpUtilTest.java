/**************************************************************************************** 
 Copyright © 2003-2012 hbasesoft Corporation. All rights reserved. Reproduction or       <br>
 transmission in whole or in part, in any form or by any means, electronic, mechanical <br>
 or otherwise, is prohibited without the prior written consent of the copyright owner. <br>
 ****************************************************************************************/
package com.hbasesoft.framework.common.utils.io;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * <Description> <br>
 * 
 * @author 王伟<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2017年9月22日 <br>
 * @since V1.0<br>
 * @see com.hbasesoft.framework.common <br>
 */
public class HttpUtilTest {

    /**
     * Description: <br>
     * 
     * @author 王伟<br>
     * @taskId <br>
     *         <br>
     */
    @Test
    public void doGet() {
        System.out.println(HttpUtil.doGet("https://www.towngasvcc.com", "utf-8"));
    }

    /**
     * Description: <br>
     * 
     * @author 王伟<br>
     * @taskId <br>
     *         <br>
     */
    @Test
    public void doPost() {
        Map<String, String> param = new HashMap<>();
        System.out.println(HttpUtil.doPost("http://www.baidu.com", param));
    }

    /**
     * Description: <br>
     * 
     * @author 王伟<br>
     * @taskId <br>
     *         <br>
     */
    @Test
    public void doGetDowloadFile() {
        HttpUtil.downloadFile("https://timgsa.baidu.com/timg?image"
            + "&quality=80&size=b9999_10000&sec=1506669107&di=3f964616fbb30dc8e9090f3921ce6dbf"
            + "&imgtype=jpg&er=1&src=http%3A%2F%2Fimgsrc.baidu.com%2Fbaike%2Fpic%"
            + "2Fitem%2Fcb8065380cd79123ea3a4a45af345982b2b7802d.jpg", "a2.jpg");
    }
}
