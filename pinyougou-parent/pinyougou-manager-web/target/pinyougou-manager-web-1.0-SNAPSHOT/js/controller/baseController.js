app.controller("baseController",function ($scope) {

    //分页控件配置currentPage:当前页   totalItems :总记录数  itemsPerPage:每页记录数  perPageOptions :分页选项  onChange:当页码变更后自动触发的方法
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function(){
            $scope.reloadList();
        }
    };

    //刷新列表
    $scope.reloadList=function(){
        $scope.search( $scope.paginationConf.currentPage ,  $scope.paginationConf.itemsPerPage );
    };

    $scope.selectIds=[];//用户勾选ID集合

    //用户勾选复选框勾选判断
    $scope.updateSelecttion=function ($event,id) {
        if ($event.target.checked){
            $scope.selectIds.push(id);//push向数组里添加元素
        } else {
            var index = $scope.selectIds.indexOf(id);//查找值的位置
            $scope.selectIds.splice(index,1);//index移除的下边  1表示移除的个数
        }
    };

    //转换json格式
    $scope.jsonToString=function (jsonString,key) {
        var json= JSON.parse(jsonString);

        var values = "";

        for (var i=0;i<json.length;i++){
            if (i>0){
                values+=",";
            }
            values+=json[i][key];
        }

        return values;
    }

});