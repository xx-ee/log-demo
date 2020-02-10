package com.log.service.Impl;

import com.log.VO.TongjiVo;
import com.log.mapper.Tongji2Mapper;
import com.log.model.Tongji2;
import com.log.response.DataGridView;
import com.log.service.IDataViewService;
import com.log.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


/**
 * @ClassName: DataViewServiceImpl
 * @Description:
 * @Author: xiedong
 * @Date: 2020/2/10 13:05
 */
@Service
@Slf4j
public class DataViewServiceImpl implements IDataViewService {
    @Autowired(required = false)
    private Tongji2Mapper tongji2Mapper;
    @Override
    public DataGridView queryCurrtData()
    {
        DataGridView dataGridView = new DataGridView();

        //查询当天数据
        Tongji2 tongji21 = new Tongji2();
        long currMills=System.currentTimeMillis()/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();
        tongji21.setReporttime(new Date(currMills));
        List<Tongji2> datas = this.tongji2Mapper.select(tongji21);
        if(datas==null||datas.size()==0)
        {
            dataGridView.setCode(200);
            dataGridView.setCount(0l);
            dataGridView.setMsg("暂无数据");
        }else
        {
            ArrayList<TongjiVo> tongjiVos = new ArrayList<>();
            for (Tongji2 tongji2 : datas) {
                TongjiVo tongjiVo = new TongjiVo();
                tongjiVo.setRTime(DateUtil.getYYYY_MM_DD(tongji2.getReporttime()));
                tongjiVo.setNewcust(tongji2.getNewcust());
                tongjiVo.setNewip(tongji2.getNewip());
                tongjiVo.setPv(tongji2.getPv());
                tongjiVo.setUv(tongji2.getUv());
                tongjiVo.setVv(tongji2.getVv());
                tongjiVos.add(tongjiVo);
            }
            dataGridView.setCode(200);
            dataGridView.setCount(tongjiVos.size()+0l);
            dataGridView.setData(tongjiVos);
            dataGridView.setMsg("查询成功");
        }
        return dataGridView;
    }
}
