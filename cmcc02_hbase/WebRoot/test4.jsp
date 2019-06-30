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
            	        text: '动态数据',
            	        subtext: '纯属虚构'
            	    },
            	    tooltip : {
            	        trigger: 'axis'
            	    },
            	    legend: {
            	        data:['最新成交价', '预购队列']
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
            	    dataZoom : {
            	        show : false,
            	        start : 0,
            	        end : 100
            	    },
            	    xAxis : [
            	        {
            	            type : 'category',
            	            boundaryGap : true,
            	            data : (function (){
            	                var now = new Date();
            	                var res = [];
            	                var len = 10;
            	                while (len--) {
            	                	   var res = [];
                   	                var len = 30;
                   	                for(i=1;i<=len;i++){
                   	                    res.push(i);
                   	                }
                   	                return res;
            	                }
            	                return res;
            	            })()
            	        },
            	        {
            	            type : 'category',
            	            boundaryGap : true,
            	            data : (function (){
            	                var res = [];
            	                var len = 30;
            	                for(i=1;i<len;i++){
            	                    res.push(i);
            	                }
            	                return res;
            	            })()
            	        }
            	    ],
            	    yAxis : [
            	        {
            	            type : 'value',
            	            scale: true,
            	            name : '价格',
            	            boundaryGap: [0.2, 0.2]
            	        },
            	        {
            	            type : 'value',
            	            scale: true,
            	            name : '预购量',
            	            boundaryGap: [0.2, 0.2]
            	        }
            	    ],
            	    series : [
            	        {
            	            name:'预购队列',
            	            type:'line',
            	            xAxisIndex: 1,
            	            yAxisIndex: 1,
            	            data:(function (){
            	                var res = [];
            	                var len = 10;
            	                while (len--) {
            	                    res.push(Math.round(Math.random() * 1000));
            	                }
            	                return res;
            	            })()
            	        },
            	        {
            	            name:'最新成交价',
            	            type:'line',
            	            data:(function (){
            	                var res = [];
            	                var len = 5;
            	                while (len--) {
            	                    res.push((Math.random()*10 + 5).toFixed(1) - 0);
            	                }
            	                return res;
            	            })()
            	        }
            	    ]
            	};
            	var lastData = 11;
            	var axisData;
            	var timeTicket= 1
            	clearInterval(timeTicket);
            	    axisData = 1
            	timeTicket = setInterval(function (){
            	    lastData += Math.random() * ((Math.round(Math.random() * 10) % 2) == 0 ? 1 : -1);
            	    lastData = lastData.toFixed(1) - 0;
            	    console.debug(axisData);
            	    axisData= axisData+0.5;
            	    // 动态数据接口 addData
            	    myChart.addData([
            	        [
            	            0,        // 系列索引
            	            Math.round(Math.random() * 1000), // 新增数据
            	            true,     // 新增数据是否从队列头部插入
            	            false     // 是否增加队列长度，false则自定删除原有数据，队头插入删队尾，队尾插入删队头
            	        ],
            	        [
            	            1,        // 系列索引
            	            lastData, // 新增数据
            	            false,    // 新增数据是否从队列头部插入
            	            true  ,    // 是否增加队列长度，false则自定删除原有数据，队头插入删队尾，队尾插入删队头
            	            axisData  // 坐标轴标签
            	        ]
            	    ]);
            	}, 2000);
            	                    
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
