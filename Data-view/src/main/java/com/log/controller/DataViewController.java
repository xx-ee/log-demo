package com.log.controller;

import com.log.response.DataGridView;
import com.log.service.IDataViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: DataViewController
 * @Description:
 * @Author: xiedong
 * @Date: 2020/2/10 13:04
 */
@RestController
public class DataViewController {
    @Autowired
    private IDataViewService iDataViewService;
    @RequestMapping("/dataview")
    public DataGridView getDataView(){
        return this.iDataViewService.queryCurrtData();
    }
}
