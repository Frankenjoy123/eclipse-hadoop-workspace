<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>小区掉话监控</title>
</head>
<body>
    <div>
		<h2>小区掉话监控</h2>
	</div>
    <div id="main" style="height:500px;border:1px solid #ccc;padding:10px;"></div>
      <script type="text/javascript" src="asset/js/jquery.min.js"></script>
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
            'echarts/chart/line' 
        ],
        function (ec,curTheme) {
            //--- 折柱 ---
            console.debug(curTheme);
            myChart = ec.init(document.getElementById('main'),curTheme);
            option = {
            	    title : {
            	        text: '小区基站监控',
            	        subtext: '中国移动'
            	    },
            	    tooltip : {
            	        trigger: 'axis',
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
            	        data:['数据1']
            	    },
            	    toolbox: {
            	        show : true,
            	        feature : {
            	            mark : {show: true},
            	            dataZoom : {show: true},
            	            dataView : {show: true, readOnly: false},
            	            magicType : {show: true, type: ['line']},
            	            restore : {show: true},
            	            saveAsImage : {show: true}
            	        }
            	    },
            	    calculable : true,
            	    xAxis : [
            	        {
            	            type: 'value',
            	            splitNumber:24,
            	            splitLine : {show : true},
            	            max:24,
            	            scale:true
            	           
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
            	            symbol: 'none',
            	            /*
            	            itemStyle: {
            	                normal: {
            	                    areaStyle: {
            	                        // 区域图，纵向渐变填充
            	                        color : (function (){
            	                            var zrColor = require('zrender/tool/color');
            	                            return zrColor.getLinearGradient(
            	                                0, 200, 0, 400,
            	                                [[0, 'rgba(255,0,0,0.8)'],[0.8, 'rgba(255,255,255,0.1)']]
            	                            )
            	                        })()
            	                    }
            	                }
            	            },
            	            */
            	            markPoint : {
            	                data : [
            	                    // 纵轴，默认
            	                 //   {type : 'max', name: '当前掉话汇总',symbol: 'emptyCircle' ,itemStyle:{normal:{color:'#dc143c',label:{position:'top'}}} },
            	                 //   {type : 'min', name: '最小值',symbol: 'emptyCircle', itemStyle:{normal:{color:'#dc143c',label:{position:'bottom'}}}},
            	                    // 横轴
            	                 //   {type : 'max', name: '最大值',   symbol: 'emptyCircle', itemStyle:{normal:{color:'#1e90ff',label:{position:'right'}}}},
            	                   // {type : 'min', name: '最小值', valueIndex: 0, symbol: 'emptyCircle', itemStyle:{normal:{color:'#1e90ff',label:{position:'left'}}}}
            	                ]
            	            },
            	            data : (function (){
            	                var res = [];
            	                var len = -1;
            	                while (len<=23) {
            	                    //res.push([++len,0] );
            	                    res.push(++len );
            	                }
            	                console.debug(res);
            	                return res;
            	            })()
            	        }
            	    ]
            	};
            	                    
            myChart.setOption(option );
            
       
        }
    );
 
    function changedata3(data){
		if(data[0]<=0){
		 myChart.clear();
		 myChart.setOption(option );
			console.debug(data);
		}
        // 动态数据接口 addData
        myChart.addData([
                         
            [ 
				0,        // 系列索引
				//[start=start, Math.round(count=Math.random() * 10+count)], // 新增数据
				data,
				false,    // 新增数据是否从队列头部插入
				true 
            ] 
        ]);
        
        myChart.setOption({series : [
                          	        {
                          	        	 markPoint : {
                          	                data : [
 												{name: '标注1',value:data[1] , xAxis: data[0], yAxis:  data[1] ,symbol: 'emptyCircle' ,itemStyle:{normal:{color:'#dc143c',label:{position:'top'}}}  }
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
    }
    
    
    
    $(function(){
    	$.ajax({
    		type:'post',
    		url:'oneCellDropNumServlet.action?cell_num=29448-000002',
    		data:{flag:'first'},
    		dataType:'json',
    		success:function(rs){
    			if(rs&&rs.length>0){
    				for(var i=0;i<rs.length;i++){
		    			console.debug("--",rs[i]);
		    			 eval("var jsonrs = "+rs[i]);
		    			 if(jsonrs){
		    					changedata3([jsonrs.xAxis,jsonrs.call_drop_num]);
		    			 }
    					
    				}
    				
    			}
    		}
    		
    	})
    	
    })
        window.setInterval(function(){
    	$.ajax({
    		type:'post',
    		url:'oneCellDropNumServlet.action?cell_num=29448-000002',
    		//data:'',
    		dataType:'json',
    		success:function(rs){
    			
    			console.debug("--",rs[0]);
    			 eval("var jsonrs = "+rs[0]);
    			 if(jsonrs){
    			changedata3([jsonrs.xAxis,jsonrs.call_drop_num]);
    				 
    			 }
    		}
    		
    	})
    	
    }, 3000) 
    </script>
</body>
</html>
