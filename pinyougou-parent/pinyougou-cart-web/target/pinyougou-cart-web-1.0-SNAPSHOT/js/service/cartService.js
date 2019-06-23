//购物车服务层
app.service('cartService',function($http){


    //购物车列表
    this.findCartList=function(){
        return $http.get('cart/findCartList.do');
    };

    //添加商品到购物车
    this.addGoodsToCartList=function(itemId,num){
        return $http.get('cart/addGoodsToCartList.do?itemId='+itemId+'&num='+num);
    };


    this.sum = function (cartList) {
        //总数量 总金额
        var totleValue = {totalNum: 0,totalMoney: 0};

        for (var i = 0; i <cartList.length ; i++) {
            var car = cartList[i]; //购物车对象
            for (var j = 0; j <car.orderItems.length ; j++){
                var orderItem = car.orderItems[j]; //购物车明细
                totleValue.totalNum += orderItem.num; //累加数量
                totleValue.totalMoney += orderItem.totalFee; //累加数量
            }
        }

        return totleValue;
    };

    //获取当前登录用户的收货地址
    this.findAddressList = function () {
        return $http.get('address/findListByLoginUser.do')
    };

    //提交订单
    this.sbumitOrder=function (order) {
        return $http.post('order/add.do',order)
    }




});