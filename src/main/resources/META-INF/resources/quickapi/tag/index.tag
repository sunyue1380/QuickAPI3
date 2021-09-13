<indexTag>
    <div class="block" if="{null==apiDocument}">
        <div class="notification is-danger">
            <p>后台接口信息获取失败!</p>
            <p><a href="/quickapi/apiDocument.js" target="_blank">点此查看详情</a></p>
        </div>
    </div>
    <div class="columns is-multiline" if="{null!=apiDocument}">
        <div class="column is-2">
            <navTag></navTag>
        </div>
        <div class="column is-10">
            <settingsTag show="{tabName=='settings'}"></settingsTag>
            <apiTag show="{tabName=='api'}"></apiTag>
        </div>
    </div>
    <script>
        let self = this;
        self.apiDocument = null;
        if(apiDocument){
            self.apiDocument = apiDocument;
        }
        this.on("mount",function(){
            riot.eventBus.on('showApi', function(api){
                self.tabName = "api";
                self.update();
            });
            riot.eventBus.on('showSettings', function(){
                self.tabName = "settings";
                self.update();
            });
        });
    </script>
</indexTag>