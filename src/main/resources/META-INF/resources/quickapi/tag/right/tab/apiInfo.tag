<apiInfoTag>
    <div class="columns is-multiline">
        <div class="column is-12">
            <pre ref="apiInfoView"></pre>
        </div>
    </div>
    <script>
        let self = this;
        this.on("mount",function(){
            riot.eventBus.on('showApi', function(api){
                self.api = api;
                self.update();
            });
        });
        this.on("updated",function(){
            self.editor = new JsonEditor(self.refs.apiInfoView, self.api);
        });
    </script>
</apiInfoTag>