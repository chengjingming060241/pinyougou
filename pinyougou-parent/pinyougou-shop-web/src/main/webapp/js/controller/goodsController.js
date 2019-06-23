 //控制层 $location静态页面传值
app.controller('goodsController' ,function($scope,$controller   ,goodsService,uploadService ,itemCatService,typeTemplateService,$location){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		goodsService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}  ;
	
	//分页
	$scope.findPage=function(page,rows){			
		goodsService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	};
	
	//查询实体 
	$scope.findOne=function(){
		//$location.search() 自带方法，获取所有页面数据

		var id = $location.search()['id'];
		if (id==null){
			return;
		}
		goodsService.findOne(id).success(
			function(response){
				$scope.entity= response;

				//商品介绍 转换富文本内容
				editor.html($scope.entity.goodsDesc.introduction);
				//商品图片
				$scope.entity.goodsDesc.itemImages=JSON.parse($scope.entity.goodsDesc.itemImages);
				//扩展属性
				$scope.entity.goodsDesc.customAttributeItems=JSON.parse($scope.entity.goodsDesc.customAttributeItems);
				//规格选择
                $scope.entity.goodsDesc.specificationItems=JSON.parse($scope.entity.goodsDesc.specificationItems);
                //转换SKU列表中的规格对象
                for (var i = 0; i <$scope.entity.itemList.length ; i++) {
					$scope.entity.itemList[i].spec = JSON.parse($scope.entity.itemList[i].spec);
                }


			}
		);				
	};
	
	//保存 
	$scope.save=function(){

		//把富文本编辑器的值提取出来,并赋值
        $scope.entity.goodsDesc.introduction=editor.html();

        var serviceObject;//服务层对象
		if($scope.entity.goods.id!=null){//如果有ID
			serviceObject=goodsService.update( $scope.entity ); //修改  
		}else{
			serviceObject=goodsService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
                    alert("保存成功");
                    // $scope.entity={};
                    // editor.html("");  //清空富文本编辑器
					location.href='goods.html'

				}else{
					alert(response.message);
				}
			}		
		);				
	};
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		goodsService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	};
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		goodsService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	};


    //上传图片
	$scope.uploadFile=function () {
		uploadService.uploadFile().success(
			function (response) {
				if (response.success){ //如果上传成功，取出url
					$scope.image_entity.url = response.message;
				} else {
					alert(response.message)
				}
            }
		);
    };

	$scope.entity = {goodsDesc:{itemImages:[],specificationItems:[]}};
    //将当前上传的图片添加到图片列表中

	$scope.add_Images_entity=function () {
		$scope.entity.goodsDesc.itemImages.push($scope.image_entity);

    };

    //移除列表中的图片
    $scope.remove_image_entity=function (index) {
        $scope.entity.goodsDesc.itemImages.splice(index,1);
    };

    //查询一级商品类表
    $scope.selectItemCatList=function () {
		itemCatService.findByParentId(0).success(
			function (response) {
				$scope.itemCat1List = response;
            }
		)
    };

    //查询二级商品类表
    $scope.$watch('entity.goods.category1Id',function (newValue,oldValue) {

    	itemCatService.findByParentId(newValue).success(
            function (response) {
                $scope.itemCat2List = response;
            }
        )
    });

    //查询三级商品类表
    $scope.$watch('entity.goods.category2Id',function (newValue,oldValue) {

        itemCatService.findByParentId(newValue).success(
            function (response) {
                $scope.itemCat3List = response;
            }
        )
    });

    //读取模板Id
    $scope.$watch('entity.goods.category3Id',function (newValue,oldValue) {

        itemCatService.findOne(newValue).success(
           function (response) {
			   $scope.entity.goods.typeTemplateId = response.typeId;
           }
        )
    });

    //监控事件 读取模板Id,获取品牌下拉列表 获取属性 获取规格
    $scope.$watch('entity.goods.typeTemplateId',function (newValue,oldValue) {

    	//品牌
        typeTemplateService.findOne(newValue).success(
            function (response) {
                $scope.typeTemplate = response; //模板对象

                $scope.typeTemplate.brandIds = JSON.parse($scope.typeTemplate.brandIds); //品牌列表类型转换

				//扩展属性
				if ($location.search()['id']==null){  //如果是增加商品
                    $scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.typeTemplate.customAttributeItems);
				}

            }
        );

        //规格
		typeTemplateService.findSpecList(newValue).success(
			function (response) {
				$scope.specList = response;
            }
		);
    });


    //勾选常用规格
    $scope.updateSpecAttribute=function ($event,name,value) {
		var object = $scope.searchObjectByKey($scope.entity.goodsDesc.specificationItems,'attributeName',name);

		if (object != null){
			if ($event.target.checked){ //勾选就添加值
				object.attributeValue.push(value);
			} else { //取消勾选
                object.attributeValue.splice(object.attributeValue.indexOf(value),1);  //移除已勾选的值
				//如果选项都取消，将整体移除
				if (object.attributeValue.length==0){
                    $scope.entity.goodsDesc.specificationItems.splice($scope.entity.goodsDesc.specificationItems.indexOf(object),1);
				}
			}
		}else{
            $scope.entity.goodsDesc.specificationItems.push({"attributeName":name,"attributeValue":[value]});
		}
    };


    //创建SKU列表
	$scope.createItemList=function () {

		//初始化列表
		$scope.entity.itemList=[{spec:{},price:0,num:9999,status:"0",isDefault:"0"}];

		//定义变量
		var items = $scope.entity.goodsDesc.specificationItems;

        for (var i = 0; i <items.length ; i++) {
        		//{"attributeName":"网络制式","attributeValue":["移动3G","移动4G"]}
            $scope.entity.itemList = addColumn($scope.entity.itemList,items[i].attributeName,items[i].attributeValue);
        }
    };

    //深克隆方法
	addColumn=function (list,columnName,columnValues) {
		var newList=[];
		for(var i=0;i<list.length;i++){
			var oldRow = list[i];

			for (var j =0;j<columnValues.length;j++){
				var newRow = JSON.parse(JSON.stringify(oldRow));//深克隆
				newRow.spec[columnName] = columnValues[j];
				newList.push(newRow);
			}
		}
		return newList;
    };


    $scope.status=['未审核','已审核','审核未通过','已关闭'];

	//查询商品分类列表
	$scope.itemCatList = [];
	$scope.findItemCatList=function () {
		itemCatService.findAll().success(
			function (response) {
				for (var i =0;i<response.length;i++){
					$scope.itemCatList[response[i].id] = response[i].name;
				}
            }
		)
    };

	//判断规格与规格选项是否应该被勾选 true勾选 反之
	$scope.checkAttributeValue=function (specName,optionName) {
		var items = $scope.entity.goodsDesc.specificationItems;
		var object = $scope.searchObjectByKey(items,'attributeName',specName);

		if (object!=null){
			if (object.attributeValue.indexOf(optionName)>=0){  //如果能查到结果
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
    }






});	
