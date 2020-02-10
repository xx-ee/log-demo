package com.log.base;

import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;
import tk.mybatis.mapper.common.RowBoundsMapper;

/**
 * @ClassName: BaseMapper
 * @Description:
 * @Author: xiedong
 * @Date: 2020/1/13 16:38
 */
public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T>, RowBoundsMapper<T>, IdsMapper<T> {
}
