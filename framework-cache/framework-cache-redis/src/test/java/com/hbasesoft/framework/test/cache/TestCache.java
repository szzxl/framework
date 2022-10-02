/**
 * 
 */
package com.hbasesoft.framework.test.cache;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hbasesoft.framework.cache.core.CacheHelper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <Description> <br>
 * 
 * @author wangwei<br>
 * @version 1.0<br>
 * @CreateDate 2015年6月21日 <br>
 * @see com.hbasesoft.framework.test.cache <br>
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class TestCache {

    /** age */
    private static final int AGE_23 = 23;

    /** age */
    private static final int AGE_24 = 24;

    /** age */
    private static final int AGE_26 = 26;

    /** age */
    private static final int AGE_27 = 27;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Person {

        /** name */
        private String name;

        /** age */
        private int age;

        /**
         * Description: <br>
         * 
         * @author 王伟<br>
         * @taskId <br>
         * @return <br>
         */
        @Override
        public String toString() {
            return "Person [name=" + name + ", age=" + age + "]";
        }
    }

    /**
     * Description: putNode<br>
     * 
     * @author 王伟 <br>
     * @ <br>
     */
    @Before
    public void putNode() {
        Map<String, Person> map = new HashMap<String, Person>();
        map.put("zhangsan", new Person("张三", AGE_23));
        map.put("lisi", new Person("李四", AGE_24));
        map.put("wangwu", new Person("王五", AGE_24));
        CacheHelper.getCache().putNode("person", map);
    }

    /**
     * Description: <br>
     * 
     * @author yang.zhipeng <br>
     * @taskId <br>
     * @ <br>
     */
    @After
    public void getNode() {
        System.out.println(CacheHelper.getCache().getNode("person", Person.class));
    }

    /**
     * Description: <br>
     * 
     * @author yang.zhipeng <br>
     * @taskId <br>
     * @ <br>
     */
    @Test
    public void getValue() {
        Person preson = CacheHelper.getCache().getNodeValue("person", "zhangsan");
        System.out.println(preson);
    }

    /**
     * Description: <br>
     * 
     * @author yang.zhipeng <br>
     * @taskId <br>
     * @ <br>
     */
    @Test
    public void putValue() {
        CacheHelper.getCache().putNodeValue("person", "liuliu", new Person("溜溜", AGE_26));
    }

    /**
     * Description: updateValue<br>
     * 
     * @author 王伟 <br>
     * @ <br>
     */
    @Test
    public void updateValue() {
        CacheHelper.getCache().putNodeValue("person", "liuliu", new Person("溜溜", AGE_27));
    }

    /**
     * Description: removeValue<br>
     * 
     * @author 王伟 <br>
     * @ <br>
     */
    @Test
    public void removeValue() {
        CacheHelper.getCache().removeNodeValue("person", "zhangsan");
    }

    /**
     * Description: removeNode<br>
     * 
     * @author 王伟 <br>
     * @ <br>
     */
    @Test
    public void removeNode() {
        CacheHelper.getCache().remove("person");
    }

    /**
     * Description: clean<br>
     * 
     * @author 王伟 <br>
     * @ <br>
     */
    @Test
    public void clean() {
        CacheHelper.getCache().clear();
    }
}
