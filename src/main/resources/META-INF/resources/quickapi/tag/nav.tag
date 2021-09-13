<navTag>
    <h1 class="title">{apiDocument.title}</h1>
    <h2 class="subtitle">
        <span class="tag is-primary">{apiDocument.date}</span>
    </h2>
    <div class="content" if="{null!=apiDocument.description}">
        <p>{apiDocument.description}</p>
    </div>
    <div class="field">
        <div class="control">
            <input class="input is-primary" type="text" placeholder="请输入要查询的API" ref="searchText" onkeypress="{searchAPI}" onkeyup="{searchAPI}">
        </div>
    </div>
    <aside class="menu">
        <ul class="menu-list">
            <li onclick="{showSettings}"><a class="{'is-active':showSettingState}"><span>文档设置</span></a></li>
        </ul>
        <p class="menu-label" if="{collectAPIList.length>0}">
            收藏列表
        </p>
        <ul class="menu-list" if="{collectAPIList.length>0}">
            <li each="{api in collectAPIList}" onclick="{showCurrentApi}">
                <a class="{'is-active':currentAPI==(api.requestMethod+'_'+api.url)}" title="{api.url}">
                    <s if="{api.deprecated}">{api.displayName}</s>
                    <span if="{!api.deprecated}">{api.displayName}</span>
                </a>
            </li>
        </ul>
        <p class="menu-label">
            接口列表
        </p>
        <ul class="menu-list">
            <li each="{apiController in apiDocument.apiControllerList}" if="{showApiController(apiController);}" title="{apiController.className}">
                <s if="{apiController.deprecated}">{apiController.displayName}
                    <span class="tag is-info is-small">{apiController.apiList.length}</span>
                </s>
                <p if="{!apiController.deprecated}">{apiController.displayName}
                    <span class="tag is-info is-small">{apiController.apiList.length}</span>
                </p>
                <ul>
                    <li each="{api in apiController.apiList}" onclick="{showCurrentApi}">
                        <a if="{showApi(apiController,api);}" class="{'is-active':currentAPI==(api.requestMethod+'_'+api.url)}" title="{api.url}">
                            <s if="{api.deprecated}">{api.displayName}</s>
                            <span if="{!api.deprecated}">{api.displayName}</span>
                        </a>
                    </li>
                </ul>
            </li>
        </ul>
    </aside>
    <script>
        let self = this;
        self.mixin('commonService');
        self.apiDocument = apiDocument;

        this.on("mount",function(){
            riot.eventBus.on('refreshCollectAPIList',function(){
                self.refreshCollectAPIList();
            });
        });
        
        //收藏API
        self.collectAPIList = [];
        self.refreshCollectAPIList = function(){
            self.collectAPIList = [];
            let apiStorageMap = self.getApiStorageMap();
            for(let prop in apiStorageMap){
                if(apiStorageMap[prop].collectState){
                    self.collectAPIList.push(this.getApi(apiStorageMap[prop]));
                }
            }
            this.update();
        };
        self.refreshCollectAPIList();

        let apiStorageMap = self.getApiStorageMap();
        for(let prop in apiStorageMap){
            if(apiStorageMap[prop].executeOnRefreshState){
                let api = self.getApi(apiStorageMap[prop]);
                let parameters = apiStorageMap[prop]["requestParameterCases"]["默认参数"];
                self.executeAjax(api,parameters);
            }
        }

        self.searchAPI = function(){};
        /**过滤APIController*/
        self.showApiController = function(apiController){
            if(null==self.refs.searchText.value||self.refs.searchText.value===""){
                return true;
            }
            if(apiController.displayName.indexOf(self.refs.searchText.value)>=0){
                return true;
            }
            let apiList = apiController.apiList;
            for(let i=0;i<apiList.length;i++){
                if(self.showApi(apiController,apiList[i])){
                    return true;
                }
            }
            return false;
        };
        /**过滤API*/
        self.showApi = function(apiController,api){
            if(null==self.refs.searchText.value||self.refs.searchText.value===""){
                return true;
            }
            if(apiController.displayName.indexOf(self.refs.searchText.value)>=0){
                return true;
            }
            if(api.displayName.indexOf(self.refs.searchText.value)>=0||api.url.indexOf(self.refs.searchText.value)>=0){
                return true;
            }
            return false;
        };
        /**显示API信息*/
        self.showCurrentApi = function () {
            self.currentAPI = this.api.requestMethod +"_" + this.api.url;
            self.showSettingState = false;
            riot.eventBus.trigger('showApi', this.api);
        }
        /**显示API信息*/
        self.showSettings = function () {
            self.showSettingState = true;
            riot.eventBus.trigger('showSettings');
        }
    </script>
</navTag>