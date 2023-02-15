/**************************************************************************************** 
 Copyright © 2003-2012 hbasesoft Corporation. All rights reserved. Reproduction or       <br>
 transmission in whole or in part, in any form or by any means, electronic, mechanical <br>
 or otherwise, is prohibited without the prior written consent of the copyright owner. <br>
 ****************************************************************************************/
package com.hbasesoft.framework.shell.tx;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.query.Criteria;
import org.springframework.data.cassandra.core.query.CriteriaDefinition;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.stereotype.Component;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.hbasesoft.framework.common.utils.date.DateUtil;
import com.hbasesoft.framework.shell.core.CommandHandler;
import com.hbasesoft.framework.shell.core.Shell;
import com.hbasesoft.framework.shell.core.vo.AbstractOption;
import com.hbasesoft.framework.shell.tx.TxQuery.Option;
import com.hbasesoft.framework.shell.tx.entity.TxClientinfoEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * <Description> <br>
 * 
 * @author 王伟<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2020年8月14日 <br>
 * @since V1.0<br>
 * @see com.hbasesoft.framework.shell.tx <br>
 */
@Component
public class TxQuery implements CommandHandler<Option> {

    /** */
    @Autowired
    private CassandraOperations cassandraOperations;

    /** */
    private static final int NUM3 = 3;

    /** */
    private static final int NUM4 = 4;

    /** */
    private static final int NUM5 = 5;

    /** */
    private static final int NUM6 = 6;

    /** */
    private static final int NUM_10 = 10;

    /** */
    private static final int NUM10 = 10;

    /** */
    private static final int NUM100 = 100;

    /**
     * Description: <br>
     * 
     * @author 王伟<br>
     * @taskId <br>
     * @param cmd
     * @param option
     * @param shell <br>
     */
    @Override
    public void execute(final JCommander cmd, final Option option, final Shell shell) {

        List<CriteriaDefinition> cds = new ArrayList<>();

        PrintStream shellOut = shell.getOut();

        if (StringUtils.isNotEmpty(option.id)) {
            cds.add(Criteria.where("id").is(option.id));
        }

        if (StringUtils.isNotEmpty(option.mark)) {
            cds.add(Criteria.where("mark").is(option.mark));
        }

        if (StringUtils.isNotEmpty(option.clientInfo)) {
            cds.add(Criteria.where("clientInfo").is(option.clientInfo));
        }

        if (!option.all) {
            cds.add(Criteria.where("nextRetryTime").lte(DateUtil.getCurrentDate()));
        }

        if (option.isCount()) {
            Query q = Query.query(cds.toArray(new CriteriaDefinition[0])).withAllowFiltering();

            long s = cassandraOperations.count(q, TxClientinfoEntity.class);
            shellOut.println("统计到：" + s + "条数据。");
        }
        else {
            if (option.pageSize > NUM100 || option.pageSize < 1) {
                option.pageSize = NUM10;
            }
            Query q = Query.query(cds.toArray(new CriteriaDefinition[0])).withAllowFiltering().limit(option.pageSize);
            List<TxClientinfoEntity> entities = cassandraOperations.select(q, TxClientinfoEntity.class);

            shellOut.println(
                "ID\t\t标记(mark)\t\t参数(args)\t\t上下文(context)\t\t最大重试次数(maxRetryTimes)\t\t当前已重试次数(currentRetryTimes)\t\t"
                    + "重试配置(retryConfigs)\t\t下次重试时间(nextRetryTime)\t\t客户端信息（clientInfo）\t\t创建时间(createTime)");

            if (CollectionUtils.isNotEmpty(entities)) {
                for (TxClientinfoEntity entity : entities) {
                    shellOut.print(entity.getId());
                    shellOut.print("\t\t");
                    shellOut.print(entity.getMark());
                    shellOut.print("\t\t");
                    shellOut.print(entity.getArgs());
                    shellOut.print("\t\t");
                    shellOut.print(entity.getContext());
                    shellOut.print("\t\t");
                    shellOut.print(entity.getMaxRetryTimes());
                    shellOut.print("\t\t");
                    shellOut.print(entity.getCurrentRetryTimes());
                    shellOut.print("\t\t");
                    shellOut.print(entity.getRetryConfigs());
                    shellOut.print("\t\t");
                    shellOut.print(
                        entity.getNextRetryTime() == null ? null : DateUtil.date2String(entity.getNextRetryTime()));
                    shellOut.print("\t\t");
                    shellOut.print(entity.getClientInfo());
                    shellOut.print("\t\t");
                    shellOut.println(DateUtil.date2String(entity.getCreateTime()));
                }
            }
        }

    }

    /**
     * Description: <br>
     * 
     * @author 王伟<br>
     * @taskId <br>
     * @return <br>
     */
    @Override
    public String toString() {
        return "查询需要重试节点的相关命令";
    }

    @Getter
    @Setter
    public static class Option extends AbstractOption {

        /** */
        @Parameter(names = {
            "-id"
        }, help = true, order = 1, description = "根据ID查询")
        private String id;

        /** */
        @Parameter(names = {
            "--mark", "-m"
        }, help = true, order = 2, description = "根据标记查询")
        private String mark;

        /** */
        @Parameter(names = {
            "--clientInfo", "-ci"
        }, help = true, order = NUM3, description = "根据客户端信息查询")
        private String clientInfo;

        /** */
        @Parameter(names = {
            "--all", "-a"
        }, help = true, order = NUM4, description = "查询所有，包含未过期的内容")
        private boolean all = false;

        /** */
        @Parameter(names = {
            "--count", "-c"
        }, help = true, order = NUM5, description = "统计数量")
        private boolean count = false;

        /** */
        @Parameter(names = {
            "--size", "-s"
        }, help = true, order = NUM6, description = "每页的数量")
        private int pageSize = NUM_10;

    }
}
