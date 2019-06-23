 //控制层 
app.controller('userController' ,function($scope,$controller   ,userService){	
	

	//注册
	$scope.reg=function (){

		//比较两次密码是否相同
		if ($scope.password!=$scope.entity.password){
			alert("两次密码输入不一致，请从新输入");
			$scope.entity.password = "";
			$scope.password = "";

			return;
		}

		//提交注册
        userService.add($scope.entity,$scope.smscode).success(
        	function (response) {
			alert(response.message);
        });
	};

	//发送验证码
	$scope.sendCode=function () {
		if ($scope.entity.phone==null ||$scope.entity.phone==""){
			alert("请填写手机号");
			return;
		} ;

		userService.sendCode($scope.entity.phone).success(
			function (response) {
				alert(response.message)
            }
		);
    };

    
});	
