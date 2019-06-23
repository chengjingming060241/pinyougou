app.service('uploadService',function ($http) {

    //上传图片
    this.uploadFile = function () {
        var formData = new FormData();
        formData.append('file',file.files[0]);//file 文件上传的name

        return $http({
           url:'../upload.do',
            method:'POST',
            data:formData,
            headers: {'Content-Type':undefined},
            transformRequest: angular.identity
        });
    }

});