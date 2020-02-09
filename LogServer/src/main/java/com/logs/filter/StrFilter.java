package com.logs.filter;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * @ProjectName: logdemo
 * @Package: com.logs.filter
 * @ClassName: StrFilter
 * @Author: dong
 * @Description: 自定义日志过滤器
 * @Date: 2019/9/7 0:23
 * @Version: 1.0
 */
public class StrFilter extends Filter<ILoggingEvent> {
    @Override
    public FilterReply decide(ILoggingEvent event) {
        if (event.getMessage().contains("页面"))
        {
            return FilterReply.ACCEPT;
        }
        else
        {
            return FilterReply.DENY;
        }
    }
}
