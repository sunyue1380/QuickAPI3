<basicTag>
    <div class="columns is-multiline" if="{null!=api}">
        <div class="column is-12">
            <h1 class="title">
                <span>{api.displayName}</span>
                <button if="{!apiStorage.collectState}" class="button is-primary" onclick="{collect}">收藏</button>
                <button if="{apiStorage.collectState}" class="button is-danger" onclick="{cancelCollect}">取消收藏</button>
                <button if="{!apiStorage.executeOnRefreshState}" class="button is-primary" onclick="{executeOnRefresh}">刷新时执行</button>
                <button if="{apiStorage.executeOnRefreshState}" class="button is-danger" onclick="{cancelExecuteOnRefresh}">取消刷新时执行</button>
            </h1>
            <article class="message is-danger" if="{api.deprecated}">
                <div class="message-body">
                    请注意,该接口已被废弃!
                </div>
            </article>
        </div>
        <div class="column is-12">
            <div class="tags has-addons">
                <span class="tag is-large is-info">{api.requestMethod}</span>
                <span class="tag is-large is-primary">{api.url}</span>
            </div>
        </div>
        <div class="column is-12" if="{null!=api.description}">
            <div class="content">
                <p class="subtitle">{api.description}</p>
            </div>
        </div>
        <div class="column is-12">
            <div class="tags">
                <span class="tag is-info" if="{null!=api.author}">{api.author}</span>
                <span class="tag is-info" if="{null!=api.since}">{api.since}</span>
            </div>
        </div>
        <div class="column is-12" if="{api.apiExceptions.length>0}">
            <h1 class="title">异常信息</h1>
            <table class="table is-fullwidth">
                <tr><td>异常名称</td><td>描述</td></tr>
                <tr each="{apiException in api.apiExceptions}">
                    <td>{apiException.className}</td>
                    <td>{apiException.description}</td>
                </tr>
            </table>
        </div>
        <div class="column is-12">
            <h1 class="title">请求类型</h1>
            <span class="tag is-info is-large">{api.contentType}</span>
        </div>
    </div>
    <script>
        let self = this;
        self.mixin("commonService");
        this.on("mount",function(){
            riot.eventBus.on('showApi', function(api){
                self.api = api;
                self.apiStorage = self.getApiStorage(self.api);
                self.update();
            });
        });
        /**收藏API*/
        self.collect = function(){
            self.apiStorage.collectState = true;
            self.saveApiStorage(self.apiStorage);
            riot.eventBus.trigger('refreshCollectAPIList');
        };
        /**取消收藏API*/
        self.cancelCollect = function(){
            self.apiStorage.collectState = false;
            self.saveApiStorage(self.apiStorage);
            riot.eventBus.trigger('refreshCollectAPIList');
        }
        /**刷新时执行*/
        self.executeOnRefresh = function(){
            self.apiStorage.executeOnRefreshState = true;
            self.saveApiStorage(self.apiStorage);
        };
        /**取消刷新时执行*/
        self.cancelExecuteOnRefresh = function(){
            self.apiStorage.executeOnRefreshState = false;
            self.saveApiStorage(self.apiStorage);
        }
    </script>
</basicTag>