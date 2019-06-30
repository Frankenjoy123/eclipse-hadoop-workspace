<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>ECharts</title>
    
 
</head>

<body>
    <!--Step:1 Prepare a dom for ECharts which (must) has size (width & hight)-->
    <!--Step:1 为ECharts准备一个具备大小（宽高）的Dom-->
    <div id="main"  class="main" style="height:400px;border:1px solid #ccc;padding:10px; width:900px;"></div>
    <!--Step:2 Import echarts.js-->
    <!--Step:2 引入echarts.js-->
    <script src="js/echarts.js"></script>
    <script type="text/javascript">
    // Step:3 conifg ECharts's path, link to echarts.js from current page.
    // Step:3 为模块加载器配置echarts的路径，从当前页面链接到echarts.js，定义所需图表路径
    require.config({
        paths: {
            echarts: './js'
        }
    });
    
    // Step:4 require echarts and use it in the callback.
    // Step:4 动态加载echarts然后在回调函数中开始使用，注意保持按需加载结构定义图表路径
    require(
        [
            'echarts',
            'echarts/chart/line',
            'echarts/chart/bar',
            'echarts/chart/scatter',
            'echarts/chart/k',
            'echarts/chart/pie',
            'echarts/chart/radar',
            'echarts/chart/force',
            'echarts/chart/chord',
            'echarts/chart/gauge',
            'echarts/chart/funnel',
            'echarts/chart/eventRiver',
        ],
        function (ec,curTheme) {
            //--- 折柱 ---
            console.debug(curTheme);
            myChart = ec.init(document.getElementById('main'),curTheme);
            option = {
            	    title : {
            	        text: '双数值轴折线',
            	        subtext: '纯属虚构'
            	    },
            	    tooltip : {
            	        trigger: 'axis',
            	         enterable:true,
            	        axisPointer:{
            	            type : 'cross',
            	            lineStyle: {
            	                type : 'dashed',
            	                width : 1
            	            }
            	        },
            	        formatter : function (params) {
            	            return params.seriesName + ' : [ '
            	                   + params.value[0] + ', ' 
            	                   + params.value[1] + ' ]';
            	        }
            	    },
            	    legend: {
            	        data:['数据1' ]
            	    },
            	    toolbox: {
            	        show : true,
            	        feature : {
            	            mark : {show: true},
            	            dataZoom : {show: true},
            	            dataView : {show: true, readOnly: false},
            	            magicType : {show: true, type: ['line', 'bar']},
            	            restore : {show: true},
            	            saveAsImage : {show: true}
            	        }
            	    },
            	    calculable : true,
            	    xAxis : [
            	        {
            	            type: 'value'
            	        }
            	    ],
            	    yAxis : [
            	        {
            	            type: 'value',
            	            axisLine: {
            	                lineStyle: {
            	                    color: '#dc143c'
            	                }
            	            }
            	        }
            	    ],
            	    series : [
            	        {
            	            name:'数据1',
            	            type:'line',
            	            smooth:true,
            	            data:[
            	                  [1.5, 10], [5, 7], [8, 8], [12, 6], [11, 12], [16, 9], [14, 6], [17, 4], [19, 9]
            	            ],
            	            markPoint : {
            	                data : [
            	                    // 纵轴，默认
            	               //     {type : 'max', name: '最大值',symbol: 'emptyCircle', itemStyle:{normal:{color:'#dc143c',label:{position:'top'}}}},
            	                //    {type : 'min', name: '最小值',symbol: 'emptyCircle', itemStyle:{normal:{color:'#dc143c',label:{position:'bottom'}}}},
            	                    // 横轴
            	                //    {type : 'max', name: '最大值', valueIndex: 0, symbol: 'emptyCircle', itemStyle:{normal:{color:'#1e90ff',label:{position:'right'}}}},
            	                //    {type : 'min', name: '最小值', valueIndex: 0, symbol: 'emptyCircle', itemStyle:{normal:{color:'#1e90ff',label:{position:'left'}}}}
            	                ]
            	            } 
            	        } 
            	    ]
            	};
            	                    
            myChart.setOption(option );
            
       
        }
    );
    var count=10;
    function changedata(){
    	myChart.setSeries([{ },{data:[ [1, 2], [2, 3], [4, 2], [7, 5], [11, 2], [18, 3],[19.5,8]]}]);
    	 console.debug(myChart.getSeries());
    	
    }
    function changedata1(){
    	//myChart.setSeries({data:[26.0, 47.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 73.3]})
    	myChart.setSeries([{name:'asdfasdf',data:[7, 47.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 73.3]},{data:[5, 3.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 173.3]}]);
    	 console.debug(myChart.getSeries());
    	
    }
    var start=6
    function changedata3(){
		var cc = Math.round(Math.random() * 10);
        // 动态数据接口 addData
        myChart.addData([
                         
            [ 
				0,        // 系列索引
				[start,cc], // 新增数据
				false,    // 新增数据是否从队列头部插入
				true 
            ],
            [
                
            ]
        ]);
        myChart.setOption({series : [
                         	        {
                         	        	 markPoint : {
                         	                data : [
												{name: '标注1',value:cc , xAxis: start, yAxis: cc , itemStyle:{normal:{color:'#dc143c',label:{position:'top'}}}}
                         	                    // 纵轴，默认
                         	                    // {type : 'max', name: '最大值',symbol: 'emptyCircle', itemStyle:{normal:{color:'#dc143c',label:{position:'top'}}}},
                         	                    // {type : 'min', name: '最小值',symbol: 'emptyCircle', itemStyle:{normal:{color:'#dc143c',label:{position:'bottom'}}}},
                         	                    // 横轴
                         	                    // {type : 'max', name: '最大值', valueIndex: 0, symbol: 'emptyCircle', itemStyle:{normal:{color:'#1e90ff',label:{position:'right'}}}},
                         	                   // {type : 'min', name: '最小值', valueIndex: 0, symbol: 'emptyCircle', itemStyle:{normal:{color:'#1e90ff',label:{position:'left'}}}}
                         	                ]
                         	            } 	
                         	        }
                         	        ]
        })
                         
        start++;
           console.debug(myChart.getSeries())
    }
    </script>
    <input onclick="changedata3();" type="button" value="test3">
</body>
</html>
