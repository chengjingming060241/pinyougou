app.controller("searchController",function ($scope,searchService,$location) {


    //定义搜索对象的结构category：商品分类
    $scope.searchMap = {'keywords':'','category':'','brand':'','spec':{},'price':'','pageNo':1,'pageSize':30,'sort':'','sortField':''};

    //搜索
    $scope.search=function () {
        $scope.searchMap.pageNo = parseInt($scope.searchMap.pageNo); //转换成数字
        searchService.search($scope.searchMap).success(
          function (response) {
              $scope.resultMap = response;
              //$scope.searchMap.pageNo = 1; //查询后显示第一页
              builPageLabel(); //构建分页栏
          }
        );
    };

    //构建分页标签(totalPages为总页数)
    builPageLabel=function(){
        $scope.pageLabel = [];
        var firstPage = 1;//开始页码
        var lastPage = $scope.resultMap.totalPages;//截止页码
        $scope.firstDot = true; //前面有点
        $scope.lastDot = true; //后面有点


        if ($scope.resultMap.totalPages>5){ //假如页码数量大于5页

            if ($scope.searchMap.pageNo<=3){ //假如当前页小于等于3，显示前5页
                lastPage = 5;
                $scope.firstDot = false;
            } else if ($scope.searchMap.pageNo>=$scope.resultMap.totalPages-2){  //显示后5页
                firstPage = $scope.resultMap.totalPages-4;
                $scope.lastDot = false;
            } else {  //显示已当前页为中心的5页
                firstPage = $scope.searchMap.pageNo - 2;
                lastPage = $scope.searchMap.pageNo + 2;
            }
        }else {
            $scope.firstDot = false; //前面无点
            $scope.lastDot = false; //后面无点
        }

        for (var i = firstPage; i <= lastPage ; i++) {
            $scope.pageLabel.push(i);
        }

    };


    //添加搜索项 改变searchMap的值
    $scope.addSearchItem=function (key,value) {

        if (key=='category'||key=='brand'||key=='price'){  //如果用户点击的是分类或者品牌或者价格
            $scope.searchMap[key] = value;

        } else { //用户点击的是规格
            $scope.searchMap.spec[key] = value;
        }
        $scope.search() //查询
    };

    //撤销搜索项 改变searchMap的值
    $scope.removeSearchItem=function (key) {

        if (key=='category'||key=='brand' ||key=='price'){ //如果用户点击的是分类或者品牌或者价格
            $scope.searchMap[key] = "";

        } else { //用户点击的是规格
           delete $scope.searchMap.spec[key];
        }

        $scope.search() //查询
    };

    //点击页码/分页查询
    $scope.queryByPage=function (pageNo) {
        if (pageNo<1 || pageNo>$scope.resultMap.totalPages){
            return;
        }
        $scope.searchMap.pageNo = pageNo;
        $scope.search() //查询
    };

    //判断是否是第一页
    $scope.isTopPage=function () {
        if ($scope.searchMap.pageNo==1){
            return true;
        } else {
            return false;
        }
    };
    //判断是否是最后一页
    $scope.isEndPage=function () {
        if ($scope.searchMap.pageNo==$scope.resultMap.totalPages){
            return true;
        } else {
            return false;
        }
    };

    //排序查询
    $scope.sortSearch = function (sortField,sort) {
        $scope.searchMap.sortField = sortField;
        $scope.searchMap.sort = sort;
        $scope.search() //查询
    };

    //判断关键字是否是品牌
    $scope.keywordsIsBrand=function () {
        for (var i =0;i<$scope.resultMap.brandList.length;i++){
            //是否包含此字符串
            if ($scope.searchMap.keywords.indexOf($scope.resultMap.brandList[i].text)>=0){
                return true;
            }
        }
        return false;
    };

    //接受主页面传递的值
    $scope.loadkeywords=function () {
      $scope.searchMap.keywords = $location.search()['keywords'];

      $scope.search() //查询
    }

    


});