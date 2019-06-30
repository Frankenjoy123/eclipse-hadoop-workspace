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
    <div id="main" style="height:500px;border:1px solid #ccc;padding:10px;"></div>
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
            'echarts/chart/bar',
            'echarts/chart/line',
            'echarts/chart/map'
        ],
        function (ec) {
            //--- 折柱 ---
            myChart = ec.init(document.getElementById('main'));
            option = {
            	    title : {
            	        text: '未来一周气温变化',
            	        subtext: '纯属虚构'
            	    },
            	    tooltip : {
            	        trigger: 'axis'
            	    },
            	    legend: {
            	        data:['最高气温','最低气温']
            	    },
            	    toolbox: {
            	        show : true,
            	        feature : {
            	            mark : {show: true},
            	            dataView : {show: true, readOnly: false},
            	            magicType : {show: true, type: ['line', 'bar']},
            	            restore : {show: true},
            	            saveAsImage : {show: true}
            	        }
            	    },
            	    calculable : true,
            	    xAxis : [
            	        {
            	            type : 'category',
            	            boundaryGap : false,
            	            data : ['9.30','10.00','10.30','11.00','11.30','12.00','14.00','15.00','15.30','16.00','16.30','17.00']
            	        }
            	    ],
            	    yAxis : [
            	        {
            	            type : 'value',
            	            axisLabel : {
            	                formatter: '{value} °C'
            	            }
            	        }
            	    ],
            	    series : [
            	        {
            	            name:'最高气温',
            	            type:'line',
            	            data:[11, 11, 15, 13, 12,'-', '-'],
            	            markPoint : {
            	                data : [
            	                    {type : 'max', name: '最大值'},
            	                    {type : 'min', name: '最小值'}
            	                ]
            	            },
            	            markLine : {
            	                data : [
            	                    {type : 'average', name: '平均值'}
            	                ]
            	            }
            	        },
            	        {
            	            name:'最低气温',
            	            type:'line',
            	            data:[1, -2, 2, 5, 3, 2, 0],
            	            markPoint : {
            	                data : [
            	                    {name : '周最低', value : -2, xAxis: 1, yAxis: -1.5}
            	                ]
            	            },
            	            markLine : {
            	                data : [
            	                    {type : 'average', name : '平均值'}
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
    	//myChart.setSeries({data:[26.0, 47.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 73.3]})
    	myChart.setSeries([{ },{data:[5, 3.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 173.3]}]);
    	 console.debug(myChart.getSeries());
    	
    }
    function changedata1(){
    	//myChart.setSeries({data:[26.0, 47.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 73.3]})
    	myChart.setSeries([{name:'asdfasdf',data:[7, 47.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 73.3]},{data:[5, 3.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 173.3]}]);
    	 console.debug(myChart.getSeries());
    	
    }
    </script>
    <input onclick="changedata();" type="button" value="test">
    <input onclick="changedata1();" type="button" value="test1">
</body>
</html>
