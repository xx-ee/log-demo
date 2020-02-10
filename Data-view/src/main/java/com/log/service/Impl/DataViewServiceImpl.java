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
//        Tongji2 tongji21 = new Tongji2();
//        long currMills=System.currentTimeMillis()/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();
//        tongji21.setReporttime(new Date(currMills));
//        List<Tongji2> datas = this.tongji2Mapper.select(tongji21);
        List<Tongji2> tongji2s = this.tongji2Mapper.selectAll();
        if(tongji2s==null||tongji2s.size()==0)
        {
            dataGridView.setCode(200);
            dataGridView.setCount(0l);
            dataGridView.setMsg("暂无数据");
        }else
        {
            ArrayList<TongjiVo> tongjiVos = new ArrayList<>();
            ArrayList<Tongji2> tongjiVos1 = new ArrayList<>();
            for (Tongji2 tongji2 : tongji2s) {
                if (tongji2.getReporttime().before(DateUtil.todayLastDate())&&tongji2.getReporttime().after(DateUtil.todayFirstDate())){
                    tongji2.setWebTime(DateUtil.getYyyy_MM_DD_Hh_Mm_Ss(new Date()).split(" ")[1]);
                    tongjiVos1.add(tongji2);
                }
            }
            dataGridView.setCode(200);
            dataGridView.setCount(tongjiVos1.size()+0l);
            dataGridView.setData(tongjiVos1);
            dataGridView.setMsg("查询成功");
        }
        return dataGridView;
    }
}
