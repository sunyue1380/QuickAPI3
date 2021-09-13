<returnEntityTag>
    <div class="columns is-multiline" if="{null!=api}">
        <div class="column is-12">
            <h1 class="title">返回值</h1>
            <span class="tag is-info is-large">{api.returnClassName}</span>
        </div>
        <div class="column is-12" if="{api.returnEntityNameList.length>0}">
            <h1 class="title">返回实体类信息</h1>
            <entityTableTag each="{returnEntityName in api.returnEntityNameList}" entity="{apiDocument.apiEntityMap[returnEntityName]}"></entityTableTag>
        </div>
    </div>
    <script>
        let self = this;
        this.on("mount",function(){
            riot.eventBus.on('showApi', function(api){
                self.api = api;
                self.apiDocument = apiDocument;
                self.update();
            });
        });
    </script>
</returnEntityTag>