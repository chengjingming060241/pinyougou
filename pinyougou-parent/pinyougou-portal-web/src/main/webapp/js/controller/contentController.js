app.controller('contentController',function ($scope,contentService) {


    $scope.contentList=[]; //广告类表

    //根据广告分类查询广告
    $scope.findByCategoryId=function (categoryId) {
        contentService.findByCategoryId(categoryId).success(
            function (response) {
                $scope.contentList[categoryId] = response;
            }
        );
    };

    //搜索（传递参数）
    $scope.search=function () {
        location.href="http://localhost:9104/search.html#?keywords="+$scope.keywords;
    }






});