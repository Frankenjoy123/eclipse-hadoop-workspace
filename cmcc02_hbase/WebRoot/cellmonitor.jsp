<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>中国移动无线网络优化监控系统</title>
</head>
<body>
	<div>
		<h2>小区掉话监控</h2>
	</div>
    <div id="main" style="height:500px;border:1px solid #ccc;padding:10px;"></div>
    <script type="text/javascript" src="asset/js/jquery.min.js"></script>
    <script src="js/echarts.js"></script>
    <script type="text/javascript">
    
    window.setInterval(function(){
    	$.ajax({
    		type:'post',
    		url:'cellMonitorServlet.action',
    		//data:'',
    		dataType:'json',
    		success:function(rs){
    			//console.debug(rs);
    			changedata(rs);
    		}
    		
    	})
    	
    }, 3000) 
    require.config({
        paths: {
            echarts: './js'
        }
    });
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
            myChart.setOption({
                tooltip : {
                    trigger: 'axis'
                },
                legend: {
                    data:['基站掉话率告警' ]
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
                        data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月']
                    }
                ],
                yAxis : [
                    {
                        type : 'value',
                        splitArea : {show : true}
                    }
                ],
                series : [
                    {
                        name:'基站掉话率告警',
                        type:'bar',
                        data:[2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 3.3]
                    } 
                ]
            });
            
       
        }
    );
    var count=10;
    function changedata(datars){
    	myChart.setOption({
    			xAxis : [
                     {
                          data : datars.xAxis
                     }
                 ],
                 series : [
                      {
                          data:datars.series
                      } 
                 ]
    	
    	})
    }
    </script>
    <input onclick="changedata();" type="button" value="test">
</body>
</html>
