<executeTag>
    <div class="columns is-multiline" if="{null!=api}">
        <div class="column is-12">
            <button class="button is-primary" onclick="{executeAPI}">执行请求</button>
        </div>
        <div class="column is-12" if="{null!=response}">
            <table class="table is-striped is-narrow is-hoverable is-fullwidth">
                <tr>
                    <td>耗时</td><td><span class="tag is-info">{response.consumeTime}</span></td>
                </tr>
                <tr>
                    <td>返回状态码</td><td><span class="tag is-primary">{response.status} {response.statusText}</span></td>
                </tr>
            </table>
            <div class="column is-12">
                <div class="tabs is-toggle">
                    <ul>
                        <li class="{'is-active':tabName=='header'}" data-tab="header" onclick="{changeTab}"><a>HTTP头部</a></li>
                        <li class="{'is-active':tabName=='textView'}" data-tab="textView" onclick="{changeTab}"><a>文本</a></li>
                        <li class="{'is-active':tabName=='jsonView'}" data-tab="jsonView" onclick="{changeTab}"><a>JSON</a></li>
                        <li class="{'is-active':tabName=='htmlView'}" data-tab="htmlView" onclick="{changeTab}"><a>HTML</a></li>
                    </ul>
                </div>
            </div>
            <div class="column is-12">
                <table if="{tabName=='header'}" class="table is-striped is-narrow is-hoverable is-fullwidth">
                    <tr each="{header in response.headers}">
                        <td>{header.key}</td>
                        <td>{header.value}</td>
                    </tr>
                </table>
                <textarea if="{tabName=='textView'}" class="textarea" readonly cols="100" rows="5" value="{response.responseText}"></textarea>
                <pre if="{tabName=='jsonView'}" ref="json"></pre>
                <iframe show="{tabName=='htmlView'}" ref="html" style="width: 100%; height: 300px;"></iframe>
            </div>
            <div class="column is-12">
                <button class="button is-primary" onclick="{copyResponse}">复制到剪贴板</button>
            </div>
        </div>
    </div>
    <script>
        let self = this;
        self.mixin("commonService");

        this.on("mount",function(){
            riot.eventBus.on('showApi', function(api){
                self.api = api;
                self.response = null;
                self.tabName = null;
                self.update();
            });
        });
        this.on("updated",function(){
            if(null!=self.response){
                if(self.tabName=="htmlView"){
                    self.iframe = document.all ? self.refs.html.contentWindow.document : self.refs.html.contentDocument;
                    self.iframe.contentEditable = true;
                    self.iframe.designMode = 'on';
                    self.iframe.open();
                    self.iframe.write(self.response.responseText);
                    self.iframe.close();
                }else if(self.tabName=="jsonView"&&null!=self.response.contentType&&self.response.contentType.indexOf("application/json")>=0){
                    self.editor = new JsonEditor(self.refs.json, JSON.parse(self.response.responseText));
                }
            }
        })
        self.changeTab = function(event){
            self.tabName = event.currentTarget.dataset.tab;
        };
        self.executeAPI = function(){
            let refs = self.parent.tags.parametertabletag.refs;
            //检查必填项
            for(let i=0;i<self.api.apiParameterList.length;i++){
                let apiParameter = self.api.apiParameterList[i];
                if(apiParameter.requestType==="file"){
                    if(apiParameter.required&&refs[apiParameter.name].files.length===0){
                        alert("请填写必填项:"+apiParameter.name);
                        return;
                    }
                }else{
                    let value = refs[apiParameter.name].value;
                    if(apiParameter.required&&(typeof(value)=="undefined"||value==="")){
                        alert("请填写必填项:"+apiParameter.name);
                        return;
                    }
                }
            }
            //请求参数
            let parameters = {};
            for(let prop in refs){
                if(refs[prop].type=="file"){
                    parameters[prop] = refs[prop];
                }else{
                    parameters[prop] = refs[prop].value;
                }
            }
            delete parameters.requestParameterCaseName;

            let startTime = new Date().getTime();
            self.executeAjax(self.api,parameters,function(){
                if(this.readyState!==4){
                    return;
                }
                self.showAjaxResponse(this,startTime);
            });

            //存储请求参数
            let apiStorage = self.getApiStorage(self.api);
            apiStorage.requestParameterCases["默认参数"] = parameters;
            self.saveApiStorage(apiStorage);
        };

        /**展示ajax返回结果*/
        self.showAjaxResponse = function(xhr, startTime){
            //获取返回信息
            let endTime = new Date().getTime();
            self.response = {
                consumeTime: (endTime-startTime)+"ms",
                status: xhr.status,
                statusText: xhr.statusText,
                contentType: null,
                headers: []
            };

            //添加头部信息
            let responseHeaders = xhr.getAllResponseHeaders();
            let tokens = responseHeaders.split("\n");
            for(let i=0;i<tokens.length;i++){
                let key = tokens[i].substring(0,tokens[i].indexOf(":"));
                let value = tokens[i].substring(key.length+1);
                self.response.headers.push({
                    "key": key,
                    "value": value,
                });
                if(key=="content-type"){
                    self.response.contentType = value;
                }
            }
            //获取返回体信息
            let disposition = xhr.getResponseHeader("content-disposition");
            if(null!=disposition){
                //如果是文件下载
                let a = document.createElement("a");
                let fileNameIndexOf = disposition.indexOf("filename*=");
                if(fileNameIndexOf>=0){
                    let filename = disposition.substring(fileNameIndexOf+"filename*=".length);
                    const charset = filename.substring(0,filename.indexOf("''")).replace("\"","");
                    filename = filename.substring(filename.indexOf("''")+2).replace("\"","");
                    filename = decodeURIComponent(filename);
                    a.download = filename;
                }else{
                    fileNameIndexOf = disposition.indexOf("filename=");
                    if(fileNameIndexOf>=0){
                        let filename = disposition.substring(fileNameIndexOf+"filename=".length).replace("\"","");
                        a.download = filename;
                    }
                }
                a.href = window.URL.createObjectURL(new Blob([xhr.response], {type: xhr.getResponseHeader("content-type")}));
                a.click();
                return;
            }else{
                self.response.responseText = new TextDecoder("utf-8").decode(xhr.response);
                let contentType = xhr.getResponseHeader("content-type");
                if(contentType&&contentType.indexOf("application/json")>=0){
                    self.tabName = "jsonView";
                }else if(xhr.status<400){
                    self.tabName = "textView";
                }else{
                    self.tabName = "htmlView";
                }
                self.update();
            }
        };
        self.copyResponse = function(){
            self.copyToClipBoard(self.response.responseText);
            alert("结果已复制到剪贴板!");
        };
    </script>
</executeTag>