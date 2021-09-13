<parameterTableTag>
    <div class="columns is-multiline" if="{null!=api}">
        <div class="column is-12">
            <h1 class="title">请求参数</h1>
            <table class="table is-striped is-narrow is-hoverable is-fullwidth">
                <tr>
                    <th>名称</th>
                    <th>注解参数名称</th>
                    <th>描述</th>
                    <th>参数类型</th>
                    <th>请求类型</th>
                    <th>参数位置</th>
                    <th>是否必须</th>
                    <th>默认值</th>
                    <th>参数值</th>
                </tr>
                <tr each="{apiParameter in api.apiParameterList}">
                    <td>{apiParameter.name}</td>
                    <td>{apiParameter.annotationParameterName}</td>
                    <td>{apiParameter.description}</td>
                    <td>{apiParameter.typeClassName}</td>
                    <td>{apiParameter.requestType}</td>
                    <td>{apiParameter.position}</td>
                    <td>
                        <span if="{apiParameter.required}" class="tag is-danger">是</span>
                        <span if="{!apiParameter.required}" class="tag is-success">否</span>
                    </td>
                    <td>{apiParameter.defaultValue}</td>
                    <td>
                        <input if="{apiParameter.requestType=='text'}" class="input" type="text" ref="{apiParameter.name}" value="{null==apiParameter.defaultValue?'':apiParameter.defaultValue}"/>
                        <input if="{apiParameter.requestType=='file'}" class="input" type="file" ref="{apiParameter.name}"/>
                        <textarea if="{apiParameter.requestType=='textarea'}" class="textarea" rows="10" cols="30" ref="{apiParameter.name}" value="{apiParameter.defaultValue?apiParameter.defaultValue:apiParameter.instance}"></textarea>
                    </td>
                </tr>
                <tr if="{api.apiParameterList.length==0}"><td colspan="8">该接口不需要传递参数</td></tr>
            </table>
        </div>
        <div class="column is-12" if="{api.apiParameterList.length>0}">
            <h1 class="title">请求参数方案</h1>
            <div class="field has-addons">
                <div class="control">
                    <input ref="requestParameterCaseName" class="input is-primary" type="text" placeholder="请求方案名称" value="{currentRequestParameterCaseName}">
                </div>
                <div class="control">
                    <a class="button is-info" onclick="{saveRequestParameter}">
                        保存
                    </a>
                </div>
            </div>

            <div class="field is-grouped is-grouped-multiline">
                <div class="control" each="{val,key in apiStorage.requestParameterCases}">
                    <div class="tags has-addons are-medium">
                        <a class="{currentRequestParameterCaseName==key?'tag is-success':'tag is-link'}" onclick="{changeRequestParameter}">{key}</a>
                        <a if="{key!='默认参数'}" class="tag is-delete" onclick="{deleteRequestParameter}"></a>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script>
        let self = this;
        self.mixin("commonService");
        this.on("mount",function(){
            riot.eventBus.on('showApi', function(api){
                self.api = api;
                self.currentRequestParameterCaseName = "默认参数";
                self.apiStorage = self.getApiStorage(self.api);
                self.update();
            });
        });
        this.on("updated",function(){
            if(null!=self.api){
                //设置请求参数
                if(self.apiStorage.requestParameterCases&&self.apiStorage.requestParameterCases[self.currentRequestParameterCaseName]){
                    let requestParameter = self.apiStorage.requestParameterCases[self.currentRequestParameterCaseName];
                    for(let prop in requestParameter){
                        self.refs[prop].value = requestParameter[prop];
                    }
                }
            }
        });
        /**改变请求参数*/
        self.changeRequestParameter = function(){
            if(this.key){
                self.currentRequestParameterCaseName = this.key;
            }
            let requestParameter = self.apiStorage.requestParameterCases[self.currentRequestParameterCaseName];
            for(let prop in requestParameter){
                self.refs[prop].value = requestParameter[prop];
            }
        };
        /**删除请求参数*/
        self.deleteRequestParameter = function(){
            if(confirm("确定删除该请求参数方案吗?")){
                delete self.apiStorage.requestParameterCases[this.key];
            }
            self.currentRequestParameterCaseName = "默认参数";
            self.changeRequestParameter();
            self.saveApiStorage(self.apiStorage);
        };
        /**保存请求参数*/
        self.saveRequestParameter = function(){
            let requestParameterCaseName = self.refs.requestParameterCaseName.value;
            if(requestParameterCaseName==""){
                alert("请输入请求方案名称!");
                return;
            }
            let requestParameters = {};
            for(let prop in self.refs){
                requestParameters[prop] = self.refs[prop].value;
            }
            delete requestParameters.requestParameterCaseName;
            self.apiStorage.requestParameterCases[requestParameterCaseName] = requestParameters;
            self.currentRequestParameterCaseName = requestParameterCaseName;
            self.saveApiStorage(self.apiStorage);
        }
    </script>
</parameterTableTag>