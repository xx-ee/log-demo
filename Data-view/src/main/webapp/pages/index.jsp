<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
    <title>网站流量分析实时指标展示</title>
    <script type="text/javascript" src="/js/echarts.js"></script>
    <script type="text/javascript" src="/js/jquery.js"></script>
    <script type="text/javascript">
        window.onload=function(){
            var tmpPv = [];
            var tmpUv = [];
            var tmpVv = [];
            var tmpNewIp = [];
            var tmpNewCust = [];
            var webTime=[]


            var myChart = null;
            window.setInterval(function(){
                myChart = echarts.init(document.getElementById("div_1"));
                //{'reportTime'='xxx','pv'=xxx,'uv'=xx,'vv'=xx,'newip'=xx,'newcust'=xx}
                $.get("/dataview",function(datax){
                    console.log(datax)
                    console.log(datax.data[0].reporttime)


                    if (datax!=null&&datax.data!=null&&datax.data.length>0)
                    {
                    datax=datax.data
                        console.log(datax[0].pv)
                    tmpPv.push(datax[0].pv)
                    if (tmpPv.length>60){tmpPv.shift()}

                    tmpUv.push(datax[0].uv)
                    if (tmpUv.length>60){tmpUv.shift()}

                    tmpVv.push(datax[0].vv)
                    if (tmpVv.length>60){tmpVv.shift()}

                    tmpNewIp.push(datax[0].newip)
                    if (tmpNewIp.length>60){tmpNewIp.shift()}

                    webTime.push(datax[0].webTime)
                    if (webTime.length>60){webTime.shift()}


                    tmpNewCust.push(datax[0].newcust)
                    if (tmpNewCust.length>60){tmpNewCust.shift()}
                    }

                    // console.log(tmpPv)
                    // console.log(tmpUv)
                    // console.log(tmpVv)
                    // console.log(tmpNewIp)
                    // console.log(tmpNewCust)
                    //3.构建option
                    var option = {

                        backgroundColor: '#0f375f',
                        title: {
                            text: '网站流量分析实时指标展示'
                        },
                        tooltip: {
                            trigger: 'axis'
                        },
                        legend: {
                            data: ['点击量', '独立访客数', '会话总数', '新增IP总数', '新增独立访客数'],
                            textStyle: {
                                color: '#ccc'
                            }
                        },
                        grid: {
                            left: '3%',
                            right: '4%',
                            bottom: '3%',
                            containLabel: true
                        },
                        toolbox: {
                            feature: {
                                saveAsImage: {}
                            }
                        },
                        xAxis: {
                            type: 'category',
                            boundaryGap: false,
                            axisLine: {
                                lineStyle: {
                                    color: '#ccc'
                                }
                            },
                            data:webTime
                            // ,
                            // data: (function (){
                            //     var now = new Date();
                            //     var res = [];
                            //     var len = 60;
                            //     while (len--) {
                            //         res.unshift(now.toLocaleTimeString().replace(/^\D*/,''));
                            //         now = new Date(now - 2000);
                            //     }
                            //     return res;
                            // })()
                        },
                        yAxis: {
                            type: 'value',
                            axisLine: {
                                lineStyle: {
                                    color: '#ccc'
                                }
                            }
                        },
                        series: [
                            {
                                name: '点击量',
                                type: 'line',
                                stack: '总量',
                                data: tmpPv
                            },
                            {
                                name: '独立访客数',
                                type: 'line',
                                stack: '总量',
                                data: tmpUv
                            },
                            {
                                name: '会话总数',
                                type: 'line',
                                stack: '总量',
                                data: tmpVv
                            },
                            {
                                name: '新增IP总数',
                                type: 'line',
                                stack: '总量',
                                data: tmpNewIp
                            },
                            {
                                name: '新增独立访客数',
                                type: 'line',
                                stack: '总量',
                                data: tmpNewCust
                            }
                        ]
                    };
                    myChart.setOption(option);
                })
            },2000)
        }
    </script>
</head>
<body>
<div id="div_1" style="width: 1200px;height:600px;"></div>
</body>
</html>
