<parameterEntityTag>
    <div class="columns is-multiline" if="{null!=api}">
        <div class="column is-12" if="{api.parameterEntityNameList.length>0}">
            <entityTableTag each="{parameterEntityName in api.parameterEntityNameList}" entity="{apiDocument.apiEntityMap[parameterEntityName]}"></entityTableTag>
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
</parameterEntityTag>